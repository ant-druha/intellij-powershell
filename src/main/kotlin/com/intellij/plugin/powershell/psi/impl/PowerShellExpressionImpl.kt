package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.lang.resolve.PowerShellTypeUtil
import com.intellij.plugin.powershell.psi.PowerShellExpression
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement

open class PowerShellExpressionImpl(node: ASTNode) : PowerShellPsiElementImpl(node), PowerShellExpression, PowerShellTypedElement {
  override fun getType(): PowerShellType {

    return PowerShellTypeUtil.inferExpressionType(this)
  }
}