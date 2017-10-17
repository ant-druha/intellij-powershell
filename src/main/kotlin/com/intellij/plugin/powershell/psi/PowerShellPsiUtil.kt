package com.intellij.plugin.powershell.psi

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellTypes.PATH_EXPRESSION
import com.intellij.plugin.powershell.psi.PowerShellTypes.TARGET_VARIABLE_EXPRESSION
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

object PowerShellPsiUtil {

  fun isLabel(node: ASTNode): Boolean {
    if (node.elementType != PowerShellTypes.SIMPLE_ID) return false

    val treeNext = findNextSiblingSkipping(node, arrayOf(PowerShellTypes.NLS, TokenType.WHITE_SPACE))
    val nodeNextType = treeNext.elementType
    if (nodeNextType == PowerShellTypes.SWITCH_STATEMENT || nodeNextType == PowerShellTypes.FOREACH_STATEMENT || nodeNextType == PowerShellTypes.FOR_STATEMENT || nodeNextType == PowerShellTypes.WHILE_STATEMENT
        || nodeNextType == PowerShellTypes.DO_STATEMENT)
      return true

    return false
  }

  private fun findNextSiblingSkipping(node: ASTNode, toSkip: Array<IElementType>?): ASTNode {
    var result = node.treeNext
    if (toSkip == null || toSkip.isEmpty()) return result
    while (toSkip.contains(result.elementType)) result = result.treeNext
    return result
  }

  fun isVariableScope(node: ASTNode): Boolean {
    return node.treeParent.elementType == TARGET_VARIABLE_EXPRESSION
  }

  fun isPathExpression(node: ASTNode): Boolean {
    return node.treeParent.elementType == PATH_EXPRESSION
  }
}