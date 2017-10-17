package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

/**
 * Andrey 09/08/17.
 */
class PowerShellLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
  override fun getCodeSample(settingsType: SettingsType): String {
    return "foreach (\$i in get-childitem | sort-object length)\n" +
        "{\n" +
        "    \$i  #highlight\n" +
        "    \$sum += \$i.length\n" +
        "}\n" +
        "\n" +
        "# 3) variable name token: '\$_.length ' and 'get-childitem'\n" +
        "switch -regex -casesensitive (get-childitem | sort length)\n" +
        "{\n" +
        "\"^5\" {\"length for \$_ started with 5\" ; continue}\n" +
        "{\$_.length >20000} {\"length of \$_ is greater than 20000\"\n" +
        "}\n" +
        "default {\"Didn't match anything else...\"}\n" +
        "}"
  }

  override fun getIndentOptionsEditor(): IndentOptionsEditor? {
    return SmartIndentOptionsEditor()
  }

  override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
    when (settingsType) {
      LanguageCodeStyleSettingsProvider.SettingsType.SPACING_SETTINGS -> consumer.showStandardOptions("SPACE_BEFORE_METHOD_CALL_PARENTHESES",
          "SPACE_BEFORE_METHOD_PARENTHESES",
          "SPACE_BEFORE_IF_PARENTHESES",
          "SPACE_AROUND_ASSIGNMENT_OPERATORS",
          "SPACE_AROUND_LOGICAL_OPERATORS",
          "SPACE_AROUND_EQUALITY_OPERATORS",
          "SPACE_AROUND_RELATIONAL_OPERATORS",
          "SPACE_AROUND_ADDITIVE_OPERATORS",
          "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",
          "SPACE_AROUND_SHIFT_OPERATORS",
          "SPACE_BEFORE_METHOD_LBRACE",
          "SPACE_BEFORE_ELSE_LBRACE",
          "SPACE_BEFORE_WHILE_KEYWORD",
          "SPACE_BEFORE_ELSE_KEYWORD",
          "SPACE_WITHIN_METHOD_CALL_PARENTHESES",
          "SPACE_WITHIN_METHOD_PARENTHESES",
          "SPACE_WITHIN_IF_PARENTHESES",
          "SPACE_BEFORE_COLON",
          "SPACE_AFTER_COLON",
          "SPACE_AFTER_COMMA",
          "SPACE_AFTER_COMMA_IN_TYPE_ARGUMENTS",
          "SPACE_BEFORE_COMMA",
          "SPACE_AROUND_UNARY_OPERATOR"
      )
      LanguageCodeStyleSettingsProvider.SettingsType.BLANK_LINES_SETTINGS -> consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE")
      LanguageCodeStyleSettingsProvider.SettingsType.WRAPPING_AND_BRACES_SETTINGS -> consumer.showStandardOptions("RIGHT_MARGIN",
          "KEEP_LINE_BREAKS",
          "KEEP_FIRST_COLUMN_COMMENT",
          "BRACE_STYLE",
          "METHOD_BRACE_STYLE",
          "CALL_PARAMETERS_WRAP",
          "CALL_PARAMETERS_LPAREN_ON_NEXT_LINE",
          "CALL_PARAMETERS_RPAREN_ON_NEXT_LINE",
          "METHOD_PARAMETERS_WRAP",
          "METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE",
          "METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE",
          "ELSE_ON_NEW_LINE",
          "ALIGN_MULTILINE_PARAMETERS",
          "ALIGN_MULTILINE_PARAMETERS_IN_CALLS",
          "ALIGN_MULTILINE_BINARY_OPERATION",
          "BINARY_OPERATION_WRAP",
          "BINARY_OPERATION_SIGN_ON_NEXT_LINE",
          "PARENTHESES_EXPRESSION_LPAREN_WRAP",
          "PARENTHESES_EXPRESSION_RPAREN_WRAP",
          "SPECIAL_ELSE_IF_TREATMENT")
      LanguageCodeStyleSettingsProvider.SettingsType.INDENT_SETTINGS -> consumer.showStandardOptions("INDENT_SIZE",
          "CONTINUATION_INDENT_SIZE",
          "TAB_SIZE")
      LanguageCodeStyleSettingsProvider.SettingsType.COMMENTER_SETTINGS -> consumer.showStandardOptions("COMMENTER_SETTINGS")
      LanguageCodeStyleSettingsProvider.SettingsType.LANGUAGE_SPECIFIC -> consumer.showStandardOptions("LANGUAGE_SPECIFIC")
    }
  }

  override fun getDefaultCommonSettings(): CommonCodeStyleSettings? {
    val defaultSettings = CommonCodeStyleSettings(language)
    val indentOptions = defaultSettings.initIndentOptions()
    indentOptions.INDENT_SIZE = 4
    indentOptions.CONTINUATION_INDENT_SIZE = 8
    indentOptions.TAB_SIZE = 4
    return defaultSettings
  }

  override fun getLanguage(): Language {
    return PowerShellLanguage.INSTANCE
  }

}



