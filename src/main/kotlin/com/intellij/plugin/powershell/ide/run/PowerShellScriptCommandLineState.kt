package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.util.text.StringUtil
import java.util.*
import java.util.regex.Pattern

class PowerShellScriptCommandLineState(private val runConfiguration: PowerShellRunConfiguration, environment: ExecutionEnvironment) :
    CommandLineState(environment), RunProfileState {

  override fun startProcess(): ProcessHandler {
    var scriptPath = runConfiguration.getScriptPath()
    val scriptParameters = runConfiguration.getScriptParameters()
    val scriptOptions = runConfiguration.getScriptOptions()

    scriptPath = if (scriptPath == null) "" else scriptPath
    val commandString = ArrayList<String>()
    commandString.add("powershell")
    if (!StringUtil.isEmpty(scriptOptions)) {
      val options = scriptOptions?.split(" ".toRegex())?.dropLastWhile({ it.isEmpty() })?.toTypedArray()
      if (options != null && options.isNotEmpty()) commandString.addAll(Arrays.asList(*options))
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

    val commandLine = GeneralCommandLine(commandString)
    return PowerShellProcessHandler(commandLine)
  }
}

class PowerShellProcessHandler(commandLine: GeneralCommandLine) : KillableColoredProcessHandler(commandLine)