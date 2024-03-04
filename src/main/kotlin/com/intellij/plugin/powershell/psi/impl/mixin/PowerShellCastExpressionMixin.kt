package com.intellij.plugin.powershell.psi.impl.mixin

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellCastExpression
import com.intellij.plugin.powershell.psi.impl.PowerShellExpressionImplGen
import com.intellij.plugin.powershell.psi.impl.castType
import com.intellij.plugin.powershell.psi.types.PowerShellType

abstract class PowerShellCastExpressionMixin(node: ASTNode?) : PowerShellExpressionImplGen(node), PowerShellCastExpression {

  override fun getType(): PowerShellType {
    return castType
  }
}
