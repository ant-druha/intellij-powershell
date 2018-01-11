package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.io.FileUtil
import java.io.File

object PSLanguageHostUtils {
  private val LOG: Logger = Logger.getInstance(javaClass)

  fun getLanguageHostLogsDir(): String {
    val extDir = getPSExtensionDir()
    if (extDir == null || !checkExists(extDir)) return FileUtil.toCanonicalPath(System.getProperty("idea.log.path") + "/PowerShell")
    return FileUtil.toCanonicalPath(extDir + "/logs")
  }

  fun getModulesDir(): String {
    return FileUtil.toCanonicalPath(getPSExtensionDir() + "/modules")
  }

  fun getPSExtensionDir(): String? {
    val home = System.getProperty("user.home")
    //"/Users/Andrey/.vscode/extensions/ms-vscode.powershell-1.5.1"
    val vsExtensions = FileUtil.toCanonicalPath(home + "/.vscode/extensions")
    val psExtensionDir = File(vsExtensions).listFiles { _, name -> name.contains("powershell", true) }
    val psVsCodeExtensions = if (psExtensionDir != null && psExtensionDir.isNotEmpty()) psExtensionDir[0] else null
    if (psVsCodeExtensions == null || !checkExists(psVsCodeExtensions.canonicalPath)) {
      LOG.warn("Path to PS extensions: $psVsCodeExtensions does not exist")
    }
    return psVsCodeExtensions?.canonicalPath
  }

  fun checkExists(path: String): Boolean {
    return File(path).exists()
  }
}