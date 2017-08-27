package com.intellij.plugin.powershell.psi

import com.intellij.psi.PsiElement

/**
 * Andrey 24/08/17.
 */
interface PowerShellCallableReference : PowerShellReference {
  fun getNameElement(): PsiElement?
}