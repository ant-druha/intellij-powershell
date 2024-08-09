package com.intellij.plugin.powershell.lang

import com.intellij.lexer.Lexer
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter
import com.intellij.testFramework.LexerTestCase

class PowerShellLexerTest : LexerTestCase() {
  override fun createLexer(): Lexer {
    return PowerShellLexerAdapter()
  }

  override fun getDirPath(): String {
    error("Should not be called.")
  }

  fun testLineComment() {
    doTest("hello#world", "SIMPLE_ID ('hello#world')")
    doTest(
      "hello #world", """
   SIMPLE_ID ('hello')
   WHITE_SPACE (' ')
   COMMENT ('#world')
   """.trimIndent()
    )
  }

  fun testWhiteSpace() {
    doTest(" ", "WHITE_SPACE (' ')")
    doTest("`\n", "WHITE_SPACE ('`\\n')")
  }

  fun testGenericNames() {
    doTest("Get-Power", "GENERIC_ID_PART ('Get-Power')")
    doTest(
      "Get-Power2", """
   GENERIC_ID_PART ('Get-Power')
   DEC_INTEGER ('2')
   """.trimIndent()
    )
    doTest(
      "Get-Power!2", """
   GENERIC_ID_PART ('Get-Power')
   ! ('!')
   DEC_INTEGER ('2')
   """.trimIndent()
    )
    doTest(
      "echoargs.exe --% \"%path%\"", """
   SIMPLE_ID ('echoargs')
   . ('.')
   SIMPLE_ID ('exe')
   WHITE_SPACE (' ')
   --% ('--%')
   VERBATIM_ARG_INPUT (' "%path%"')
   """.trimIndent()
    )
  }

  fun testIntegerLiteral() {
    doTest("123", "DEC_INTEGER ('123')")
    doTest("200000000000", "DEC_INTEGER ('200000000000')")
    doTest("123l", "DEC_INTEGER ('123l')")
    doTest("0x200000000000", "HEX_INTEGER ('0x200000000000')")
    doTest("123d", "DEC_INTEGER ('123d')")
    doTest("0x2001l", "HEX_INTEGER ('0x2001l')")
    doTest("123L", "DEC_INTEGER ('123L')")
    doTest("0x2001l", "HEX_INTEGER ('0x2001l')")
    doTest("123D", "DEC_INTEGER ('123D')")
    doTest("0x2002L", "HEX_INTEGER ('0x2002L')")
    doTest("0x2003Lkb", "HEX_INTEGER ('0x2003Lkb')")
    doTest("0x2003kb", "HEX_INTEGER ('0x2003kb')")
    doTest("0x2003Kb", "HEX_INTEGER ('0x2003Kb')")
    doTest("0x2003Gb", "HEX_INTEGER ('0x2003Gb')")
    doTest("0x2003pB", "HEX_INTEGER ('0x2003pB')")
    doTest("0xfff81", "HEX_INTEGER ('0xfff81')")
    doTest("0xFFf81", "HEX_INTEGER ('0xFFf81')")
  }

  fun testIntegerLiteralSign() {
    doTest(
      "+123", """
   + ('+')
   DEC_INTEGER ('123')
   """.trimIndent()
    )
    doTest(
      "-123", """
   DASH ('-')
   DEC_INTEGER ('123')
   """.trimIndent()
    )
  }

  fun testRealLiteral() {
    doTest("1.", "REAL_NUM ('1.')")
    doTest("1.23", "REAL_NUM ('1.23')")
    doTest(".45e35", "REAL_NUM ('.45e35')")
    doTest("32.e+12", "REAL_NUM ('32.e+12')")
    doTest("1.2345e-3d", "REAL_NUM ('1.2345e-3d')")
    doTest("123.456E-231", "REAL_NUM ('123.456E-231')")
    doTest("1.20d", "REAL_NUM ('1.20d')")
    doTest("1.23450e1d", "REAL_NUM ('1.23450e1d')")
  }

  fun testSimpleVariableId() {
    doTest(
      "\$totalCost", """$ ('$')
SIMPLE_ID ('totalCost')"""
    )
    doTest(
      "\$итог", """$ ('$')
SIMPLE_ID ('итог')"""
    )
    doTest(
      "\$総計", """$ ('$')
SIMPLE_ID ('総計')"""
    )
    doTest(
      "\$values[2]", """$ ('$')
SIMPLE_ID ('values')
[ ('[')
DEC_INTEGER ('2')
] (']')"""
    )
  }

