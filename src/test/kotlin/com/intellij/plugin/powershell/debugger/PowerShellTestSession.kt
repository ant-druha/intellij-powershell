package com.intellij.plugin.powershell.debugger

import com.intellij.execution.ExecutionException
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.ide.debugger.PowerShellDebugProcess
import com.intellij.plugin.powershell.ide.run.*
import com.intellij.xdebugger.*
import kotlinx.coroutines.runBlocking
import java.nio.file.Path
import java.time.Duration
import java.util.concurrent.Semaphore
import kotlin.io.path.Path
import com.jetbrains.rd.util.reactive.Signal
import kotlin.io.path.pathString

class PowerShellTestSession(val project: Project, val scriptPath: Path) {
  val waitForStopTimeout: Duration = Duration.ofMinutes(3)
  val waitForBackgroundTimeout: Duration = Duration.ofSeconds(10)
  val sessionListener: PowerShellDebugSessionListener = PowerShellDebugSessionListener()

  private val projectPath: Path
    get() = Path(project.basePath!!)
  private val defaultWorkingDirectory
    get() = projectPath.resolve("scripts")

  val configuration: PowerShellRunConfiguration = createConfiguration(defaultWorkingDirectory.toString(), scriptPath.pathString)

  fun startDebugSession() = startDebugSession(configuration)

  fun startDebugSession(configuration: PowerShellRunConfiguration): XDebugSession {
    val executor = DefaultDebugExecutor.getDebugExecutorInstance()
    val runner = ProgramRunner.getRunner(executor.id, configuration) as PowerShellProgramDebugRunner
    val environment = ExecutionEnvironment(
      executor,
      runner,
      RunnerAndConfigurationSettingsImpl(RunManagerImpl.getInstanceImpl(project), configuration),
      project
    )
    val state = configuration.getState(executor, environment)
    runBlocking { state.prepareExecution() }
    val debuggerManager = XDebuggerManager.getInstance(environment.project)

    val session = debuggerManager.startSession(environment, object : XDebugProcessStarter() {
      @Throws(ExecutionException::class)
      override fun start(session: XDebugSession): XDebugProcess {
        environment.putUserData(XSessionKey, session)
        val executionResult = state.execute(environment.executor, runner)
        val clientSession = environment.getUserData(ClientSessionKey)
        return PowerShellDebugProcess(
          session,
          executionResult,
          clientSession!!
        )
      }
    })
    session.addSessionListener(sessionListener)
    return session
  }

  fun createConfiguration(
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

  // if greater than 0 -> paused, resumed, stopped
  val pausedSemaphore = Semaphore(0)
  val resumedSemaphore = Semaphore(0)
  val stoppedSemaphore = Semaphore(0)

  val pausedEvent = Signal<Unit>()
  val resumedEvent = Signal<Unit>()
  val stoppedEvent = Signal<Unit>()

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
