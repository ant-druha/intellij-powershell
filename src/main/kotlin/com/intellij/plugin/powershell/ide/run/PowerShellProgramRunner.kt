package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.AsyncProgramRunner
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.showRunContent
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.rd.util.toPromise
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.plugin.powershell.ide.PluginProjectRoot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import org.jetbrains.concurrency.Promise

/**
 * The only purpose of this runner is to call [RunProfileState.execute] or a background thread instead of a foreground
 * one, as our [RunProfileState] implementation requires FS access that's only possible from the background.
 */
class PowerShellProgramRunner : AsyncProgramRunner<RunnerSettings>() {

  override fun getRunnerId() = "com.intellij.plugin.powershell.ide.run.PowerShellProgramRunner"

  override fun canRun(executorId: String, profile: RunProfile) =
    executorId == DefaultRunExecutor.EXECUTOR_ID && profile is PowerShellRunConfiguration

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun execute(environment: ExecutionEnvironment, state: RunProfileState): Promise<RunContentDescriptor?> =
    PluginProjectRoot.getInstance(environment.project).coroutineScope.async(Dispatchers.Default) {
      state as PowerShellScriptCommandLineState
      state.prepareExecution()
      val executionResult = state.execute(environment.executor, this@PowerShellProgramRunner)
      val descriptor = withUiContext { showRunContent(executionResult, environment) }
      descriptor
    }.toPromise()
}
