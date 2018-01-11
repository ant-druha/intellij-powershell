package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.google.common.io.Files
import com.google.gson.JsonParser
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.checkExists
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getLanguageHostLogsDir
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getModulesDir
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getPSExtensionDir
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.WinNT
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset


class LanguageHostStarter {

  private val LOG: Logger = Logger.getInstance(javaClass)
  private var socket: Socket? = null
  private var sessionInfoFile: File? = null
  private var powerShellProcess: Process? = null
  private var sessionInfo: SessionInfo? = null

  private data class SessionInfo(val languageServicePort: Int, val debugServicePort: Int, val powerShellVersion: String?, val status: String?)

  companion object {
    @Volatile private var sessionCount = 0
  }

  /**
   * @throws PSEditorServicesScriptNotFound
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
   * @throws PSEditorServicesScriptNotFound
   */
  private fun getServerPort(): Int? {
    sessionInfo = startServerSession()
    return sessionInfo?.languageServicePort
  }

  private fun getSessionCount(): Int {
    return ++sessionCount - 1
  }

  /**
   * @throws PSEditorServicesScriptNotFound
   */
  private fun startServerSession(): SessionInfo? {
    val startupScript = getStartupScriptPath()
    if (StringUtil.isEmpty(startupScript)) {
      LOG.warn("PowerShell language host startup script not found.")
      return null
    }
    val sessionDetailsPath = createSessionDetailsPath()
    val logPath = createLogPath()
    val args = "-EditorServicesVersion '1.5.1' -HostName 'IntelliJ Editor Host' -HostProfileId 'JetBrains.IJ' " +
        "-HostVersion '1.5.1' -AdditionalModules @('PowerShellEditorServices.VSCode') " +
        "-BundledModulesPath '${getModulesDir()}' -EnableConsoleRepl " +
        "-LogLevel 'Diagnostic' -LogPath '$logPath' -SessionDetailsPath '$sessionDetailsPath' -FeatureFlags @()"
    val scriptText = "$startupScript $args\n"

    val encoding = "utf8"
    val scriptFile = File.createTempFile("start-pses-host", ".ps1")

    try {
      Files.write(scriptText, scriptFile, Charset.forName(encoding))
    } catch (e: Exception) {
      LOG.error("Error writing $scriptFile script file: $e")
    }

    val fileWithSessionInfo = File(sessionDetailsPath)
    sessionInfoFile = fileWithSessionInfo
    val executableName = if (SystemInfo.isUnix) "powershell" else "powershell.exe"
    val psCommand = "$executableName ${scriptFile.canonicalPath}"

    LOG.info("Language server starting... exe: '$psCommand',\n launch command: $startupScript $args")
    val process = Runtime.getRuntime().exec(psCommand)
    powerShellProcess = process

    //todo retry starting language service process one more time
    if (!waitForSessionFile(fileWithSessionInfo)) return null

    val sessionInfo = readSessionFile(fileWithSessionInfo) ?: return null

    val pid = getProcessID(process)
    var msg = "PowerShell language host process started"
    if (pid.compareTo(-1) != 0) msg += ", pid: $pid"
    LOG.info("$msg.")
    process.outputStream.close()
    return sessionInfo
  }

  private fun readSessionFile(sessionFile: File): SessionInfo? {
    try {
      val line = Files.readFirstLine(sessionFile, Charset.forName("utf8"))
      val jsonResult = JsonParser().parse(line).asJsonObject
      val langServicePort = jsonResult.get("languageServicePort")?.asInt
      val debugServicePort = jsonResult.get("debugServicePort")?.asInt
      val status = jsonResult.get("status")?.asString
      if (langServicePort == null || debugServicePort == null) return null
      return SessionInfo(langServicePort, debugServicePort, null, status)
    } catch (e: Exception) {
      LOG.error("Error reading/parsing session details file $sessionFile: $e")
      return null
    }
  }

  //{"status":"started","channel":"tcp","debugServicePort":18690,"languageServicePort":18648}
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
   * @throws  PSEditorServicesScriptNotFound
   */
  private fun getStartupScriptPath(): String {
    val lspInitMain = ApplicationManager.getApplication().getComponent(LSPInitMain::class.java)
    val path = lspInitMain.state.editorServicesStartupScript.trim()
    if (StringUtil.isEmpty(path)) {
      LOG.warn("PowerShell language host startup script is not specified in settings.")

      LOG.warn("Trying to guess...")
      val result = FileUtil.toCanonicalPath("${getPSExtensionDir()}/scripts/Start-EditorServices.ps1")
      if (!checkExists(result)) {
        LOG.warn("Guessed script path $result does not exist.")
        throw PSEditorServicesScriptNotFound()
      }
      return result
    } else if (!checkExists(path)) {
      LOG.warn("Specified in settings PowerShell language host startup script $path does not exist.")
      throw PSEditorServicesScriptNotFound()
    }
    return path
  }

  private fun createLogPath(): String =
      FileUtil.toCanonicalPath("${getLanguageHostLogsDir()}/EditorServices-IJ-${getSessionCount()}.log")

  private fun createSessionDetailsPath(): String =
      FileUtil.toCanonicalPath("${getPSExtensionDir()}/sessions/PSES-IJ-${System.currentTimeMillis()}-session.info")


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