package com.intellij.plugin.powershell.ide.injection

import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.lang.util.PowerShellStringUtil
import com.intellij.plugin.powershell.psi.PowerShellPsiElementFactory
import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression
import com.intellij.psi.AbstractElementManipulator
import com.intellij.util.IncorrectOperationException


class PowerShellStringManipulator : AbstractElementManipulator<PowerShellStringLiteralExpression>() {
  override fun getRangeInElement(element: PowerShellStringLiteralExpression): TextRange = element.getContentRange()

  @Throws(IncorrectOperationException::class)
  override fun handleContentChange(element: PowerShellStringLiteralExpression, range: TextRange, newContent: String): PowerShellStringLiteralExpression {
    val oldText = element.node.text
    val contentRange = element.getContentRange()
    val newString = oldText.substring(0, contentRange.startOffset) + escapeString(element, newContent) + oldText.substring(contentRange.endOffset)
//    val newString = oldText.substring(0, range.startOffset) + escapeString(element, newContent) + oldText.substring(range.endOffset)//TODO[#190]: fix range to use it from parameter
    val stringLiteral = PowerShellPsiElementFactory.createExpression(element.project, newString)
    return element.replace(stringLiteral) as PowerShellStringLiteralExpression
  }

  //from host editor to PowerShell string
  private fun escapeString(element: PowerShellStringLiteralExpression, content: String): String {
    var result = ""
    val oldText = element.text

    if (element.hasSubstitutions()) {
      val VAR_SIMPLE_CHAR_REGEX = "\\p{Lu}|\\p{Ll}|\\p{Lt}|\\p{Lm}|\\p{Lo}|_|\\p{Nd}|#"
      val textAndExpr = "(?<textAndExpr>(?<text>((?!\\$(\\(|$VAR_SIMPLE_CHAR_REGEX)).|\\n|\\$\\([^)]*$)*)(?<expr>\\$(\\([^)]*\\)|($VAR_SIMPLE_CHAR_REGEX)+))*)".toRegex()
      val resAll = textAndExpr.findAll(content)
      for (res in resAll) {
        val expandableExpr = res.groups["expr"]?.value ?: ""
        val textGroup = res.groups["text"]?.value ?: ""
        val exprIdx = oldText.indexOf(expandableExpr, Math.max(0, result.length - 1))
        val isExpressionInHost = exprIdx > 0 && oldText.elementAt(exprIdx - 1) != '`'
        result += escapeExpandableText(textGroup, element) + if (isExpressionInHost) expandableExpr else escapeExpandableText(expandableExpr, element)
      }
    } else {
      result = when {
        element.isExpandable() -> escapeExpandableText(content, element)
        else -> escapeVerbatimText(content, element)
      }
    }
    return result
  }

  private fun escapeExpandableText(text: String, element: PowerShellStringLiteralExpression) =
      if (element.isHereString()) escapeHereString(text) else escapeExpandableString(text, element.getQuoteChar(), element.getQuoteEscapeChar())

  private fun escapeVerbatimText(text: String, element: PowerShellStringLiteralExpression): String {
    return if (element.isHereString()) text else escapeQuotes(text, element.getQuoteChar(), element.getQuoteEscapeChar())
  }

  private fun escapeExpandableString(text: String, quoteChar: Char, escapeChar: Char): String {
    val escaped = PowerShellStringUtil.escapeStringCharacters(text, "$", false, true, StringBuilder()).toString()
    return escapeQuotes(escaped, quoteChar, escapeChar)
  }

  private fun escapeHereString(text: String): String {
    return PowerShellStringUtil.escapeHereStringCharacters(text, "$", false, true, StringBuilder()).toString()
  }

  //TODO[#191]: implement in PowerShellStringUtil.escapeStringCharacters()
  private fun escapeQuotes(str: String?, quoteChar: Char, escapeChar: Char): String {
    return str?.replace("$quoteChar", "$escapeChar$quoteChar") ?: ""
  }

}
