package com.intellij.plugin.powershell.ide.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.PowerShellIcons
import com.intellij.psi.PsiDirectory

class NewPowerShellFileAction : CreateFileFromTemplateAction("PowerShell script",
                                                             "Creates a PowerShell script file from template",
                                                             PowerShellIcons.FILE), DumbAware {
  override fun getActionName(directory: PsiDirectory?, newName: String?, templateName: String?): String = "New PowerShell script$newName"

  override fun buildDialog(project: Project?, directory: PsiDirectory?, builder: CreateFileFromTemplateDialog.Builder?) {
    builder?.setTitle("New PowerShell file")?.addKind("PowerShell file", PowerShellIcons.FILE, "PowerShell File.ps1")
  }
}