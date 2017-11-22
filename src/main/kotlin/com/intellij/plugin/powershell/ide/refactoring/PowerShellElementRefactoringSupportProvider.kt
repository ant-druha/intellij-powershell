package com.intellij.plugin.powershell.ide.refactoring

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.plugin.powershell.psi.PowerShellPsiElement
import com.intellij.psi.PsiElement

class PowerShellElementRefactoringSupportProvider : RefactoringSupportProvider() {

  override fun isSafeDeleteAvailable(element: PsiElement): Boolean = isComponent(element)

  override fun isInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean = false

  override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean =
      isComponent(element) && !isBracedVariable(element) && !isVariableWithNamespace(element)

  override fun isAvailable(context: PsiElement): Boolean = context is PowerShellPsiElement

}
