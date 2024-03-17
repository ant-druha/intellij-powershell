package com.intellij.plugin.powershell.ide.run

import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kotlinx.coroutines.runBlocking
import java.nio.file.Path
import kotlin.io.path.Path

class PowerShellRunConfigurationTests : BasePlatformTestCase() {

  private val projectPath: Path
    get() = Path(project.basePath!!)
  private val defaultWorkingDirectory
    get() = projectPath.resolve("scripts")

  fun testCustomWorkingDirectoryPath() {
    val customDir = projectPath.resolve("ttt")
    assertWorkingDirectory(custom = customDir.toString(), expected = customDir)
  }

  fun testNoCustomWorkingDirectory() {
    assertWorkingDirectory(custom = null, expected = defaultWorkingDirectory)
  }

  fun testEmptyCustomWorkingDirectory() {
    assertWorkingDirectory(custom = "", expected = defaultWorkingDirectory)
  }

  fun testCustomWorkingDirectoryPathVariable() {
    assertWorkingDirectory(custom = "\$PROJECT_DIR$/foobar", expected = projectPath.resolve("foobar"))
  }

  fun testInvalidWorkingDir() {
    assertWorkingDirectory(custom = invalidPath, expected = defaultWorkingDirectory)
  }
  fun testInvalidScriptPath() {
    assertWorkingDirectory(custom = null, scriptPath = invalidPath, expected = Path(System.getProperty("user.home")))
  }

  private fun assertWorkingDirectory(custom: String?, scriptPath: String? = null, expected: Path) {
    val configuration = createConfiguration(custom, scriptPath)
    val executor = DefaultRunExecutor.getRunExecutorInstance()
    val runner = ProgramRunner.getRunner(executor.id, configuration) as PowerShellProgramRunner
    val environment = ExecutionEnvironment(
      executor,
      runner,
      RunnerAndConfigurationSettingsImpl(RunManagerImpl.getInstanceImpl(project), configuration),
      project
    )
    val state = configuration.getState(executor, environment)
    runBlocking { state.prepareExecution() }
    assertEquals(expected, state.workingDirectory)
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

private const val invalidPath = "\u0000"
