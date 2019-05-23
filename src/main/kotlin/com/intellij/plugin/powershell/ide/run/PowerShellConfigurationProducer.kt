package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.RunConfigurationProducer
import com.intellij.openapi.util.Ref
import com.intellij.plugin.powershell.psi.impl.PowerShellFile
import com.intellij.psi.PsiElement

class PowerShellConfigurationProducer : RunConfigurationProducer<PowerShellRunConfiguration>(PowerShellConfigurationType()) {

  override fun setupConfigurationFromContext(configuration: PowerShellRunConfiguration, context: ConfigurationContext, sourceElement: Ref<PsiElement>): Boolean {
    val elem = context.psiLocation
    val file = (elem?.containingFile ?: return false) as? PowerShellFile ?: return false
    val vFile = file.virtualFile
    val scriptPath = if (vFile != null) file.virtualFile.path else null
    if (scriptPath != null) {
      configuration.scriptPath = scriptPath
      val parts = scriptPath.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      if (parts.isNotEmpty()) {
        configuration.name = parts[parts.size - 1]
      }
    }
    return true
  }


  override fun isConfigurationFromContext(configuration: PowerShellRunConfiguration, context: ConfigurationContext): Boolean {
    val file = context.psiLocation?.containingFile ?: return false
    val currentFile = file.virtualFile
    return currentFile!=null && currentFile.path == configuration.scriptPath
  }
}