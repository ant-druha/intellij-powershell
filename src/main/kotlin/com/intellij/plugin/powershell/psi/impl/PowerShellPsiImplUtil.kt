package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.annotations.Contract

private fun isWhiteSpace(node: ASTNode): Boolean {
  return node.elementType === TokenType.WHITE_SPACE
}

fun ASTNode.isWhiteSpaceOrNls(): Boolean {
  return isWhiteSpace(this) || elementType === PowerShellTypes.NEWLINE || elementType === PowerShellTypes.LF
}

val PowerShellAssignmentExpression.targetVariables: List<PowerShellTargetVariableExpression>
  get() {
    val result: MutableList<PowerShellTargetVariableExpression> = ArrayList()
    val targets = PsiTreeUtil.findChildrenOfAnyType(
      this,
      PowerShellTargetVariableExpression::class.java
    )
    if (!targets.isEmpty()) result.addAll(targets)
    return result
  }

val PowerShellAssignmentExpression.rhsElement: PowerShellPsiElement?
  get() {
    val child = this.firstChild ?: return null
    val rhsNode = TreeUtil.findSibling(child.node, PowerShellTypes.EQ)
    if (rhsNode != null) {
      return PsiTreeUtil.getNextSiblingOfType(
        rhsNode.psi,
        PowerShellPsiElement::class.java
      )
    }
    return null
  }

val PowerShellMemberAccessExpression.identifier: PsiElement?
  get() {
    return this.referenceIdentifier
  }

val PowerShellInvocationExpression.identifier: PsiElement?
  get() {
    return referenceIdentifier
  }

val PowerShellCastExpression.castType: PowerShellType
  get() {
    val typeLiteral = checkNotNull(
      PsiTreeUtil.getChildOfType(
        this,
        PowerShellTypeLiteralExpression::class.java
      )
    )
    return typeLiteral.typeElement.getType()
  }

@Contract(pure = true)
fun PsiElement.isTypeDeclarationContext(): Boolean {
  val context = context
  return context is PowerShellClassDeclarationStatement || context is PowerShellEnumDeclarationStatement
}
