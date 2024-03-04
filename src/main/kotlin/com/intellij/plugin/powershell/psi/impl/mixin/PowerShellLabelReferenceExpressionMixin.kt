package com.intellij.plugin.powershell.psi.impl.mixin

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellLabelReferenceExpression
import com.intellij.plugin.powershell.psi.impl.PowerShellReferencePsiElementImpl
import com.intellij.plugin.powershell.psi.types.PowerShellType

abstract class PowerShellLabelReferenceExpressionMixin(
  node: ASTNode
) : PowerShellReferencePsiElementImpl(node), PowerShellLabelReferenceExpression {

  override fun getType(): PowerShellType {
    return PowerShellType.UNKNOWN
  }
}
