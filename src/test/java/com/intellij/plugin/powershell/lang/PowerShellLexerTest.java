package com.intellij.plugin.powershell.lang;

import com.intellij.lexer.Lexer;
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter;
import com.intellij.testFramework.LexerTestCase;

/**
 * Andrey 16/08/17.
 */
public class PowerShellLexerTest extends LexerTestCase {
  @Override
  protected Lexer createLexer() {
    return new PowerShellLexerAdapter();
  }

  @Override
  protected String getDirPath() {
    return null;
  }


  public void testLineComment() {
    doTest("hello#world", "SIMPLE_ID ('hello#world')"); doTest("hello #world", "SIMPLE_ID ('hello')\n" + "WHITE_SPACE (' ')\n" + "COMMENT ('#world')");
  }

  public void testWhiteSpace() {
    doTest(" ", "WHITE_SPACE (' ')"); doTest("`\n", "WHITE_SPACE ('`\\n')");
  }

  public void testGenericNames() {
    doTest("Get-Power", "GENERIC_ID ('Get-Power')"); doTest("Get-Power2", "GENERIC_ID ('Get-Power2')"); doTest("Get-Power!2", "GENERIC_ID ('Get-Power!2')");
  }

  public void testIntegerLiteral() {
    doTest("123", "DEC_INTEGER ('123')"); doTest("200000000000", "DEC_INTEGER ('200000000000')"); doTest("123l", "DEC_INTEGER ('123l')"); doTest("0x200000000000", "HEX_INTEGER ('0x200000000000')"); doTest("123d", "DEC_INTEGER ('123d')"); doTest("0x2001l", "HEX_INTEGER ('0x2001l')");
    doTest("123L", "DEC_INTEGER ('123L')"); doTest("0x2001l", "HEX_INTEGER ('0x2001l')"); doTest("123D", "DEC_INTEGER ('123D')"); doTest("0x2002L", "HEX_INTEGER ('0x2002L')"); doTest("0x2003Lkb", "HEX_INTEGER ('0x2003Lkb')"); doTest("0x2003kb", "HEX_INTEGER ('0x2003kb')");
    doTest("0x2003Kb", "HEX_INTEGER ('0x2003Kb')"); doTest("0x2003Gb", "HEX_INTEGER ('0x2003Gb')"); doTest("0x2003pB", "HEX_INTEGER ('0x2003pB')");
  }

  public void testIntegerLiteralSign() {
    doTest("+123", "+ ('+')\n" + "DEC_INTEGER ('123')"); doTest("-123", "- ('-')\n" + "DEC_INTEGER ('123')");

  }

  public void testRealLiteral() {
    doTest("1.", "REAL_NUM ('1.')"); doTest("1.23", "REAL_NUM ('1.23')"); doTest(".45e35", "REAL_NUM ('.45e35')"); doTest("32.e+12", "REAL_NUM ('32.e+12')"); doTest("1.2345e-3d", "REAL_NUM ('1.2345e-3d')"); doTest("123.456E-231", "REAL_NUM ('123.456E-231')");
    doTest("1.20d", "REAL_NUM ('1.20d')"); doTest("1.23450e1d", "REAL_NUM ('1.23450e1d')");
  }

  public void testSimpleVariableId() {
    doTest("$totalCost", "$ ('$')\n" + "SIMPLE_ID ('totalCost')"); doTest("$итог", "$ ('$')\n" + "SIMPLE_ID ('итог')");
    doTest("$総計", "$ ('$')\n" + "SIMPLE_ID ('総計')");
    doTest("$values[2]", "$ ('$')\n" + "SIMPLE_ID ('values')\n" + "[ ('[')\n" + "DEC_INTEGER ('2')\n" + "] (']')");
  }

  public void testBracedVariableId() {
    doTest("${Name with`twhite space and `{punctuation`}}", "$ ('$')\n" + "{ ('{')\n" + "BRACED_ID ('Name with`twhite space and `{punctuation`}')" + "\n" + "} ('}')");

    doTest("${E:\\File.txt}", "$ ('$')\n" + "{ ('{')\n" + "BRACED_ID ('E:\\File.txt')\n" + "} ('}')");
  }

  public void testExpandableStringLiteral() {
    doTest("\"Hello World!\"", "EXPANDABLE_STRING ('\"Hello World!\"')"); doTest("“Hello World!\"", "EXPANDABLE_STRING ('“Hello World!\"')");
    doTest("\"\"", "EXPANDABLE_STRING ('\"\"')"); doTest("\"Hello World!”", "EXPANDABLE_STRING ('\"Hello World!”')");
    doTest("„Hello World!„", "EXPANDABLE_STRING ('„Hello World!„')");
    doTest("\"I said, \"\"Hello\"\".\"", "EXPANDABLE_STRING ('\"I said, \"\"Hello\"\".\"')");
    doTest("\"I said, „„Hello„„.\"", "EXPANDABLE_STRING ('\"I said, „„Hello„„.\"')");
    doTest("\"I said, 'Hello'\"", "EXPANDABLE_STRING ('\"I said, 'Hello'\"')");
    doTest("\"column1`tcolumn2`nsecond line, `\"Hello`\", ```Q`5`!\"", "EXPANDABLE_STRING ('\"column1`tcolumn2`nsecond line, `\"Hello`\", " +
        "```Q`5`!\"')");
    // TODO: 02/09/17 variable substitution:
    doTest("\">$a.Length<\"", "EXPANDABLE_STRING ('\">$a.Length<\"')"); doTest("\"${Lengt}\"", "EXPANDABLE_STRING ('\"${Lengt}\"')");
  }

  public void testVerbatimStringLiteral() {
    doTest("'Hello World!'", "VERBATIM_STRING (''Hello World!'')"); doTest("‘Hello, World'", "VERBATIM_STRING ('‘Hello, World'')");
    doTest("'What‘'s the time?'", "VERBATIM_STRING (''What‘'s the time?'')"); doTest("‘Hello, World'", "VERBATIM_STRING ('‘Hello, World'')");
    doTest("‚Hello, World‚", "VERBATIM_STRING ('‚Hello, World‚')"); doTest("‛Hello, World‛", "VERBATIM_STRING ('‛Hello, World‛')");
    doTest("'What''s the time?'", "VERBATIM_STRING (''What''s the time?'')");
  }

  public void testExpandableHereStringLiteral() {
    doTest("@\"\n" + "\"@", "EXPANDABLE_HERE_STRING ('@\"\\n\"@')"); doTest("@„\n" + "„@", "EXPANDABLE_HERE_STRING ('@„\\n„@')");
    doTest("@\"\n" + "line 1\n" + "\"@", "EXPANDABLE_HERE_STRING ('@\"\\n" + "line 1\\n" + "\"@')");
  }

  public void testVerbatimHereStringLiteral() {
    doTest("@'\n" + "'@", "VERBATIM_HERE_STRING ('@'\\n'@')"); doTest("@‚\n" + "‚@", "VERBATIM_HERE_STRING ('@‚\\n‚@')");
    doTest("@‘\n" + "'@", "VERBATIM_HERE_STRING ('@‘\\n'@')"); doTest("@‛\n" + "‛@", "VERBATIM_HERE_STRING ('@‛\\n‛@')");
    doTest("@'\n" + "line 1\n" + "'@", "VERBATIM_HERE_STRING ('@'\\n" + "line 1\\n" + "'@')");
  }
}
