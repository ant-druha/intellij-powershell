package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.impl.PowerShellPsiImplUtil
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

internal fun canAddSpaceBefore(node: ASTNode): Boolean = !isLabel(node) && !isVariableScope(node) && !isPathExpression(node)

internal fun isFinallyClauseContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.FINALLY_CLAUSE
}

internal fun isDataStatementContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.DATA_STATEMENT
}

internal fun isTrapStatementContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.TRAP_STATEMENT
}

internal fun isCatchClauseContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.CATCH_CLAUSE
}

internal fun isTryStatementBlockContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.TRY_STATEMENT
}

internal fun containsNewLines(blockNode: ASTNode?): Boolean {
  return blockNode != null && StringUtil.countNewLines(blockNode.text) >= 1
}

internal fun isRedirectionOperator(type: IElementType) = PowerShellTypes.OP_FR === type || PowerShellTypes.OP_MR === type

internal fun isCastExpressionSuperContext(node: ASTNode): Boolean {
  return node.treeParent?.treeParent?.elementType === PowerShellTypes.CAST_EXPRESSION
}

internal fun isArgumentListContext(node: ASTNode) =
    node.treeParent?.elementType === PowerShellTypes.PARENTHESIZED_ARGUMENT_LIST

internal fun isSwitchStatementContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.SWITCH_STATEMENT
}

//internal fun isFirstInChainedCall(node: ASTNode): Boolean {
//  if (node.treeParent?.elementType !== PowerShellTypes.INVOCATION_EXPRESSION) return false
//  return findSiblingSkipping(node, arrayOf(PowerShellTypes.NLS, TokenType.WHITE_SPACE, PowerShellTypes.DOT, PowerShellTypes.COLON2), false)?.elementType !== PowerShellTypes.INVOCATION_EXPRESSION
//}

internal fun isSubExpressionContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.SUB_EXPRESSION
}

internal fun isDoStatementContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.DO_STATEMENT
}

internal fun isWhileStatementContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.WHILE_STATEMENT
}

internal fun isForEachStatementContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.FOREACH_STATEMENT
}

internal fun isElseClauseContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.ELSE_CLAUSE || node.treeParent?.elementType === PowerShellTypes.ELSEIF_CLAUSE
}

internal fun isIfStatementContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.IF_STATEMENT
}

internal fun isFunctionDeclarationContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.FUNCTION_STATEMENT
}

internal fun isMethodDeclarationContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.METHOD_DECLARATION_STATEMENT
}

internal fun isParameterClauseContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.PARAMETER_CLAUSE
}

internal fun isBlockBodyContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.BLOCK_BODY
}

internal fun isTypeLiteral(node: ASTNode): Boolean {
  return node.elementType === PowerShellTypes.TYPE_LITERAL_EXPRESSION
}

internal fun isAttributeArgument(node: ASTNode): Boolean {
  return node.elementType === PowerShellTypes.ATTRIBUTE_ARGUMENT
}

internal fun isExpressionInPipelineTail(node: ASTNode): Boolean {
  return node.elementType === PowerShellTypes.COMMAND_CALL_EXPRESSION
      && findSiblingSkippingWS(node, false)?.elementType === PowerShellTypes.PIPE
}

internal fun isPipelineContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.PIPELINE
}

internal fun isConstructorDeclarationContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.CONSTRUCTOR_DECLARATION_STATEMENT
}

internal fun isCallableDeclarationContext(node: ASTNode): Boolean {
  return isFunctionDeclarationContext(node) || isMethodDeclarationContext(node) || isConstructorDeclarationContext(node)
}

internal fun isAttribute(node: ASTNode): Boolean {
  return node.elementType === PowerShellTypes.ATTRIBUTE
}

internal fun isPropertyDeclarationContext(node: ASTNode): Boolean {
  return node.treeParent.elementType === PowerShellTypes.PROPERTY_DECLARATION_STATEMENT
}

internal fun isEnumDeclarationContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.ENUM_DECLARATION_STATEMENT
}

internal fun isClassDeclarationContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.CLASS_DECLARATION_STATEMENT
}

internal fun isScriptBlockExpressionContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.SCRIPT_BLOCK_EXPRESSION
}

internal fun isHashLiteralContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.HASH_LITERAL_EXPRESSION
}

internal fun isReferenceDoubleColonOperator(type: IElementType): Boolean {
  return type === PowerShellTypes.COLON2
}

internal fun isUnaryOperator(node: ASTNode): Boolean {
  if (node.psi.context !is PowerShellUnaryExpression) return false
  val type = node.elementType
  val text = node.text
  return type === PowerShellTypes.OP_BNOT || type === PowerShellTypes.EXCL_MARK || type === PowerShellTypes.OP_NOT || type === PowerShellTypes.MM || type === PowerShellTypes.PP || type === PowerShellTypes.COMMA || type === PowerShellTypes.PLUS || type === PowerShellTypes.DASH
      || (type === PowerShellTypes.OP_C && (text.contains("split") || text.contains("join")))
}

internal fun isComparisonOperator(type: IElementType): Boolean {
  return type === PowerShellTypes.OP_C
}

