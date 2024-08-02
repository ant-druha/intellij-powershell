package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionException
import com.intellij.execution.ExecutionResult
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.runners.AsyncProgramRunner
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.showRunContent
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.rd.util.toPromise
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.plugin.powershell.ide.PluginProjectRoot
import com.intellij.plugin.powershell.ide.debugger.PowerShellBreakpointType
import com.intellij.plugin.powershell.ide.debugger.PowerShellDebugProcess
import com.intellij.plugin.powershell.ide.debugger.PowerShellDebugSession
import com.intellij.plugin.powershell.lang.debugger.PSDebugClient
import com.intellij.plugin.powershell.lang.lsp.languagehost.terminal.PowerShellConsoleTerminalRunner
import com.intellij.terminal.TerminalExecutionConsole
import com.intellij.util.io.await
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugProcessStarter
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.launch.DSPLauncher
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import org.eclipse.lsp4j.jsonrpc.Launcher
import org.jetbrains.concurrency.Promise
import java.io.InputStream
import java.io.OutputStream
import java.lang.Boolean
import java.util.concurrent.TimeUnit
import kotlin.OptIn
import kotlin.String
import kotlin.Throws

/**
 * The main purpose of this runner is to call [RunProfileState.execute] or a background thread instead of a foreground
 * one, as our [RunProfileState] implementation requires FS access that's only possible from the background.
 */
class PowerShellProgramDebugRunner : AsyncProgramRunner<RunnerSettings>() {

  override fun getRunnerId() = "com.intellij.plugin.powershell.ide.run.PowerShellProgramDebugRunner"

  override fun canRun(executorId: String, profile: RunProfile) =
    executorId == DefaultDebugExecutor.EXECUTOR_ID && profile is PowerShellRunConfiguration

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun execute(environment: ExecutionEnvironment, state: RunProfileState): Promise<RunContentDescriptor?> =
    PluginProjectRoot.getInstance(environment.project).coroutineScope.async(Dispatchers.Default) {
      state as PowerShellScriptCommandLineState
      withUiContext {
        FileDocumentManager.getInstance().saveAllDocuments()
      }
      state.prepareExecution()
      val descriptor = withUiContext {
        val debuggerManager = XDebuggerManager.getInstance(environment.project)
        debuggerManager.startSession(environment, object : XDebugProcessStarter() {
          @Throws(ExecutionException::class)
          override fun start(session: XDebugSession): XDebugProcess {
            environment.putUserData(XSessionKey, session)
            //val executionResult = state.execute(environment.executor, this@PowerShellProgramDebugRunner)
            //val clientSession = environment.getUserData(ClientSessionKey)
            val result = startDebugging(environment, state.runConfiguration, session)
            return PowerShellDebugProcess(session, result?.second!!, result.first) //todo add null check
          }
        }).runContentDescriptor
      }
      descriptor
      //withUiContext { showRunContent(executionResult, environment) }
    }.toPromise()

private fun startDebugging(environment: ExecutionEnvironment, runConfiguration: PowerShellRunConfiguration, session: XDebugSession)
: Pair<PowerShellDebugSession, DefaultExecutionResult>? {
  val processRunner = PowerShellConsoleTerminalRunner(environment.project)
  processRunner.useConsoleRepl()
  return runBlocking {
    val InOutPair = processRunner.establishDebuggerConnection()
    val process = processRunner.getProcess() ?: return@runBlocking null
    val handler = KillableProcessHandler(process, "PowerShellEditorService")

    val console = TerminalExecutionConsole(environment.project, handler)
    handler.startNotify()
    val powerShellDebugSession = processDebugging(InOutPair.first!!, InOutPair.second!!, session, runConfiguration, environment) //todo nullcheck
    Pair(powerShellDebugSession, DefaultExecutionResult(console, handler))
  }
}

  private suspend fun processDebugging(
    inputStream: InputStream, outputStream: OutputStream, debugSession: XDebugSession,
    runConfiguration: PowerShellRunConfiguration, environment: ExecutionEnvironment
  ): PowerShellDebugSession {
    val targetPath = runConfiguration.scriptPath

    val client = PSDebugClient(debugSession)
    val launcher: Launcher<IDebugProtocolServer> = DSPLauncher.createClientLauncher(client, inputStream, outputStream)
    launcher.startListening()

    val arguments = InitializeRequestArguments()
    arguments.clientID = "client1"
    arguments.adapterID = "adapter1"
    arguments.setSupportsRunInTerminalRequest(false)

    val remoteProxy = launcher.remoteProxy

    val capabilities: Capabilities = remoteProxy.initialize(arguments).await()

    val scope = PluginProjectRoot.getInstance(environment.project).coroutineScope

    val powerShellDebugSession = PowerShellDebugSession(client, remoteProxy, debugSession, scope, debugSession)
    environment.putUserData(ClientSessionKey, powerShellDebugSession)
    val allBreakpoints = XDebuggerManager.getInstance(environment.project).breakpointManager.getBreakpoints(
      PowerShellBreakpointType::class.java
    )
    allBreakpoints.filter { x -> x.sourcePosition != null && x.sourcePosition!!.file.exists() && x.sourcePosition!!.file.isValid }
      .groupBy { x -> VfsUtil.virtualToIoFile(x.sourcePosition!!.file).toURI().toASCIIString() }
      .forEach { entry ->

        val fileURL = entry.key

        val breakpointArgs = SetBreakpointsArguments()
        val source = Source()
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
        remoteProxy.setBreakpoints(breakpointArgs).await()
      }

    val launchArgs: MutableMap<String, Any> = HashMap()
    launchArgs["terminal"] = "none"
    launchArgs["script"] = targetPath
    launchArgs["noDebug"] = false
    launchArgs["__sessionId"] = "sessionId"
    remoteProxy.launch(launchArgs).await()

    // Signal that the configuration is finished
    remoteProxy.configurationDone(ConfigurationDoneArguments())
    return powerShellDebugSession
  }
}

val XSessionKey = com.intellij.openapi.util.Key.create<XDebugSession>("PowerShellXDebugSession")
val ClientSessionKey = com.intellij.openapi.util.Key.create<PowerShellDebugSession>("PowerShellDebugSession")
