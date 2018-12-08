package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.google.common.io.Files
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.notification.BrowseNotificationAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.PowerShellIcons
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.plugin.powershell.ide.run.checkExists
import com.intellij.plugin.powershell.ide.run.escapePath
import com.intellij.plugin.powershell.ide.run.findPsExecutable
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.BUNDLED_PSES_PATH
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getEditorServicesModuleVersion
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getEditorServicesStartupScript
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getPSExtensionModulesDir
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinNT
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.*
import java.net.Socket
import java.nio.channels.Channels
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


open class EditorServicesLanguageHostStarter(protected val myProject: Project) : LanguageHostConnectionManager {

  private val LOG: Logger = Logger.getInstance(javaClass) 
  private var socket: Socket? = null
  private var myReadPipe: RandomAccessFile? = null
  private var myWritePipe: RandomAccessFile? = null
  private var sessionInfoFile: File? = null
  private var myProcess: Process? = null
  private var sessionInfo: SessionInfo? = null

  companion object {
    @Volatile
    private var sessionCount = 0
    private val myHostDetails = HostDetails(ApplicationNamesInfo.getInstance().fullProductName, "com.intellij.plugin.PowerShell", getHostVersion())
    private var cachedEditorServicesModuleVersion: String? = null
    private var cachedPowerShellExtensionDir: String? = null
    private var isUseBundledPowerShellExtensionPath: Boolean = false

    fun isUseBundledPowerShellExtension(): Boolean = isUseBundledPowerShellExtensionPath
    fun setUseBundledPowerShellExtension(value: Boolean) {
      isUseBundledPowerShellExtensionPath = value
    }

    private fun getHostVersion(): String =
        "${ApplicationInfo.getInstance().majorVersion}.${ApplicationInfo.getInstance().minorVersion}"

    private data class HostDetails(val name: String, val profileId: String, val version: String)
    private open class SessionInfo private constructor(protected val powerShellVersion: String?, protected val status: String?) {

      internal class Pipes internal constructor(internal val languageServiceReadPipeName: String,
                                                internal val languageServiceWritePipeName: String,
                                                internal val debugServiceReadPipeName: String,
                                                internal val debugServiceWritePipeName: String,
                                                powerShellVersion: String?,
                                                status: String?) : SessionInfo(powerShellVersion, status) {
        override fun toString(): String {
          return "{languageServiceReadPipeName:$languageServiceReadPipeName,languageServiceWritePipeName:$languageServiceWritePipeName," +
              "debugServiceReadPipeName:$debugServiceReadPipeName,debugServiceWritePipeName:$debugServiceWritePipeName," +
              "powerShellVersion:$powerShellVersion,status:$status}"
        }
      }

      internal class Tcp internal constructor(internal val languageServicePort: Int,
                                              internal val debugServicePort: Int,
                                              powerShellVersion: String?,
                                              status: String?) : SessionInfo(powerShellVersion, status) {
        override fun toString(): String {
          return "{languageServicePort:$languageServicePort,debugServicePort:$debugServicePort,powerShellVersion:$powerShellVersion,status:$status}"
        }
      }
    }

    /**
     * @throws PowerShellExtensionNotFound
     */
    fun getEditorServicesVersion(psExtensionPath: String): String {
      var result = cachedEditorServicesModuleVersion
      if (StringUtil.isNotEmpty(result)) return result!!
      result = getEditorServicesModuleVersion(getPSExtensionModulesDir(psExtensionPath))
      cachedEditorServicesModuleVersion = result
      return result
    }

    /**
     * @throws PowerShellExtensionNotFound
     */
    private fun getPowerShellEditorServicesHome(): String {
      var result = cachedPowerShellExtensionDir
      if (StringUtil.isNotEmpty(result)) return result!!

      val lspMain = ApplicationManager.getApplication().getComponent(LSPInitMain::class.java)
      result = lspMain.getPowerShellInfo().powerShellExtensionPath?.trim()
      if (StringUtil.isEmpty(result)) {
        result = BUNDLED_PSES_PATH
        isUseBundledPowerShellExtensionPath = true
      }
      cachedPowerShellExtensionDir = result

      return result!!
    }
  }

