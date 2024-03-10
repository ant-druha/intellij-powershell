package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.formatting.*
import com.intellij.formatting.templateLanguages.BlockWithParent
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.PowerShellTypes.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings.*
import com.intellij.psi.formatter.FormatterUtil
import com.intellij.psi.formatter.WrappingUtil
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.impl.source.SourceTreeToPsiMap
import com.intellij.psi.impl.source.tree.CompositeElement


/**
 * Andrey 05/08/17.
 */
open class PowerShellBlockImpl(node: ASTNode, wrap: Wrap?, alignment: Alignment?, indent: Indent?, indentSize: Int, commonSettings: CommonCodeStyleSettings, psStyleSettings: PowerShellCodeStyleSettings)
  : AbstractBlock(node, wrap, alignment), PowerShellCodeBlock {

  private val myCommonSettings = commonSettings
  private val myPSSettings = psStyleSettings
  private val mySpacingProcessor = PowerShellSpacingProcessor(myCommonSettings, myPSSettings)

  private var myParent: PowerShellBlockImpl? = null
  private val myIndent: Indent? = indent
  private val myIndentSize = indentSize
  //  private var myCurrentChildWrap: Wrap? = null
  private var myBaseAlignment: Alignment = Alignment.createAlignment()

  constructor(file: PsiFile, setting: CodeStyleSettings) : this(file.node, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(),
      Indent.getAbsoluteNoneIndent(),
      setting.getIndentSize(PowerShellFileType.INSTANCE),
      setting.getCommonSettings(PowerShellLanguage.INSTANCE),
      setting.getCustomSettings(PowerShellCodeStyleSettings::class.java))

  override fun isLeaf(): Boolean = false

  override fun getIndent(): Indent? = myIndent

  override fun getChildIndent(): Indent? = createChildIndent(myNode)

  override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
    val children = subBlocks
    if (newChildIndex < 1 || newChildIndex >= children.size) return super.getChildAttributes(newChildIndex)

    val newChildAt = children[newChildIndex]
    val prevChild = children[newChildIndex - 1]
    if (newChildAt is ASTBlock && prevChild is ASTBlock) {
      val newType = newChildAt.node?.elementType
      val prevType = prevChild.node?.elementType
      if (prevType === BLOCK_BODY && newType === RCURLY) {
        return ChildAttributes.DELEGATE_TO_PREV_CHILD
      } else if (prevType === LCURLY && newType === RCURLY) {
        return ChildAttributes(Indent.getNormalIndent(), getFirstChildAlignment(children))
      }
    }
    return ChildAttributes(newChildAt.indent, getFirstChildAlignment(children))
  }


  private fun getFirstChildAlignment(children: MutableList<Block>): Alignment? {
    return children.map { it.alignment }.firstOrNull { it != null }
  }

  private fun createChildIndent(parent: ASTNode): Indent? {
    if (SourceTreeToPsiMap.treeElementToPsi(parent) is PsiFile) return Indent.getNoneIndent()
    val type = parent.elementType
    //statement_block rule
    if (type === SWITCH_CLAUSE_BLOCK || type === CLASS_DECLARATION_STATEMENT || type === FUNCTION_STATEMENT || type === ENUM_DECLARATION_STATEMENT
        || type === METHOD_DECLARATION_STATEMENT || type === CONSTRUCTOR_DECLARATION_STATEMENT || type === CATCH_CLAUSE || type === TRY_STATEMENT
        || type === WHILE_STATEMENT || type === IF_STATEMENT || type === FINALLY_CLAUSE || type === ELSE_CLAUSE || type === ELSEIF_CLAUSE
        || type === FOR_STATEMENT || type === FOREACH_STATEMENT || type === TRAP_STATEMENT || type === DATA_STATEMENT
        || type === INLINESCRIPT_STATEMENT || type === PARALLEL_STATEMENT || type === SEQUENCE_STATEMENT || type === SWITCH_STATEMENT
        || type === DO_STATEMENT) {
      return Indent.getNormalIndent()
    }
    if (type === BLOCK_BODY) return Indent.getNoneIndent()
    return null
  }

  override fun getSpacing(child1: Block?, child2: Block): Spacing? {
    return if (child2 is PowerShellCodeBlock) mySpacingProcessor.getSpacing(child1 as? PowerShellCodeBlock?, child2) else null
  }

  override fun buildChildren(): MutableList<Block> {
    if (isLeaf) {
      return EMPTY
    }
    val tlChildren = ArrayList<Block>()
    var childNode: ASTNode? = node.firstChildNode
    while (childNode != null) {
      if (childNode.text.trim { it <= ' ' }.isEmpty()) {
        childNode = childNode.treeNext
        continue
      }
      val childBlock: PowerShellCodeBlock = if (childNode.elementType === INVOCATION_EXPRESSION) {
        createCallExpressionBlock(childNode, null /*createWrap(childNode)*/, createAlignment(childNode), createIndent(childNode))
      } else {
        PowerShellBlockImpl(childNode, createWrap(childNode), createAlignment(childNode), createIndent(childNode), myIndentSize, myCommonSettings, myPSSettings)
      }
      childBlock.parent = this
      tlChildren.add(childBlock)
      childNode = childNode.treeNext
    }
    return tlChildren
  }

  private fun createCallExpressionBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, indent: Indent?): PowerShellCodeBlock {
    val nodes = ArrayList<ASTNode>()
    if (node.elementType === INVOCATION_EXPRESSION && node.treeParent?.elementType !== INVOCATION_EXPRESSION) {
      collectNodes(nodes, node)
    }
    val callChainBlock = ChainMethodCallBlockBuilder(alignment, wrap, indent, myCommonSettings, myPSSettings).build(nodes)
    callChainBlock.parent = this
    return callChainBlock
  }

  private fun collectNodes(nodes: ArrayList<ASTNode>, node: ASTNode) {
    var child = node.firstChildNode
    while (child != null) {
      if (!FormatterUtil.containsWhiteSpacesOnly(child)) {
        val type = child.elementType
        if (type === INVOCATION_EXPRESSION) {
          collectNodes(nodes, child)
        } else if (type !== NLS) {
          nodes.add(child)
        }
      }
      child = child.treeNext
    }
  }

  private fun createAlignment(node: ASTNode): Alignment? {

    if (isCallArgument(node) && myCommonSettings.ALIGN_MULTILINE_PARAMETERS_IN_CALLS) return myBaseAlignment

    if (isForParameter(node) && myCommonSettings.ALIGN_MULTILINE_FOR) return myBaseAlignment

    if (isFunctionParameter(node) && myCommonSettings.ALIGN_MULTILINE_PARAMETERS) return myBaseAlignment

    if (isBlockParameter(node) && myPSSettings.ALIGN_MULTILINE_BLOCK_PARAMETERS) return myBaseAlignment

//    if (isInvocationExpressionQualifier(node) && myCommonSettings.ALIGN_MULTILINE_CHAINED_METHODS) return myBaseAlignment

    if (isLhsOrRhsBinaryExpression(node) && myCommonSettings.ALIGN_MULTILINE_BINARY_OPERATION) return myBaseAlignment

    if (isTypeLiteral(node)) {
      if (isCatchClauseContext(node) && myPSSettings.ALIGN_MULTILINE_CATCH_TYPE_LIST) {
        return myBaseAlignment
      }
    }
    if (myPSSettings.ALIGN_MULTILINE_ATTRIBUTE_ARGUMENT && isAttributeArgument(node)) return myBaseAlignment

    if (myPSSettings.ALIGN_MULTILINE_PIPELINE_STATEMENT && isPipelineContext(node)) return myBaseAlignment

    if (myNode.elementType === PARENTHESIZED_ARGUMENT_LIST
        && myCommonSettings.ALIGN_MULTILINE_PARAMETERS_IN_CALLS
        && myCommonSettings.CALL_PARAMETERS_LPAREN_ON_NEXT_LINE) return myBaseAlignment

    if (myNode.elementType === PARAMETER_CLAUSE
        && myCommonSettings.ALIGN_MULTILINE_PARAMETERS
        && myCommonSettings.METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE) return myBaseAlignment

    return null
  }

  private fun isLhsOrRhsBinaryExpression(node: ASTNode) = isBinaryExpressionContext(node) && node.psi is PowerShellExpression

  private fun createIndent(node: ASTNode): Indent? {
    val type = node.elementType
    return when {
      type === BLOCK_BODY -> getBlockIndent(node)
      type === LCURLY || type === RCURLY -> getBraceIndent(node)
      isCallArgument(node) || isFunctionParameter(node) -> Indent.getContinuationIndent()
      isBlockParameter(node) -> Indent.getNormalIndent()
      isForParameter(node) -> Indent.getContinuationIndent()
      isRhsBinaryExpressionContext(node) -> Indent.getContinuationIndent()
      isInvocationExpressionQualifier(node) -> Indent.getContinuationIndent()
      isExpressionInPipelineTail(node) -> Indent.getContinuationIndent()
      isAttributeArgument(node) -> Indent.getContinuationIndent()
      type === EXPANDABLE_HERE_STRING_END -> Indent.getAbsoluteNoneIndent()
      isArrayElement(node) -> Indent.getNormalIndent()
      else -> Indent.getNoneIndent()
    }
  }

  private fun getBraceIndent(braceNode: ASTNode): Indent {
    if (isClassDeclarationContext(braceNode) || isEnumDeclarationContext(braceNode)) {
      if (myCommonSettings.CLASS_BRACE_STYLE == NEXT_LINE_SHIFTED || myCommonSettings.CLASS_BRACE_STYLE == NEXT_LINE_SHIFTED2) {
        return Indent.getNormalIndent()
      }
    } else if (isCallableDeclarationContext(braceNode)) {
      if (myCommonSettings.METHOD_BRACE_STYLE == NEXT_LINE_SHIFTED || myCommonSettings.METHOD_BRACE_STYLE == NEXT_LINE_SHIFTED2) {
        return Indent.getNormalIndent()
      }
    } else if (myCommonSettings.BRACE_STYLE == NEXT_LINE_SHIFTED || myCommonSettings.BRACE_STYLE == NEXT_LINE_SHIFTED2) {
      return Indent.getNormalIndent()
    }
    return Indent.getNoneIndent()
  }

  private fun getBlockIndent(blockNode: ASTNode): Indent? {
    if (isClassDeclarationContext(blockNode) || isEnumDeclarationContext(blockNode)) {
      if (myCommonSettings.CLASS_BRACE_STYLE == NEXT_LINE_SHIFTED2) {
        return Indent.getSpaceIndent(myIndentSize * 2)
      }
    } else if (isCallableDeclarationContext(blockNode)) {
      if (myCommonSettings.METHOD_BRACE_STYLE == NEXT_LINE_SHIFTED2) {
        return Indent.getSpaceIndent(myIndentSize * 2)
      }
    } else if (myCommonSettings.BRACE_STYLE == NEXT_LINE_SHIFTED2) {
      return Indent.getSpaceIndent(myIndentSize * 2)
    }
    return Indent.getNormalIndent()
  }

  private fun createWrap(childNode: ASTNode): Wrap {
    if ((isFunctionParameter(childNode) || isBlockParameter(childNode)) && findSiblingSkipping(childNode, arrayOf(NLS, WHITE_SPACE), false)?.elementType != LP) {
      val firstNode = isFirstNodeInParameter(childNode, true)
      val wrapValue = if (isFunctionParameter(childNode)) myCommonSettings.METHOD_PARAMETERS_WRAP else myPSSettings.BLOCK_PARAMETER_CLAUSE_WRAP
      val paramWrap = Wrap.createWrap(WrappingUtil.getWrapType(wrapValue), firstNode)
      if (firstNode) {
        if (isAttribute(childNode)) {
          val attributeWrap = Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.PARAMETER_ANNOTATION_WRAP), true)
          if (myCommonSettings.PARAMETER_ANNOTATION_WRAP == WRAP_ALWAYS) {
            return attributeWrap
          }
          val resultWrapType = wrapValue or myCommonSettings.PARAMETER_ANNOTATION_WRAP
          return Wrap.createChildWrap(attributeWrap, WrappingUtil.getWrapType(resultWrapType), true)
        } else {
          return paramWrap
        }
      } else if (isFollowedByAttribute(childNode)) {//seems to be always the case
        return Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.PARAMETER_ANNOTATION_WRAP), true)
      }
      return paramWrap
    }

    if (isCallArgument(childNode) && findSiblingSkipping(childNode, arrayOf(NLS, WHITE_SPACE), false)?.elementType != LP) {
      return Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.CALL_PARAMETERS_WRAP), isFirstNodeInParameter(childNode, true))
    }

    if (isForParameter(childNode)) {
      return Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.FOR_STATEMENT_WRAP), isFirstNodeInForParameter(childNode))
    }

    if (isRhsBinaryExpressionContext(childNode)) {
      return Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.BINARY_OPERATION_WRAP), true)
    }

    if (isAttribute(childNode) || isFollowedByAttribute(childNode)) {
      if (isClassDeclarationContext(childNode)) {
        return Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.CLASS_ANNOTATION_WRAP), true)
      }
      if (isPropertyDeclarationContext(childNode)) {
        return Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.FIELD_ANNOTATION_WRAP), true)
      }
      if (isCallableDeclarationContext(childNode)) {
        return Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.METHOD_ANNOTATION_WRAP), true)
      }
      if (isParameterClauseContext(childNode)) {
        return Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.PARAMETER_ANNOTATION_WRAP), true)
      }
      if (isBlockParameterClauseContext(childNode)) {
        return Wrap.createWrap(WrappingUtil.getWrapType(myPSSettings.BLOCK_PARAMETER_CLAUSE_WRAP), true)
      }
      if (isBlockBodyContext(childNode)) {//attribute in the param block
        return Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.PARAMETER_ANNOTATION_WRAP), true)
      }
    }

    if (isTypeLiteral(childNode)) {
      if (isCatchClauseContext(childNode)) {
        return Wrap.createWrap(WrappingUtil.getWrapType(myPSSettings.CATCH_TYPE_LIST_WRAP), true)
      }
    }

    if (isAttributeArgument(childNode) && findSiblingSkippingWS(childNode, false)?.elementType != LP) {
      return Wrap.createWrap(WrappingUtil.getWrapType(myPSSettings.ATTRIBUTE_ARGUMENT_WRAP), true)
    }

    if (isExpressionInPipelineTail(childNode)) {
      return Wrap.createWrap(WrappingUtil.getWrapType(myPSSettings.PIPELINE_TAIL_WRAP), true)
    }

    if (childNode.elementType === LP) {
      val nextSibling = findSiblingSkippingWS(childNode)
      if (nextSibling != null && isBinaryExpression(nextSibling) && myCommonSettings.PARENTHESES_EXPRESSION_LPAREN_WRAP) {
        return Wrap.createWrap(WrapType.ALWAYS, true)
      }
    }

    if (childNode.elementType === RP) {
      val prevSibling = findSiblingSkippingWS(childNode, false)
      if (prevSibling != null && isBinaryExpression(prevSibling) && myCommonSettings.PARENTHESES_EXPRESSION_RPAREN_WRAP) {
        return Wrap.createWrap(WrapType.ALWAYS, true)
      }
    }

    return Wrap.createWrap(WrapType.NONE, true)
  }

  override fun setParent(newParent: BlockWithParent?) {
    myParent = newParent as PowerShellBlockImpl?
  }

  override fun getParent(): BlockWithParent? = myParent

}

