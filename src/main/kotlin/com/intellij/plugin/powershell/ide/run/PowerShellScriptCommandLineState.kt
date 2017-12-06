package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.EnvironmentUtil
import java.io.File
import java.util.*
import java.util.regex.Pattern

class PowerShellScriptCommandLineState(private val runConfiguration: PowerShellRunConfiguration, environment: ExecutionEnvironment) :
    CommandLineState(environment), RunProfileState {
  private val LOG = Logger.getInstance("#" + javaClass.name)

  override fun startProcess(): ProcessHandler {
    val command = buildCommand(runConfiguration.getScriptPath(), runConfiguration.getCommandOptions(), runConfiguration.getScriptParameters())
    val commandLine = GeneralCommandLine(command)
    val userHome = EnvironmentUtil.getValue("HOME")
    if (StringUtil.isNotEmpty(userHome)) commandLine.setWorkDirectory(userHome)
    LOG.debug("Command line: " + command.toString())
    LOG.debug("Environment: " + commandLine.parentEnvironment.toString())
    return PowerShellProcessHandler(commandLine)
  }

  private fun buildCommand(scriptPath: String, commandOptions: String, scriptParameters: String): ArrayList<String> {
    val commandString = java.util.ArrayList<String>()
    if (SystemInfo.isUnix) {
      val shell = EnvironmentUtil.getValue("SHELL")
      if (shell != null) {
        commandString.add(shell)
        commandString.add("-c")
        commandString.add("powershell" + " $commandOptions".trimEnd() + " $scriptPath" + " $scriptParameters".trimEnd())
        return commandString
      } else {
        val exec = "/usr/local/bin/powershell"
        if (File(exec).exists()) commandString.add(exec)
        else {
          LOG.warn("Can not find full path to powershell executable")
          commandString.add("powershell")
        }
      }
    } else commandString.add("powershell")
    if (!StringUtil.isEmpty(commandOptions)) {
      val options = commandOptions.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
      if (options.isNotEmpty()) commandString.addAll(Arrays.asList(*options))
    }
    commandString.add(scriptPath)
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