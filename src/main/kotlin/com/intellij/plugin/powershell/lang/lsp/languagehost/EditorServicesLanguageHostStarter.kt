package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.google.common.io.Files
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.intellij.execution.configurations.PtyCommandLine
import com.intellij.execution.process.*
import com.intellij.notification.BrowseNotificationAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.withSyncIOBackgroundContext
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.PowerShellIcons
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.plugin.powershell.ide.run.checkExists
import com.intellij.plugin.powershell.ide.run.escapePath
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.BUNDLED_PSES_PATH
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getEditorServicesModuleVersion
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getEditorServicesStartupScript
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getPSExtensionModulesDir
import com.intellij.util.application
import com.intellij.util.execution.ParametersListUtil
import com.intellij.util.io.BaseOutputReader
import com.intellij.util.io.await
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinNT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.jetbrains.plugins.terminal.TerminalProjectOptionsProvider
import kotlinx.coroutines.withContext
import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.*
import java.net.Socket
import java.nio.channels.Channels
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicInteger

private val logger = logger<EditorServicesLanguageHostStarter>()

private const val INTELLIJ_POWERSHELL_PARENT_PID = "INTELLIJ_POWERSHELL_PARENT_PID"

open class EditorServicesLanguageHostStarter(protected val myProject: Project) : LanguageHostConnectionManager {

  private var socket: Socket? = null
  private var myReadPipe: RandomAccessFile? = null
  private var myWritePipe: RandomAccessFile? = null
  private var sessionInfoFile: File? = null
  private var myProcess: Process? = null
  private var sessionInfo: SessionInfo? = null