class PowerShellSpacingProcessor(private val myCommonSettings: CommonCodeStyleSettings, private val myPowerShellSettings: PowerShellCodeStyleSettings) {
  private var myChild1: ASTNode? = null
  private var myChild2: ASTNode? = null
  private var myParent: PsiElement? = null

  private fun init(child: ASTNode?) {
    if (child == null) return

    var treePrev: ASTNode? = child.treePrev
    while (treePrev != null && isWhiteSpaceOrNls(treePrev)) {
      treePrev = treePrev.treePrev
    }

    if (treePrev == null) {
      init(child.treeParent)
    } else {
      myChild2 = child

      myChild1 = treePrev
      val parent = treePrev.treeParent as CompositeElement
      myParent = SourceTreeToPsiMap.treeElementToPsi(parent)
    }
  }

  internal fun getSpacing(child1: PowerShellCodeBlock?, child2: PowerShellCodeBlock): Spacing? {
    if (child1 == null) {
      return null
    }
    init(child2.node)

    if (myChild1 == null || myChild2 == null) return null

    val node1 = myChild1 as ASTNode
    val type1 = myChild1!!.elementType
    val node2 = myChild2 as ASTNode
    val type2 = myChild2!!.elementType

    if (node1.treeParent == node2.treeParent) {
      // Check for a command argument: we should not rearrange anything inside an argument.
      val parent = node1.treeParent
      if (parent.elementType === COMMAND_ARGUMENT) return null
    }

    if (LCURLY === type1 || RCURLY === type2) {
      val braceNode = if (LCURLY === type1) node1 else node2
      val blockBody = findSiblingSkipping(braceNode, arrayOf(NLS, WHITE_SPACE), braceNode === node1)
      val blockBodyToken = blockBody?.elementType
      if (blockBodyToken === IDENTIFIER || blockBodyToken === SIMPLE_ID) return null

      var isKeepOneLine = false
      val isSimpleBlock = blockBodyToken === BLOCK_BODY && !containsNewLines(blockBody) || (blockBodyToken === LCURLY || blockBodyToken === RCURLY)
      if (isSimpleBlock) {
        isKeepOneLine = if (isCallableDeclarationContext(braceNode)) {
          myCommonSettings.KEEP_SIMPLE_METHODS_IN_ONE_LINE
        } else if (isClassDeclarationContext(braceNode) || isEnumDeclarationContext(braceNode)) {
          myCommonSettings.KEEP_SIMPLE_CLASSES_IN_ONE_LINE
        } else if (isScriptBlockExpressionContext(braceNode)) {
          myCommonSettings.KEEP_SIMPLE_LAMBDAS_IN_ONE_LINE
        } else if (isHashLiteralContext(braceNode)) {
          myPowerShellSettings.KEEP_SIMPLE_HASH_LITERAL_IN_ONE_LINE
        } else {
          myCommonSettings.KEEP_SIMPLE_BLOCKS_IN_ONE_LINE
        }
      }
      return createSpacing(myCommonSettings.SPACE_WITHIN_BRACES, !isKeepOneLine)
    }
    if (SQBR_L === type1 || SQBR_R === type2) {
      if (isCastExpressionSuperContext(node1) || isCastExpressionSuperContext(node2)) return simpleSpacing(myCommonSettings.SPACE_WITHIN_CAST_PARENTHESES)

      return simpleSpacing(myCommonSettings.SPACE_WITHIN_BRACKETS)
    }
    if (type1 === TYPE_LITERAL_EXPRESSION && node1.treeParent?.elementType == CAST_EXPRESSION) return simpleSpacing(myCommonSettings.SPACE_AFTER_TYPE_CAST)

    if (LP === type1) {
      if (isArgumentListContext(node1)) {
        val treeNext = findSiblingSkipping(node1, arrayOf(NLS, WHITE_SPACE))
        val lfAfterLparen = myCommonSettings.CALL_PARAMETERS_WRAP != DO_NOT_WRAP && myCommonSettings.CALL_PARAMETERS_LPAREN_ON_NEXT_LINE
        if (treeNext?.elementType === RP) {
          val lfAfterRparen = myCommonSettings.CALL_PARAMETERS_WRAP != DO_NOT_WRAP && myCommonSettings.CALL_PARAMETERS_RPAREN_ON_NEXT_LINE
          return createSpacing(myCommonSettings.SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES, lfAfterLparen || lfAfterRparen)
        }

        return createSpacing(myCommonSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES, lfAfterLparen)
      }
      if (isParameterClauseContext(node1) || isBlockParameterClauseContext(node1)) {
        val treeNext = findSiblingSkipping(node1, arrayOf(NLS, WHITE_SPACE))
        val methodParam = isParameterClauseContext(node1)
        val wrapValue = if (methodParam) myCommonSettings.METHOD_PARAMETERS_WRAP else myPowerShellSettings.BLOCK_PARAMETER_CLAUSE_WRAP
        val lParenNextLine = if (methodParam) myCommonSettings.METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE else myPowerShellSettings.BLOCK_PARAMETERS_LPAREN_ON_NEXT_LINE
        val lfAfterLparen = wrapValue != DO_NOT_WRAP && lParenNextLine

        if (treeNext?.elementType === RP) {
          val rParenNextLine = if (methodParam) myCommonSettings.METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE else myPowerShellSettings.BLOCK_PARAMETERS_RPAREN_ON_NEXT_LINE
          val lfAfterRparen = wrapValue != DO_NOT_WRAP && rParenNextLine
          return createSpacing(myCommonSettings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES, lfAfterLparen || lfAfterRparen)
        }
        return createSpacing(myCommonSettings.SPACE_WITHIN_METHOD_PARENTHESES, lfAfterLparen)
      }
      if (isForClauseContext(node1)) {
        return createSpacing(myCommonSettings.SPACE_WITHIN_FOR_PARENTHESES, myCommonSettings.FOR_STATEMENT_LPAREN_ON_NEXT_LINE)
      }

      if (isIfStatementContext(node1)) return simpleSpacing(myCommonSettings.SPACE_WITHIN_IF_PARENTHESES)

      if (isForEachStatementContext(node1)) return simpleSpacing(myCommonSettings.SPACE_WITHIN_FOR_PARENTHESES)

      if (isWhileStatementContext(node1)) return simpleSpacing(myCommonSettings.SPACE_WITHIN_WHILE_PARENTHESES)

      if (isSwitchStatementContext(node1)) return simpleSpacing(myCommonSettings.SPACE_WITHIN_SWITCH_PARENTHESES)

      if (isParenthesizedExpressionContext(node1)) simpleSpacing(myCommonSettings.SPACE_WITHIN_PARENTHESES)

      if (isSubExpressionContext(node1)) return simpleSpacing(myPowerShellSettings.SPACE_WITHIN_SUB_EXPRESSION_PARENTHESES)

      if (node1.treeParent?.elementType === ATTRIBUTE) return simpleSpacing(myCommonSettings.SPACE_WITHIN_ANNOTATION_PARENTHESES)

    }
    if (RP === type2) {
      if (isArgumentListContext(node2)) {
        val treePrev = findSiblingSkipping(node2, arrayOf(NLS, WHITE_SPACE), false)
        val lfAfterRparen = myCommonSettings.CALL_PARAMETERS_WRAP != DO_NOT_WRAP && myCommonSettings.CALL_PARAMETERS_RPAREN_ON_NEXT_LINE
        if (treePrev?.elementType === LP) return createSpacing(myCommonSettings.SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES, lfAfterRparen)

        return createSpacing(myCommonSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES, lfAfterRparen)
      }
      if (isParameterClauseContext(node2) || isBlockParameterClauseContext(node1)) {
        val treePrev = findSiblingSkipping(node2, arrayOf(NLS, WHITE_SPACE), false)
        val methodParam = isParameterClauseContext(node2)
        val wrapValue = if (methodParam) myCommonSettings.METHOD_PARAMETERS_WRAP else myPowerShellSettings.BLOCK_PARAMETER_CLAUSE_WRAP
        val rParenNextLine = if (methodParam) myCommonSettings.METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE else myPowerShellSettings.BLOCK_PARAMETERS_RPAREN_ON_NEXT_LINE
        val lfAfterRparen = wrapValue != DO_NOT_WRAP && rParenNextLine
        if (treePrev?.elementType === LP) return createSpacing(myCommonSettings.SPACE_WITHIN_EMPTY_METHOD_PARENTHESES, lfAfterRparen)

        return createSpacing(myCommonSettings.SPACE_WITHIN_METHOD_PARENTHESES, lfAfterRparen)
      }
      if (isForClauseContext(node2)) {
        return createSpacing(myCommonSettings.SPACE_WITHIN_FOR_PARENTHESES, myCommonSettings.FOR_STATEMENT_RPAREN_ON_NEXT_LINE)
      }
      if (isIfStatementContext(node2)) return simpleSpacing(myCommonSettings.SPACE_WITHIN_IF_PARENTHESES)

      if (isForEachStatementContext(node2)) return simpleSpacing(myCommonSettings.SPACE_WITHIN_FOR_PARENTHESES)

      if (isWhileStatementContext(node2)) return simpleSpacing(myCommonSettings.SPACE_WITHIN_WHILE_PARENTHESES)

      if (isSwitchStatementContext(node2)) return simpleSpacing(myCommonSettings.SPACE_WITHIN_SWITCH_PARENTHESES)

      if (isParenthesizedExpressionContext(node2)) return simpleSpacing(myCommonSettings.SPACE_WITHIN_PARENTHESES)

      if (isSubExpressionContext(node2)) return simpleSpacing(myPowerShellSettings.SPACE_WITHIN_SUB_EXPRESSION_PARENTHESES)

      if (node2.treeParent?.elementType === ATTRIBUTE) return simpleSpacing(myCommonSettings.SPACE_WITHIN_ANNOTATION_PARENTHESES)

    }

    if (type2 === LP) {
      if (type1 === IF) {
        return simpleSpacing(myCommonSettings.SPACE_BEFORE_IF_PARENTHESES)
      } else if (type1 == FOR || type1 == FOREACH) {
        return simpleSpacing(myCommonSettings.SPACE_BEFORE_FOR_PARENTHESES)
      } else if (type1 == WHILE) {
        return simpleSpacing(myCommonSettings.SPACE_BEFORE_WHILE_PARENTHESES)
      } else if (isSwitchStatementContext(node1)) {
        return simpleSpacing(myCommonSettings.SPACE_BEFORE_SWITCH_PARENTHESES)
      } else if (node1.psi.context is PowerShellAttribute) {
        return simpleSpacing(myCommonSettings.SPACE_BEFORE_ANOTATION_PARAMETER_LIST)
      }
    }

    if (type2 === PARAMETER_CLAUSE) {
      return simpleSpacing(myCommonSettings.SPACE_BEFORE_METHOD_PARENTHESES)
    } else if (type2 === PARENTHESIZED_ARGUMENT_LIST) {
      return simpleSpacing(myCommonSettings.SPACE_BEFORE_METHOD_CALL_PARENTHESES)
    }

    if (isAssignmentOperator(type1) || isAssignmentOperator(type2)) {
      return simpleSpacing(myCommonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
    }
    if (isComparisonOperator(type1) || isComparisonOperator(type2)) {
      return simpleSpacing(myCommonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)
    }
    if (isAdditiveOperator(node1) || isAdditiveOperator(node2)) {
      return simpleSpacing(myCommonSettings.SPACE_AROUND_ADDITIVE_OPERATORS)
    }
    if (isLogicalOperator(type1) || isLogicalOperator(type2)) {
      return simpleSpacing(myCommonSettings.SPACE_AROUND_LOGICAL_OPERATORS)
    }
    if (isBitwiseOperator(type1) || isBitwiseOperator(type2)) {
      return simpleSpacing(myCommonSettings.SPACE_AROUND_BITWISE_OPERATORS)
    }
    if (isMultiplicativeOperator(type1) || isMultiplicativeOperator(type2)) {
      if (!isIdentifier(node1.treeParent) && !isIdentifier(node2.treeParent)) return simpleSpacing(myCommonSettings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS)
    }
    if (isWhitespaceFlexibleUnaryOperator(node1) || isWhitespaceFlexibleUnaryOperator(node2)) {
      return simpleSpacing(myCommonSettings.SPACE_AROUND_UNARY_OPERATOR)
    }
    if (isReferenceDoubleColonOperator(type1) || isReferenceDoubleColonOperator(type2)) {
      return simpleSpacing(myCommonSettings.SPACE_AROUND_METHOD_REF_DBL_COLON)
    }
    if (type2 === LCURLY) {
      if (isClassDeclarationContext(node2) || isEnumDeclarationContext(node2)) {
        if (myCommonSettings.CLASS_BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForDeclaration(node2, myCommonSettings.SPACE_BEFORE_CLASS_LBRACE)
        }
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_CLASS_LBRACE, myCommonSettings.CLASS_BRACE_STYLE)
      }
      if (isCallableDeclarationContext(node2)) {
        if (myCommonSettings.METHOD_BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForDeclaration(node2, myCommonSettings.SPACE_BEFORE_METHOD_LBRACE)
        }
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_METHOD_LBRACE, myCommonSettings.METHOD_BRACE_STYLE)
      }
      if (isIfStatementContext(node2)) {
        if (myCommonSettings.BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForControlStatement(node2, myCommonSettings.SPACE_BEFORE_IF_LBRACE)
        }
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_IF_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isForClauseContext(node2) || isForEachStatementContext(node2)) {
        if (myCommonSettings.BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForControlStatement(node2, myCommonSettings.SPACE_BEFORE_FOR_LBRACE)
        }
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_FOR_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isWhileStatementContext(node2)) {
        if (myCommonSettings.BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForControlStatement(node2, myCommonSettings.SPACE_BEFORE_WHILE_LBRACE)
        }
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_WHILE_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isDoStatementContext(node2)) {
        if (myCommonSettings.BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForControlStatement(node2, myCommonSettings.SPACE_BEFORE_DO_LBRACE)
        }
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_DO_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isSwitchStatementContext(node2)) {
        if (myCommonSettings.BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForControlStatement(node2, myCommonSettings.SPACE_BEFORE_SWITCH_LBRACE)
        }
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_SWITCH_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isTryStatementBlockContext(node2)) {
        if (myCommonSettings.BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForControlStatement(node2, myCommonSettings.SPACE_BEFORE_TRY_LBRACE)
        }
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_TRY_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isElseClauseContext(node2)) {
        if (myCommonSettings.BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForControlStatement(node2, myCommonSettings.SPACE_BEFORE_TRY_LBRACE)
        }
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_ELSE_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isCatchClauseContext(node2)) {
        if (myCommonSettings.BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForControlStatement(node2, myCommonSettings.SPACE_BEFORE_TRY_LBRACE)
        }
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_CATCH_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isFinallyClauseContext(node2)) {
        return createBraceSpacing(myCommonSettings.SPACE_BEFORE_FINALLY_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isDataStatementContext(node2)) {
        if (myCommonSettings.BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForControlStatement(node2, myCommonSettings.SPACE_BEFORE_TRY_LBRACE)
        }
        return createBraceSpacing(myPowerShellSettings.SPACE_BEFORE_DATA_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isTrapStatementContext(node2)) {
        if (myCommonSettings.BRACE_STYLE == NEXT_LINE_IF_WRAPPED) {
          return dependentBraceSpacingForControlStatement(node2, myCommonSettings.SPACE_BEFORE_TRY_LBRACE)
        }
        return createBraceSpacing(myPowerShellSettings.SPACE_BEFORE_TRAP_LBRACE, myCommonSettings.BRACE_STYLE)
      }
      if (isScriptBlockExpressionContext(node2)) {
        return createBraceSpacing(0, Int.MAX_VALUE, myCommonSettings.BRACE_STYLE)
      }
    }

    if (type2 === ELSE_CLAUSE) {
      return createSpacing(myCommonSettings.SPACE_BEFORE_ELSE_KEYWORD, myCommonSettings.ELSE_ON_NEW_LINE)
    }
    if (type2 === ELSEIF_CLAUSE) {
      return createSpacing(myCommonSettings.SPACE_BEFORE_ELSE_KEYWORD, myCommonSettings.SPECIAL_ELSE_IF_TREATMENT)
    }
    if (type2 === CATCH_CLAUSE) {
      return createSpacing(myCommonSettings.SPACE_BEFORE_CATCH_KEYWORD, myCommonSettings.CATCH_ON_NEW_LINE)
    }
    if (type2 === FINALLY_CLAUSE) {
      return createSpacing(myCommonSettings.SPACE_BEFORE_FINALLY_KEYWORD, myCommonSettings.FINALLY_ON_NEW_LINE)
    }
    if (type2 === WHILE && isDoStatementContext(node2)) {
      return createSpacing(myCommonSettings.SPACE_BEFORE_WHILE_KEYWORD, myCommonSettings.WHILE_ON_NEW_LINE)
    }

    if (COMMA === type1) return simpleSpacing(myCommonSettings.SPACE_AFTER_COMMA)
    if (COMMA === type2) return simpleSpacing(myCommonSettings.SPACE_BEFORE_COMMA)

    if (type1 == DS) return Spacing.createSpacing(0, 0, 0, true, 0)

    if (type1 === COLON) {
      return if (canAddSpaceBefore(node2)) simpleSpacing(myPowerShellSettings.SPACE_AFTER_COLON) else null
    }

    if (type2 == COLON) return if (canAddSpaceBefore(node2)) simpleSpacing(myPowerShellSettings.SPACE_BEFORE_COLON) else null

    if (type1 == SEMI) return simpleSpacing(myCommonSettings.SPACE_AFTER_SEMICOLON)

    if (type2 == SEMI) return simpleSpacing(myCommonSettings.SPACE_BEFORE_SEMICOLON)

    if ((PowerShellTokenTypeSets.KEYWORDS.contains(type1) && !isIdentifier(node1.treeParent)/*|| HANDLER_PARAMETER_LABEL === type1*/) && NLS !== type2) {
      return Spacing.createSpacing(1, 1, 0, true, 0)
    }

    if (isRedirectionOperator(type1)) return simpleSpacing(myCommonSettings.SPACE_AROUND_RELATIONAL_OPERATORS)

    return null
  }

  private fun dependentBraceSpacingForDeclaration(node: ASTNode, addSingleSpace: Boolean): Spacing {
    val declaration = node.psi.context
    var startOffset: Int? = null
    if (declaration is PowerShellComponent) {
      startOffset = declaration.nameIdentifier?.textRange?.startOffset
    }
    if (startOffset == null) startOffset = myParent!!.textRange.startOffset

    return dependentLFSpacing(addSingleSpace, TextRange(startOffset, node.textRange.endOffset))
  }

  private fun dependentBraceSpacingForControlStatement(node: ASTNode, addSingleSpace: Boolean): Spacing {
    val startOffset = node.psi.context?.textRange?.startOffset ?: myParent!!.textRange.startOffset
    return dependentLFSpacing(addSingleSpace, TextRange(startOffset, node.textRange.endOffset))
  }

  private fun createSpacing(addSpace: Boolean, ensureLineBreak: Boolean, keepLineBreaks: Boolean = myCommonSettings.KEEP_LINE_BREAKS,
                            keepBlankLines: Int = keepBlankLines()): Spacing {
    val spaces = if (addSpace) 1 else 0
    val minLines = if (ensureLineBreak) 1 else 0
    return Spacing.createSpacing(spaces, spaces, minLines, keepLineBreaks, keepBlankLines)
  }


  private fun simpleSpacing(addSingleSpace: Boolean, minLinesFeeds: Int = 0): Spacing {
    val spaces = if (addSingleSpace) 1 else 0
    return Spacing.createSpacing(spaces, spaces, minLinesFeeds, myCommonSettings.KEEP_LINE_BREAKS, keepBlankLines())
  }

  private fun spacingRemoveLineBreaks(addSingleSpace: Boolean): Spacing {
    val spaces = if (addSingleSpace) 1 else 0
    return Spacing.createSpacing(spaces, spaces, 0, false, 0)
  }

  private fun spacingEnsureLineBreak(addSingleSpace: Boolean = false): Spacing {
    val spaces = if (addSingleSpace) 1 else 0
    return Spacing.createSpacing(spaces, spaces, 1, false, 1)
  }

  private fun dependentLFSpacing(addSingleSpace: Boolean, dependantTextRange: TextRange): Spacing {
    val spaces = if (addSingleSpace) 1 else 0
    return Spacing.createDependentLFSpacing(spaces, spaces, dependantTextRange, false, keepBlankLines())
  }

  private fun createBraceSpacing(addSingleSpace: Boolean, bracePlacementStyle: Int, dependantTextRange: TextRange? = null): Spacing {
    return when (bracePlacementStyle) {
      END_OF_LINE -> spacingRemoveLineBreaks(addSingleSpace)
      NEXT_LINE -> spacingEnsureLineBreak(addSingleSpace)
      NEXT_LINE_SHIFTED -> spacingEnsureLineBreak(addSingleSpace)
      NEXT_LINE_SHIFTED2 -> spacingEnsureLineBreak(addSingleSpace)
      NEXT_LINE_IF_WRAPPED -> if (dependantTextRange != null) dependentLFSpacing(addSingleSpace, dependantTextRange) else simpleSpacing(addSingleSpace)
      else -> simpleSpacing(addSingleSpace)
    }
  }

  private fun createBraceSpacing(minSpaces: Int, maxSpaces: Int, bracePlacementStyle: Int, dependantTextRange: TextRange? = null): Spacing {
    return when (bracePlacementStyle) {
      END_OF_LINE -> Spacing.createSpacing(minSpaces, maxSpaces, 0, false, 0)
      NEXT_LINE -> Spacing.createSpacing(minSpaces, maxSpaces, 1, false, 0)
      NEXT_LINE_SHIFTED -> Spacing.createSpacing(minSpaces, maxSpaces, 1, false, 0)
      NEXT_LINE_SHIFTED2 -> Spacing.createSpacing(minSpaces, maxSpaces, 1, false, 0)
      NEXT_LINE_IF_WRAPPED -> {
        if (dependantTextRange != null) Spacing.createDependentLFSpacing(minSpaces, maxSpaces, dependantTextRange, false, keepBlankLines())
        else Spacing.createSpacing(minSpaces, maxSpaces, 0, myCommonSettings.KEEP_LINE_BREAKS, keepBlankLines())
      }
      else -> Spacing.createSpacing(minSpaces, maxSpaces, 0, myCommonSettings.KEEP_LINE_BREAKS, keepBlankLines())
    }
  }

  private fun keepBlankLines(): Int = myCommonSettings.KEEP_BLANK_LINES_IN_CODE
}
