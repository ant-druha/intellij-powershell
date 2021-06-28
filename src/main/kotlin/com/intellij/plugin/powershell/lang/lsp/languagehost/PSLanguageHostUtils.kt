package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.SystemInfo
import com.intellij.plugin.powershell.ide.run.checkExists
import com.intellij.plugin.powershell.ide.run.getModuleVersion
import com.intellij.plugin.powershell.ide.run.join
import com.intellij.util.concurrency.AppExecutorUtil
import org.jetbrains.concurrency.AsyncPromise
import org.jetbrains.concurrency.CancellablePromise
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

object PSLanguageHostUtils {
  val LOG: Logger = Logger.getInstance(javaClass)
  val BUNDLED_PSES_PATH = join(PathManager.getPluginsPath(), "PowerShell/lib/LanguageHost")

  fun getLanguageHostLogsDir(): String {
    val extDir: String
    return try {
      extDir = findPSExtensionsDir()
      if (!checkExists(extDir)) join(PathManager.getLogPath(), "PowerShell") else join(extDir, "logs")
    } catch (e: PowerShellExtensionNotFound) {
      join(PathManager.getLogPath(), "PowerShell")
    }
  }

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

  @Throws(PowerShellExtensionNotFound::class)
  private fun findPSExtensionsDir(): String {
    val home = System.getProperty("user.home") ?: throw PowerShellExtensionNotFound("Can not get user home directory")
    val vsExtensions = join(home, ".vscode/extensions")
    if (!checkExists(vsExtensions)) throw PowerShellExtensionNotFound("The '~/.vscode/extensions' directory does not exist")
    val psExtensions = File(vsExtensions).listFiles { _, name -> name.contains("powershell", true) }
    val result = if (psExtensions != null && psExtensions.isNotEmpty()) psExtensions.max()!! else null
    if (result == null || !result.exists()) {
      val msg = "Wrong path to PowerShell extension: $result"
      LOG.warn(msg)
      throw PowerShellExtensionNotFound(msg)
    }
    return result.canonicalPath
  }

  @Throws(PowerShellExtensionError::class)
  fun getEditorServicesModuleVersion(moduleBase: String): String {
    return getModuleVersion(moduleBase, "PowerShellEditorServices")
  }

  fun getPowerShellVersion(powerShellExePath: String): CancellablePromise<String> {
    val cancellablePromise: CancellablePromise<String>
    if (ApplicationManager.getApplication().isDispatchThread) {
      cancellablePromise = ReadAction.nonBlocking<String> { readPowerShellVersion(powerShellExePath, null) }
        .submit(AppExecutorUtil.getAppExecutorService())
    } else {
      cancellablePromise = AsyncPromise()
      val indicator = ProgressManager.getInstance().progressIndicator
      ProgressManager.getInstance().runInReadActionWithWriteActionPriority(
        {
          try {
            indicator.checkCanceled()
            val powerShellVersion = readPowerShellVersion(powerShellExePath, indicator)
            cancellablePromise.setResult(powerShellVersion)
          } catch (e: Exception) {
            when (e) {
              is ProcessCanceledException -> {
                cancellablePromise.setError(e)
              }
              is RuntimeException -> {
                cancellablePromise.setError(e)
              }
              else -> {
                cancellablePromise.setError(e)
              }
            }
          }

        }, indicator
      )
    }
    return cancellablePromise
  }
}

private const val readPSVersionCommandProcessLock = "readPSVersionCommandProcessLock"

@Throws(ProcessCanceledException::class, RuntimeException::class, PowerShellControlFlowException::class)
fun readPowerShellVersion(exePath: String, indicator: ProgressIndicator? = null): String {
  synchronized(readPSVersionCommandProcessLock) {
    var br: BufferedReader? = null
    var er: BufferedReader? = null
    var process: Process? = null
    val qInner = if (SystemInfo.isWindows) '\'' else '"'
    val commandString = "-join(${qInner}PowerShell $qInner, \$PSVersionTable.PSVersion,$qInner $qInner, \$PSVersionTable.PSEdition)"
    try {
      process = GeneralCommandLine(arrayListOf(exePath, "-command", commandString)).createProcess()
      for (i in 1..6) {
        process.waitFor(2000L, TimeUnit.MILLISECONDS)
        indicator?.checkCanceled()
      }
      if (process.isAlive) {
        throw RuntimeException("Execution timed out for : ${arrayListOf(exePath, "--version")}")
      }
      if (process.exitValue() != 0) {
        throw RuntimeException("Execution failed with code ${process.exitValue()}: ${arrayListOf(exePath, "--version")}")
      }
      br = BufferedReader(InputStreamReader(process.inputStream))
      er = BufferedReader(InputStreamReader(process.errorStream))
      return if (br.ready()) br.readLine() else ""
    } catch (e: Exception) {
      PSLanguageHostUtils.LOG.warn("Command execution failed: ${arrayListOf(exePath, "--version")} ${e.message}", e)
      throw PowerShellControlFlowException(e.message, e.cause)
    } finally {
      br?.close()
      er?.close()
      process?.destroy()
    }
  }
}
