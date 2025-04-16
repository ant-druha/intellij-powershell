package com.intellij.plugin.powershell.ide.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.plugin.powershell.PowerShellIcons
import com.intellij.plugin.powershell.lang.lsp.LanguageServer

class PowerShellConsoleAction : AnAction(PowerShellIcons.FILE) {

  override fun actionPerformed(e: AnActionEvent) {
    val project = e.project ?: return
    val server = LanguageServer.getInstance(project).serverWithConsoleProcess.value
    ProgressManager.getInstance().run(object : Task.Backgroundable(project, "Starting PowerShell terminal console", false) {
      override fun run(indicator: ProgressIndicator) {
        indicator.text = "Starting PowerShell terminal console..."
        server.start()
      }
    })
  }
}
