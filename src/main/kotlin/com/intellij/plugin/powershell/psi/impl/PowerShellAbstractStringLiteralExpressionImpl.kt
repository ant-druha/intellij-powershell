package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.lang.util.PowerShellStringUtil
import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression
import com.intellij.plugin.powershell.psi.PowerShellSubExpression
import com.intellij.plugin.powershell.psi.PowerShellTypes
import com.intellij.plugin.powershell.psi.PowerShellVariable
import com.intellij.psi.ElementManipulators
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost

abstract class PowerShellAbstractStringLiteralExpressionImpl(node: ASTNode) : PowerShellExpressionImpl(node), PsiLanguageInjectionHost, PowerShellStringLiteralExpression {

  override fun hasSubstitutions(): Boolean = isExpandable() && children.any { it is PowerShellVariable || it is PowerShellSubExpression }

  override fun getQuoteEscapeChar(): Char {
    if (!isExpandable()) return '\''
    return if (getStringContent().contains("\"\"")) '"' else '`'
  }

  override fun getQuoteChar(): Char = if (isExpandable()) '"' else '\''

  override fun getStringContent(): String = getContentRange().substring(text)

  override fun getContentRange(): TextRange {
    val length = textLength
    if (isHereString()) {
      val endIndex = text.indexOf("${getQuoteChar()}@") - 1
      return TextRange(3, endIndex)
    }
    return TextRange(1, length - 1)
  }

  override fun isHereString(): Boolean = firstChild.node.elementType == PowerShellTypes.EXPANDABLE_HERE_STRING_START || firstChild.node.elementType == PowerShellTypes.VERBATIM_HERE_STRING

  override fun isExpandable(): Boolean = firstChild.node.elementType == PowerShellTypes.DQ_OPEN || firstChild.node.elementType == PowerShellTypes.EXPANDABLE_HERE_STRING_START

  override fun isValidHost(): Boolean = true

  override fun updateText(text: String): PsiLanguageInjectionHost = ElementManipulators.handleContentChange<PowerShellStringLiteralExpression>(this, text)

  override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> = StringLiteralEscaper(this)

  class StringLiteralEscaper internal constructor(host: PowerShellStringLiteralExpression) : LiteralTextEscaper<PowerShellStringLiteralExpression>(host) {
    private var mySourceOffsets: IntArray? = null

    override fun getRelevantTextRange(): TextRange = myHost.getContentRange()

    // from PowerShell string literal to the external editor of the injected language 
    override fun decode(rangeInsideHost: TextRange, outChars: StringBuilder): Boolean {
      val text = myHost.text
      val hostText = rangeInsideHost.substring(text)
      val hereString = myHost.isHereString()
      val expandable = myHost.isExpandable()
      mySourceOffsets = IntArray(hostText.length + 1)

      if (expandable) {
        val quoteToEscape = if (hereString) null else myHost.getQuoteChar()
        return PowerShellStringUtil.parseExpandableString(hostText, outChars, mySourceOffsets!!, quoteToEscape)
        //      val stopChars = if (quoteToEscape != null) charArrayOf('$', quoteToEscape) else charArrayOf('$')
        //      return PowerShellStringUtil.parseExpandableString(hostText, outChars, mySourceOffsets!!, null, *stopChars)
      }
      return if (hereString) {
        outChars.append(hostText)
        for (i in mySourceOffsets!!.indices) {
          mySourceOffsets!![i] = i
        }
        true
      } else {
        decodeVerbatimString(hostText, outChars, mySourceOffsets!!)
      }
    }

    private fun decodeVerbatimString(hostText: String, outChars: StringBuilder, sourceOffsets: IntArray): Boolean {
      var startIndex = 0
      var delta = 0
      var quoteIndex = hostText.indexOf("''")

      fun appendWithQuote() {
        outChars.append(hostText.substring(startIndex, quoteIndex))
        for (i in startIndex..quoteIndex) {
          sourceOffsets[i - delta] = i
        }
        outChars.append("'")
        startIndex = quoteIndex + 2
        delta = startIndex - outChars.length
        quoteIndex = hostText.indexOf("''", startIndex)
      }

      fun appendWithoutQuote() {
        outChars.append(hostText.substring(startIndex))
        for (i in startIndex..hostText.length) {
          sourceOffsets[i - delta] = i
        }
      }

      while (quoteIndex > 0) {
        appendWithQuote()
      }
      appendWithoutQuote()
      return true
    }

    override fun getOffsetInHost(offsetInDecoded: Int, rangeInsideHost: TextRange): Int {
      val offsets = mySourceOffsets
      if (offsets == null || offsetInDecoded >= offsets.size) return -1
      return Math.min(offsets[offsetInDecoded], rangeInsideHost.length) + rangeInsideHost.startOffset
    }

    override fun isOneLine(): Boolean = false
  }

}