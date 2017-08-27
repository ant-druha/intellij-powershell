package com.intellij.plugin.powershell.ide.editor.highlighting

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
    lateinit var KEYWORD: TextAttributesKey
    lateinit var COMMENT: TextAttributesKey
    lateinit var STRING: TextAttributesKey
    lateinit var NUMBER: TextAttributesKey
    lateinit var COMMAND_NAME: TextAttributesKey
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
    val POWER_SHELL_COMMAND_NAME = "POWER_SHELL_COMMAND_NAME"


    KEYWORD = createTextAttributesKey(POWER_SHELL_KEYWORD, DefaultLanguageHighlighterColors.KEYWORD)
    COMMENT = createTextAttributesKey(POWER_SHELL_LINE_COMMENT, DefaultLanguageHighlighterColors.LINE_COMMENT)
    STRING = createTextAttributesKey(POWER_SHELL_STRING, DefaultLanguageHighlighterColors.STRING)
    NUMBER = createTextAttributesKey(POWER_SHELL_NUMBER, DefaultLanguageHighlighterColors.NUMBER)
    COMMAND_NAME = createTextAttributesKey(POWER_SHELL_COMMAND_NAME, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE)
//    val COMMAND_NAME_ATTR = createTextAttributesKey(POWER_SHELL_COMMAND_NAME, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE)

    fillMap(ATTRIBUTES, PowerShellTokenTypeSets.KEYWORDS, KEYWORD)
    fillMap(ATTRIBUTES, PowerShellTokenTypeSets.COMMENTS, COMMENT)
    fillMap(ATTRIBUTES, PowerShellTokenTypeSets.STRINGS, STRING)
    fillMap(ATTRIBUTES, PowerShellTokenTypeSets.NUMBERS, NUMBER)
//    fillMap(PowerShellSyntaxHighlighter.ATTRIBUTES, COMMAND_NAME_ATTR, PowerShellTypes.COMMAND_NAME) todo: via Annotator
  }
}