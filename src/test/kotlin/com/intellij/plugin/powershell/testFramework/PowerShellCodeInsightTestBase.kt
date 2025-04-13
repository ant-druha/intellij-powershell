package com.intellij.plugin.powershell.testFramework

import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl
import com.intellij.testFramework.junit5.fixture.tempPathFixture
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import java.nio.file.Path
import kotlin.time.Duration.Companion.seconds

open class PowerShellCodeInsightTestBase {
  val tempPathFixture = tempPathFixture()
  lateinit var tempPath: Path
  lateinit var codeInsightTestFixture: CodeInsightTestFixture

  val project get() = codeInsightTestFixture.project

  @BeforeEach
  fun setupFixture(testInfo: TestInfo){
    tempPath = tempPathFixture.get()
    val factory = IdeaTestFixtureFactory.getFixtureFactory()
    val fixtureBuilder = factory.createLightFixtureBuilder(null, testInfo.displayName)
    val fixture = fixtureBuilder.getFixture()
    codeInsightTestFixture = IdeaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(fixture,
      TempDirTestFixtureImpl())
    codeInsightTestFixture.testDataPath = getTestDataPath()
    codeInsightTestFixture.setUp()
  }

  @AfterEach
  fun tearDownEdt() {
    runInEdt {
      LSPInitMain.getInstance().dispose() // TODO: Remove this after LSPInitMain is refactored
      codeInsightTestFixture.tearDown()
      tearDownInEdt()
    }
  }

  protected open fun tearDownInEdt() {}

  open fun getTestDataPath(): String = tempPath.toString()

  fun waitForEditorConnects(path: Path) {
    runBlocking {
      withTimeout(20.seconds) {
        LSPInitMain.getEditorLanguageServer(project).apply {
          waitForInit()
          waitForEditorConnect(path)
        }
      }
    }
  }

  fun waitForEditorManagerCreated(path: Path) {
    runBlocking {
      withTimeout(20.seconds) {
        LSPInitMain.getEditorLanguageServer(project).apply {
          waitForEditorManagerCreated(path)
        }
      }
    }
  }
}
