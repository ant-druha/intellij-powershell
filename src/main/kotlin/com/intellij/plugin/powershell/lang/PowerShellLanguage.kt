package com.intellij.plugin.powershell.lang

import com.intellij.lang.Language

class PowerShellLanguage : Language("PowerShell") {

  companion object {
    @JvmStatic
    val INSTANCE = PowerShellLanguage()

    /**
     * Language id for PowerShell LSP.
     */
    const val LSP_ID = "powershell"
  }
}
