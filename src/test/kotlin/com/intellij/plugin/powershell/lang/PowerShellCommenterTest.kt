package com.intellij.plugin.powershell.lang

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PowerShellCommenterTest : BasePlatformTestCase() {

  fun testCommentExtension() {
    myFixture.configureByText("file.ps1", """
      <#<caret>
      #>
    """.trimIndent())
    myFixture.performEditorAction(IdeActions.ACTION_EDITOR_ENTER)
    myFixture.checkResult("""
      <#
      <caret>
      #>
    """.trimIndent())
  }
}
