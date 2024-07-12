package com.intellij.plugin.powershell.ide.debugger

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.util.LocalTimeCounter
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.EvaluationMode
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider

class PowerShellDebuggerEditorsProvider: XDebuggerEditorsProvider() {
  private var myFileType: PowerShellFileType = PowerShellFileType.INSTANCE

  override fun getFileType(): FileType {
    return myFileType
  }

  override fun createDocument(
    project: Project,
    expression: XExpression,
    sourcePosition: XSourcePosition?,
    mode: EvaluationMode
  ): Document {
    val psiFile = PsiFileFactory.getInstance(project)
      .createFileFromText(
        "pwsh." + myFileType.getDefaultExtension(), myFileType, expression.expression,
        LocalTimeCounter.currentTime(), true
      )

    val document = checkNotNull(PsiDocumentManager.getInstance(project).getDocument(psiFile))
    return document
  }
}
