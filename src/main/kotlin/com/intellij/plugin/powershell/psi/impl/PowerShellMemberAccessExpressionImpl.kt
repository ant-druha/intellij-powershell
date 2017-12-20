package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellExpression
import com.intellij.plugin.powershell.psi.PowerShellQualifiedReferenceElement
import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement

open class PowerShellMemberAccessExpressionImpl(node: ASTNode) : PowerShellQualifiedReferenceExpression(node), PowerShellQualifiedReferenceElement<PowerShellExpression>, PowerShellTypedElement