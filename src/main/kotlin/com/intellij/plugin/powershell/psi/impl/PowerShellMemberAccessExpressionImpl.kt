package com.intellij.plugin.powershell.psi.impl

import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.ide.resolve.PowerShellMemberScopeProcessor
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement
import com.intellij.psi.PsiElement
import java.util.*

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

  override fun getVariants(): Array<Any> {
    val qType = getQualifierType()
    if (qType != null && qType != PowerShellType.UNKNOWN) {
      val resolveProcessor = PowerShellMemberScopeProcessor()
      PowerShellResolveUtil.processMembersForType(qType, true, resolveProcessor)
      val res = extractResults(resolveProcessor)
      val lookupElements = ArrayList<LookupElement>()
      for (r in res) {
        val e = r.element
        addLookupElement(e, lookupElements)
      }
      return lookupElements.toArray()
    }
    return super.getVariants()
  }

  private fun addLookupElement(e: PsiElement, lookupElements: ArrayList<LookupElement>) {
    val icon = e.getIcon(0)
    val res: LookupElementBuilder = when (e) {
      is PowerShellVariable -> {
        val lookupString = e.name ?: e.text
        LookupElementBuilder.create(e, lookupString).withIcon(icon).withPresentableText(lookupString)
      }
      is PowerShellCallableDeclaration -> LookupElementBuilder.create(e).withIcon(icon).withInsertHandler(ParenthesesInsertHandler.WITH_PARAMETERS)
      is PowerShellComponent -> LookupElementBuilder.create(e).withIcon(icon)
      else -> LookupElementBuilder.create(e)
    }
    lookupElements.add(res)
  }
}