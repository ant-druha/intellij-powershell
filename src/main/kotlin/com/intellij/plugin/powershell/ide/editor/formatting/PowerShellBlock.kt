package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.formatting.*
import com.intellij.formatting.templateLanguages.BlockWithParent
import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.plugin.powershell.psi.PowerShellPsiUtil
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets
import com.intellij.plugin.powershell.psi.PowerShellTypes.*
import com.intellij.plugin.powershell.psi.impl.PowerShellPsiImplUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.impl.source.SourceTreeToPsiMap
import com.intellij.psi.impl.source.tree.CompositeElement
import com.intellij.psi.tree.IElementType
import java.util.*

/**
 * Andrey 05/08/17.
 */
class PowerShellBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private var mySettings: CodeStyleSettings) : AbstractBlock(node, wrap, alignment), BlockWithParent {

  private val mySpacingProcessor = PowerShellSpacingProcessor(node, mySettings.getCommonSettings(PowerShellLanguage.INSTANCE))

  override fun isLeaf(): Boolean {
    return false
  }

  override fun getIndent(): Indent? {
    val elementType = node.elementType

    if (elementType === BLOCK_BODY) {
      return Indent.getNormalIndent()
    }
    return Indent.getNoneIndent()
  }

  override fun getSpacing(child1: Block?, child2: Block): Spacing? {
    return mySpacingProcessor.getSpacing(child1 as PowerShellBlock?, child2 as PowerShellBlock)
  }

  override fun buildChildren(): MutableList<Block> {
    if (isLeaf) {
      return AbstractBlock.EMPTY
    }
    val tlChildren = ArrayList<Block>()
    var childNode: ASTNode? = node.firstChildNode
    while (childNode != null) {
      if (childNode.text.trim { it <= ' ' }.isEmpty()) {
        childNode = childNode.treeNext
        continue
      }
      val childBlock = PowerShellBlock(childNode, createChildWrap(childNode), Alignment.createAlignment(), mySettings)
      childBlock.parent = this
      tlChildren.add(childBlock)
      childNode = childNode.treeNext
    }
    return tlChildren
  }

  private fun createChildWrap(childNode: ASTNode): Wrap? {
    return Wrap.createWrap(WrapType.NONE, false)
  }

  override fun setParent(newParent: BlockWithParent?) {
    myParent = newParent
  }

  private var myParent: BlockWithParent? = null

  override fun getParent(): BlockWithParent? {
    return myParent
  }
}

class PowerShellSpacingProcessor(private val myNode: ASTNode, private val mySettings: CommonCodeStyleSettings) {
  private var myChild1: ASTNode? = null
  private var myChild2: ASTNode? = null
  private var myType1: IElementType? = null
  private var myType2: IElementType? = null
  private var myParent: PsiElement? = null

  private fun isWhiteSpace(node: ASTNode?): Boolean {
    return node != null && (PowerShellPsiImplUtil.isWhiteSpaceOrNls(node) || node.textLength == 0)
  }

  private fun _init(child: ASTNode?) {
    if (child == null) return

    var treePrev: ASTNode? = child.treePrev
    while (treePrev != null && PowerShellPsiImplUtil.isWhiteSpace(treePrev)) {
      treePrev = treePrev.treePrev
    }

    if (treePrev == null) {
      _init(child.treeParent)
    } else {
      myChild2 = child
      myType2 = myChild2!!.elementType

      myChild1 = treePrev
      myType1 = myChild1!!.elementType
      val parent = treePrev.treeParent as CompositeElement
      myParent = SourceTreeToPsiMap.treeElementToPsi(parent)
    }
  }

  internal fun getSpacing(child1: PowerShellBlock?, child2: PowerShellBlock): Spacing? {
    if (child1 == null) {
      return null
    }
    val node = child2.node
    _init(node)

    val node1 = child1.node
    val type1 = node1.elementType
    val node2 = child2.node
    val parent2 = node2.treeParent.elementType
    val type2 = node2.elementType

    if (LCURLY === type1 || RCURLY === type2) return Spacing.createSpacing(0, 0, 0, true, 0)

//    // handlerCall(params)
//    if (LP === type2 && HANDLER_POSITIONAL_PARAMETERS_CALL_EXPRESSION === parent2) {
//      return addSingleSpaceIf(mySettings.SPACE_BEFORE_METHOD_CALL_PARENTHESES, false)
//    }
//    if (LP === type2 && HANDLER_POSITIONAL_PARAMETERS_DEFINITION === parent2) {
//      return addSingleSpaceIf(mySettings.SPACE_BEFORE_METHOD_PARENTHESES, false)
//    }
//
    if (IF === type1 && LP === type2) {
      return addSingleSpaceIf(mySettings.SPACE_BEFORE_IF_PARENTHESES, false)
    }

    if (LP === type1 || RP === type2) return Spacing.createSpacing(0, 0, 0, true, 0)

    if (COMMA === type2) return Spacing.createSpacing(0, 0, 0, true, 0)
    if (COMMA === type1) return Spacing.createSpacing(1, 1, 0, true, 0)//todo setting

//    if (type1 === IDENTIFIER && type2 === HANDLER_PARAMETER_LABEL) {
//      return Spacing.createSpacing(1, 1, 0, true, 0)
//    }

    if (type1 == DS) return Spacing.createSpacing(0, 0, 0, true, 0)

    if (type1 === COLON && type2 != COLON) {
      if (PowerShellPsiUtil.isLabel(node2) || PowerShellPsiUtil.isVariableScope(node2)) return Spacing.createSpacing(0, 0, 0, true, 0)

      return Spacing.createSpacing(1, 1, 0, true, 0)//todo setting
    }

    if (type2 === COLON && type1 != COLON) return Spacing.createSpacing(0, 0, 0, true, 0)

    if ((PowerShellTokenTypeSets.KEYWORDS.contains(type1) /*|| HANDLER_PARAMETER_LABEL === type1*/) && NLS !== type2) {
      return Spacing.createSpacing(1, 1, 0, true, 0)
    }
    return if (OP_C === type1 || OP_FR === type1 || OP_MR === type1) {
      Spacing.createSpacing(1, 1, 0, true, 0)
    } else Spacing.createSpacing(0, Integer.MAX_VALUE, 0, mySettings.KEEP_LINE_BREAKS, mySettings.KEEP_BLANK_LINES_IN_CODE)
  }

  private fun addLineBreak(): Spacing {
    return Spacing.createSpacing(0, 0, 1, false, mySettings.KEEP_BLANK_LINES_IN_CODE)
  }

  private fun addSingleSpaceIf(condition: Boolean, linesFeed: Boolean): Spacing {
    val spaces = if (condition) 1 else 0
    val lines = if (linesFeed) 1 else 0
    return Spacing.createSpacing(spaces, spaces, lines, mySettings.KEEP_LINE_BREAKS, mySettings.KEEP_BLANK_LINES_IN_CODE)
  }
}
