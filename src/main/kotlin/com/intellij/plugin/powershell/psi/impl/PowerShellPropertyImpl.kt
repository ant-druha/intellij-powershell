package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.UnfairTextRange
import com.intellij.plugin.powershell.ide.search.PowerShellComponentType
import com.intellij.plugin.powershell.psi.*
import com.intellij.psi.PsiElement
import javax.swing.Icon

open class PowerShellPropertyImpl(node: ASTNode) : /*PowerShellAbstractComponent*/PowerShellTargetVariableImpl(node), PowerShellMemberDeclaration {

  override fun getContainingClass(): PsiElement? = context

  override fun getRangeInElement(): TextRange {
    val myVar = getVariable()
    if (myVar != null) return myVar.rangeInElement
    val namId = nameIdentifier
    if (namId != null) {
      val range = namId.node.textRange
      return range.shiftRight(-node.startOffset)
    }
    return UnfairTextRange(0, this.textRange.endOffset - this.textRange.startOffset)
  }

  override fun getNameIdentifier(): PowerShellIdentifier? {
    val variable = getVariable()
    return variable?.nameIdentifier
  }

  private fun findFirstAssignmentTarget(): PowerShellVariable? {
    val assignmentVariables = findChildByClass(PowerShellAssignmentExpression::class.java)?.targetVariables
    return if (assignmentVariables != null && assignmentVariables.size > 0) assignmentVariables[0] else null
  }

  private fun getVariable(): PowerShellVariable? = findChildByClass(PowerShellTargetVariableExpression::class.java) ?: findFirstAssignmentTarget()

  override fun getPrefix(): String = getVariable()?.getPrefix() ?: ""

  override fun getSuffix(): String? = getVariable()?.getSuffix()

  override fun getScopeName(): String? = getVariable()?.getScopeName()

  override fun getIcon(flags: Int): Icon? = PowerShellComponentType.FIELD.getIcon()
}