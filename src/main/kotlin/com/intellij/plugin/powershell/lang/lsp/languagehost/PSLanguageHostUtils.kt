package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.plugin.powershell.ide.run.checkExists
import com.intellij.plugin.powershell.ide.run.getModuleVersion
import com.intellij.plugin.powershell.ide.run.join
import java.io.File

object PSLanguageHostUtils {
  private val LOG: Logger = Logger.getInstance(javaClass)
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
    return if (isExtensionDirectoryFormat(psExtensionDir)) join(psExtensionDir, "modules/PowerShellEditorServices/Start-EditorServices.ps1")
    else join(BUNDLED_PSES_PATH, "modules/PowerShellEditorServices/Start-EditorServices.ps1")
  }

  private fun isExtensionDirectoryFormat(psExtensionDir: String): Boolean {
    return checkExists("$psExtensionDir/modules")
  }

  @Throws(PowerShellExtensionNotFound::class)
  private fun findPSExtensionsDir(): String {
    val home = System.getProperty("user.home") ?: throw PowerShellExtensionNotFound("Can not get user home directory")
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

}