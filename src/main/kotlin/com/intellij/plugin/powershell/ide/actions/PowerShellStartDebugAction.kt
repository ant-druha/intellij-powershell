package com.intellij.plugin.powershell.ide.actions

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.PtyCommandLine
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.runners.showRunContent
import com.intellij.execution.util.ExecUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.runAndLogException
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.options.advanced.AdvancedSettings
import com.intellij.openapi.progress.ModalTaskOwner.project
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.withUiContext
import com.intellij.plugin.powershell.ide.run.PowerShellScriptCommandLineState
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.psi.PsiDocumentManager
import com.intellij.terminal.TerminalExecutionConsole
import java.nio.charset.Charset


class PowerShellStartDebugAction : AnAction(){
  val logger = logger<PowerShellStartDebugAction>()

  override fun actionPerformed(e: AnActionEvent) {
    logger.info("PowerShellStartDebugAction LOG STARTED")
    val currentProject: Project = e.project ?: return
    var currentDoc = FileEditorManager.getInstance(currentProject).selectedTextEditor?.getDocument()
    val psiFile = PsiDocumentManager.getInstance(currentProject).getPsiFile(currentDoc ?: return)
    val vFile = psiFile!!.originalFile.virtualFile
    val path = vFile.path
    val pwshExe = LSPInitMain.getInstance().getPowerShellExecutable()
    val commandLine = GeneralCommandLine(pwshExe, "-File", "c:\\debugger.ps1", path, "7")
    commandLine.setCharset(Charset.forName("UTF-8"))
    commandLine.setWorkDirectory(currentProject.basePath)
    val result = ExecUtil.execAndGetOutput(commandLine)
    logger.info(result.toString())
  }

  private fun getTerminalCharSet(): Charset {
    val name = AdvancedSettings.getString("terminal.character.encoding")
    return logger.runAndLogException { charset(name) } ?: Charsets.UTF_8
  }
}

