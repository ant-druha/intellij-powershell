package com.intellij.plugin.powershell.testFramework

import com.intellij.plugin.powershell.lang.lsp.LanguageServer
import com.intellij.testFramework.LightPlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl
import com.intellij.testFramework.junit5.fixture.tempPathFixture
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.file.Path
import kotlin.time.Duration.Companion.seconds

@ExtendWith(EdtInterceptor::class)
open class PowerShellCodeInsightTestBase {
  val tempPathFixture = tempPathFixture()
  lateinit var tempPath: Path
  lateinit var codeInsightTestFixture: CodeInsightTestFixture
  lateinit var ideaProjectTestFixture: IdeaProjectTestFixture
  val project get() = codeInsightTestFixture.project

  @BeforeEach
  fun setupFixture(testInfo: TestInfo){
    tempPath = tempPathFixture.get()
    val factory = IdeaTestFixtureFactory.getFixtureFactory()
    val fixtureBuilder = factory.createLightFixtureBuilder(null, testInfo.displayName)
    ideaProjectTestFixture = fixtureBuilder.getFixture()
    codeInsightTestFixture = IdeaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(ideaProjectTestFixture,
      TempDirTestFixtureImpl())
    codeInsightTestFixture.testDataPath = getTestDataPath()
    codeInsightTestFixture.setUp()
  }

  @AfterEach
  fun tearDownEdt() {
    runInEdt {
      codeInsightTestFixture.tearDown()
      LightPlatformTestCase.closeAndDeleteProject()
      tearDownInEdt()
    }
  }

  protected open fun tearDownInEdt() {}

  open fun getTestDataPath(): String = tempPath.toString()

  fun waitForEditorConnects(path: Path) {
    runBlocking {
      withTimeout(20.seconds) {
        LanguageServer.getInstance(project).editorLanguageServer.value.apply {
          waitForInit()
          waitForEditorConnect(path)
        }
      }
    }
  }

  fun waitForEditorManagerCreated(path: Path) {
    runBlocking {
      withTimeout(20.seconds) {
        LanguageServer.getInstance(project).editorLanguageServer.value.apply {
          waitForEditorManagerCreated(path)
        }
      }
    }
  }
}
