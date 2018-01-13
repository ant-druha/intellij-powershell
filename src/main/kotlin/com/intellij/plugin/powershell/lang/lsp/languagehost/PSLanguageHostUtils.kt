package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.io.FileUtil
import java.io.File

object PSLanguageHostUtils {
  private val LOG: Logger = Logger.getInstance(javaClass)

  fun getLanguageHostLogsDir(): String {
    val extDir: String
    try {
      extDir = findPSExtensionsDir()
      return if (!checkExists(extDir)) join(PathManager.getLogPath(), "PowerShell")
      else join(extDir, "logs")
    } catch (e: PowerShellExtensionNotFound) {
      return join(PathManager.getLogPath(), "PowerShell")
    }
  }

  fun getPSExtensionModulesDir(psExtensionDir: String): String {
    return join(psExtensionDir, "modules")
  }

  fun getEditorServicesStartupScript(psExtensionDir: String): String {
    return join(psExtensionDir, "scripts/Start-EditorServices.ps1")
  }

  /**
   * @throws PowerShellExtensionNotFound
   */
  fun findPSExtensionsDir(): String {
    val home = System.getProperty("user.home")
    val vsExtensions = join(home, ".vscode/extensions")
    val psExtensions = File(vsExtensions).listFiles { _, name -> name.contains("powershell", true) }
    val result = if (psExtensions.isNotEmpty()) psExtensions.sortedDescending().first() else null
    if (result == null || !result.exists()) {
      val msg = "Wrong path to PowerShell extension: $result"
      LOG.warn(msg)
      throw PowerShellExtensionNotFound(msg)
    }
    return result.canonicalPath
  }

  @Throws(PowerShellExtensionError::class)
  fun getEditorServicesModuleVersion(moduleBase: String): String? {
    return getModuleVersion(moduleBase, "PowerShellEditorServices")
  }

  /**
   * @throws PowerShellExtensionError
   */
  private fun getModuleVersion(moduleBase: String, moduleName: String): String? {
    val moduleFile = join(moduleBase, "$moduleName/$moduleName.psd1")
    if (!checkExists(moduleFile)) throw PowerShellExtensionError("Module manifest file not found: $moduleFile")
    val lines = FileUtil.loadLines(moduleFile)
    for (l in lines) {
      if (l.contains("ModuleVersion", true)) {
        //todo can be not in one line
        return l.trimStart { c -> c != '=' }.substringAfter('=').trim().trim { c -> c == '\'' }
      }
    }
    return null
  }

  fun checkExists(path: String?): Boolean {
    return FileUtil.exists(path)
  }

  fun join(vararg pathPart: String): String {
    return FileUtil.toCanonicalPath(FileUtil.join(*pathPart))
  }
}