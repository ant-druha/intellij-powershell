package com.intellij.plugin.powershell.lang

import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import java.lang.StringBuilder

class PowerShellCodeInsightTest : LightPlatformCodeInsightFixtureTestCase() {

  fun testDecodeExpandableString() {
    //quotes
    checkStringDecode("This is sample host \"\"double quoted\"\" string",
                      "This is sample host \"double quoted\" string")
    checkStringDecode("This is sample host 'double quoted' string",
                      "This is sample host 'double quoted' string")

    //new lines
    checkStringDecode("var array = {`\"1`\":`\"2`\"}`nfor (var obj in array) {`n    obj.concat(`\"1234`\",`\"1212112`\",`\"22222`\")`n}`n",
                      "var array = {\"1\":\"2\"}\n" +
                          "for (var obj in array) {\n" +
                          "    obj.concat(\"1234\",\"1212112\",\"22222\")\n" +
                          "}\n")

    //sub-expressions
    checkStringDecode("This is a sample `\"string`\" with \$( Write-Output \"Sub-expression\" )end \$( Write-Output \"Sub-expression \" ) `\"of`\" string.",
                      "This is a sample \"string\" with \$( Write-Output \"Sub-expression\" )end \$( Write-Output \"Sub-expression \" ) \"of\" string.")
    checkStringDecode("Test2 is a sample `\"string`\" with \$( Write-Output \"Sub-expression\" ) and \$sample_var `\"of`\" string.",
                      "Test2 is a sample \"string\" with \$( Write-Output \"Sub-expression\" ) and \$sample_var \"of\" string.")

    //escape chars
    checkStringDecode("sample string `\$MyInvocation text",
                      "sample string \$MyInvocation text")
    checkStringDecode("text1 ```` `\$foo ```` `\"``'text2",
                      "text1 `` \$foo `` \"`'text2")
    checkStringDecode("Text2 `\"string`\" with `$( Write-Output `\"Sub-expression`\" )end `$( Write-Output `\"Sub-expression `\" ) `\"of`\" string.",
                      "Text2 \"string\" with $( Write-Output \"Sub-expression\" )end $( Write-Output \"Sub-expression \" ) \"of\" string.")
    checkStringDecode("$(dsdkkdkd) `$(ddd) `\" `$(sss)sasa `$(wqwq) `\$eee `$(",
                      "$(dsdkkdkd) $(ddd) \" $(sss)sasa $(wqwq) \$eee $(")
  }

  fun testDecodeExpandableHereString() {
    //quotes
    decodeExpandableHereString("This is sample host \"string\" content",
                               "This is sample host \"string\" content")
    decodeExpandableHereString("This is sample host 'string' content",
                               "This is sample host 'string' content")

    //new lines
    decodeExpandableHereString("1st line\n" +
                                   "2nd line`n3rd line",
                               "1st line\n" +
                                   "2nd line\n" +
                                   "3rd line")

    //sub-expressions
    decodeExpandableHereString("1st line \$( Write-Output \"Hello world!\") still `\"1st`\" line\n" +
                                   "2nd line",
                               "1st line \$( Write-Output \"Hello world!\") still \"1st\" line\n" +
                                   "2nd line")
    decodeExpandableHereString("\n" +
                                   "   var ss = \"sasasas\";\n" +
                                   "    var w2 = \"sasasas\";``sasasas``\n" +
                                   "    var content = \$('#home-content');\n" +
                                   "    \$v#bt_str;\n" +
                                   "    \"qwe\" ``123``",
                               "\n" +
                                   "   var ss = \"sasasas\";\n" +
                                   "    var w2 = \"sasasas\";`sasasas`\n" +
                                   "    var content = \$('#home-content');\n" +
                                   "    \$v#bt_str;\n" +
                                   "    \"qwe\" `123`")

    //escape chars
    decodeExpandableHereString("text1 `\$newVar text2",
                               "text1 \$newVar text2")
    decodeExpandableHereString("text1 ```` `\$foo ```` \"``'text2",
                               "text1 `` \$foo `` \"`'text2")
  }

