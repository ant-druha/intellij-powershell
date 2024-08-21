package com.intellij.plugin.powershell.testFramework

import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.ide.run.PowerShellConfigurationType
import com.intellij.plugin.powershell.ide.run.PowerShellProgramDebugRunner
import com.intellij.plugin.powershell.ide.run.PowerShellRunConfiguration
import com.intellij.plugin.powershell.ide.run.bootstrapDebugSession
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebugSessionListener
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.Signal
import java.nio.file.Path
import java.time.Duration
import java.util.concurrent.Semaphore
import kotlin.io.path.Path
import kotlin.io.path.pathString

class PowerShellTestSession(val project: Project, scriptPath: Path) {
  val waitForBackgroundTimeout: Duration = Duration.ofSeconds(30)
  val sessionListener: PowerShellDebugSessionListener = PowerShellDebugSessionListener()

  private val projectPath: Path
    get() = Path(project.basePath!!)
  private val defaultWorkingDirectory
    get() = projectPath.resolve("scripts")

  private val configuration: PowerShellRunConfiguration = createConfiguration(defaultWorkingDirectory.toString(), scriptPath.pathString)

  suspend fun startDebugSession(lifetime: Lifetime) = startDebugSession(lifetime, configuration)

  private suspend fun startDebugSession(lifetime: Lifetime, configuration: PowerShellRunConfiguration): XDebugSession {
    val executor = DefaultDebugExecutor.getDebugExecutorInstance()
    val runner = ProgramRunner.getRunner(executor.id, configuration) as PowerShellProgramDebugRunner
    val environment = ExecutionEnvironment(
      executor,
      runner,
      RunnerAndConfigurationSettingsImpl(RunManagerImpl.getInstanceImpl(project), configuration),
      project
    )
    val state = configuration.getState(executor, environment)
    state.prepareExecution()

    val session = bootstrapDebugSession(project, environment, state, sessionListener)
    lifetime.onTermination { session.stop() } // TODO: Wait for stop? Think about graceful teardown for the debug process.
    return session
  }

  private fun createConfiguration(
    customWorkingDirectory: String?,
    customScriptPath: String?
  ): PowerShellRunConfiguration {
    val type = PowerShellConfigurationType()
    val factory = type.configurationFactories.single()
    return PowerShellRunConfiguration(project, factory, "Test").apply {
      scriptPath = customScriptPath ?: projectPath.resolve("scripts/MyScript.ps1").toString()
      this.customWorkingDirectory = customWorkingDirectory
    }
  }
}

class PowerShellDebugSessionListener : XDebugSessionListener {

  // if greater than 0 → paused, resumed, stopped
  val pausedSemaphore = Semaphore(0)
  private val resumedSemaphore = Semaphore(0)
  private val stoppedSemaphore = Semaphore(0)

  private val pausedEvent = Signal<Unit>()
  private val resumedEvent = Signal<Unit>()
  private val stoppedEvent = Signal<Unit>()

  override fun sessionPaused() {
    pausedEvent.fire(Unit)
    if (pausedSemaphore.availablePermits() > 0)
      return
    pausedSemaphore.release()
  }

  override fun sessionResumed() {
    resumedEvent.fire(Unit)
    if (resumedSemaphore.availablePermits() > 0)
      return
    resumedSemaphore.release()
  }

  override fun sessionStopped() {
    stoppedEvent.fire(Unit)
    if (stoppedSemaphore.availablePermits() > 0)
      return
    stoppedSemaphore.release()
  }

}
