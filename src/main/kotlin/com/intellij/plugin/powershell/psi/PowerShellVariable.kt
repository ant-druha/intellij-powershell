package com.intellij.plugin.powershell.psi

import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement
import com.intellij.psi.PsiElement

/**
 * Andrey 18/08/17.
 */
interface PowerShellVariable : PowerShellComponent, PowerShellReferencePsiElement, PowerShellTypedElement {
  override fun getReference(): PowerShellReferencePsiElement?
  fun getScopeName(): String?
  fun getScope(): PsiElement?
  fun getPrefix(): String
  fun getSuffix(): String?
  fun isBracedVariable(): Boolean
  /**
   * returns full name of the element, including namespace
   */
  fun getQualifiedName(): String
}