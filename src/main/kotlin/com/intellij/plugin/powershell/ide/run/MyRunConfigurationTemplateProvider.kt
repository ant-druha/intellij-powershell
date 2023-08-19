package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.impl.RunConfigurationTemplateProvider
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.plugin.powershell.lang.lsp.ide.settings.FormUIUtil

class MyRunConfigurationTemplateProvider : RunConfigurationTemplateProvider {
  override fun getRunConfigurationTemplate(factory: ConfigurationFactory, runManager: RunManagerImpl): RunnerAndConfigurationSettingsImpl? {
    val templateConfiguration = factory.createTemplateConfiguration(runManager.project, runManager) as? PowerShellRunConfiguration ?: return null
    templateConfiguration.executablePath = FormUIUtil.getGlobalSettingsExecutablePath()
    return RunnerAndConfigurationSettingsImpl(runManager, templateConfiguration, true)
  }
}