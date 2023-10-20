package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import com.intellij.plugin.powershell.ide.PluginAppRoot
import com.intellij.plugin.powershell.ide.run.checkExists
import com.intellij.plugin.powershell.ide.run.getModuleVersion
import com.intellij.plugin.powershell.ide.run.join
import com.intellij.util.io.awaitExit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.runInterruptible
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
  val qInner = if (SystemInfo.isWindows) '\'' else '"'
  val commandString = "(\$PSVersionTable.PSVersion, \$PSVersionTable.PSEdition) -join $qInner $qInner"
  return coroutineScope {
    try {
      process = GeneralCommandLine(arrayListOf(exePath, "-command", commandString)).createProcess()
      fun readStream(stream: InputStream) = async {
        runInterruptible { stream.reader().use { it.readText() } }
      }

      val stdOutReader = readStream(process!!.inputStream)
      readStream(process!!.errorStream)
      val exitCode = process!!.awaitExit()
      if (exitCode != 0) {
        error("Process exit code $exitCode.")
      }

      PSVersionInfo.parse(stdOutReader.await())
    } catch (e: Exception) {
      PSLanguageHostUtils.LOG.warn("Command execution failed: ${arrayListOf(exePath, "--version")} ${e.message}", e)
      throw PowerShellControlFlowException(e.message, e.cause)
    } finally {
      process?.destroy()
    }
  }
}
