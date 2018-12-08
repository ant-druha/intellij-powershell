/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.ide.listeners

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class LSPTypedHandler : TypedHandlerDelegate() {

  override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
    return Result.CONTINUE
  }
}