package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.InvalidDataException
import com.intellij.openapi.util.WriteExternalException
import com.intellij.openapi.util.text.StringUtil
import org.jdom.Element

class PowerShellRunConfiguration(project: Project, configurationFactory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase(project, configurationFactory, name) {

  private val SCRIPT_PATH_URL = "scriptUrl"
  private val SCRIPT_PARAMETERS = "scriptParameters"
  private val COMMAND_OPTIONS = "commandOptions"

  private var scriptPath: String? = null
  private var scriptParameters: String? = null
  private var commandOptions: String? = null

  override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = PowerShellRunSettingsEditor(project, this)

  @Throws(ExecutionException::class)
  override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? =
      PowerShellScriptCommandLineState(this, environment)

   fun getScriptPath(): String = StringUtil.notNullize(scriptPath)

   fun getScriptParameters(): String = StringUtil.notNullize(scriptParameters)

   fun setScriptPath(scriptPath: String) {
    this.scriptPath = scriptPath
  }

   fun setScriptParameters(scriptParameters: String) {
    this.scriptParameters = scriptParameters
  }

  @Throws(InvalidDataException::class)
  override fun readExternal(element: Element) {
    super.readExternal(element)
    val scriptUrl = element.getAttributeValue(SCRIPT_PATH_URL)
    val scriptParams = element.getAttributeValue(SCRIPT_PARAMETERS)
    val commandOptions = element.getAttributeValue(COMMAND_OPTIONS)
    if (!StringUtil.isEmpty(scriptUrl)) {
      scriptPath = scriptUrl
    }
    if (!StringUtil.isEmpty(scriptParams)) {
      scriptParameters = scriptParams
    }
    if (!StringUtil.isEmpty(commandOptions)) {
      this.commandOptions = commandOptions
    }
  }

  @Throws(WriteExternalException::class)
  override fun writeExternal(element: Element) {
    super.writeExternal(element)
    if (!StringUtil.isEmpty(scriptPath)) {
      element.setAttribute(SCRIPT_PATH_URL, scriptPath)
    }
    if (!StringUtil.isEmpty(scriptParameters)) {
      element.setAttribute(SCRIPT_PARAMETERS, scriptParameters)
    }
    if (!StringUtil.isEmpty(commandOptions)) {
      element.setAttribute(COMMAND_OPTIONS, commandOptions)
    }
  }

   fun getCommandOptions(): String = StringUtil.notNullize(commandOptions)

   fun setCommandOptions(commandOptions: String) {
    this.commandOptions = commandOptions
  }
}