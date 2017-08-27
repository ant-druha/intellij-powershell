package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Andrey 09/08/17.
 */
class PowerShellCodeStyleConfigurable(settings: CodeStyleSettings, cloneSettings: CodeStyleSettings?) : CodeStyleAbstractConfigurable(settings, cloneSettings, "Power Shell") {
  override fun createPanel(settings: CodeStyleSettings?): CodeStyleAbstractPanel {
    return PowerShellCodeStyleMainPanel(currentSettings, settings)
  }

  override fun getHelpTopic(): String? {
    return null
  }
}

class PowerShellCodeStyleMainPanel(currentSettings: CodeStyleSettings?, settings: CodeStyleSettings?) :
    TabbedLanguageCodeStylePanel(PowerShellLanguage.INSTANCE, currentSettings, settings)
