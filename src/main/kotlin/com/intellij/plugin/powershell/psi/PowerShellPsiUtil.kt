package com.intellij.plugin.powershell.psi

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellTypes.PATH_EXPRESSION
import com.intellij.plugin.powershell.psi.PowerShellTypes.TARGET_VARIABLE_EXPRESSION
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType


fun isLabel(node: ASTNode): Boolean {
  if (node.elementType != PowerShellTypes.SIMPLE_ID) return false

  val treeNext = findSiblingSkipping(node, arrayOf(PowerShellTypes.NLS, TokenType.WHITE_SPACE))
  val nodeNextType = treeNext?.elementType
  if (nodeNextType == PowerShellTypes.SWITCH_STATEMENT || nodeNextType == PowerShellTypes.FOREACH_STATEMENT || nodeNextType == PowerShellTypes.FOR_STATEMENT || nodeNextType == PowerShellTypes.WHILE_STATEMENT
      || nodeNextType == PowerShellTypes.DO_STATEMENT)
    return true

  return false
}

internal fun findSiblingSkipping(node: ASTNode, toSkip: Array<IElementType>, forward: Boolean = true): ASTNode? {
  var result = if (forward) node.treeNext else node.treePrev
  if (toSkip.isEmpty()) return result
  while (result != null && toSkip.contains(result.elementType)) result = if (forward) result.treeNext else result.treePrev
  return result
}

internal fun findSiblingSkippingWS(node: ASTNode, forward: Boolean = true): ASTNode? {
  return findSiblingSkipping(node, arrayOf(PowerShellTypes.NLS, TokenType.WHITE_SPACE), forward)
}

fun isVariableScope(node: ASTNode): Boolean {
  return node.treeParent.elementType == TARGET_VARIABLE_EXPRESSION
}

fun isPathExpression(node: ASTNode): Boolean {
  return node.treeParent.elementType == PATH_EXPRESSION
}