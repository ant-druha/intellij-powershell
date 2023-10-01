package com.intellij.plugin.powershell.lang

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.openapi.util.SystemInfo
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.TempDirTestFixture
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

class PowerShellCompletionTests : BasePlatformTestCase() {

  override fun getTestDataPath() = "src/test/resources/testData"

  override fun createTempDirTestFixture(): TempDirTestFixture {
    return TempDirTestFixtureImpl()
  }

  fun testCompletion() {
    if (SystemInfo.isWindows) return // TODO[#151]: Unstable on Windows

    val psiFile = myFixture.configureByFile("codeinsight/completion.ps1")
    runBlocking {
      withTimeout(20.seconds) {
        LSPInitMain.getEditorLanguageServer(project).apply {
          waitForInit()
          waitForEditorConnect(psiFile.virtualFile.toNioPath())
        }
      }
    }

    myFixture.complete(CompletionType.BASIC)
    val lookupElementStrings = myFixture.lookupElementStrings
    TestCase.assertNotNull(lookupElementStrings)
    assertContainsElements(lookupElementStrings!!, "Get-Alias")
  }
}
