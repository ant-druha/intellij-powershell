package com.intellij.plugin.powershell.lang

import com.intellij.application.options.CodeStyle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.ide.editor.formatting.PowerShellCodeStyleSettings
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.formatter.FormatterTestCase
import com.intellij.util.IncorrectOperationException
import junit.framework.TestCase

class PowerShellFormatterTest : FormatterTestCase() {

  private val myTestDataDir = "src/test/resources/testData/"

  override fun getBasePath(): String = "format"
  override fun getFileExtension(): String = "ps1"
  override fun getTestDataPath(): String = myTestDataDir

  fun testBlockIndent() {
    doTest("indents", "indents_res")
  }

  fun testBracesShiftedIndent() {
    val tempTestSettings = getCommonSettings()
    tempTestSettings.BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE_SHIFTED2
    tempTestSettings.METHOD_BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE_SHIFTED2
    doTest("indent_brace", "indent_brace_res")
  }

  fun testDependentBracePlacement() {
    val settings = getCommonSettings()
    settings.BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED
    settings.METHOD_BRACE_STYLE = CommonCodeStyleSettings.NEXT_LINE_IF_WRAPPED
    doTest("braces1", "braces1_if_wrapped_res")
  }

  fun testPipelineWrap() {
    val psSettings = getPowerShellSettings()
    psSettings.PIPELINE_TAIL_WRAP = CommonCodeStyleSettings.WRAP_ALWAYS
    psSettings.ALIGN_MULTILINE_PIPELINE_STATEMENT = true
    doTest("indent_brace", "indent_brace_pipeline_wrap_res")
  }

  fun testBlockParametersWrap() {
    doTest("wrap_block_parameters", "wrap_block_parameters_default_res")
    val psSettings = getPowerShellSettings()
    psSettings.ALIGN_MULTILINE_BLOCK_PARAMETERS = true
    psSettings.BLOCK_PARAMETER_CLAUSE_WRAP = CommonCodeStyleSettings.WRAP_ALWAYS
    doTest("wrap_block_parameters", "wrap_block_parameters_res")
  }

  fun testDefault1() {
    val tempTestSettings = getCommonSettings()
    tempTestSettings.KEEP_LINE_BREAKS = false
    doTest("default1", "default1_res")
  }

  fun testChainedCallWrap() {
    val settings = getCommonSettings()
    settings.RIGHT_MARGIN = 60
    settings.METHOD_CALL_CHAIN_WRAP = CommonCodeStyleSettings.WRAP_AS_NEEDED
    doTest("wrap_chained_call", "wrap_chained_call_if_long_res")

    settings.WRAP_FIRST_METHOD_IN_CALL_CHAIN = true
    doTest("wrap_chained_call", "wrap_chained_call_if_long_first_wrap_res")
  }
  
  fun testNoSpacingForCompoundIdentifiers() {
    val settings = getCommonSettings()
    settings.SPACE_AROUND_MULTIPLICATIVE_OPERATORS = true
    doTest("compound_identifiers", "compound_identifiers_res")
  }

  fun testChainedCallWrapAlignment() {
    val settings = getCommonSettings()
    settings.RIGHT_MARGIN = 60
    settings.METHOD_CALL_CHAIN_WRAP = CommonCodeStyleSettings.WRAP_AS_NEEDED
    settings.ALIGN_MULTILINE_CHAINED_METHODS = true

    doTest("wrap_chained_call", "wrap_chained_call_align_res")

    settings.METHOD_CALL_CHAIN_WRAP = CommonCodeStyleSettings.WRAP_ALWAYS
    doTest("wrap_chained_call", "wrap_chained_call_always_res")
  }

  fun testWrapBinaryExpr() {
    val settings = getCommonSettings()
    settings.RIGHT_MARGIN = 30
    settings.BINARY_OPERATION_WRAP = CommonCodeStyleSettings.WRAP_ALWAYS

    doTest("wrap_binary_expr", "wrap_binary_expr_always_res")

    settings.ALIGN_MULTILINE_BINARY_OPERATION = true
    doTest("wrap_binary_expr", "wrap_binary_expr_align")

    settings.BINARY_OPERATION_WRAP = CommonCodeStyleSettings.WRAP_AS_NEEDED
    doTest("wrap_binary_expr", "wrap_binary_expr_as_needed")

    settings.ALIGN_MULTILINE_BINARY_OPERATION = false
    settings.PARENTHESES_EXPRESSION_LPAREN_WRAP = true
    doTest("wrap_binary_expr", "wrap_binary_expr_lp_new_line")
  }


  fun testBraces() {
    val before = "switch -regex -casesensitive (get-childitem | sort length) {\n" +
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
        "\n" +
        "function TrapTest {\n" +
        "    trap {\n" +
        "        \"Error found.\"\n" +
        "    }\n" +
        "    nonsenseString\n" +
        "}\n" +
        "\n" +
        "class A {\n" +
        "    doSomethingMethod() {\n" +
        "\n" +
        "    }\n" +
        "}"
    val after = "switch -regex -casesensitive (get-childitem | sort length)\n" +
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
        "\n" +
        "function TrapTest\n" +
        "{\n" +
        "    trap\n" +
        "    {\n" +
        "        \"Error found.\"\n" +
        "    }\n" +
        "    nonsenseString\n" +
        "}\n" +
        "\n" +
        "class A\n" +
        "{\n" +
        "    doSomethingMethod()\n" +
        "    {\n" +
        "\n" +
        "    }\n" +
        "}"

    doTextTest(before, after)

    val settings = getCommonSettings()
    settings.BRACE_STYLE = CommonCodeStyleSettings.END_OF_LINE
    settings.METHOD_BRACE_STYLE = CommonCodeStyleSettings.END_OF_LINE
    settings.CLASS_BRACE_STYLE = CommonCodeStyleSettings.END_OF_LINE

    doTextTest(after, before, false)
  }

  private fun doTextTest(text: String, textAfter: String, reformatAsText: Boolean = true, textRange: TextRange? = null) {
    val file = createFileFromText(text)
    val manager = PsiDocumentManager.getInstance(project)
    val document = manager.getDocument(file)
    CommandProcessor.getInstance().executeCommand(project, {
      ApplicationManager.getApplication().runWriteAction {
        try {
          if (textRange != null) {
            CodeStyleManager.getInstance(project).reformatText(file, textRange.startOffset, textRange.endOffset)
          }
          if (reformatAsText) {
            val formatRange = file.textRange
            CodeStyleManager.getInstance(project).reformatText(file, formatRange.startOffset, formatRange.endOffset)
          } else {
            CodeStyleManager.getInstance(project).reformat(file)
          }
        } catch (e: IncorrectOperationException) {
          TestCase.assertTrue(e.localizedMessage, false)
        }
      }
    }, "", "")

    assertEquals(textAfter, document!!.text)
    manager.commitDocument(document)
    assertEquals(textAfter, file.text)
  }

  private fun createFileFromText(text: String) = PsiFileFactory.getInstance(project).createFileFromText(PowerShellLanguage.INSTANCE, text)

  private fun getPowerShellSettings(): PowerShellCodeStyleSettings = settings.getCustomSettings(PowerShellCodeStyleSettings::class.java)

  private fun getCommonSettings() =  CodeStyle.getLanguageSettings(createFileFromText(""), PowerShellLanguage.INSTANCE)
}