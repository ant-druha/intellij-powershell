package com.intellij.plugin.powershell.lang.lsp.psi

import com.intellij.plugin.powershell.ide.search.PowerShellComponentType
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.psi.PsiElement
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionItemKind

interface LSPWrapperPsiElement : PowerShellComponent {

  fun getCompletionKind(): CompletionItemKind
  fun getCompletionItem(): CompletionItem?
  fun getDocumentation(): String?
  override fun getName(): String?
  override fun getParent(): PsiElement?
  fun setType(type: PowerShellComponentType)
  fun getType(): PowerShellComponentType
}