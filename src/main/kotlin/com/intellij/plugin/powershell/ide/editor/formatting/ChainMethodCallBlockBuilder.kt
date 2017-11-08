package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.psi.PowerShellTypes
import com.intellij.plugin.powershell.psi.PowerShellTypes.COLON2
import com.intellij.plugin.powershell.psi.PowerShellTypes.DOT
import com.intellij.psi.PsiComment
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.formatter.WrappingUtil
import java.util.*


class ChainMethodCallBlockBuilder(private val myAlignment: Alignment?, private val myWrap: Wrap?, private val myBlockIndent: Indent?,
                                  private val myCommonSettings: CommonCodeStyleSettings, private val myPSSettings: PowerShellCodeStyleSettings) {

  fun build(nodes: ArrayList<ASTNode>): PowerShellCodeBlock {
    val blocks = buildCallChunkBlocks(nodes)

    val indent: Indent = myBlockIndent ?: (myCommonSettings.indentOptions?.
        let { Indent.getContinuationIndent(it.USE_RELATIVE_INDENTS) } ?: Indent.getContinuationIndent())
    return SyntheticPowerShellCodeBlock(blocks, myAlignment, indent, myWrap)
  }

  private fun buildCallChunkBlocks(nodes: ArrayList<ASTNode>): MutableList<Block> {
    val methodCall = splitMethodCallOnChunksByDots(nodes)

    var wrap: Wrap? = null
    var chainedCallsAlignment: Alignment? = null

    val result = ArrayList<Block>()

    for (i in methodCall.indices) {
      val currentCallChunk = methodCall[i]
      if (isCall(currentCallChunk) || isComment(currentCallChunk)) {
        if (wrap == null) wrap = createCallChunkWrap(i, methodCall)

        if (chainedCallsAlignment == null) chainedCallsAlignment = createCallChunkAlignment(i, methodCall)

      } else {
        wrap = null
        chainedCallsAlignment = null
      }

      val block = createCallChunkBlock(currentCallChunk.nodes, wrap, chainedCallsAlignment)
      if (block != null) result.add(block)
    }

    return result
  }

  private fun getFirstCallChunkIndex(methodCall: MutableList<ChainedCallChunk>, startFrom: Int = 0): Int {
    return (startFrom until methodCall.size).indexOfFirst { isCall(methodCall[it]) }
  }

  private fun createCallChunkBlock(subNodes: MutableList<ASTNode>, wrap: Wrap?, chainedCallsAlignment: Alignment?): PowerShellCodeBlock? {
    if (subNodes.isEmpty()) return null

    val indent = Indent.getContinuationWithoutFirstIndent(myCommonSettings.indentOptions?.USE_RELATIVE_INDENTS == true)
    return SyntheticPowerShellCodeBlock(createAstBlocks(subNodes), chainedCallsAlignment, indent, wrap)
  }

  private fun createAstBlocks(nodes: MutableList<ASTNode>, wrap: Wrap? = null, alignment: Alignment? = null): MutableList<Block> {
    val result = mutableListOf<Block>()
    if (nodes.isEmpty()) return result
    val indent = Indent.getContinuationWithoutFirstIndent(myCommonSettings.indentOptions?.USE_RELATIVE_INDENTS ?: false)
    nodes.mapTo(result) { newPowerShellBlock(it, myCommonSettings, myPSSettings, indent, wrap, alignment) }
    return result
  }

  private fun newPowerShellBlock(node: ASTNode, commonSettings: CommonCodeStyleSettings, psSettings: PowerShellCodeStyleSettings, indent: Indent?, wrap: Wrap?, alignment: Alignment?): PowerShellCodeBlock {
    val indentSize = CodeStyleSettingsManager.getInstance(node.psi.project).currentSettings.getIndentSize(PowerShellFileType.INSTANCE)
    return PowerShellBlockImpl(node, wrap, alignment, indent, indentSize, commonSettings, psSettings)
  }

  private fun createCallChunkAlignment(chunkIndex: Int, methodCall: MutableList<ChainedCallChunk>): Alignment? {
    val current = methodCall[chunkIndex]
    return if (shouldAlignMethod(current, methodCall)) Alignment.createAlignment() else null
  }

  private fun shouldAlignMethod(currentMethodChunk: ChainedCallChunk, methodCall: MutableList<ChainedCallChunk>): Boolean {
    return myCommonSettings.ALIGN_MULTILINE_CHAINED_METHODS && !currentMethodChunk.isEmpty()
        && !chunkIsFirstInChainMethodCall(currentMethodChunk, methodCall)
  }

  private fun chunkIsFirstInChainMethodCall(currentMethodChunk: ChainedCallChunk, methodCall: MutableList<ChainedCallChunk>): Boolean {
    return !methodCall.isEmpty() && currentMethodChunk == methodCall[0]
  }

  private fun createCallChunkWrap(chunkIndex: Int, methodCall: MutableList<ChainedCallChunk>): Wrap? {
    val isSubsequentCall = chunkIndex > getFirstCallChunkIndex(methodCall)
    if (myCommonSettings.WRAP_FIRST_METHOD_IN_CALL_CHAIN) {
      val next = if (chunkIndex + 1 < methodCall.size) methodCall[chunkIndex + 1] else null
      if (next != null && isCall(next) || isSubsequentCall) {
        return WrappingUtil.createWrap(myCommonSettings.METHOD_CALL_CHAIN_WRAP)
      }
    } else if (isSubsequentCall) {
      return WrappingUtil.createWrap(myCommonSettings.METHOD_CALL_CHAIN_WRAP)
//      return Wrap.createWrap(WrappingUtil.getWrapType(myCommonSettings.METHOD_CALL_CHAIN_WRAP), false)//first calls are not wrapped
    }
    return null
  }

  private fun isComment(chunk: ChainedCallChunk): Boolean {
    val nodes = chunk.nodes
    return if (nodes.size == 1) nodes[0].psi is PsiComment else false
  }

  private fun splitMethodCallOnChunksByDots(nodes: ArrayList<ASTNode>): MutableList<ChainedCallChunk> {
    val result = mutableListOf<ChainedCallChunk>()
    var current = mutableListOf<ASTNode>()
    for (node in nodes) {
      if (node.elementType === DOT || node.elementType === COLON2 || node.psi is PsiComment) {
        current.add(node)
        result.add(ChainedCallChunk(current))
        current = ArrayList()
      } else {
        current.add(node)
      }
    }

    if (!current.isEmpty()) {
      result.add(ChainedCallChunk(current))
    }
    return result
  }

  private fun isCall(callChunk: ChainedCallChunk): Boolean {
    val nodes = callChunk.nodes
    return nodes.size >= 2 && nodes[1].elementType === PowerShellTypes.PARENTHESIZED_ARGUMENT_LIST
  }

  class ChainedCallChunk(internal val nodes: MutableList<ASTNode>) {
    fun isEmpty(): Boolean {
      return nodes.isEmpty()
    }
  }
}
