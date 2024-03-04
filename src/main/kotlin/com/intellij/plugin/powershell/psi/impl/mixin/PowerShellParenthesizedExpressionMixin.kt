package com.intellij.plugin.powershell.psi.impl.mixin

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellExpression
import com.intellij.plugin.powershell.psi.PowerShellParenthesizedExpression
import com.intellij.plugin.powershell.psi.impl.PowerShellExpressionImplGen
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.psi.util.PsiTreeUtil

abstract class PowerShellParenthesizedExpressionMixin(
  node: ASTNode?
) : PowerShellExpressionImplGen(node), PowerShellParenthesizedExpression {

  override fun getType(): PowerShellType {
    val exprInParenthesis = PsiTreeUtil.getChildOfType(
      this,
      PowerShellExpression::class.java
    )
    return exprInParenthesis?.getType() ?: PowerShellType.UNKNOWN
  }
}
