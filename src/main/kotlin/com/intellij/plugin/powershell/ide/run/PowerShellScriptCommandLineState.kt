package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.lang.lsp.languagehost.PowerShellNotInstalled
import java.util.regex.Pattern

class PowerShellScriptCommandLineState(private val runConfiguration: PowerShellRunConfiguration, environment: ExecutionEnvironment) :
    CommandLineState(environment), RunProfileState {
  private val LOG = Logger.getInstance("#" + javaClass.name)

  override fun startProcess(): ProcessHandler {
    try {
      val command = buildCommand(
        runConfiguration.executablePath,
        runConfiguration.scriptPath,
        runConfiguration.getCommandOptions(),
        runConfiguration.scriptParameters
      )
      val commandLine = GeneralCommandLine(command)
      commandLine.setWorkDirectory(runConfiguration.workingDirectory)
      runConfiguration.environmentVariables.configureCommandLine(commandLine, true)
      LOG.debug("Command line: $command")
      LOG.debug("Environment: " + commandLine.environment.toString())
      LOG.debug("Effective Environment: " + commandLine.effectiveEnvironment.toString())
      return PowerShellProcessHandler(commandLine)
    } catch (e: PowerShellNotInstalled) {
      LOG.warn("Can not start PowerShell: ${e.message}")
      throw ExecutionException(e.message, e)
    }
  }

  private fun buildCommand(executablePath: String, scriptPath: String, commandOptions: String, scriptParameters: String): ArrayList<String> {
    val commandString = ArrayList<String>()
    commandString.add(executablePath)
    if (!StringUtil.isEmpty(commandOptions)) {
      val options = commandOptions.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      if (options.isNotEmpty()) commandString.addAll(mutableListOf(*options))
    }
    commandString.add("-File")
    commandString.add(scriptPath)
    if (!StringUtil.isEmpty(scriptParameters)) {
      val regex = Pattern.compile("\"([^\"]*)\"|([^ ]+)")
      val matchedParams = ArrayList<String>()
      val matcher = regex.matcher(scriptParameters)
      while (matcher.find()) {
        for (i in 1..matcher.groupCount()) {
          try {
            val p = matcher.group(i)
            if (!StringUtil.isEmpty(p)) matchedParams.add(p)
          } catch (e: IllegalStateException) {
          } catch (e: IndexOutOfBoundsException) {
          }

        }
      }
      commandString.addAll(matchedParams)
    }
    return commandString
  }
}

class PowerShellProcessHandler(commandLine: GeneralCommandLine) : KillableColoredProcessHandler(commandLine)