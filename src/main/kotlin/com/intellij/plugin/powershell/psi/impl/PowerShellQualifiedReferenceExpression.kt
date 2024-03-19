package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.Logger
import com.intellij.plugin.powershell.ide.resolve.PowerShellMemberScopeProcessor
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveResult
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.psi.PowerShellAttributesHolder
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellExpression
import com.intellij.plugin.powershell.psi.PowerShellQualifiedReferenceElement
import com.intellij.plugin.powershell.psi.types.PowerShellType

abstract class PowerShellQualifiedReferenceExpression(node: ASTNode) : PowerShellQualifiedReferenceElementImpl<PowerShellExpression>(node), PowerShellQualifiedReferenceElement<PowerShellExpression>, PowerShellExpression {

  val LOG = Logger.getInstance(javaClass)
  override fun getType(): PowerShellType {
    val resolved = resolve()
    if (resolved != null) {
      return inferTypeFromResolved(resolved)
    }
    return PowerShellType.UNKNOWN
  }

  protected open fun inferTypeFromResolved(resolved: PowerShellComponent): PowerShellType {
    //TODO[#198]: create function type and return function image here
    if (resolved is PowerShellAttributesHolder) resolved.getAttributeList().mapNotNull { it.typeLiteralExpression }.forEach { return it.getType() }
    return PowerShellType.UNKNOWN
  }

  override fun getQualifier(): PowerShellExpression? = findChildByClass(PowerShellExpression::class.java)

  override fun multiResolve(incompleteCode: Boolean): Array<PowerShellResolveResult> {
    val qType = getQualifierType()
    if (qType == PowerShellType.UNKNOWN) {
      LOG.debug("Type is unknown for reference: '$text'")
      return emptyArray()
    }
    val resolveProcessor = PowerShellResolveUtil.getMemberScopeProcessor(this)
    if (resolveProcessor != null && qType != null) {
      if (PowerShellResolveUtil.processMembersForType(qType, resolveProcessor)) return extractResults(resolveProcessor)
    }
    return super.multiResolve(incompleteCode)
  }

  protected fun extractResults(resolveProcessor: PowerShellMemberScopeProcessor): Array<PowerShellResolveResult> {
    val res = resolveProcessor.getResult()
    return Array(res.size) { res[it] }
  }

  protected fun getQualifierType(): PowerShellType? {
    return qualifier?.getType()
  }

}
