package com.intellij.plugin.powershell.lang

import com.intellij.lang.Language


/**
 * Andrey 26/06/17.
 */
class PowerShellLanguage : Language("PowerShell") {

  companion object {
    @JvmStatic
    val INSTANCE = PowerShellLanguage()
  }

}
