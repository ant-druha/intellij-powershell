package com.intellij.plugin.powershell.ide.debugger

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.plugin.powershell.ide.PluginProjectRoot
import com.intellij.plugin.powershell.ide.run.PowerShellRunConfiguration
import com.intellij.plugin.powershell.lang.debugger.PSDebugClient
import com.intellij.plugin.powershell.lang.lsp.languagehost.terminal.PowerShellConsoleTerminalRunner
import com.intellij.terminal.TerminalExecutionConsole
import com.intellij.util.io.await
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import kotlinx.coroutines.runBlocking
import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.launch.DSPLauncher
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import org.eclipse.lsp4j.jsonrpc.Launcher
import java.io.InputStream
import java.io.OutputStream

class PowerShellDebugServiceStarter {
  companion object {

    fun startDebugServiceProcess(
      environment: ExecutionEnvironment,
      runConfiguration: PowerShellRunConfiguration,
      session: XDebugSession
    )
      : Pair<PowerShellDebugSession, DefaultExecutionResult>? {
      val processRunner = PowerShellConsoleTerminalRunner(environment.project)
      processRunner.useConsoleRepl()
      return runBlocking {
        val InOutPair = processRunner.establishDebuggerConnection()
        val process = processRunner.getProcess() ?: return@runBlocking null
        val handler = KillableProcessHandler(process, "PowerShellEditorService").apply {
          setShouldKillProcessSoftly(false)
        }

        val console = TerminalExecutionConsole(environment.project, handler)
        handler.startNotify()
        val powerShellDebugSession = startDebugService(
          InOutPair.first!!,
          InOutPair.second!!,
          session,
          runConfiguration,
          environment
        ) //todo nullcheck
        Pair(powerShellDebugSession, DefaultExecutionResult(console, handler))
      }
    }

    private suspend fun startDebugService(
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
      arguments.supportsRunInTerminalRequest = false

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
          source.path = fileURL
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
}

val ClientSessionKey = com.intellij.openapi.util.Key.create<PowerShellDebugSession>("PowerShellDebugSession")