  fun testDecodeVerbatimString() {
    //quotes
    decodeVerbatimString("This is sample host \"string\" content",
                         "This is sample host \"string\" content")
    decodeVerbatimString("This is sample host ''string'' content",
                         "This is sample host 'string' content")

    //new lines
    decodeVerbatimString("1st line `n still 1st line\n" +
                             "2nd line",
                         "1st line `n still 1st line\n" +
                             "2nd line")

    //escape chars
    decodeVerbatimString("test escapes \$foo ` \" '' ",
                         "test escapes \$foo ` \" ' ")
  }

  private fun decodeVerbatimString(stringContent: String, expectedOutText: String) = checkStringDecode(stringContent, expectedOutText, false)
  private fun decodeVerbatimHereString(stringContent: String, expectedOutText: String) = checkStringDecode(stringContent, expectedOutText, false, true)
  private fun decodeExpandableHereString(stringContent: String, expectedOutText: String) = checkStringDecode(stringContent, expectedOutText, true, true)

  fun testDecodeVerbatimHereString() {
    decodeVerbatimHereString("This is sample host \"string\" content",
                             "This is sample host \"string\" content")
    decodeVerbatimHereString("This is sample host 'string' content",
                             "This is sample host 'string' content")
  }


  fun testContentChangeExpandableString() {
    //quotes
    checkInjectedText("var foo = \"text\";",
                      "var foo = `\"text`\";")
    checkInjectedText("var bar = \"new text\";",
                      "var bar = \"\"new text\"\";", defaultText = "var foo = \"\"old text\"\";")

    //new lines
    checkInjectedText("var array = {\"1\":\"2\"}\n" +
                          "for (var obj in array) {\n" +
                          "    obj.concat(\"1234\",\"1212112\",\"22222\")\n" +
                          "}\n",
                      "var array = {`\"1`\":`\"2`\"}`nfor (var obj in array) {`n    obj.concat(`\"1234`\",`\"1212112`\",`\"22222`\")`n}`n")

    //sub-expressions todo missingValue
//    checkInjectedText("This is a sample \"string\" with \$( Write-Output \"Sub-expression\" )end \$( Write-Output \"Sub-expression \" ) \"of\" string.",
//                      "This is a sample `\"string`\" with \$( Write-Output \"Sub-expression\" )end \$( Write-Output \"Sub-expression \" ) `\"of`\" string.")

    //escape chars
    checkInjectedText("I said  \$wwww ''Hello!''",
                      "I said  `\$wwww ''Hello!''")
    checkInjectedText("test \"with\" escape` ``` chars \" `",
                      "test `\"with`\" escape`` `````` chars `\" ``")
    checkInjectedText("text2 \"start\" $( Write-Output \"Hello 'Power' \"Shell\"!\") text end \"Here\" \"new text\";",
                      "text2 \"\"start\"\" `$( Write-Output \"\"Hello 'Power' \"\"Shell\"\"!\"\") text end \"\"Here\"\" \"\"new text\"\";", defaultText = "var foo = \"\"old text\"\";")

    checkInjectedText("text3 \"start\" $( Write-Output \"Hello 'Power' \"Shell\"!\") text end \"Here\" \"new text\";",
                      "text3 `\"start`\" `$( Write-Output `\"Hello 'Power' `\"Shell`\"!`\") text end `\"Here`\" `\"new text`\";")
    checkInjectedText("text4 \"start\" \$sample_var text end \"Here\" \"new text\";",
                      "text4 `\"start`\" `\$sample_var text end `\"Here`\" `\"new text`\";")
    checkInjectedText("$(ddd) \" $(sss)sasa \$(wqwq) \$eee $(",
                      "`$(ddd) `\" `$(sss)sasa `$(wqwq) `\$eee `$(")
  }


  fun testContentChangeExpandableHereString() {
    //quotes
    updateExpandableHereString("var foo = \"text\";",
                               "var foo = \"text\";")

    //new lines
    updateExpandableHereString("var foo = \"text\";",
                               "var foo = \"text\";")


    //escape chars
    updateExpandableHereString("text1 \$newVar text2",
                               "text1 `\$newVar text2")
  }

