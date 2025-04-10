package com.intellij.plugin.powershell.testFramework

import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.testFramework.junit5.fixture.projectFixture
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.nio.file.Path
import kotlin.io.path.Path

abstract class PowerShellTestBase {
  protected val projectFixture = projectFixture()

  lateinit var project: Project

  val projectPath: Path
    get() = Path(project.basePath!!)

  @BeforeEach
  open fun setUp() {
    project = projectFixture.get()
  }

  @AfterEach
  open fun tearDown() {
  }

  @AfterEach
  fun tearDownEdt() {
    runInEdt {
      LSPInitMain.getInstance().dispose() // TODO: Remove this after LSPInitMain is refactored
      tearDownInEdt()
    }
  }

  protected open fun tearDownInEdt() {}
}