  companion object {
    private var sessionCount = AtomicInteger()
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
    open class SessionInfo private constructor(val powerShellVersion: String?, val status: String?) {

      class Pipes(
        val languageServiceReadPipeName: String,
        val languageServiceWritePipeName: String,
        val debugServiceReadPipeName: String,
        val debugServiceWritePipeName: String,
        powerShellVersion: String?,
        status: String?
      ) : SessionInfo(powerShellVersion, status) {
        override fun toString(): String {
          return "{languageServiceReadPipeName:$languageServiceReadPipeName,languageServiceWritePipeName:$languageServiceWritePipeName," +
              "debugServiceReadPipeName:$debugServiceReadPipeName,debugServiceWritePipeName:$debugServiceWritePipeName," +
              "powerShellVersion:$powerShellVersion,status:$status}"
        }
      }

      class Tcp(
        val languageServicePort: Int,
        val debugServicePort: Int,
        powerShellVersion: String?,
        status: String?
      ) : SessionInfo(powerShellVersion, status) {
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

      val lspMain = LSPInitMain.getInstance()
      result = lspMain.state.powerShellExtensionPath?.trim()
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
  override suspend fun establishConnection(): Pair<InputStream?, OutputStream?> {
    val sessionInfo = startServerSession() ?: return Pair(null, null)
    if (sessionInfo is SessionInfo.Pipes) {
      val readPipeName = sessionInfo.languageServiceReadPipeName
      val writePipeName = sessionInfo.languageServiceWritePipeName
      return withContext(Dispatchers.IO) {
        if (SystemInfo.isWindows) {
          val readPipe = RandomAccessFile(readPipeName, "rwd")
          val writePipe = RandomAccessFile(writePipeName, "r")
          val serverReadChannel = readPipe.channel
          val serverWriteChannel = writePipe.channel
          val inSf = Channels.newInputStream(serverWriteChannel)
          val outSf = BufferedOutputStream(Channels.newOutputStream(serverReadChannel))
          Pair(inSf, outSf)
        } else {
          val readSock = AFUNIXSocket.newInstance()
          val writeSock = AFUNIXSocket.newInstance()
          readSock.connect(AFUNIXSocketAddress.of(File(readPipeName)))
          writeSock.connect(AFUNIXSocketAddress.of(File(writePipeName)))
          Pair(writeSock.inputStream, readSock.outputStream)
        }
      }
    } else {
      return withContext(Dispatchers.IO) block@{
        val port = (sessionInfo as? SessionInfo.Tcp)?.languageServicePort ?: return@block Pair(null, null)
        try {
          socket = Socket("127.0.0.1", port)
        } catch (e: Exception) {
          logger.error("Unable to open connection to language host: $e")
        }
        if (socket == null) {
          logger.error("Unable to create socket: " + toString())
        }
        if (socket?.isConnected == true) {
          logger.info("Connection to language host established: ${socket?.localPort} -> ${socket?.port}")
          val inputStream = socket?.getInputStream()
          val outputStream = socket?.getOutputStream()
          if (inputStream != null && outputStream != null) return@block Pair(inputStream, outputStream)
        }

        Pair(null, null)
      }
    }
  }

  override suspend fun  establishDebuggerConnection(): Pair<InputStream, OutputStream>? {
    val sessionInfo = startServerSession(true) ?: return null
    if (sessionInfo is SessionInfo.Pipes) {
      val readPipeName = sessionInfo.debugServiceReadPipeName
      val writePipeName = sessionInfo.debugServiceWritePipeName
      return withSyncIOBackgroundContext {
        if (SystemInfo.isWindows) {
          val readPipe = RandomAccessFile(readPipeName, "rwd")
          val writePipe = RandomAccessFile(writePipeName, "r")
          val serverReadChannel = readPipe.channel
          val serverWriteChannel = writePipe.channel
          val inSf = Channels.newInputStream(serverWriteChannel)
          val outSf = BufferedOutputStream(Channels.newOutputStream(serverReadChannel))
          Pair(inSf, outSf)
        } else {
          val readSock = AFUNIXSocket.newInstance()
          val writeSock = AFUNIXSocket.newInstance()
          readSock.connect(AFUNIXSocketAddress.of(File(readPipeName)))
          writeSock.connect(AFUNIXSocketAddress.of(File(writePipeName)))
          Pair(writeSock.inputStream, readSock.outputStream)
        }
      }
    } else {
      return withSyncIOBackgroundContext block@{
        val port = (sessionInfo as? SessionInfo.Tcp)?.debugServicePort ?: return@block null
        try {
          socket = Socket("127.0.0.1", port)
        } catch (e: Exception) {
          logger.error("Unable to open connection to language host: $e")
        }
        if (socket == null) {
          logger.error("Unable to create socket: " + toString())
        }
        if (socket?.isConnected == true) {
          logger.info("Connection to language host established: ${socket?.localPort} -> ${socket?.port}")
          val inputStream = socket?.getInputStream()
          val outputStream = socket?.getOutputStream()
          if (inputStream != null && outputStream != null) return@block Pair(inputStream, outputStream)
        }

        null
      }
    }
  }

  private fun getSessionCount(): Int {
    return sessionCount.getAndIncrement()
  }

  private fun getSessionDetailsFile(): File {
    var file = sessionInfoFile
    if (file != null) return file
    file = File(createSessionDetailsPath(getPowerShellEditorServicesHome()))
    FileUtil.createParentDirs(file)
    sessionInfoFile = file
    return file
  }

  override fun createProcess( command: List<String>, environment: Map<String, String>?): Process {
    return PtyCommandLine(command)
      .withConsoleMode(false)
      .withEnvironment(environment)
      .createProcess()
  }

  private suspend fun buildCommandLine(isDebugServiceOnly: Boolean = false): List<String> {
    val psExtensionPath = getPowerShellEditorServicesHome()
    val startupScript = getStartupScriptPath(psExtensionPath)
    if (StringUtil.isEmpty(startupScript)) {
      logger.warn("PowerShell language host startup script not found.")
      return emptyList()
    }
    val sessionDetailsPath = FileUtil.toCanonicalPath(getSessionDetailsFile().canonicalPath)
    val logPath = createLogPath(psExtensionPath)
    val lspInitMain = LSPInitMain.getInstance()
    val psExecutable = lspInitMain.getPowerShellExecutable()

    val psVersion = PSLanguageHostUtils.getPowerShellVersion(psExecutable).await()

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
    val useReplSwitch = if (useConsoleRepl() || isDebugServiceOnly) "-EnableConsoleRepl" else ""
    val logLevel = if (useConsoleRepl() || isDebugServiceOnly) "Normal" else "Diagnostic"
    val debugServiceOnlySwitch = if(isDebugServiceOnly) " -DebugServiceOnly" else ""
    var args = "$psesVersionString -HostName '${myHostDetails.name}' -HostProfileId '${myHostDetails.profileId}' " +
        "-HostVersion '${myHostDetails.version}' -AdditionalModules @() " +
        "-BundledModulesPath '$bundledModulesPath' $useReplSwitch $debugServiceOnlySwitch " +
        "-LogLevel '$logLevel' -LogPath '$logPath' -SessionDetailsPath '$sessionDetailsPath' -FeatureFlags @() $splitInOutPipesSwitch"

    val preamble =
      if (SystemInfo.isWindows) {
        when (psVersion.edition) {
          PowerShellEdition.Core -> "\$null = Start-ThreadJob {\n" +
            "  Wait-Process -Id \$env:$INTELLIJ_POWERSHELL_PARENT_PID\n" +
            "  [System.Environment]::Exit(0)\n" +
            "}\n"
          else -> "\$hostPid = \$PID\n" +
            "\$null = Start-Job { " +
            "Wait-Process -Id \$env:$INTELLIJ_POWERSHELL_PARENT_PID; Stop-Process -Id \$using:hostPid " +
            "}\n"
        }
      } else ""
    val scriptText = "$preamble${escapePath(startupScript)} $args"

    val scriptFile = withContext(Dispatchers.IO) {
      File.createTempFile("start-pses-host", ".ps1").apply {
        deleteOnExit()
        try {
          FileUtil.writeToFile(this, scriptText)
        } catch (e: Exception) {
          logger.error("Error writing $this script file: $e")
        }
      }
    }

    FileUtil.createParentDirs(File(logPath))
    val command = mutableListOf<String>()
    command.add(psExecutable)
    command.add("-NoProfile")
    command.add("-NonInteractive")
    command.addAll(listOf("-ExecutionPolicy", "RemoteSigned")) // to run under default Restricted policy on PowerShell 5
    command.addAll(listOf("-File", scriptFile.canonicalPath))
    logger.info("Language server startup command: '$command',\n launch command: $scriptText")
    return command
  }

  /**
   * @throws PowerShellExtensionError
   * @throws PowerShellExtensionNotFound
   * @throws PowerShellNotInstalled
   */
  public suspend fun startServerSession(isDebugServiceOnly: Boolean = false): SessionInfo? {
    cachedPowerShellExtensionDir = null
    cachedEditorServicesModuleVersion = null
    val commandLine = buildCommandLine(isDebugServiceOnly)
    val process = createProcess(commandLine, mapOf(INTELLIJ_POWERSHELL_PARENT_PID to ProcessHandle.current().pid().toString()))
    val pid: Long = getProcessID(process)
    processOutput(
      process,
      pid,
      ParametersListUtil.join(commandLine),
      getEditorServicesVersion(getPowerShellEditorServicesHome())
    )

    val fileWithSessionInfo = getSessionDetailsFile()
    if (!waitForSessionFile(fileWithSessionInfo)) {
      process.destroyForcibly()
      return null
    }

    val sessionInfo = readSessionFile(fileWithSessionInfo)
    if (sessionInfo == null) {
      process.destroyForcibly()
      return null
    }

    myProcess = process

    var msg = "PowerShell language host process started, $sessionInfo"
    if (pid.compareTo(-1) != 0) msg += ", pid: $pid"
    logger.info("$msg.")
    this.sessionInfo = sessionInfo
    return sessionInfo
  }

  private fun processOutput(process: Process, pid: Long, commandLine: String, editorServicesVersion: String) {
    if (useConsoleRepl()) return

    fun showInstallNotification() {
      val content = "Required $editorServicesVersion 'PowerShellEditorServices' module is not found. Please install PowerShell VS Code extension"
      val title = "PowerShellEditorServices $editorServicesVersion module not found."
      val notify = Notification("PowerShell.MissingExtension", title, content, NotificationType.INFORMATION)
      notify.setIcon(PowerShellIcons.FILE)
      notify.addAction(BrowseNotificationAction("Install VSCode PowerShell", MessagesBundle.message("powershell.vs.code.extension.install.link")))
      Notifications.Bus.notify(notify, myProject)
    }

    val handler = object : OSProcessHandler(process, commandLine) {
      override fun readerOptions() = BaseOutputReader.Options.forMostlySilentProcess()
    }
    handler.addProcessListener(object : ProcessAdapter() {
      private var isFirstLineProcessed = false
      private val decoder = AnsiEscapeDecoder()
      override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        decoder.escapeText(event.text, outputType) { line, type ->
          val text = "host[$pid]: ${line.trimEnd()}"
          if (ProcessOutputType.isStderr(type)) {
            logger.warn(text)
          } else {
            logger.info(text)
            if (ProcessOutputType.isStdout(type) && !isFirstLineProcessed) {
              if (line == "needs_install") {
                showInstallNotification()
              }
              isFirstLineProcessed = true
            }
          }
        }
      }

      override fun processTerminated(event: ProcessEvent) {
        logger.info("Language host with PID = $pid has exited with code ${event.exitCode}.")
      }
    })
    handler.startNotify()
  }

  private fun readTcpInfo(jsonResult: JsonObject): SessionInfo? {
    val langServicePort = jsonResult.get("languageServicePort")?.asInt
    val debugServicePort = jsonResult.get("debugServicePort")?.asInt
    val powerShellVersion = jsonResult.get("powerShellVersion")?.asString
    val status = jsonResult.get("status")?.asString
    if (langServicePort == null || debugServicePort == null) {
      logger.warn("languageServicePort or debugServicePort are null")
      return null
    }
    return SessionInfo.Tcp(langServicePort, debugServicePort, powerShellVersion, status)
  }

  private fun readPipesInfo(jsonResult: JsonObject): SessionInfo {
    val langServiceReadPipeName = jsonResult.get("languageServiceReadPipeName")?.asString
    val langServiceWritePipeName = jsonResult.get("languageServiceWritePipeName")?.asString
    val debugServiceReadPipeName = jsonResult.get("debugServiceReadPipeName")?.asString
    val debugServiceWritePipeName = jsonResult.get("debugServiceWritePipeName")?.asString
    val powerShellVersion = jsonResult.get("powerShellVersion")?.asString
    val status = jsonResult.get("status")?.asString

    //todo make types nullable
    return SessionInfo.Pipes(langServiceReadPipeName ?: "", langServiceWritePipeName ?: "", debugServiceReadPipeName ?: "", debugServiceWritePipeName ?: "", powerShellVersion, status)
  }

  private fun readSessionFile(sessionFile: File): SessionInfo? {
    return try {
      val line = Files.asCharSource(sessionFile, Charset.forName("utf8")).readFirstLine()
      val jsonResult = JsonParser.parseString(line).asJsonObject
      readPipesInfo(jsonResult) ?: readTcpInfo(jsonResult)
    } catch (e: Exception) {
      logger.warn("Error reading/parsing session details file $sessionFile: $e")
      null
    }
  }

  private suspend fun waitForSessionFile(fileWithSessionInfo: File): Boolean {
    var tries = 120
    val waitTimeoutMillis = 500L // the total timeout is about 1 minute
    try {
      while (!fileWithSessionInfo.exists() && tries > 0) {
        tries--
        delay(waitTimeoutMillis)
        logger.debug("Waiting for session info file ${fileWithSessionInfo.path} ... Tries left: $tries")
      }
    } catch (e: Exception) {
      logger.warn("Error while waiting session info file: $e")
    }

    return if (!fileWithSessionInfo.exists()) {
      logger.warn("Timed out waiting for session file to appear.")
      false
    } else true
  }

  /**
   * @throws PowerShellExtensionError
   * @throws PowerShellExtensionNotFound
   */
  private fun getStartupScriptPath(psExtensionPath: String): String {
    val result = getEditorServicesStartupScript(psExtensionPath)
    if (!checkExists(result)) {
      val reason = "Guessed script path $result does not exist."
      logger.warn(reason)
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
        logger.info("PowerShell language host process exited: ${process?.exitValue()}")
      } else {
        logger.info("PowerShell language host process terminated")
      }
    } catch (e: Exception) {
      logger.error("Error when shutting down language server process: $e")
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
      } //for Unix-based OS
    } catch (_: Exception) {
      result = -1
    }

    return result
  }

  final override fun getProcess(): Process? = myProcess
  override fun isConnected(): Boolean = myProcess?.isAlive == true
  protected open fun getLogFileName(): String = "EditorServices-IJ-${getSessionCount()}"
  private fun createLogPath(psExtensionPath: String): String = FileUtil.toCanonicalPath("$psExtensionPath/sessions/${getLogFileName()}.log")
}