  fun testContentChangeVerbatimString() {
    //quotes
    updateVerbatimString("var foo = \"text\";",
                         "var foo = \"text\";")
    updateVerbatimString("var foo = 'text';",
                         "var foo = ''text'';")

    //new lines
    updateVerbatimString("test `n\n" +
                             " ",
                         "test `n\n" +
                             " ")

    //escape chars
    updateVerbatimString("test \$foo",
                         "test \$foo")
    updateVerbatimString("test `\$foo escape",
                         "test `\$foo escape")
  }

  fun testContentChangeVerbatimHereString() {
    //quotes
    updateVerbatimHereString("var foo = \"text\";",
                             "var foo = \"text\";")
    updateVerbatimHereString("var foo = 'text';",
                             "var foo = 'text';")

    //new lines
    updateVerbatimHereString("1st line`n same line\n" +
                                 "new line",
                             "1st line`n same line\n" +
                                 "new line")

    //escape chars
    updateVerbatimHereString("test escape `\$foo chars",
                             "test escape `\$foo chars")
    updateVerbatimHereString("test escape \$foo chars",
                             "test escape \$foo chars")
  }

  private fun updateExpandableHereString(newInjectedText: String, expectedStringContent: String) = checkInjectedText(newInjectedText, expectedStringContent, true, true)
  private fun updateVerbatimString(newInjectedText: String, expectedStringContent: String) = checkInjectedText(newInjectedText, expectedStringContent, false)
  private fun updateVerbatimHereString(newInjectedText: String, expectedStringContent: String) = checkInjectedText(newInjectedText, expectedStringContent, false, true)

  //external editor -> PowerShell string
  private fun checkInjectedText(newInjectedText: String, expectedStringContent: String, isExpandable: Boolean = true, isHere: Boolean = false, defaultText: String = "var foo = 1") {
    val string = createStringExpression(isHere, isExpandable, defaultText)
    object : WriteCommandAction<Any>(project, "Test checkInjectedText. New Text='$newInjectedText'", myFixture.file) {
      override fun run(result: Result<Any>) {
        string.updateText(newInjectedText)
        val newString = PsiTreeUtil.findChildOfType(myFixture.file, PowerShellStringLiteralExpression::class.java) ?: error("file text='${myFixture.file}'")
        assert(newString.getStringContent() == expectedStringContent, { "expected=\t'$expectedStringContent'\nactual=\t\t'${newString.getStringContent()}'" })
      }
    }.execute()
  }

  //PowerShell string -> external editor
  private fun checkStringDecode(stringContent: String, expectedOutText: String, isExpandable: Boolean = true, isHere: Boolean = false) {
    val string = createStringExpression(isHere, isExpandable, stringContent)
    val escaper = string.createLiteralTextEscaper()
    val sb = StringBuilder()
    val result = escaper.decode(string.getContentRange(), sb)
    assert(result)
    assert(expectedOutText == sb.toString(), { "expected=\t'$expectedOutText'\nactual=\t\t'$sb'" })
  }

  private fun createStringExpression(isHere: Boolean, isExpandable: Boolean, stringContent: String): PowerShellStringLiteralExpression {
    val nodeText = getNodeText(isHere, isExpandable, stringContent)
    return createStringExpression(nodeText)
  }

  private fun getNodeText(isHere: Boolean, isExpandable: Boolean, stringContent: String): String {
    return if (isHere) {
      if (isExpandable) "@\"\n$stringContent\n\"@" else "@'\n$stringContent\n'@"
    } else {
      if (isExpandable) "\"$stringContent\"" else "'$stringContent'"
    }
  }

  private fun createStringExpression(nodeText: String): PowerShellStringLiteralExpression {
    val file = myFixture.configureByText("foo.ps1", nodeText)
    return PsiTreeUtil.findChildOfType(file, PowerShellStringLiteralExpression::class.java) ?: error("text='$nodeText'")
  }

}