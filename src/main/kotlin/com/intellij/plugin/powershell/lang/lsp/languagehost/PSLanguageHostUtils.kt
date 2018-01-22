package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.io.FileUtil
import java.io.File

object PSLanguageHostUtils {
  private val LOG: Logger = Logger.getInstance(javaClass)
  val BUNDLED_PSES_PATH = join(PathManager.getPluginsPath(), "PowerShell/lib/LanguageHost")

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
    return if (isExtensionDirectoryFormat(psExtensionDir)) join(psExtensionDir, "modules")
    else psExtensionDir
  }

  fun getEditorServicesStartupScript(psExtensionDir: String): String {
    return if (isExtensionDirectoryFormat(psExtensionDir)) join(psExtensionDir, "scripts/Start-EditorServices.ps1")
    else join(BUNDLED_PSES_PATH, "scripts/Start-EditorServices.ps1")
  }

  /**
   * if this path is a 'ms-vscode.PowerShell-XXX' extension directory
   */
  private fun isExtensionDirectoryFormat(psExtensionDir: String): Boolean {
    return checkExists("$psExtensionDir/scripts") && checkExists("$psExtensionDir/modules")
  }

  @Throws(PowerShellExtensionNotFound::class)
  private fun findPSExtensionsDir(): String {
    val home = System.getProperty("user.home")?: throw PowerShellExtensionNotFound("")
    val vsExtensions = join(home, ".vscode/extensions")
    if (!checkExists(vsExtensions)) throw PowerShellExtensionNotFound("The '~/.vscode/extensions' directory does not exist")
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
  fun getEditorServicesModuleVersion(moduleBase: String): String {
    return getModuleVersion(moduleBase, "PowerShellEditorServices")
  }

  /**
   * @throws PowerShellExtensionError
   */
  private fun getModuleVersion(moduleBase: String, moduleName: String): String {
    val moduleFile = join(moduleBase, "$moduleName/$moduleName.psd1")
    if (!checkExists(moduleFile)) throw PowerShellExtensionError("Module manifest file not found: $moduleFile")
    val lines = FileUtil.loadLines(moduleFile)
    for (l in lines) {
      if (l.contains("ModuleVersion", true)) {
        //todo can be not in one line
        return l.trimStart { c -> c != '=' }.substringAfter('=').trim().trim { c -> c == '\'' }
      }
    }
    throw PowerShellExtensionError("ModuleVersion info not found in: $moduleFile")
  }

  fun checkExists(path: String?): Boolean {
    return FileUtil.exists(path)
  }

  fun join(vararg pathPart: String): String {
    return FileUtil.toCanonicalPath(FileUtil.join(*pathPart))
  }
}