  /**
   * @throws PowerShellExtensionError
   * @throws PowerShellExtensionNotFound
   * @throws PowerShellNotInstalled
   */
  override fun establishConnection(): Pair<InputStream?, OutputStream?> {
    val sessionInfo = startServerSession() ?: return Pair(null, null)//long operation
    if (sessionInfo is SessionInfo.Pipes) {
      val readPipeName = sessionInfo.languageServiceReadPipeName
      val writePipeName = sessionInfo.languageServiceWritePipeName
      return if (SystemInfo.isWindows) {
        val readPipe = RandomAccessFile(readPipeName, "rwd")
        val writePipe = RandomAccessFile(writePipeName, "rwd")
        val serverReadChannel = readPipe.channel
        val serverWriteChannel = writePipe.channel
        val inSf = Channels.newInputStream(serverWriteChannel)
        val outSf = BufferedOutputStream(Channels.newOutputStream(serverReadChannel))
        Pair(inSf, outSf)
      } else {
        val readSock = AFUNIXSocket.newInstance()
        val writeSock = AFUNIXSocket.newInstance()
        readSock.connect(AFUNIXSocketAddress(File(readPipeName)))
        writeSock.connect(AFUNIXSocketAddress(File(writePipeName)))
        Pair(writeSock.inputStream, readSock.outputStream)
      }
    } else {
      val port = (sessionInfo as? SessionInfo.Tcp)?.languageServicePort ?: return Pair(null, null)
      try {
        socket = Socket("127.0.0.1", port)
      } catch (e: Exception) {
        LOG.error("Unable to open connection to language host: $e")
      }
      if (socket == null) {
        LOG.error("Unable to create socket: " + toString())
      }
      if (socket?.isConnected == true) {
        LOG.info("Connection to language host established: ${socket?.localPort} -> ${socket?.port}")
        val inputStream = socket?.getInputStream()
        val outputStream = socket?.getOutputStream()
        if (inputStream != null && outputStream != null) return Pair(inputStream, outputStream)
      }
    }
    return Pair(null, null)
  }

  private fun getSessionCount(): Int {
    return ++sessionCount - 1
  }

  private fun getSessionDetailsFile(): File {
    var file = sessionInfoFile
    if (file != null) return file
    file = File(createSessionDetailsPath(getPowerShellEditorServicesHome()))
    FileUtil.createParentDirs(file)
    sessionInfoFile = file
    return file
  }

  override fun createProcess(project: Project, command: List<String>, directory: String?): Process {
    return GeneralCommandLine(command).createProcess()
  }

  private fun buildCommandLine(): List<String> {
    val psExtensionPath = getPowerShellEditorServicesHome()
    val startupScript = getStartupScriptPath(psExtensionPath)
    if (StringUtil.isEmpty(startupScript)) {
      LOG.warn("PowerShell language host startup script not found.")
      return emptyList()
    }
    val sessionDetailsPath = FileUtil.toCanonicalPath(getSessionDetailsFile().canonicalPath)
    val logPath = createLogPath(psExtensionPath)
    var psesVersionString = getEditorServicesVersion(psExtensionPath)
    var splitInOutPipesSwitch = ""
    val psesVersion = PSESVersion.create(psesVersionString)
    if (psesVersion != null && psesVersion.isGreaterOrEqual(PSESVersion.v1_7_0)) {
      psesVersionString = ""
      splitInOutPipesSwitch = "-SplitInOutPipes"
    } else {
      psesVersionString = "-EditorServicesVersion '$psesVersionString'"
    }
    val bundledModulesPath = getPSExtensionModulesDir(psExtensionPath)
    val additionalModules = ""//todo check if something could be added here
    val useReplSwitch = if (useConsoleRepl()) "-EnableConsoleRepl" else ""
    val logLevel = if (useConsoleRepl()) "Normal" else "Diagnostic"
    val args = "$psesVersionString -HostName '${myHostDetails.name}' -HostProfileId '${myHostDetails.profileId}' " +
        "-HostVersion '${myHostDetails.version}' -AdditionalModules @($additionalModules) " +
        "-BundledModulesPath '$bundledModulesPath' $useReplSwitch " +
        "-LogLevel '$logLevel' -LogPath '$logPath' -SessionDetailsPath '$sessionDetailsPath' -FeatureFlags @() $splitInOutPipesSwitch"
    val scriptText = "${escapePath(startupScript)} $args\n"

    val scriptFile = File.createTempFile("start-pses-host", ".ps1")
    scriptFile.deleteOnExit()
    try {
      FileUtil.writeToFile(scriptFile, scriptText)
    } catch (e: Exception) {
      LOG.error("Error writing $scriptFile script file: $e")
    }

    FileUtil.createParentDirs(File(logPath))
    val command = mutableListOf<String>()
    command.add(findPsExecutable())
    command.add("-NoProfile")
    command.add("-NonInteractive")
    command.add(scriptFile.canonicalPath)
    LOG.info("Language server startup command: '$command',\n launch command: $scriptText")
    return command
  }

