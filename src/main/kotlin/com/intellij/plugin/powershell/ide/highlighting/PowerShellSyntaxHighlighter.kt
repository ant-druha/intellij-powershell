package com.intellij.plugin.powershell.ide.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets
import com.intellij.psi.tree.IElementType
import java.util.*

/**
 * Andrey 17/07/17.
 */
class PowerShellSyntaxHighlighter : SyntaxHighlighterBase() {


  companion object {

    val ATTRIBUTES = HashMap<IElementType, TextAttributesKey>()

  }

  override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
    return SyntaxHighlighterBase.pack(ATTRIBUTES[tokenType])
  }

  override fun getHighlightingLexer(): Lexer {
    return PowerShellLexerAdapter()
  }


  init {
    val POWER_SHELL_KEYWORD = "POWER_SHELL_KEYWORD"
    val POWER_SHELL_NUMBER = "POWER_SHELL_NUMBER"
    val POWER_SHELL_STRING = "POWER_SHELL_STRING"
    val POWER_SHELL_LINE_COMMENT = "POWER_SHELL_LINE_COMMENT"
//    val POWER_SHELL_COMMAND_NAME = "POWER_SHELL_COMMAND_NAME"


    val KEYWORD = createTextAttributesKey(POWER_SHELL_KEYWORD, DefaultLanguageHighlighterColors.KEYWORD)
    val LINE_COMMENT = createTextAttributesKey(POWER_SHELL_LINE_COMMENT, DefaultLanguageHighlighterColors.LINE_COMMENT)
    val STRING = createTextAttributesKey(POWER_SHELL_STRING, DefaultLanguageHighlighterColors.STRING)
    val NUMBER = createTextAttributesKey(POWER_SHELL_NUMBER, DefaultLanguageHighlighterColors.NUMBER)
//    val COMMAND_NAME_ATTR = createTextAttributesKey(POWER_SHELL_COMMAND_NAME, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE)

    fillMap(PowerShellSyntaxHighlighter.ATTRIBUTES, PowerShellTokenTypeSets.KEYWORDS, KEYWORD)
    fillMap(PowerShellSyntaxHighlighter.ATTRIBUTES, PowerShellTokenTypeSets.LINE_COMMENT, LINE_COMMENT)
    fillMap(PowerShellSyntaxHighlighter.ATTRIBUTES, PowerShellTokenTypeSets.STRINGS, STRING)
    fillMap(PowerShellSyntaxHighlighter.ATTRIBUTES, PowerShellTokenTypeSets.NUMBERS, NUMBER)
//    fillMap(PowerShellSyntaxHighlighter.ATTRIBUTES, COMMAND_NAME_ATTR, PowerShellTypes.COMMAND_NAME) todo: via Annotator
  }
}