internal fun isMultiplicativeOperator(type: IElementType): Boolean {
  return type == PowerShellTypes.STAR || type == PowerShellTypes.DIV || type == PowerShellTypes.PERS
}

internal fun isBitwiseOperator(type: IElementType): Boolean {
  return type == PowerShellTypes.OP_BAND || type == PowerShellTypes.OP_BOR || type == PowerShellTypes.OP_BXOR
}

internal fun isLogicalOperator(type: IElementType): Boolean {
  return type == PowerShellTypes.OP_AND || type == PowerShellTypes.OP_OR || type == PowerShellTypes.OP_XOR
}

internal fun isAssignmentOperator(type: IElementType): Boolean {
  return type === PowerShellTypes.EQ || type === PowerShellTypes.EQ_DASH || type === PowerShellTypes.EQ_PLUS || type === PowerShellTypes.EQ_STAR || type === PowerShellTypes.EQ_DIV || type === PowerShellTypes.EQ_PERS
}

internal fun isAdditiveOperator(node: ASTNode): Boolean {
  val type = node.elementType
  return (type === PowerShellTypes.PLUS || type === PowerShellTypes.DASH) && node.treeParent?.elementType === PowerShellTypes.ADDITIVE_EXPRESSION
}

internal fun isWhiteSpaceOrNls(node: ASTNode): Boolean {
  return PowerShellPsiImplUtil.isWhiteSpaceOrNls(node) || node.textLength == 0
}

internal fun isForParameter(node: ASTNode): Boolean {
  return node.treeParent.elementType === PowerShellTypes.FOR_CLAUSE && node.psi is PowerShellExpression
}

internal fun isForClauseContext(node: ASTNode): Boolean {
  return node.treeParent.elementType === PowerShellTypes.FOR_CLAUSE
}

internal fun isBinaryExpressionContext(node: ASTNode): Boolean {
  val parent = node.treeParent
  return parent != null && isBinaryExpression(parent)
}

internal fun isBinaryExpression(node: ASTNode): Boolean {
  val type = node.elementType
  return type === PowerShellTypes.LOGICAL_EXPRESSION || type === PowerShellTypes.BITWISE_EXPRESSION || type === PowerShellTypes.COMPARISON_EXPRESSION || type === PowerShellTypes.ADDITIVE_EXPRESSION || type === PowerShellTypes.MULTIPLICATIVE_EXPRESSION
}

internal fun isParenthesizedExpressionContext(node: ASTNode): Boolean {
  return node.treeParent?.elementType === PowerShellTypes.PARENTHESIZED_EXPRESSION
}

internal fun isFollowedByAttribute(childNode: ASTNode) = findSiblingSkipping(childNode, arrayOf(PowerShellTypes.NLS, TokenType.WHITE_SPACE), false)?.elementType === PowerShellTypes.ATTRIBUTE

internal fun isRhsBinaryExpressionContext(node: ASTNode): Boolean {
  if (!isBinaryExpressionContext(node)) return false
  val type = findSiblingSkipping(node, arrayOf(PowerShellTypes.NLS, TokenType.WHITE_SPACE), false)?.elementType
  return node.psi is PowerShellExpression && PowerShellTokenTypeSets.OPERATORS.contains(type)
}

internal fun isFirstNodeInForParameter(node: ASTNode): Boolean {
  val prevIEType = findSiblingSkipping(node, arrayOf(PowerShellTypes.NLS, TokenType.WHITE_SPACE), false)?.elementType
  return node.treeParent.elementType === PowerShellTypes.FOR_CLAUSE && (prevIEType === PowerShellTypes.SEMI || prevIEType === PowerShellTypes.LP)
}

internal fun isFunctionParameter(childNode: ASTNode) = isParameterClauseContext(childNode) && canBeParameter(childNode)

internal fun isCallArgument(node: ASTNode): Boolean {
  return node.psi is PowerShellExpression && node.treeParent?.elementType === PowerShellTypes.PARENTHESIZED_ARGUMENT_LIST
}

internal fun isInvocationExpressionQualifier(node: ASTNode): Boolean {
  if (node.treeParent?.elementType !== PowerShellTypes.INVOCATION_EXPRESSION) return false
  val prevElement = findSiblingSkipping(node, arrayOf(PowerShellTypes.NLS, TokenType.WHITE_SPACE), false)?.elementType
  return (prevElement === PowerShellTypes.DOT || prevElement === PowerShellTypes.COLON2) && (node.elementType === PowerShellTypes.REFERENCE_IDENTIFIER || node.psi is PowerShellExpression)
}

internal fun isFirstNodeInParameter(node: ASTNode, checkFirstParameter: Boolean = false): Boolean {
  val prevSibling = findSiblingSkipping(node, arrayOf(PowerShellTypes.NLS, TokenType.WHITE_SPACE), false)
  return prevSibling != null && (prevSibling.elementType === PowerShellTypes.COMMA || (checkFirstParameter && prevSibling.elementType === PowerShellTypes.LP))
}

internal fun canBeParameter(node: ASTNode): Boolean {
  return node.elementType === PowerShellTypes.ATTRIBUTE
      || node.elementType === PowerShellTypes.TARGET_VARIABLE_EXPRESSION
      || node.elementType === PowerShellTypes.EQ
      || node.psi is PowerShellExpression
}