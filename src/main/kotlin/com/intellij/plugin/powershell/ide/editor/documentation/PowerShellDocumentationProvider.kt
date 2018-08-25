package com.intellij.plugin.powershell.ide.editor.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.plugin.powershell.lang.lsp.psi.LSPWrapperPsiElement
import com.intellij.psi.PsiElement

class PowerShellDocumentationProvider : AbstractDocumentationProvider() {
  override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
    return if (element is LSPWrapperPsiElement) element.getDocumentation()
    else super.generateDoc(element, originalElement)
  }

  override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
    return if (element is LSPWrapperPsiElement) element.getDocumentation()
    else super.getQuickNavigateInfo(element, originalElement)
  }
}