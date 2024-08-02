package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.formatting.*
import com.intellij.formatting.templateLanguages.BlockWithParent
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.psi.PowerShellTypes


class SyntheticPowerShellCodeBlock(private val mySubBlocks: MutableList<Block>, private val myBlockAlignment: Alignment?,
                                   private val myBlockIndent: Indent, private val myBlockWrap: Wrap?) : PowerShellCodeBlock {

  override fun getNode(): ASTNode? = null

  private val myBlockTextRange = TextRange(mySubBlocks[0].textRange.startOffset, mySubBlocks[mySubBlocks.size - 1].textRange.endOffset)
  private var myParent: BlockWithParent? = null
  private var myChildAttributes: ChildAttributes? = null

  override fun setParent(newParent: BlockWithParent?) {
    myParent = newParent
  }

  override fun getParent(): BlockWithParent? = myParent

  override fun isLeaf(): Boolean = false

  override fun getAlignment(): Alignment? = myBlockAlignment

  override fun getSpacing(child1: Block?, child2: Block): Spacing? {
    return null
  }

  override fun getTextRange(): TextRange = myBlockTextRange

  override fun getSubBlocks(): MutableList<Block> = mySubBlocks

  override fun isIncomplete(): Boolean = subBlocks.size > 0 && subBlocks[subBlocks.size - 1].isIncomplete

  override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
    return if (myChildAttributes != null) {
      myChildAttributes as ChildAttributes
    } else {
      var alignment: Alignment? = null
      if (mySubBlocks.size > newChildIndex) {
        val block = mySubBlocks[newChildIndex]
        alignment = block.alignment
      } else if (mySubBlocks.size == newChildIndex) {
        if (isRParenth(getRightMostBlock())) {
          alignment = getDotAlignment()
        }
      }

      ChildAttributes(indent, alignment)
    }
  }

  private fun getDotAlignment(): Alignment? {
    if (mySubBlocks.size > 1) {
      val block = mySubBlocks[1]
      if (isDotFirst(block)) {
        return block.alignment
      }
    }
    return null
  }

  private fun isDotFirst(block: Block): Boolean {
    var current = block
    while (!current.subBlocks.isEmpty()) {
      current = block.subBlocks[0]
    }
    return isLeafBlock(current)
  }

  private fun isLeafBlock(current: Block): Boolean {
    return try {
      val leafBlock = Class.forName("com.intellij.psi.formatter.java.LeafBlock").asSubclass(current::class.java)
      getAstNode(leafBlock)?.elementType === PowerShellTypes.DOT
    } catch (e: Exception) {
      false
    }
  }

  private fun getAstNode(leafBlock: Class<out Block>): ASTNode? {
    return try {
      val nodeFieldClass = leafBlock.getDeclaredField("node")
      nodeFieldClass.get(leafBlock) as? ASTNode
    } catch (e: Exception) {
      null
    }
  }

  private fun getRightMostBlock(): Block? {
    var rightMost: Block? = null
    var subBlocks = subBlocks
    while (!subBlocks.isEmpty()) {
      val lastIndex = subBlocks.size - 1
      rightMost = subBlocks[lastIndex]
      subBlocks = rightMost.subBlocks
    }
    return rightMost
  }

  private fun isRParenth(block: Block?): Boolean {
    return block != null && getAstNode(block::class.java)?.elementType === PowerShellTypes.RP
  }

  override fun getWrap(): Wrap? = myBlockWrap

  override fun getIndent(): Indent = myBlockIndent
}
