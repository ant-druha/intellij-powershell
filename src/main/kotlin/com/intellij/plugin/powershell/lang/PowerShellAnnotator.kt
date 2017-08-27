package com.intellij.plugin.powershell.lang

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter
import com.intellij.plugin.powershell.psi.PowerShellCommandName
import com.intellij.psi.PsiElement

/**
 * Andrey 05/08/17.
 */
class PowerShellAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element is PowerShellCommandName) {
      createInfoAnnotation(holder, element, PowerShellSyntaxHighlighter.COMMAND_NAME)
    }

  }

  private fun createInfoAnnotation(holder: AnnotationHolder,
                                   element: PsiElement?,
                                   attributeKey: TextAttributesKey?) {
    if (element != null && attributeKey != null) {
      holder.createInfoAnnotation(element, null).textAttributes = attributeKey
    }
  }
}