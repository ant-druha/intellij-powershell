package com.intellij.plugin.powershell.lang

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.plugin.powershell.testFramework.PowerShellCodeInsightTestBase
import com.intellij.testFramework.junit5.TestApplication
import org.junit.jupiter.api.Test

@TestApplication
class PowerShellCommenterTest : PowerShellCodeInsightTestBase() {

  @Test
  fun testCommentExtension() {
    codeInsightTestFixture.configureByText(
      "file.ps1", """
      <#<caret>
      #>
    """.trimIndent()
    )
    waitForEditorManagerCreated(codeInsightTestFixture.file.virtualFile.toNioPath())
    codeInsightTestFixture.performEditorAction(IdeActions.ACTION_EDITOR_ENTER)
    waitForEditorManagerCreated(codeInsightTestFixture.file.virtualFile.toNioPath())
    codeInsightTestFixture.checkResult(
      """
      <#
      <caret>
      #>
    """.trimIndent()
    )
    waitForEditorManagerCreated(codeInsightTestFixture.file.virtualFile.toNioPath())
  }
}