  fun testSimpleVariableNamespace() {
    doTest(
      "\$function: F", """$ ('$')
SIMPLE_ID ('function')
: (':')
WHITE_SPACE (' ')
SIMPLE_ID ('F')"""
    )
    doTest(
      "\$Function: F", """$ ('$')
SIMPLE_ID ('Function')
: (':')
WHITE_SPACE (' ')
SIMPLE_ID ('F')"""
    )
    doTest(
      "\$global: values", """$ ('$')
SIMPLE_ID ('global')
: (':')
WHITE_SPACE (' ')
SIMPLE_ID ('values')"""
    )
    doTest(
      "\$using: values", """$ ('$')
SIMPLE_ID ('using')
: (':')
WHITE_SPACE (' ')
SIMPLE_ID ('values')"""
    )
    doTest(
      "\$Variable:Count", """$ ('$')
SIMPLE_ID ('Variable')
: (':')
SIMPLE_ID ('Count')"""
    )
    doTest(
      "\$Env: Path", """$ ('$')
SIMPLE_ID ('Env')
: (':')
WHITE_SPACE (' ')
SIMPLE_ID ('Path')"""
    )
  }

  fun testBracedVariableId() {
    doTest(
      "\${Name with`twhite space and `{punctuation`}}", """
   ${"$"}{ ('${"$"}{')
   BRACED_ID ('Name with`twhite space and `{punctuation`}')
   } ('}')
   """.trimIndent()
    )
    doTest(
      "\${E:\\File.txt}", """
   ${"$"}{ ('${"$"}{')
   SIMPLE_ID ('E')
   : (':')
   BRACED_ID ('\File.txt')
   } ('}')
   """.trimIndent()
    )
  }

  fun testVariableScope() {
    doTest(
      "\$function: var_name", """$ ('$')
SIMPLE_ID ('function')
: (':')
WHITE_SPACE (' ')
SIMPLE_ID ('var_name')
"""
    )
    doTest(
      "\$local: var_name", """$ ('$')
SIMPLE_ID ('local')
: (':')
WHITE_SPACE (' ')
SIMPLE_ID ('var_name')
"""
    )
    doTest(
      "\$my_scope:var_name", """$ ('$')
SIMPLE_ID ('my_scope')
: (':')
SIMPLE_ID ('var_name')
"""
    )
    doTest(
      "\${my_scope: var_/'name}", """
   ${"$"}{ ('${"$"}{')
   SIMPLE_ID ('my_scope')
   : (':')
   BRACED_ID (' var_/'name')
   } ('}')
   """.trimIndent()
    )
    doTest(
      "\${E: \\File.txt}", """
   ${"$"}{ ('${"$"}{')
   SIMPLE_ID ('E')
   : (':')
   BRACED_ID (' \File.txt')
   } ('}')
   """.trimIndent()
    )
    doTest(
      "\${my_scope: File.txt}", """
   ${"$"}{ ('${"$"}{')
   SIMPLE_ID ('my_scope')
   : (':')
   BRACED_ID (' File.txt')
   } ('}')
   """.trimIndent()
    )
    doTest(
      "\${not a_scope: File.txt}", """
   ${"$"}{ ('${"$"}{')
   BRACED_ID ('not a_scope: File.txt')
   } ('}')
   """.trimIndent()
    )
    doTest(
      "\${not_a_scope : File.txt}", """
   ${"$"}{ ('${"$"}{')
   BRACED_ID ('not_a_scope : File.txt')
   } ('}')
   """.trimIndent()
    )
    doTest(
      "\${a_scope:File.txt}", """
   ${"$"}{ ('${"$"}{')
   SIMPLE_ID ('a_scope')
   : (':')
   BRACED_ID ('File.txt')
   } ('}')
   """.trimIndent()
    )
  }

