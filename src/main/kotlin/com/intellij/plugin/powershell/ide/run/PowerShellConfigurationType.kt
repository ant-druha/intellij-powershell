package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.PowerShellIcons

class PowerShellConfigurationType : ConfigurationTypeBase("PowerShellRunType", "PowerShell", "Run Configuration for PowerShell", PowerShellIcons.FILE) {
  init {
    addFactory(object : ConfigurationFactory(this) {
      override fun isConfigurationSingletonByDefault(): Boolean = true
      override fun canConfigurationBeSingleton(): Boolean = false
      override fun createTemplateConfiguration(project: Project): RunConfiguration = PowerShellRunConfiguration(project, this, "Template config")
    })
  }
}