  /**
   * @throws PowerShellExtensionError
   * @throws PowerShellExtensionNotFound
   * @throws PowerShellNotInstalled
   */
  private fun startServerSession(): SessionInfo? {
    cachedPowerShellExtensionDir = null
    cachedEditorServicesModuleVersion = null
    val process = createProcess(myProject, buildCommandLine(), null)
    val fileWithSessionInfo = getSessionDetailsFile()
    //todo retry starting language service process one more time
    if (!waitForSessionFile(fileWithSessionInfo)) {
      process.destroyForcibly()
      return null
    }

    val sessionInfo = readSessionFile(fileWithSessionInfo)
    if (sessionInfo == null) {
      process.destroyForcibly()
      return null
    }

    if (!checkOutput(process, getEditorServicesVersion(getPowerShellEditorServicesHome()))) {}
    val pid: Long = getProcessID(process)
    myProcess = process

    var msg = "PowerShell language host process started, $sessionInfo"
    if (pid.compareTo(-1) != 0) msg += ", pid: $pid"
    LOG.info("$msg.")
    this.sessionInfo = sessionInfo
    return sessionInfo
  }

  private fun checkOutput(process: Process, editorServicesVersion: String): Boolean {
    process.waitFor(3000L, TimeUnit.MILLISECONDS)
    if (useConsoleRepl()) return true
    val br = BufferedReader(InputStreamReader(process.inputStream))
    val er = BufferedReader(InputStreamReader(process.errorStream))
    val result = if (br.ready()) br.readLine() else ""
    if ("needs_install" == result) {
      val content = "Required $editorServicesVersion 'PowerShellEditorServices' module is not found. Please install PowerShell VS Code extension"
      val title = "PowerShellEditorServices $editorServicesVersion module not found."
      val notify = Notification("PowerShell Extension Not Found", PowerShellIcons.FILE, title, null, content, NotificationType.INFORMATION, null)
      notify.addAction(BrowseNotificationAction("Install VSCode PowerShell", MessagesBundle.message("powershell.vs.code.extension.install.link")))
      Notifications.Bus.notify(notify)
      return false
    }
    if (StringUtil.isNotEmpty(result)) {
      LOG.info("Startup script output:\n$result")
    }
    if (er.ready()) {
      var errorOutput = ""
      for (line in er.readLines()) {
        errorOutput += line
      }
      if (StringUtil.isNotEmpty(errorOutput)) LOG.info("Startup script error output:\n$errorOutput")
    }
    return true
  }

  private fun readTcpInfo(jsonResult: JsonObject): SessionInfo? {
    val langServicePort = jsonResult.get("languageServicePort")?.asInt
    val debugServicePort = jsonResult.get("debugServicePort")?.asInt
    val powerShellVersion = jsonResult.get("powerShellVersion")?.asString
    val status = jsonResult.get("status")?.asString
    if (langServicePort == null || debugServicePort == null) {
      LOG.warn("languageServicePort or debugServicePort are null")
      return null
    }
    return SessionInfo.Tcp(langServicePort, debugServicePort, powerShellVersion, status)
  }

  private fun readPipesInfo(jsonResult: JsonObject): SessionInfo? {
    val langServiceReadPipeName = jsonResult.get("languageServiceReadPipeName")?.asString
    val langServiceWritePipeName = jsonResult.get("languageServiceWritePipeName")?.asString
    val debugServiceReadPipeName = jsonResult.get("debugServiceReadPipeName")?.asString
    val debugServiceWritePipeName = jsonResult.get("debugServiceWritePipeName")?.asString
    val powerShellVersion = jsonResult.get("powerShellVersion")?.asString
    val status = jsonResult.get("status")?.asString
    if (langServiceReadPipeName == null || langServiceWritePipeName == null || debugServiceReadPipeName == null || debugServiceWritePipeName == null) {
      LOG.warn("languageServiceReadPipeName or debugServiceReadPipeName are null")
      return null
    }
    return SessionInfo.Pipes(langServiceReadPipeName, langServiceWritePipeName, debugServiceReadPipeName, debugServiceWritePipeName, powerShellVersion, status)
  }

