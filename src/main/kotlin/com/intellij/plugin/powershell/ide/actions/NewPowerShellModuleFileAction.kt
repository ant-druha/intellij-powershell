// SPDX-FileCopyrightText: 2026 intellij-powershell contributors <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.PowerShellIcons
import com.intellij.psi.PsiDirectory

class NewPowerShellModuleFileAction : CreateFileFromTemplateAction("PowerShell module",
                                                                   "Creates a PowerShell module file from template",
                                                                   PowerShellIcons.FILE), DumbAware {
  override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String = "New PowerShell module $newName"

  override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
    builder.setTitle("New PowerShell module")?.addKind("PowerShell module", PowerShellIcons.FILE, "PowerShell Module.psm1")
  }
}
