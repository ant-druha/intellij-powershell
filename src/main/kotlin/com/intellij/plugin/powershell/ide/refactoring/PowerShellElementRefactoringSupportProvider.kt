package com.intellij.plugin.powershell.ide.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.psi.PsiElement

class PowerShellElementRefactoringSupportProvider : RefactoringSupportProvider() {

  override fun isSafeDeleteAvailable(element: PsiElement): Boolean = element is PowerShellComponent

  override fun isInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean = false

  override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean =
      isAvailable(element) && !isBracedVariable(element) && !isVariableWithNamespace(element)

  override fun isAvailable(context: PsiElement): Boolean = context is PowerShellComponent

}
