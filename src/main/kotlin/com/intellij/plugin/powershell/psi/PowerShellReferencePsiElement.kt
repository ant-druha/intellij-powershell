package com.intellij.plugin.powershell.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference

/**
 * Andrey 18/08/17.
 */
interface PowerShellReferencePsiElement : PowerShellPsiElement, PsiReference {
  fun getNameElement(): PsiElement?
}