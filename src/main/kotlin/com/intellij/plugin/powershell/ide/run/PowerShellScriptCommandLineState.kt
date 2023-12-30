package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionException
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.PtyCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.util.ProgramParametersUtil
import com.intellij.openapi.application.readAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.plugin.powershell.lang.lsp.languagehost.PowerShellNotInstalled
import com.intellij.terminal.TerminalExecutionConsole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Path
import java.util.regex.Pattern
import kotlin.io.path.Path

class PowerShellScriptCommandLineState(
  private val runConfiguration: PowerShellRunConfiguration,
  private val environment: ExecutionEnvironment) : RunProfileState {

  private lateinit var workingDirectory: Path
  suspend fun prepareExecution() {
    val project = runConfiguration.project
    val file = withContext(Dispatchers.IO) { LocalFileSystem.getInstance().findFileByIoFile(File(runConfiguration.scriptPath)) }
    val module = file?.let {
      readAction {
        ProjectRootManager.getInstance(project).fileIndex.getModuleForFile(it)
      }
    }
    workingDirectory = Path(
      ProgramParametersUtil.expandPathAndMacros(runConfiguration.workingDirectory, module, project)
    )
  }

  private fun startProcess(): ProcessHandler {
    try {
      val command = buildCommand(
        runConfiguration.executablePath ?: LSPInitMain.getInstance().getPowerShellExecutable(),
        runConfiguration.scriptPath,
        runConfiguration.getCommandOptions(),
        runConfiguration.scriptParameters
      )
      val commandLine = PtyCommandLine(command)
        .withConsoleMode(false)
        .withWorkDirectory(workingDirectory.toString())

      runConfiguration.environmentVariables.configureCommandLine(commandLine, true)
      logger.debug("Command line: $command")
      logger.debug("Environment: " + commandLine.environment.toString())
      logger.debug("Effective Environment: " + commandLine.effectiveEnvironment.toString())
      return KillableProcessHandler(commandLine)
    } catch (e: PowerShellNotInstalled) {
      logger.warn("Can not start PowerShell: ${e.message}")
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
            logger.error(e)
          } catch (e: IndexOutOfBoundsException) {
            logger.error(e)
          }
        }
      }
      commandString.addAll(matchedParams)
    }
    return commandString
  }

  override fun execute(executor: Executor?, runner: ProgramRunner<*>): ExecutionResult {
    val process = startProcess()
    val console = TerminalExecutionConsole(environment.project, process)
    return DefaultExecutionResult(console, process)
  }
}

private val logger = logger<PowerShellScriptCommandLineState>()
