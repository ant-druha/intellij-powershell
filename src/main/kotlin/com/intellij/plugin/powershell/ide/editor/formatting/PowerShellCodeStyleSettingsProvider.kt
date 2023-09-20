package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

/**
 * Andrey 09/08/17.
 */
class PowerShellCodeStyleSettingsProvider : CodeStyleSettingsProvider() {

  override fun getConfigurableDisplayName(): String {
    return "PowerShell"
  }

  override fun createConfigurable(settings: CodeStyleSettings, modelSettings: CodeStyleSettings): CodeStyleConfigurable {
    return PowerShellCodeStyleConfigurable(settings, modelSettings)
  }

  override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
    return PowerShellCodeStyleSettings(settings)
  }
}