package com.intellij.plugin.powershell.psi

import com.intellij.psi.PsiElement

/**
 * Andrey 18/08/17.
 */
interface PowerShellVariable : PowerShellQualifiedReferenceElement<PsiElement>, PowerShellComponent, PowerShellReferencePsiElement {
  override fun getReference(): PowerShellReferencePsiElement?
  fun getScopeName(): String?
  fun getScope(): PsiElement?
  fun getPrefix(): String
  fun getSuffix(): String?
}