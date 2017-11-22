package com.intellij.plugin.powershell.ide.refactoring

import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression
import com.intellij.plugin.powershell.psi.PowerShellTypes
import com.intellij.plugin.powershell.psi.PowerShellVariable
import com.intellij.psi.PsiElement


internal fun isBracedVariable(element: PsiElement?): Boolean = element is PowerShellTargetVariableExpression
    && element.firstChild?.node?.elementType === PowerShellTypes.BRACED_VAR_START

internal fun isComponent(element: PsiElement): Boolean = element is PowerShellComponent

internal fun isVariableWithNamespace(element: PsiElement): Boolean =
    element is PowerShellVariable && StringUtil.isNotEmpty(element.getNamespace())
