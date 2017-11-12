package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellAssignmentExpression
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression
import com.intellij.psi.PsiElement

open class PowerShellPropertyImpl(node: ASTNode) : PowerShellAbstractComponent(node) {

  override fun getNameIdentifier(): PsiElement? {
    var variable = findChildByClass(PowerShellTargetVariableExpression::class.java)
    if (variable == null) {
      val assignmentVariables = findChildByClass(PowerShellAssignmentExpression::class.java)?.targetVariables
      variable = if (assignmentVariables != null && assignmentVariables.size > 0) assignmentVariables[0] else null
    }
    return variable?.nameIdentifier
  }
}