package com.intellij.plugin.powershell.ide.refactoring

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.StandardPatterns.or
import com.intellij.plugin.powershell.psi.PowerShellFunctionStatement
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression
import com.intellij.psi.PsiElement
import com.intellij.refactoring.rename.RenameInputValidator
import com.intellij.util.ProcessingContext

class PowerShellElementRenameInputValidator : RenameInputValidator {
  override fun isInputValid(newName: String, element: PsiElement, context: ProcessingContext): Boolean =
    PowerShellNameUtils.isValidName(newName, element)

  override fun getPattern(): ElementPattern<out PsiElement> =
    or(
      psiElement(PowerShellTargetVariableExpression::class.java),
      psiElement(PowerShellFunctionStatement::class.java),
      psiElement(PowerShellMemberDeclaration::class.java)
    )

}
