package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.google.common.io.Files
import com.google.gson.JsonParser
import com.intellij.notification.BrowseNotificationAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.PowerShellIcons
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.BUNDLED_PSES_PATH
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.checkExists
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getEditorServicesModuleVersion
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getEditorServicesStartupScript
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getPSExtensionModulesDir
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinNT
import java.io.*
import java.net.Socket
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


class LanguageHostStarter {

  private val LOG: Logger = Logger.getInstance(javaClass)
  private var socket: Socket? = null
  private var sessionInfoFile: File? = null
  private var powerShellProcess: Process? = null
  private var sessionInfo: SessionInfo? = null

  companion object {
    @Volatile
    private var sessionCount = 0
    private val myHostDetails = HostDetails(ApplicationNamesInfo.getInstance().fullProductName, "com.intellij.plugin.PowerShell", ApplicationInfo.getInstance().strictVersion)
    private var cachedEditorServicesModuleVersion: String? = null
    private var cachedPowerShellExtensionDir: String? = null
    private var isUseBundledPowerShellExtensionPath: Boolean = false

    fun isUseBundledPowerShellExtension(): Boolean = isUseBundledPowerShellExtensionPath
    fun setUseBundledPowerShellExtension(value: Boolean) {
      isUseBundledPowerShellExtensionPath = value
    }

    private data class HostDetails(val name: String, val profileId: String, val version: String)
    private data class SessionInfo(val languageServicePort: Int, val debugServicePort: Int, val powerShellVersion: String?, val status: String?) {
      override fun toString(): String {
        return "{languageServicePort:$languageServicePort,debugServicePort:$debugServicePort,powerShellVersion:$powerShellVersion,status:$status}"
      }
    }
  }

