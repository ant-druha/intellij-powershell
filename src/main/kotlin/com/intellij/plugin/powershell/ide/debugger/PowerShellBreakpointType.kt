package com.intellij.plugin.powershell.ide.debugger

import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.psi.PsiDocumentManager
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.intellij.xdebugger.breakpoints.XLineBreakpointType

//XsltDebuggerBundle.message("title.xslt.breakpoints")
class PowerShellBreakpointType : XLineBreakpointType<XBreakpointProperties<*>>("powershell", MessagesBundle.message("powershell.debugger.breakpoints.title")) {
  override fun canPutAt(file: VirtualFile, line: Int, project: Project): Boolean {
    val document = FileDocumentManager.getInstance().getDocument(file) ?: return false

    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: return false
    val fileType = psiFile.fileType
    if (fileType != PowerShellFileType.INSTANCE) {
      return false
    }
    return true
  }
  override fun createBreakpointProperties(file: VirtualFile, line: Int): XBreakpointProperties<*>? {
    return null
  }
}
