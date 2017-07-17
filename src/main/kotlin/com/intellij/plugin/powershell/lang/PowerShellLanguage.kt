package com.intellij.plugin.powershell.lang

import com.intellij.lang.Language


/**
 * Andrey 26/06/17.
 */
class PowerShellLanguage : Language("PowerShell") {
    override fun getDisplayName(): String {
        return super.getDisplayName()
    }

    companion object {
        @JvmStatic val INSTANCE = PowerShellLanguage()
    }

    fun getExtensions(): Array<String> {
        return arrayOf("ps1")
    }

}