  /**
   * @throws PowerShellExtensionError
   * @throws PowerShellExtensionNotFound
   */
  fun establishConnection(): Pair<InputStream?, OutputStream?> {
    val port = getServerPort() ?: return Pair(null, null)//long operation
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
      return if (inputStream == null || outputStream == null) Pair(null, null) else Pair(inputStream, outputStream)

    }
    return Pair(null, null)
  }

  /**
   * @throws PowerShellExtensionError
   * @throws PowerShellExtensionNotFound
   */
  private fun getServerPort(): Int? {
    sessionInfo = startServerSession()
    return sessionInfo?.languageServicePort
  }

  private fun getSessionCount(): Int {
    return ++sessionCount - 1
  }

  /**
   * @throws PowerShellExtensionError
   * @throws PowerShellExtensionNotFound
   */
  private fun startServerSession(): SessionInfo? {
    val psExtensionPath = getPowerShellExtensionPath()
    val startupScript = getStartupScriptPath(psExtensionPath)
    if (StringUtil.isEmpty(startupScript)) {
      LOG.warn("PowerShell language host startup script not found.")
      return null
    }
    val sessionDetailsPath = createSessionDetailsPath(psExtensionPath)
    val logPath = createLogPath(psExtensionPath)
    val editorServicesVersion = getEditorServicesVersion(psExtensionPath)
    val bundledModulesPath = getPSExtensionModulesDir(psExtensionPath)
    val additionalModules = "PowerShellEditorServices.VSCode"//todo detect all from bundledModulesPath
    val logLevel = "Verbose" //""Diagnostic" -< does not work for older PS versions
    val args = "-EditorServicesVersion '$editorServicesVersion' -HostName '${myHostDetails.name}' -HostProfileId '${myHostDetails.profileId}' " +
        "-HostVersion '${myHostDetails.version}' -AdditionalModules @('$additionalModules') " +
        "-BundledModulesPath '$bundledModulesPath' -EnableConsoleRepl " +
        "-LogLevel '$logLevel' -LogPath '$logPath' -SessionDetailsPath '$sessionDetailsPath' -FeatureFlags @()"
    val scriptText = "$startupScript $args\n"

    val scriptFile = File.createTempFile("start-pses-host", ".ps1")
    try {
      FileUtil.writeToFile(scriptFile, scriptText)
    } catch (e: Exception) {
      LOG.error("Error writing $scriptFile script file: $e")
    }

    val fileWithSessionInfo = File(sessionDetailsPath)
    FileUtil.createParentDirs(fileWithSessionInfo)
    FileUtil.createParentDirs(File(logPath))
    sessionInfoFile = fileWithSessionInfo
    val executableName = if (SystemInfo.isUnix) "powershell" else "powershell.exe"
    val psCommand = "$executableName -NoProfile -NonInteractive ${scriptFile.canonicalPath}"

    LOG.info("Language server starting... exe: '$psCommand',\n launch command: $startupScript $args")
    val process = Runtime.getRuntime().exec(psCommand)
    powerShellProcess = process

    if (!checkOutput(process, editorServicesVersion)) {//todo
    }
    //todo retry starting language service process one more time
    if (!waitForSessionFile(fileWithSessionInfo)) return null

    val sessionInfo = readSessionFile(fileWithSessionInfo) ?: return null

    val pid = getProcessID(process)
    var msg = "PowerShell language host process started, $sessionInfo"
    if (pid.compareTo(-1) != 0) msg += ", pid: $pid"
    LOG.info("$msg.")
    process.outputStream.close()
    return sessionInfo
  }

  /**
   * @throws PowerShellExtensionNotFound
   */
  private fun getEditorServicesVersion(psExtensionPath: String): String {
    var result = cachedEditorServicesModuleVersion
    if (StringUtil.isNotEmpty(result)) return result!!
    result = getEditorServicesModuleVersion(getPSExtensionModulesDir(psExtensionPath))
    cachedEditorServicesModuleVersion = result
    return result
  }

  private fun checkOutput(process: Process, editorServicesVersion: String): Boolean {
    process.waitFor(3000L, TimeUnit.MILLISECONDS)
    val br = BufferedReader(InputStreamReader(process.inputStream))
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
      LOG.warn("Startup script output not empty:\n$result")
    }
    return true
  }

  private fun readSessionFile(sessionFile: File): SessionInfo? {
    try {
      val line = Files.readFirstLine(sessionFile, Charset.forName("utf8"))
      val jsonResult = JsonParser().parse(line).asJsonObject
      val langServicePort = jsonResult.get("languageServicePort")?.asInt
      val debugServicePort = jsonResult.get("debugServicePort")?.asInt
      val status = jsonResult.get("status")?.asString
      if (langServicePort == null || debugServicePort == null) {
        LOG.warn("languageServicePort or debugServicePort are null")
        return null
      }
      return SessionInfo(langServicePort, debugServicePort, null, status)
    } catch (e: Exception) {
      LOG.error("Error reading/parsing session details file $sessionFile: $e")
      return null
    }
  }

  private fun waitForSessionFile(fileWithSessionInfo: File): Boolean {
    var tries = 25
    val waitTimeoutMillis = 500L
    try {
      while (!fileWithSessionInfo.exists() && tries > 0) {
        tries--
        Thread.sleep(waitTimeoutMillis)
        LOG.info("Waiting for session info file ${fileWithSessionInfo.path} ... Tries left: $tries")
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

  private fun createLogPath(psExtensionPath: String): String =
      FileUtil.toCanonicalPath("$psExtensionPath/sessions/EditorServices-IJ-${getSessionCount()}.log")

  private fun createSessionDetailsPath(psExtensionPath: String): String =
      FileUtil.toCanonicalPath("$psExtensionPath/sessions/PSES-IJ-${System.currentTimeMillis()}-session.info")

  /**
   * @throws PowerShellExtensionNotFound
   */
  private fun getPowerShellExtensionPath(): String {
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


  fun closeConnection() {
    try {
      socket?.close()
      socket = null
      sessionInfoFile?.delete()
      sessionInfoFile = null
      val process = powerShellProcess?.destroyForcibly()
      if (process?.isAlive != true) {
        LOG.info("PowerShell language host process exited: ${process?.exitValue()}")
      } else {
        LOG.info("PowerShell language host process terminated")
      }
      powerShellProcess = null
    } catch (e: Exception) {
      LOG.error("Error when shutting down language server process: $e")
    }
  }

  private fun getProcessID(p: Process): Long {
    var result: Long = -1
    try {
      //for win
      if (p.javaClass.name == "java.lang.Win32Process" || p.javaClass.name == "java.lang.ProcessImpl") {
        val f = p.javaClass.getDeclaredField("handle")
        f.isAccessible = true
        val handle = f.getLong(p)
        val kernel = Kernel32.INSTANCE
        val hand = WinNT.HANDLE()
        hand.pointer = Pointer.createConstant(handle)
        result = kernel.GetProcessId(hand).toLong()
        f.isAccessible = false
      } else if (p.javaClass.name == "java.lang.UNIXProcess") {
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
}