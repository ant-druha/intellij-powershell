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
    private const val POWER_SHELL_KEYWORD = "POWER_SHELL_KEYWORD"
    private const val POWER_SHELL_NUMBER = "POWER_SHELL_NUMBER"
    private const val POWER_SHELL_STRING = "POWER_SHELL_STRING"
    private const val POWER_SHELL_LINE_COMMENT = "POWER_SHELL_LINE_COMMENT"
    private const val POWER_SHELL_COMMAND_NAME = "POWER_SHELL_COMMAND_NAME"
    private const val POWER_SHELL_COMMAND_PARAMETER = "POWER_SHELL_COMMAND_PARAMETER"
    private const val POWER_SHELL_TYPE_NAME = "POWER_SHELL_TYPE_NAME"
    private const val POWER_SHELL_TYPE_REFERENCE = "POWER_SHELL_TYPE_REFERENCE"
    private const val POWER_SHELL_VARIABLE = "POWER_SHELL_VARIABLE"
    private const val POWER_SHELL_PROPERTY_REF_NAME = "POWER_SHELL_PROPERTY_REF_NAME"
    private const val POWER_SHELL_METHOD_CALL_NAME = "POWER_SHELL_METHOD_CALL_NAME"
    private const val POWER_SHELL_METHOD_DECLARATION_NAME = "POWER_SHELL_METHOD_DECLARATION_NAME"
    private const val POWER_SHELL_LABEL_NAME = "POWER_SHELL_LABEL_NAME"

    val ATTRIBUTES = HashMap<IElementType, TextAttributesKey>()
    val KEYWORD = createTextAttributesKey(POWER_SHELL_KEYWORD, DefaultLanguageHighlighterColors.KEYWORD)
    val COMMENT = createTextAttributesKey(POWER_SHELL_LINE_COMMENT, DefaultLanguageHighlighterColors.LINE_COMMENT)
    val STRING = createTextAttributesKey(POWER_SHELL_STRING, DefaultLanguageHighlighterColors.STRING)
    val NUMBER = createTextAttributesKey(POWER_SHELL_NUMBER, DefaultLanguageHighlighterColors.NUMBER)
    val COMMAND_NAME = createTextAttributesKey(POWER_SHELL_COMMAND_NAME, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE)
    val COMMAND_PARAMETER = createTextAttributesKey(POWER_SHELL_COMMAND_PARAMETER, DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)
    val TYPE_NAME = createTextAttributesKey(POWER_SHELL_TYPE_NAME, DefaultLanguageHighlighterColors.CLASS_NAME)
    val TYPE_REFERENCE = createTextAttributesKey(POWER_SHELL_TYPE_REFERENCE, DefaultLanguageHighlighterColors.CLASS_REFERENCE)
    val VARIABLE_NAME = createTextAttributesKey(POWER_SHELL_VARIABLE, DefaultLanguageHighlighterColors.LOCAL_VARIABLE)
    val PROPERTY_REFERENCE = createTextAttributesKey(POWER_SHELL_PROPERTY_REF_NAME, DefaultLanguageHighlighterColors.IDENTIFIER)
    val METHOD_CALL = createTextAttributesKey(POWER_SHELL_METHOD_CALL_NAME, DefaultLanguageHighlighterColors.FUNCTION_CALL)
    val METHOD_DECLARATION = createTextAttributesKey(POWER_SHELL_METHOD_DECLARATION_NAME, DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
    val LABEL = createTextAttributesKey(POWER_SHELL_LABEL_NAME, DefaultLanguageHighlighterColors.LABEL)
    // val COMMAND_NAME_ATTR = createTextAttributesKey(POWER_SHELL_COMMAND_NAME, DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE)
  }

  override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
    return pack(ATTRIBUTES[tokenType])
  }

  override fun getHighlightingLexer(): Lexer {
    return PowerShellLexerAdapter()
  }


  init {
    fillMap(ATTRIBUTES, PowerShellTokenTypeSets.KEYWORDS, KEYWORD)
    fillMap(ATTRIBUTES, PowerShellTokenTypeSets.COMMENTS, COMMENT)
    fillMap(ATTRIBUTES, PowerShellTokenTypeSets.STRINGS, STRING)
    fillMap(ATTRIBUTES, PowerShellTokenTypeSets.NUMBERS, NUMBER)
//    fillMap(PowerShellSyntaxHighlighter.ATTRIBUTES, COMMAND_NAME_ATTR, PowerShellTypes.COMMAND_NAME) //via Annotator
  }
}