  fun testExpandableStringLiteral() {
    doTest(
      "\"Hello World!\"", """
   DQ_OPEN ('"')
   EXPANDABLE_STRING_PART ('Hello World!')
   DQ_CLOSE ('"')
   """.trimIndent()
    )
    doTest(
      "“Hello World!\"", """
   DQ_OPEN ('“')
   EXPANDABLE_STRING_PART ('Hello World!')
   DQ_CLOSE ('"')
   """.trimIndent()
    )
    doTest(
      "\"\"", """
   DQ_OPEN ('"')
   DQ_CLOSE ('"')
   """.trimIndent()
    )
    doTest(
      "\"Hello World!”", """
   DQ_OPEN ('"')
   EXPANDABLE_STRING_PART ('Hello World!')
   DQ_CLOSE ('”')
   """.trimIndent()
    )
    doTest(
      "„Hello World!„", """
   DQ_OPEN ('„')
   EXPANDABLE_STRING_PART ('Hello World!')
   DQ_CLOSE ('„')
   """.trimIndent()
    )
    doTest(
      "\"I said, \"\"Hello\"\".\"", """
   DQ_OPEN ('"')
   EXPANDABLE_STRING_PART ('I said, ""Hello"".')
   DQ_CLOSE ('"')
   """.trimIndent()
    )
    doTest(
      "\"I said, „„Hello„„.\"", """
   DQ_OPEN ('"')
   EXPANDABLE_STRING_PART ('I said, „„Hello„„.')
   DQ_CLOSE ('"')
   """.trimIndent()
    )
    doTest(
      "\"I said, 'Hello'\"", """
   DQ_OPEN ('"')
   EXPANDABLE_STRING_PART ('I said, 'Hello'')
   DQ_CLOSE ('"')
   """.trimIndent()
    )
    doTest(
      "\"column1`tcolumn2`nsecond line, `\"Hello`\", ```Q`5`!\"", """
   DQ_OPEN ('"')
   EXPANDABLE_STRING_PART ('column1`tcolumn2`nsecond line, `"Hello`", ```Q`5`!')
   DQ_CLOSE ('"')
   """.trimIndent()
    )
    doTest(
      "\">\$a.Length<\"", """DQ_OPEN ('"')
EXPANDABLE_STRING_PART ('>')
$ ('$')
SIMPLE_ID ('a')
EXPANDABLE_STRING_PART ('.Length<')
DQ_CLOSE ('"')"""
    )
    doTest(
      "\"\${Lengt}\"", """
   DQ_OPEN ('"')
   ${"$"}{ ('${"$"}{')
   BRACED_ID ('Lengt')
   } ('}')
   DQ_CLOSE ('"')
   """.trimIndent()
    )
    doTest(
      "\"lonely dollar $ is here$\"", """
   DQ_OPEN ('"')
   EXPANDABLE_STRING_PART ('lonely dollar ')
   EXPANDABLE_STRING_PART ('$')
   EXPANDABLE_STRING_PART (' is here')
   EXPANDABLE_STRING_PART ('$')
   DQ_CLOSE ('"')
   """.trimIndent()
    )
  }

  fun testVerbatimStringLiteral() {
    doTest("'Hello World!'", "VERBATIM_STRING (''Hello World!'')")
    doTest("‘Hello, World'", "VERBATIM_STRING ('‘Hello, World'')")
    doTest("'What‘'s the time?'", "VERBATIM_STRING (''What‘'s the time?'')")
    doTest("‘Hello, World'", "VERBATIM_STRING ('‘Hello, World'')")
    doTest("‚Hello, World‚", "VERBATIM_STRING ('‚Hello, World‚')")
    doTest("‛Hello, World‛", "VERBATIM_STRING ('‛Hello, World‛')")
    doTest("'What''s the time?'", "VERBATIM_STRING (''What''s the time?'')")
  }

  fun testExpandableHereStringLiteral() {
    doTest(
      """
            @"
            "@
            """.trimIndent(), """
   EXPANDABLE_HERE_STRING_START ('@"\n')
   EXPANDABLE_HERE_STRING_END ('"@')
   """.trimIndent()
    )
    doTest(
      """
            @„
            „@
            """.trimIndent(), """
   EXPANDABLE_HERE_STRING_START ('@„\n')
   EXPANDABLE_HERE_STRING_END ('„@')
   """.trimIndent()
    )
    doTest(
      """
            @"
            line 1
            "@
            """.trimIndent(), """
   EXPANDABLE_HERE_STRING_START ('@"\n')
   EXPANDABLE_STRING_PART ('line 1\n')
   EXPANDABLE_HERE_STRING_END ('"@')
   """.trimIndent()
    )
  }

  fun testVerbatimHereStringLiteral() {
    doTest(
      """
            @'
            '@
            """.trimIndent(), "VERBATIM_HERE_STRING ('@'\\n'@')"
    )
    doTest(
      """
            @‚
            ‚@
            """.trimIndent(), "VERBATIM_HERE_STRING ('@‚\\n‚@')"
    )
    doTest(
      """
            @‘
            '@
            """.trimIndent(), "VERBATIM_HERE_STRING ('@‘\\n'@')"
    )
    doTest(
      """
            @‛
            ‛@
            """.trimIndent(), "VERBATIM_HERE_STRING ('@‛\\n‛@')"
    )
    doTest(
      """
            @'
            line 1
            '@
            """.trimIndent(), "VERBATIM_HERE_STRING ('@'\\n" + "line 1\\n" + "'@')"
    )
  }

