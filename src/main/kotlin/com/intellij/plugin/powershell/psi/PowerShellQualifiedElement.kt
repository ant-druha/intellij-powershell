package com.intellij.plugin.powershell.psi

import com.intellij.psi.PsiElement

/**
 * Andrey 18/08/17.
 */
interface PowerShellQualifiedElement : PowerShellPsiElement {
  fun getQualifier(): PsiElement?
  /**
   * returns full name of the element, including namespace
   */
  fun getQualifiedName(): String
}