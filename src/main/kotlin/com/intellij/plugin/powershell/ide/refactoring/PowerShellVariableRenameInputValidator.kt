package com.intellij.plugin.powershell.ide.refactoring

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression
import com.intellij.psi.PsiElement
import com.intellij.refactoring.rename.RenameInputValidator
import com.intellij.util.ProcessingContext

class PowerShellVariableRenameInputValidator : RenameInputValidator {
  override fun isInputValid(newName: String, variable: PsiElement, context: ProcessingContext): Boolean =
      PowerShellNameUtils.isValidName(newName, variable)

  override fun getPattern(): ElementPattern<out PsiElement> = psiElement(PowerShellTargetVariableExpression::class.java)

}