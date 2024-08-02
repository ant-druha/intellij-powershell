package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionException
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.util.ProgramParametersUtil
import com.intellij.openapi.application.readAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.runAndLogException
import com.intellij.openapi.options.advanced.AdvancedSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.io.NioFiles.toPath
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.plugin.powershell.ide.PluginProjectRoot
import com.intellij.plugin.powershell.ide.debugger.PowerShellBreakpointType
import com.intellij.plugin.powershell.ide.debugger.PowerShellDebugSession
import com.intellij.plugin.powershell.lang.debugger.PSDebugClient
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.plugin.powershell.lang.lsp.languagehost.EditorServicesLanguageHostStarter
import com.intellij.plugin.powershell.lang.lsp.languagehost.PowerShellNotInstalled
import com.intellij.terminal.TerminalExecutionConsole
import com.intellij.util.io.await
import com.intellij.util.text.nullize
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.launch.DSPLauncher
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import org.eclipse.lsp4j.jsonrpc.Launcher
import org.jetbrains.annotations.TestOnly
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class PowerShellScriptCommandLineState(
  val runConfiguration: PowerShellRunConfiguration,
  private val environment: ExecutionEnvironment
) : RunProfileState {

  @TestOnly
  lateinit var workingDirectory: Path
  suspend fun prepareExecution() {
    val project = runConfiguration.project
    val file = withContext(Dispatchers.IO) { LocalFileSystem.getInstance().findFileByIoFile(File(runConfiguration.scriptPath)) }
    val module = file?.let {
      readAction {
        ProjectRootManager.getInstance(project).fileIndex.getModuleForFile(it)
      }
    }
    workingDirectory = runConfiguration.customWorkingDirectory.nullize(nullizeSpaces = true)?.let {
      toPath(ProgramParametersUtil.expandPathAndMacros(it, module, project))
    } ?: getDefaultWorkingDirectory(toPath(runConfiguration.scriptPath))
  }

  private fun startProcess(): ProcessHandler {
    try {
      val command = buildCommand(
        runConfiguration.executablePath ?: LSPInitMain.getInstance().getPowerShellExecutable(),
        runConfiguration.scriptPath,
        runConfiguration.getCommandOptions(),
        runConfiguration.scriptParameters
      )
      val commandLine = GeneralCommandLine(command)
        //.withConsoleMode(false)
        .withWorkDirectory(workingDirectory.toString())
        .withCharset(getTerminalCharSet())

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

  override fun execute(executor: Executor?, runner: ProgramRunner<*>): ExecutionResult? {
    val process = startProcess()
    val console = TerminalExecutionConsole(environment.project, process)
    return DefaultExecutionResult(console, process)
    /* if (executor?.id == DefaultRunExecutor.EXECUTOR_ID) {
      val process = startProcess()
      val console = TerminalExecutionConsole(environment.project, process)
      return DefaultExecutionResult(console, process)
    } else if (executor?.id == DefaultDebugExecutor.EXECUTOR_ID) {
      val processRunner = EditorServicesLanguageHostStarter(environment.project)
      processRunner.useConsoleRepl()
      return runBlocking {
        val InOutPair = processRunner.establishDebuggerConnection()
        val process = processRunner.getProcess() ?: return@runBlocking null
        val handler = KillableProcessHandler(process, "PowerShellEditorService")
        val console = TerminalExecutionConsole(environment.project, handler)
        handler.startNotify()
        val session = environment.getUserData(XSessionKey)
        processDebuging(InOutPair.first!!, InOutPair.second!!, session!!) //todo nullcheck
        DefaultExecutionResult(console, handler)
      }
    } else {
      error("Unknown executor")
    }*/
  }
  /*private fun processDebuging(inputStream: InputStream, outputStream: OutputStream, debugSession: XDebugSession){
    val targetPath = runConfiguration.scriptPath

    val client = PSDebugClient(debugSession)
    val launcher: Launcher<IDebugProtocolServer> = DSPLauncher.createClientLauncher(client, inputStream, outputStream)
    launcher.startListening()

    val arguments = InitializeRequestArguments()
    arguments.clientID = "client1"
    arguments.adapterID = "adapter1"
    arguments.setSupportsRunInTerminalRequest(false)

    val remoteProxy = launcher.remoteProxy

    val capabilities: Capabilities = remoteProxy.initialize(arguments)[10, TimeUnit.SECONDS]

    val scope = PluginProjectRoot.getInstance(environment.project).coroutineScope

    val powerShellDebugSession = PowerShellDebugSession(client, remoteProxy, debugSession, scope, debugSession)
    environment.putUserData(ClientSessionKey, powerShellDebugSession)
    val allBreakpoints = XDebuggerManager.getInstance(environment.project).breakpointManager.getBreakpoints(PowerShellBreakpointType::class.java)
    allBreakpoints.filter{x -> x.sourcePosition != null && x.sourcePosition!!.file.exists() && x.sourcePosition!!.file.isValid}
      .groupBy { x -> VfsUtil.virtualToIoFile(x.sourcePosition!!.file).toURI().toASCIIString() }
      .forEach { entry ->

        val fileURL = entry.key

      val breakpointArgs = SetBreakpointsArguments()
      val source: Source = Source()
      source.setPath(fileURL)
      breakpointArgs.source = source
      val bps = entry.value
      breakpointArgs.breakpoints = bps.map {
        val bp = it
        SourceBreakpoint().apply {
          line = bp.line + 1
          condition = bp.conditionExpression?.expression
          logMessage = bp.logExpressionObject?.expression
        }
      }.toTypedArray()
      val setBreakpointsResponse = remoteProxy.setBreakpoints(breakpointArgs).join()
      val breakpointsResponse: Array<Breakpoint> = setBreakpointsResponse.breakpoints
    }
    // Add a breakpoint.
    /*val breakpointArgs = SetBreakpointsArguments()
    val source: Source = Source()
    source.setPath(targetPath)
    breakpointArgs.source = source
    val sourceBreakpoint = SourceBreakpoint()
    sourceBreakpoint.line = 1
    val breakpoints = arrayOf(sourceBreakpoint)
    breakpointArgs.breakpoints = breakpoints
    val future = remoteProxy.setBreakpoints(breakpointArgs)
    val setBreakpointsResponse = future[10, TimeUnit.SECONDS]
    val breakpointsResponse: Array<Breakpoint> = setBreakpointsResponse.breakpoints*/

    val launchArgs: MutableMap<String, Any> = HashMap()
    launchArgs["terminal"] = "none"
    launchArgs["script"] = targetPath
    launchArgs["noDebug"] = false
    launchArgs["__sessionId"] = "sessionId"
    val launch = remoteProxy.launch(launchArgs).join()

// Signal that the configuration is finished
    remoteProxy.configurationDone(ConfigurationDoneArguments())
  }*/
}



private fun getTerminalCharSet(): Charset {
  val name = AdvancedSettings.getString("terminal.character.encoding")
  return logger.runAndLogException { charset(name) } ?: Charsets.UTF_8
}

private val logger = logger<PowerShellScriptCommandLineState>()