  @Throws(Exception::class)
  fun testDash() {
    doTest(
      """
            -3
            –3
            —3
            ―3
            """.trimIndent(), """
   DASH ('-')
   DEC_INTEGER ('3')
   NLS ('\n')
   DASH ('–')
   DEC_INTEGER ('3')
   NLS ('\n')
   DASH ('—')
   DEC_INTEGER ('3')
   NLS ('\n')
   DASH ('―')
   DEC_INTEGER ('3')
   """.trimIndent()
    )
  }

  @Throws(Exception::class)
  fun testVariableIdElementAccess() {
    doTest(
      "\$h1[\$true]", """$ ('$')
SIMPLE_ID ('h1')
[ ('[')
$ ('$')
SIMPLE_ID ('true')
] (']')"""
    )
    doTest(
      "\$h1[20.5]", """$ ('$')
SIMPLE_ID ('h1')
[ ('[')
REAL_NUM ('20.5')
] (']')"""
    )
    doTest(
      "\$h1[\$my_var]", """$ ('$')
SIMPLE_ID ('h1')
[ ('[')
$ ('$')
SIMPLE_ID ('my_var')
] (']')"""
    )
  }

  @Throws(Exception::class)
  fun testComparisonOperator() {
    doTest(
      "\$j-le100", """$ ('$')
SIMPLE_ID ('j')
OP_C ('-le')
DEC_INTEGER ('100')"""
    )
    doTest(
      "\$j-gt100", """$ ('$')
SIMPLE_ID ('j')
OP_C ('-gt')
DEC_INTEGER ('100')"""
    )
    doTest(
      "\$j-ccontains100", """$ ('$')
SIMPLE_ID ('j')
OP_C ('-ccontains')
DEC_INTEGER ('100')"""
    )
    doTest(
      "\$_-eq\$null", """$ ('$')
SIMPLE_ID ('_')
OP_C ('-eq')
$ ('$')
SIMPLE_ID ('null')"""
    )
  }

  @Throws(Exception::class)
  fun testAssignmentOperator() {
    doTest(
      "\$i = 1", """$ ('$')
SIMPLE_ID ('i')
WHITE_SPACE (' ')
= ('=')
WHITE_SPACE (' ')
DEC_INTEGER ('1')"""
    )
    doTest(
      "\$i += 1", """$ ('$')
SIMPLE_ID ('i')
WHITE_SPACE (' ')
EQ_PLUS ('+=')
WHITE_SPACE (' ')
DEC_INTEGER ('1')"""
    )
    doTest(
      "\$i -= 1", """$ ('$')
SIMPLE_ID ('i')
WHITE_SPACE (' ')
EQ_DASH ('-=')
WHITE_SPACE (' ')
DEC_INTEGER ('1')"""
    )
    doTest(
      "\$i *= 1", """$ ('$')
SIMPLE_ID ('i')
WHITE_SPACE (' ')
EQ_STAR ('*=')
WHITE_SPACE (' ')
DEC_INTEGER ('1')"""
    )
    doTest(
      "\$i /= 1", """$ ('$')
SIMPLE_ID ('i')
WHITE_SPACE (' ')
EQ_DIV ('/=')
WHITE_SPACE (' ')
DEC_INTEGER ('1')"""
    )
    doTest(
      "\$i %= 1", """$ ('$')
SIMPLE_ID ('i')
WHITE_SPACE (' ')
EQ_PERS ('%=')
WHITE_SPACE (' ')
DEC_INTEGER ('1')"""
    )
  }

  fun testFunctionMultilineDefinition() {
    doTest(
      """
            function Single-Line-Definition
            {
            }

            """.trimIndent(), """
            function ('function')
            WHITE_SPACE (' ')
            GENERIC_ID_PART ('Single-Line-Definition')
            NLS ('\n')
            { ('{')
            NLS ('\n')
            } ('}')
            NLS ('\n')
            """.trimIndent()
    )
    doTest(
      """
            function
            Multi-Line-Definition
            {
            }

            """.trimIndent(), """
            function ('function')
            NLS ('\n')
            GENERIC_ID_PART ('Multi-Line-Definition')
            NLS ('\n')
            { ('{')
            NLS ('\n')
            } ('}')
            NLS ('\n')
            """.trimIndent()
    )
  }
}
