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
import com.intellij.util.EnvironmentUtil
import java.util.*
import java.util.regex.Pattern

class PowerShellScriptCommandLineState(private val runConfiguration: PowerShellRunConfiguration, environment: ExecutionEnvironment) :
    CommandLineState(environment), RunProfileState {
  private val LOG = Logger.getInstance("#" + javaClass.name)

  override fun startProcess(): ProcessHandler {
    try {
      val command = buildCommand(runConfiguration.getScriptPath(), runConfiguration.getCommandOptions(), runConfiguration.getScriptParameters())
      val commandLine = GeneralCommandLine(command)
      val userHome = EnvironmentUtil.getValue("HOME")
      if (StringUtil.isNotEmpty(userHome)) commandLine.setWorkDirectory(userHome)
      LOG.debug("Command line: " + command.toString())
      LOG.debug("Environment: " + commandLine.parentEnvironment.toString())
      return PowerShellProcessHandler(commandLine)
    } catch (e: PowerShellNotInstalled) {
      LOG.warn("Can not start PowerShell: ${e.message}")
      throw ExecutionException(e.message, e)
    }
  }

  private fun buildCommand(scriptPath: String, commandOptions: String, scriptParameters: String): ArrayList<String> {
    val commandString = java.util.ArrayList<String>()
    commandString.add(findPsExecutable())
    if (!StringUtil.isEmpty(commandOptions)) {
      val options = commandOptions.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
      if (options.isNotEmpty()) commandString.addAll(Arrays.asList(*options))
    }
    val escaped = if (scriptPath.contains(' ')) escapePath(scriptPath) else scriptPath
    commandString.add(escaped)
    if (!StringUtil.isEmpty(scriptParameters)) {
      val regex = Pattern.compile("\"([^\"]*)\"|(\\w+)")
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