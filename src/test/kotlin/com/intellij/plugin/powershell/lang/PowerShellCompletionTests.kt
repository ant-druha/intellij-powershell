package com.intellij.plugin.powershell.lang

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.plugin.powershell.testFramework.PowerShellCodeInsightTestBase
import com.intellij.testFramework.junit5.TestApplication
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@TestApplication
class PowerShellCompletionTests : PowerShellCodeInsightTestBase() {

  override fun getTestDataPath() = "src/test/resources/testData"

  @Test
  fun testCompletion() {
    val psiFile = codeInsightTestFixture.configureByFile("codeinsight/completion.ps1")

    waitForEditorConnects(psiFile.virtualFile.toNioPath())
    codeInsightTestFixture.complete(CompletionType.BASIC)
    val lookupElementStrings = codeInsightTestFixture.lookupElementStrings
    Assertions.assertNotNull(lookupElementStrings)
    Assertions.assertTrue { lookupElementStrings!!.contains("Get-Alias") }
  }
}
