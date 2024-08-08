package com.intellij.plugin.powershell

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import javax.swing.Icon

/**
 * Andrey 17/07/17.
 */
class PowerShellFileType : LanguageFileType(PowerShellLanguage.INSTANCE) {

  companion object {
    @JvmStatic
    val INSTANCE = PowerShellFileType()
  }

  override fun getIcon(): Icon {
    return PowerShellIcons.FILE
  }

  override fun getName(): String {
    return "PowerShell"
  }

  override fun getDefaultExtension(): String {
    return "ps1"
  }

  override fun getDescription(): String {
    return "PowerShell file"
  }
}
