package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.ide.resolve.PSMethodScopeProcessor
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveResult
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.ide.resolve.PsNames
import com.intellij.plugin.powershell.psi.PowerShellExpression
import com.intellij.plugin.powershell.psi.PowerShellQualifiedReferenceElement
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration
import com.intellij.plugin.powershell.psi.types.PowerShellClassType
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement

open class PowerShellInvocationExpressionImpl(node: ASTNode) : PowerShellQualifiedReferenceExpression(node), PowerShellQualifiedReferenceElement<PowerShellExpression>, PowerShellTypedElement {

  override fun getType(): PowerShellType {
    if (isConstructorCall()) {
      //this means that this is a call 'new' -> can infer from qualifier
      val qType = getQualifierType()
      if (qType != null) return qType
    }
    return super.getType()
  }

  override fun multiResolve(incompleteCode: Boolean): Array<PowerShellResolveResult> {
    if (isConstructorCall()) {
      //TODO[#195] where is the best place to implement resolve to a default constructor?
      val qType = getQualifierType()
      if (qType is PowerShellClassType) {
        val qClass = qType.resolve()
        if (qClass is PowerShellTypeDeclaration && PowerShellResolveUtil.hasDefaultConstructor(qClass)) {
          val res = PowerShellResolveResult(qClass)
          return Array(1) { res }
        }
      } else if (qType != null && qType != PowerShellType.UNKNOWN) {
        val refName = referenceName
        if (refName != null) {
          val resolveProcessor = PSMethodScopeProcessor(refName)
          if (PowerShellResolveUtil.processMembersForType(qType, resolveProcessor)) return extractResults(resolveProcessor)
        }
      }
    }
    return super.multiResolve(incompleteCode)
  }

  private fun isConstructorCall() = PsNames.CONSTRUCTOR_CALL.equals(referenceName, true)

}
