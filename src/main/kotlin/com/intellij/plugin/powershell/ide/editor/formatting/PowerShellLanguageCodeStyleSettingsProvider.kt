package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.application.options.IndentOptionsEditor
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizableOptions
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
      "    \$i = \$i + 1\n" +
      "    \$sum += \$i.length\n" +
      "    0x0F0F -band \$i\n" +
      "    \$j = 20 +\n" +
      "            \$i\n" +
      "    # comment here\n" +
      "    Write-Object ((\$j -gt 5) -and (\$i -lt 15))\n" +
      "}\n" +
      "#logical not\n" +
      "-not-not\$false          # False\n" +
      "-not1.23                # False\n" +
      "!\"xyz\"                  # False\n" +
      "[int] \$x = 10.6         # type int, value 11\n" +
      "# 3) variable name token: '\$_.length ' and 'get-childitem'\n" +
      "switch -regex -casesensitive (get-childitem | sort length)\n" +
      "{\n" +
      "    \" ^5\" {\n" +
      "        \"length for \$_ started with 5\"; continue\n" +
      "    }\n" +
      "    { \$_.length >20000 } {\n" +
      "        \"length of \$_ is greater than 20000\"\n" +
      "    }\n" +
      "    default {\n" +
      "        \"Didn't match anything else...\"\n" +
      "    }\n" +
      "}\n" +
      "function Get-Power([int]\$x, [int]\$y)\n" +
      "{\n" +
      "    if (\$y -gt 0)\n" +
      "    {\n" +
      "        return \$x * (Get-Power \$x (--\$y))\n" +
      "    }\n" +
      "    elseif (\$y -eq 0)\n" +
      "    {\n" +
      "        return 1\n" +
      "    }\n" +
      "    else\n" +
      "    {\n" +
      "        return 1\n" +
      "    }\n" +
      "}\n" +
      "(New-Object 'float[,]' 2, 3) / 2    # [object[]], Length 2/2\n" +
      "\$values = 10, 20, 30\n" +
      "for (\$i = 0; \$i -lt \$values.Length; ++\$i)\n" +
      "{\n" +
      "    \"`\$values[\$i] = \$( \$values[\$i] )\"\n" +
      "}\n" +
      "\$a = new-object 'int[]' 10\n" +
      "\$i = 20                # out-of-bounds subscript :go_here\n" +
      "while (\$true)\n" +
      "{\n" +
      "    try\n" +
      "    {\n" +
      "        Write-Object \"j = \$j\"\n" +
      "        \$a[\$i] = 10; \"Assignment completed without error\"\n" +
      "        break\n" +
      "    }\n" +
      "    catch [IndexOutOfRangeException]\n" +
      "    {\n" +
      "        \"Handling out-of-bounds index, >\$_<`n\"\n" +
      "        \$i = 5\n" +
      "    }\n" +
      "    catch\n" +
      "    {\n" +
      "        \"Caught unexpected exception\"\n" +
      "    }\n" +
      "    finally\n" +
      "    {\n" +
      "        # …\n" +
      "    }\n" +
      "}\n" +
      "function ValidateScriptTest\n" +
      "{\n" +
      "    param ([Parameter(Mandatory = \$true)]\n" +
      "    [ValidateScript({ (\$_ -ge 1 -and \$_ -le 3) -or (\$_ -ge 20) })]\n" +
      "    [int] \$Count)\n" +
      "    \$Count = 0\n" +
      "    \$Count += 0\n" +
      "}\n" +
      "\n" +
      "[DscResource()]\n" +
      "class Person\n" +
      "{\n" +
      "    [int]\$Age\n" +
      "    Person(\$foo)\n" +
      "    {\n" +
      "    }\n" +
      "    Person([int] \$a)\n" +
      "    {\n" +
      "        \$this.Age = \$a\n" +
      "    }\n" +
      "    DoSomething(\$x)\n" +
      "    {\n" +
      "        \$this.DoSomething(\$x)\n" +
      "        \$this.DoSomethingElse()\n" +
      "    }\n" +
      "    hidden DoSomethingElse()\n" +
      "    {\n" +
      "    }\n" +
      "}\n" +
      "#constructor override\n" +
      "class Child : Person\n" +
      "{\n" +
      "    [string]\$School\n" +
      "    Child([int] \$a, [string] \$s) : base(\$a)\n" +
      "    {\n" +
      "        \$this.School = \$s\n" +
      "    }\n" +
      "}\n" +
      "[Child] \$littleOne = [Child]::new(10, \"Silver Fir Elementary School\")\n" +
      "\$littleOne.Age"
  }

  override fun getIndentOptionsEditor(): IndentOptionsEditor {
    return SmartIndentOptionsEditor()
  }

  override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
    when (settingsType) {
      SettingsType.SPACING_SETTINGS -> {
        consumer.showStandardOptions(
          "SPACE_BEFORE_METHOD_PARENTHESES",
          "SPACE_BEFORE_METHOD_CALL_PARENTHESES",

          "SPACE_BEFORE_IF_PARENTHESES",
          "SPACE_BEFORE_FOR_PARENTHESES",
          "SPACE_BEFORE_WHILE_PARENTHESES",
          "SPACE_BEFORE_SWITCH_PARENTHESES",

          "SPACE_AROUND_ASSIGNMENT_OPERATORS",
          "SPACE_AROUND_LOGICAL_OPERATORS",
          "SPACE_AROUND_RELATIONAL_OPERATORS",
          "SPACE_AROUND_BITWISE_OPERATORS",
          "SPACE_AROUND_ADDITIVE_OPERATORS",
          "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",
          "SPACE_AROUND_UNARY_OPERATOR",
          "SPACE_AROUND_METHOD_REF_DBL_COLON",

          "SPACE_BEFORE_CLASS_LBRACE",
          "SPACE_BEFORE_METHOD_LBRACE",
          "SPACE_BEFORE_IF_LBRACE",
          "SPACE_BEFORE_ELSE_LBRACE",
          "SPACE_BEFORE_FOR_LBRACE",
          "SPACE_BEFORE_WHILE_LBRACE",
          "SPACE_BEFORE_TRY_LBRACE",
          "SPACE_BEFORE_CATCH_LBRACE",
          "SPACE_BEFORE_FINALLY_LBRACE",
          "SPACE_BEFORE_DO_LBRACE",
          "SPACE_BEFORE_SWITCH_LBRACE",
          "SPACE_BEFORE_ARRAY_INITIALIZER_LBRACE",

          "SPACE_BEFORE_ELSE_KEYWORD",
          "SPACE_BEFORE_WHILE_KEYWORD",
          "SPACE_BEFORE_CATCH_KEYWORD",
          "SPACE_BEFORE_FINALLY_KEYWORD",

          "SPACE_WITHIN_BRACES",
          "SPACE_WITHIN_BRACKETS",
          "SPACE_WITHIN_PARENTHESES",
          "SPACE_WITHIN_SUB_EXPRESSION_PARENTHESES",
          "SPACE_WITHIN_METHOD_CALL_PARENTHESES",
          "SPACE_WITHIN_METHOD_PARENTHESES",
          "SPACE_WITHIN_IF_PARENTHESES",
          "SPACE_WITHIN_FOR_PARENTHESES",
          "SPACE_WITHIN_WHILE_PARENTHESES",
          "SPACE_WITHIN_SWITCH_PARENTHESES",
          "SPACE_WITHIN_CAST_PARENTHESES",
          "SPACE_AFTER_TYPE_CAST",
          "SPACE_AFTER_COMMA",
          "SPACE_BEFORE_COMMA",
          "SPACE_AFTER_SEMICOLON",
          "SPACE_BEFORE_SEMICOLON",
          "SPACE_BEFORE_ANOTATION_PARAMETER_LIST",
          "SPACE_WITHIN_ANNOTATION_PARENTHESES",
          "SPACE_WITHIN_EMPTY_METHOD_CALL_PARENTHESES",
          "SPACE_WITHIN_EMPTY_METHOD_PARENTHESES"
        )
        consumer.renameStandardOption("SPACE_BEFORE_ARRAY_INITIALIZER_LBRACE", "Block expression")

        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java,
          "SPACE_WITHIN_SUB_EXPRESSION_PARENTHESES",
          "Sub-expression parentheses",
          CodeStyleSettingsCustomizableOptions.getInstance().SPACES_WITHIN
        )

        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java,
          "SPACE_BEFORE_DATA_LBRACE",
          "'data' left brace",
          CodeStyleSettingsCustomizableOptions.getInstance().SPACES_BEFORE_LEFT_BRACE
        )
        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java,
          "SPACE_BEFORE_TRAP_LBRACE",
          "'trap' left brace",
          CodeStyleSettingsCustomizableOptions.getInstance().SPACES_BEFORE_LEFT_BRACE
        )
        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "SPACE_BEFORE_COLON", "Before colon",
          CodeStyleSettingsCustomizableOptions.getInstance().SPACES_OTHER, CodeStyleSettingsCustomizable.OptionAnchor.BEFORE
        )
        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "SPACE_AFTER_COLON", "After colon",
          CodeStyleSettingsCustomizableOptions.getInstance().SPACES_OTHER, CodeStyleSettingsCustomizable.OptionAnchor.BEFORE
        )
      }

      SettingsType.BLANK_LINES_SETTINGS -> consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE")
      SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
        consumer.showStandardOptions(
          "RIGHT_MARGIN",
          "KEEP_LINE_BREAKS",
          "KEEP_FIRST_COLUMN_COMMENT",
          "KEEP_SIMPLE_BLOCKS_IN_ONE_LINE",
          "KEEP_SIMPLE_CLASSES_IN_ONE_LINE",
          "KEEP_SIMPLE_METHODS_IN_ONE_LINE",
          "KEEP_SIMPLE_LAMBDAS_IN_ONE_LINE",//block_expression
          "WRAP_LONG_LINES",
          "CLASS_BRACE_STYLE",
          "METHOD_BRACE_STYLE",
          "BRACE_STYLE",
          "METHOD_PARAMETERS_WRAP",
          "ALIGN_MULTILINE_PARAMETERS",
          "METHOD_PARAMETERS_LPAREN_ON_NEXT_LINE",
          "METHOD_PARAMETERS_RPAREN_ON_NEXT_LINE",
          "CALL_PARAMETERS_WRAP",
          "ALIGN_MULTILINE_PARAMETERS_IN_CALLS",
          "CALL_PARAMETERS_LPAREN_ON_NEXT_LINE",
          "CALL_PARAMETERS_RPAREN_ON_NEXT_LINE",
          "WRAP_FIRST_METHOD_IN_CALL_CHAIN",
          "METHOD_CALL_CHAIN_WRAP",
          "ALIGN_MULTILINE_CHAINED_METHODS",
          "SPECIAL_ELSE_IF_TREATMENT",
          "ELSE_ON_NEW_LINE",
          "FOR_STATEMENT_WRAP",
          "ALIGN_MULTILINE_FOR",
          "FOR_STATEMENT_LPAREN_ON_NEXT_LINE",
          "FOR_STATEMENT_RPAREN_ON_NEXT_LINE",
          "WHILE_ON_NEW_LINE",
          "CATCH_ON_NEW_LINE",
          "FINALLY_ON_NEW_LINE",
          "PARAMETER_ANNOTATION_WRAP",
          "CLASS_ANNOTATION_WRAP",
          "METHOD_ANNOTATION_WRAP",
          "FIELD_ANNOTATION_WRAP",
          "BINARY_OPERATION_WRAP",
          "ALIGN_MULTILINE_BINARY_OPERATION",
          "PARENTHESES_EXPRESSION_LPAREN_WRAP",
          "PARENTHESES_EXPRESSION_RPAREN_WRAP"
        )

        consumer.renameStandardOption("KEEP_SIMPLE_LAMBDAS_IN_ONE_LINE", "Simple block expressions in one line")
        consumer.renameStandardOption("SPECIAL_ELSE_IF_TREATMENT", "'elseif' on new line")

        consumer.renameStandardOption("PARAMETER_ANNOTATION_WRAP", "Parameter attribute")
        consumer.renameStandardOption("CLASS_ANNOTATION_WRAP", "Class attribute")
        consumer.renameStandardOption("METHOD_ANNOTATION_WRAP", "Method attribute")
        consumer.renameStandardOption("FIELD_ANNOTATION_WRAP", "Field attribute")

        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java,
          "KEEP_SIMPLE_HASH_LITERAL_IN_ONE_LINE",
          "Simple hash literals in one line",
          CodeStyleSettingsCustomizableOptions.getInstance().WRAPPING_KEEP
        )
        consumer.renameStandardOption("METHOD_ANNOTATION_WRAP", "Method attribute")

        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "CATCH_TYPE_LIST_WRAP",
          MessagesBundle.message("wrapping.catch,type.list"),
          null,
          CodeStyleSettingsCustomizable.OptionAnchor.AFTER,
          "BINARY_OPERATION_WRAP",
          CodeStyleSettingsCustomizableOptions.getInstance().WRAP_OPTIONS,
          CodeStyleSettingsCustomizable.WRAP_VALUES
        )
        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "ALIGN_MULTILINE_CATCH_TYPE_LIST",
          MessagesBundle.message("code.style.align.multiline"),
          MessagesBundle.message(
            "wrapping.catch,type.list"
          )
        )

        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "ATTRIBUTE_ARGUMENT_WRAP",
          MessagesBundle.message("wrapping.attribute.argument"),
          null,
          CodeStyleSettingsCustomizable.OptionAnchor.AFTER,
          "BINARY_OPERATION_WRAP",
          CodeStyleSettingsCustomizableOptions.getInstance().WRAP_OPTIONS,
          CodeStyleSettingsCustomizable.WRAP_VALUES
        )
        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "ALIGN_MULTILINE_ATTRIBUTE_ARGUMENT",
          MessagesBundle.message("code.style.align.multiline"),
          MessagesBundle.message("wrapping.attribute.argument")
        )

        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "PIPELINE_TAIL_WRAP",
          MessagesBundle.message("wrapping.pipeline"),
          null,
          CodeStyleSettingsCustomizable.OptionAnchor.AFTER,
          "BINARY_OPERATION_WRAP",
          CodeStyleSettingsCustomizableOptions.getInstance().WRAP_OPTIONS,
          CodeStyleSettingsCustomizable.WRAP_VALUES
        )

        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "ALIGN_MULTILINE_PIPELINE_STATEMENT",
          MessagesBundle.message("code.style.align.multiline"),
          MessagesBundle.message("wrapping.pipeline")
        )

        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "BLOCK_PARAMETER_CLAUSE_WRAP",
          MessagesBundle.message("wrapping.block.parameters"),
          null,
          CodeStyleSettingsCustomizable.OptionAnchor.AFTER,
          "METHOD_PARAMETERS_WRAP",
          CodeStyleSettingsCustomizableOptions.getInstance().WRAP_OPTIONS,
          CodeStyleSettingsCustomizable.WRAP_VALUES
        )

        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "ALIGN_MULTILINE_BLOCK_PARAMETERS",
          MessagesBundle.message("code.style.align.multiline"),
          MessagesBundle.message("wrapping.block.parameters")
        )

        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "BLOCK_PARAMETERS_LPAREN_ON_NEXT_LINE",
          "New line after '('", MessagesBundle.message("wrapping.block.parameters")
        )
        consumer.showCustomOption(
          PowerShellCodeStyleSettings::class.java, "BLOCK_PARAMETERS_RPAREN_ON_NEXT_LINE",
          "Place ')' on new line", MessagesBundle.message("wrapping.block.parameters")
        )


      }

      SettingsType.INDENT_SETTINGS -> consumer.showStandardOptions(
        "INDENT_SIZE",
        "CONTINUATION_INDENT_SIZE",
        "TAB_SIZE"
      )

      SettingsType.COMMENTER_SETTINGS -> consumer.showStandardOptions("COMMENTER_SETTINGS")
      SettingsType.LANGUAGE_SPECIFIC -> consumer.showStandardOptions("LANGUAGE_SPECIFIC")
    }
  }

  override fun customizeDefaults(defaultSettings: CommonCodeStyleSettings, indentOptions: CommonCodeStyleSettings.IndentOptions) {
    defaultSettings.KEEP_SIMPLE_LAMBDAS_IN_ONE_LINE = true
    defaultSettings.RIGHT_MARGIN = 115
    defaultSettings.SPACE_WITHIN_BRACES = true

    defaultSettings.SPACE_BEFORE_ARRAY_INITIALIZER_LBRACE = true

    defaultSettings.SPACE_AFTER_TYPE_CAST = false

    defaultSettings.CATCH_ON_NEW_LINE = true
    defaultSettings.FINALLY_ON_NEW_LINE = true

    defaultSettings.ELSE_ON_NEW_LINE = true
    defaultSettings.SPECIAL_ELSE_IF_TREATMENT = true

    defaultSettings.BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE
    defaultSettings.CLASS_BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE
    defaultSettings.METHOD_BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE

    defaultSettings.FIELD_ANNOTATION_WRAP = CommonCodeStyleSettings.DO_NOT_WRAP

    indentOptions.INDENT_SIZE = 4
    indentOptions.CONTINUATION_INDENT_SIZE = 8
    indentOptions.TAB_SIZE = 4
  }

  override fun getLanguage(): Language {
    return PowerShellLanguage.INSTANCE
  }

}



