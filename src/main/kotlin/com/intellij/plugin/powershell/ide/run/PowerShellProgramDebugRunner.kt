package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.ExecutionResult
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.AsyncProgramRunner
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.showRunContent
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.rd.util.toPromise
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.plugin.powershell.ide.PluginProjectRoot
import com.intellij.plugin.powershell.ide.debugger.PowerShellDebugProcess
import com.intellij.plugin.powershell.ide.debugger.PowerShellDebugSession
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugProcessStarter
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import org.jetbrains.concurrency.Promise
import java.lang.Boolean
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
            val executionResult = state.execute(environment.executor, this@PowerShellProgramDebugRunner)
            val clientSession = environment.getUserData(ClientSessionKey)
            return PowerShellDebugProcess(session, executionResult!!, debuggerManager, clientSession!!) //todo add null check
          }
        }).runContentDescriptor
      }
      descriptor
      //withUiContext { showRunContent(executionResult, environment) }
    }.toPromise()
}

val XSessionKey = com.intellij.openapi.util.Key.create<XDebugSession>("PowerShellXDebugSession")
val ClientSessionKey = com.intellij.openapi.util.Key.create<PowerShellDebugSession>("PowerShellDebugSession")
