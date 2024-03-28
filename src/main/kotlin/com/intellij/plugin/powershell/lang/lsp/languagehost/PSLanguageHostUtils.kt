package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.plugin.powershell.ide.PluginAppRoot
import com.intellij.plugin.powershell.ide.run.checkExists
import com.intellij.plugin.powershell.ide.run.getModuleVersion
import com.intellij.plugin.powershell.ide.run.join
import com.intellij.util.io.awaitExit
import kotlinx.coroutines.*
import kotlinx.coroutines.future.asCompletableFuture
import java.io.InputStream
import java.util.concurrent.CompletableFuture

object PSLanguageHostUtils {
  val LOG: Logger = Logger.getInstance(javaClass)
  val BUNDLED_PSES_PATH = join(PathManager.getPluginsPath(), "PowerShell/lib/LanguageHost")

  fun getPSExtensionModulesDir(psExtensionDir: String): String {
    return if (isExtensionDirectoryFormat(psExtensionDir)) join(psExtensionDir, "modules")
    else psExtensionDir
  }

  fun getEditorServicesStartupScript(psExtensionDir: String): String {
    return when {
      isExtensionDirectoryFormat(psExtensionDir) -> join(psExtensionDir, "modules/PowerShellEditorServices/Start-EditorServices.ps1")
      isStandAloneDirectoryFormat(psExtensionDir) -> "$psExtensionDir/PowerShellEditorServices/Start-EditorServices.ps1"
      else -> join(BUNDLED_PSES_PATH, "modules/PowerShellEditorServices/Start-EditorServices.ps1")
    }
  }

  private fun isExtensionDirectoryFormat(psExtensionDir: String): Boolean {
    return checkExists("$psExtensionDir/modules")
  }

  private fun isStandAloneDirectoryFormat(psExtensionDir: String): Boolean {
    return checkExists("$psExtensionDir/PowerShellEditorServices/Start-EditorServices.ps1")
  }

  @Throws(PowerShellExtensionError::class)
  fun getEditorServicesModuleVersion(moduleBase: String): String {
    return getModuleVersion(moduleBase, "PowerShellEditorServices")
  }

  fun getPowerShellVersion(powerShellExePath: String): CompletableFuture<PSVersionInfo> {
    return PluginAppRoot.getInstance().coroutineScope.async(Dispatchers.IO) {
      readPowerShellVersion(powerShellExePath)
    }.asCompletableFuture()
  }
}

private suspend fun readPowerShellVersion(exePath: String): PSVersionInfo {
  var process: Process? = null
  val commandString = "(\$PSVersionTable.PSVersion, \$PSVersionTable.PSEdition) -join ' '"
  val commandLine = GeneralCommandLine(exePath, "–NoProfile", "-NonInteractive", "-Command", commandString)
  return coroutineScope {
    try {
      process = commandLine.createProcess()
      fun readStream(stream: InputStream) = async {
        runInterruptible { stream.reader().use { it.readText() } }
      }

      val stdOutReader = readStream(process!!.inputStream)
      val stdErrReader = readStream(process!!.errorStream)
      val exitCode = process!!.awaitExit()
      if (exitCode != 0) {
        val stdOut = stdOutReader.await()
        val stdErr = stdErrReader.await()
        val message = buildString {
          append("Process exit code $exitCode.")
          if (stdOut.isNotBlank()) {
            append("\nStandard output:\n$stdOut")
          }
          if (stdErr.isNotBlank()) {
            append("\nStandard error:\n$stdErr")
          }
        }
        error(message)
      }

      PSVersionInfo.parse(stdOutReader.await().trim())
    } catch (e: Exception) {
      PSLanguageHostUtils.LOG.warn("Command execution failed for ${commandLine.preparedCommandLine}", e)
      throw PowerShellControlFlowException(e.message, e.cause)
    } finally {
      process?.destroy()
    }
  }
}