  private fun readSessionFile(sessionFile: File): SessionInfo? {
    return try {
      val line = Files.readFirstLine(sessionFile, Charset.forName("utf8"))
      val jsonResult = JsonParser().parse(line).asJsonObject
      readPipesInfo(jsonResult) ?: readTcpInfo(jsonResult)
    } catch (e: Exception) {
      LOG.warn("Error reading/parsing session details file $sessionFile: $e")
      null
    }
  }

  private fun waitForSessionFile(fileWithSessionInfo: File): Boolean {
    var tries = 25
    val waitTimeoutMillis = 500L
    try {
      while (!fileWithSessionInfo.exists() && tries > 0) {
        tries--
        Thread.sleep(waitTimeoutMillis)
        LOG.debug("Waiting for session info file ${fileWithSessionInfo.path} ... Tries left: $tries")
      }
    } catch (e: Exception) {
      LOG.warn("Error while waiting session info file: $e")
    } finally {
      return if (!fileWithSessionInfo.exists()) {
        LOG.warn("Timed out waiting for session file to appear.")
        false
      } else true
    }
  }

  /**
   * @throws PowerShellExtensionError
   * @throws PowerShellExtensionNotFound
   */
  private fun getStartupScriptPath(psExtensionPath: String): String {
    val result = getEditorServicesStartupScript(psExtensionPath)
    if (!checkExists(result)) {
      val reason = "Guessed script path $result does not exist."
      LOG.warn(reason)
      throw PowerShellExtensionError(reason)
    }
    return result
  }

  private fun createSessionDetailsPath(psExtensionPath: String): String =
      FileUtil.toCanonicalPath("$psExtensionPath/sessions/PSES-IJ-${System.currentTimeMillis()}-session.info")

  override fun closeConnection() {
    try {
      socket?.close()
      socket = null
      myReadPipe?.close()
      myWritePipe?.close()
      sessionInfoFile?.delete()
      val process = myProcess?.destroyForcibly()
      if (process?.isAlive != true) {
        LOG.info("PowerShell language host process exited: ${process?.exitValue()}")
      } else {
        LOG.info("PowerShell language host process terminated")
      }
    } catch (e: Exception) {
      LOG.error("Error when shutting down language server process: $e")
    } finally {
      sessionInfoFile = null
      myProcess = null
      myReadPipe = null
      myWritePipe = null
    }
  }

  private fun getProcessID(p: Process): Long {
    var result: Long = -1
    try {
      //for win
      if (p.javaClass.name == "java.lang.Win32Process"
          || p.javaClass.name == "java.lang.ProcessImpl"
          || p.javaClass.name == "com.pty4j.windows.WinPtyProcess") {
        val f = p.javaClass.getDeclaredField("handle")
        f.isAccessible = true
        val handle = f.getLong(p)
        val kernel = Kernel32.INSTANCE
        val hand = WinNT.HANDLE()
        hand.pointer = Pointer.createConstant(handle)
        result = kernel.GetProcessId(hand).toLong()
        f.isAccessible = false
      } else if (p.javaClass.name == "java.lang.UNIXProcess" || p.javaClass.name == "com.pty4j.unix.UnixPtyProcess") {
        val f = p.javaClass.getDeclaredField("pid")
        f.isAccessible = true
        result = f.getLong(p)
        f.isAccessible = false
      }//for unix-based OS
    } catch (ex: Exception) {
      result = -1
    }

    return result
  }

  final override fun getProcess(): Process? = myProcess
  override fun isConnected(): Boolean = myProcess?.isAlive == true
  protected open fun getLogFileName(): String = "EditorServices-IJ-${getSessionCount()}"
  private fun createLogPath(psExtensionPath: String): String = FileUtil.toCanonicalPath("$psExtensionPath/sessions/${getLogFileName()}.log")
}