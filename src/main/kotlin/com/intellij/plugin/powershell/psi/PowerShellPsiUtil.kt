package com.intellij.plugin.powershell.psi

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellTypes.*
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType


fun isLabelContext(node: ASTNode): Boolean = node.treeParent?.elementType === LABEL

internal fun findSiblingSkipping(node: ASTNode, toSkip: Array<IElementType>, forward: Boolean = true): ASTNode? {
  var result = if (forward) node.treeNext else node.treePrev
  if (toSkip.isEmpty()) return result
  while (result != null && toSkip.contains(result.elementType)) result = if (forward) result.treeNext else result.treePrev
  return result
}

internal fun findSiblingSkippingWS(node: ASTNode, forward: Boolean = true): ASTNode? {
  return findSiblingSkipping(node, arrayOf(NLS, TokenType.WHITE_SPACE), forward)
}

fun isTargetVariableContext(node: ASTNode): Boolean {
  return node.treeParent.elementType == TARGET_VARIABLE_EXPRESSION
}

fun isFunctionDeclarationContext(node: ASTNode): Boolean {
  return node.treeParent.elementType == FUNCTION_STATEMENT
}

fun isPathExpressionContext(node: ASTNode): Boolean {
  return node.treeParent.elementType == PATH_EXPRESSION
}
