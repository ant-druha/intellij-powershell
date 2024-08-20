package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.CantRunException
import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.runners.AsyncProgramRunner
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.toPromise
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.plugin.powershell.ide.PluginProjectRoot
import com.intellij.plugin.powershell.ide.debugger.EditorServicesDebuggerHostStarter
import com.intellij.plugin.powershell.ide.debugger.PowerShellBreakpointType
import com.intellij.plugin.powershell.ide.debugger.PowerShellDebugProcess
import com.intellij.plugin.powershell.ide.debugger.PowerShellDebugSession
import com.intellij.plugin.powershell.lang.debugger.PSDebugClient
import com.intellij.terminal.TerminalExecutionConsole
import com.intellij.util.io.await
import com.intellij.xdebugger.*
import com.intellij.xdebugger.breakpoints.XLineBreakpoint
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.threading.coroutines.adviseSuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.launch.DSPLauncher
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import org.eclipse.lsp4j.jsonrpc.Launcher
import org.jetbrains.concurrency.Promise
import java.nio.file.Path
import kotlin.io.path.Path

class PowerShellProgramDebugRunner : AsyncProgramRunner<RunnerSettings>() {

  override fun getRunnerId() = "com.intellij.plugin.powershell.ide.run.PowerShellProgramDebugRunner"

  override fun canRun(executorId: String, profile: RunProfile) =
    executorId == DefaultDebugExecutor.EXECUTOR_ID && profile is PowerShellRunConfiguration

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun execute(environment: ExecutionEnvironment, state: RunProfileState): Promise<RunContentDescriptor?> {
    val project = environment.project

    return PluginProjectRoot.getInstance(project).coroutineScope.async(Dispatchers.Default) {
      state as PowerShellScriptCommandLineState
      withContext(Dispatchers.EDT) {
        FileDocumentManager.getInstance().saveAllDocuments()
      }
      state.prepareExecution()
      val session = bootstrapDebugSession(project, environment, state)
      session.runContentDescriptor
    }.toPromise()
  }
}

suspend fun bootstrapDebugSession(
  project: Project,
  environment: ExecutionEnvironment,
  state: PowerShellScriptCommandLineState,
  listener: XDebugSessionListener? = null
): XDebugSession {
  val debuggerManager = XDebuggerManager.getInstance(project)

  val processRunner = EditorServicesDebuggerHostStarter(project)
  val inOutPair = processRunner.establishDebuggerConnection()
    ?: throw CantRunException(MessagesBundle.message("powershell.debugger.cantRunException"))
  val process = processRunner.getProcess() ?: error("Cannot get the debugger process.")
  val handler = KillableProcessHandler(process, "PowerShellEditorService").apply {
    setShouldKillProcessSoftly(false)
  }
  val console = TerminalExecutionConsole(project, handler)
  handler.startNotify()

  val client = PSDebugClient()
  val launcher: Launcher<IDebugProtocolServer> =
    DSPLauncher.createClientLauncher(client, inOutPair.first, inOutPair.second)
  launcher.startListening()

  val arguments = InitializeRequestArguments()
  arguments.clientID = "client1" // TODO: should we renumber these?
  arguments.adapterID = "adapter1"
  arguments.supportsRunInTerminalRequest = false

  val remoteProxy = launcher.remoteProxy

  remoteProxy.initialize(arguments).await()

  val (session, breakpoints) = withContext(Dispatchers.EDT) {
    val session = debuggerManager.startSession(environment, object : XDebugProcessStarter() {
      @Throws(ExecutionException::class)
      override fun start(session: XDebugSession): XDebugProcess {
        listener?.let { session.addSessionListener(it) }

        val scope = PluginProjectRoot.getInstance(project).coroutineScope
        val debugSession = PowerShellDebugSession(client, remoteProxy, session, scope)
        val executionResult = DefaultExecutionResult(console, handler)
        debugSession.sendKeyPress.adviseSuspend(Lifetime.Eternal, Dispatchers.EDT) {
          handler.processInput.write(0)
          handler.processInput.flush()
        }

        return PowerShellDebugProcess(session, executionResult, debugSession)
      }
    })

    val breakpoints = debuggerManager.breakpointManager.getBreakpoints(
      PowerShellBreakpointType::class.java
    )

    Pair(session, breakpoints)
  }

  initializeBreakpoints(breakpoints, session, remoteProxy) // TODO: Not needed?

  val targetPath = Path(state.runConfiguration.scriptPath)
  launchDebuggee(targetPath, remoteProxy)

  remoteProxy.configurationDone(ConfigurationDoneArguments()).await()
  return session
}

private suspend fun initializeBreakpoints(
  breakpoints: Iterable<XLineBreakpoint<*>>,
  debugSession: XDebugSession,
  remoteProxy: IDebugProtocolServer
) {
  if (!debugSession.areBreakpointsMuted()) {
    breakpoints.filter { it.sourcePosition != null && it.sourcePosition!!.file.exists() && it.sourcePosition!!.file.isValid && it.isEnabled }
      .groupBy { VfsUtil.virtualToIoFile(it.sourcePosition!!.file).toURI().toASCIIString() }
      .forEach { entry ->
        val fileURL = entry.key
        val breakpointArgs = SetBreakpointsArguments()
        val source = Source()
        source.path = fileURL
        breakpointArgs.source = source
        val bps = entry.value
        breakpointArgs.breakpoints = bps.map {
          val bp = it
          SourceBreakpoint().apply {
            line = bp.line + 1 // ide breakpoints line numbering starts from 0, while PSES start from 1
            condition = bp.conditionExpression?.expression
            logMessage = bp.logExpressionObject?.expression
          }
        }.toTypedArray()
        remoteProxy.setBreakpoints(breakpointArgs).await()
      }
  }
}

private suspend fun launchDebuggee(scriptPath: Path, remoteProxy: IDebugProtocolServer) {
  val launchArgs: MutableMap<String, Any> = HashMap()
  launchArgs["terminal"] = "none"
  launchArgs["script"] = scriptPath.toString()
  launchArgs["noDebug"] = false
  launchArgs["__sessionId"] = "sessionId" // TODO: should we renumber these?
  logger.info("Starting script file \"$scriptPath\" in a debug session.")
  remoteProxy.launch(launchArgs).await()
}

private val logger = logger<PowerShellDebugSession>()
