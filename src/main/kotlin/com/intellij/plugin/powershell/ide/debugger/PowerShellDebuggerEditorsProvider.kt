package com.intellij.plugin.powershell.ide.debugger

import com.intellij.javascript.debugger.execution.xDebugProcessStarter
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.testFramework.utils.editor.saveToDisk
import com.intellij.util.LocalTimeCounter
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.EvaluationMode
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider
import java.util.concurrent.atomic.AtomicInteger

val documentSessionKey = com.intellij.openapi.util.Key.create<XDebugSession>("DocumentSessionKey")

class PowerShellDebuggerEditorsProvider(val xDebugSession: XDebugSession? = null, val fileName: String = "pwsh"): XDebuggerEditorsProvider() {
  private var myFileType: PowerShellFileType = PowerShellFileType.INSTANCE

  companion object {
    private var documentId = AtomicInteger(0);
  }

  override fun getFileType(): FileType {
    return myFileType
  }

  override fun createDocument(
    project: Project,
    expression: XExpression,
    sourcePosition: XSourcePosition?,
    mode: EvaluationMode
  ): Document {
    val id = documentId.getAndIncrement()
    val psiFile = PsiFileFactory.getInstance(project)
      .createFileFromText(
        "$fileName$id." + myFileType.getDefaultExtension(), myFileType, expression.expression,
        LocalTimeCounter.currentTime(), true
      )
    val document = checkNotNull(PsiDocumentManager.getInstance(project).getDocument(psiFile))
    //document.saveToDisk()
    val session = xDebugSession ?: XDebuggerManager.getInstance(project).currentSession
    document.putUserData(documentSessionKey, session)
    return document
  }
}
