package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellEnumLabelDeclaration
import com.intellij.plugin.powershell.psi.PowerShellExpression
import com.intellij.plugin.powershell.psi.PowerShellQualifiedReferenceElement
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement

open class PowerShellMemberAccessExpressionImpl(node: ASTNode) : PowerShellQualifiedReferenceExpression(node), PowerShellQualifiedReferenceElement<PowerShellExpression>, PowerShellTypedElement {
  override fun getType(): PowerShellType {
    val resolved = resolve()
    if (resolved is PowerShellEnumLabelDeclaration) {
      //for enum label references the type is the same as qualifier
      val qType = qualifier?.getType()
      if (qType != null) return qType
    } else if (resolved != null) {
      return inferTypeFromResolved(resolved)
    }
    return PowerShellType.UNKNOWN
  }

}