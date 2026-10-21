// SPDX-FileCopyrightText: 2026 intellij-powershell contributors <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.actions

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.plugin.powershell.testFramework.PowerShellTestBase
import com.intellij.testFramework.junit5.TestApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

@TestApplication
class NewPowerShellModuleFileActionTest : PowerShellTestBase() {
  @Test
  fun testActionAndTemplateAreRegistered() {
    val action = ActionManager.getInstance().getAction("NewPowerShellModuleFile")
    assertInstanceOf(NewPowerShellModuleFileAction::class.java, action)
    assertEquals("PowerShell module", action.templatePresentation.text)

    val template = FileTemplateManager.getInstance(project).getInternalTemplate("PowerShell Module.psm1")
    assertEquals("", template.text)
  }
}
