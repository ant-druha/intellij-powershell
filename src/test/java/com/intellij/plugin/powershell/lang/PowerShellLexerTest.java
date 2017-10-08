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
    doTest("Get-Power", "GENERIC_ID_PART ('Get-Power')"); doTest("Get-Power2", "GENERIC_ID_PART ('Get-Power2')");
    doTest("Get-Power!2", "GENERIC_ID_PART ('Get-Power!2')");
    doTest("echoargs.exe --% \"%path%\"", "SIMPLE_ID ('echoargs')\n" + ". ('.')\n" + "SIMPLE_ID ('exe')\n" + "WHITE_SPACE (' ')\n" + "--% ('--%')"
        + "\n" + "VERBATIM_ARG_INPUT (' \"%path%\"')");
  }

  public void testIntegerLiteral() {
    doTest("123", "DEC_INTEGER ('123')"); doTest("200000000000", "DEC_INTEGER ('200000000000')"); doTest("123l", "DEC_INTEGER ('123l')"); doTest("0x200000000000", "HEX_INTEGER ('0x200000000000')"); doTest("123d", "DEC_INTEGER ('123d')"); doTest("0x2001l", "HEX_INTEGER ('0x2001l')");
    doTest("123L", "DEC_INTEGER ('123L')"); doTest("0x2001l", "HEX_INTEGER ('0x2001l')"); doTest("123D", "DEC_INTEGER ('123D')"); doTest("0x2002L", "HEX_INTEGER ('0x2002L')"); doTest("0x2003Lkb", "HEX_INTEGER ('0x2003Lkb')"); doTest("0x2003kb", "HEX_INTEGER ('0x2003kb')");
    doTest("0x2003Kb", "HEX_INTEGER ('0x2003Kb')"); doTest("0x2003Gb", "HEX_INTEGER ('0x2003Gb')"); doTest("0x2003pB", "HEX_INTEGER ('0x2003pB')");
    doTest("0xfff81", "HEX_INTEGER ('0xfff81')"); doTest("0xFFf81", "HEX_INTEGER ('0xFFf81')");
  }

  public void testIntegerLiteralSign() {
    doTest("+123", "+ ('+')\n" + "DEC_INTEGER ('123')"); doTest("-123", "DASH ('-')\n" + "DEC_INTEGER ('123')");

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

  public void testSimpleVariableNamespace() {
    doTest("$function: F", "$ ('$')\n" + "SIMPLE_ID ('function')\n" + ": (':')\n" + "WHITE_SPACE (' ')\n" + "SIMPLE_ID ('F')");
    doTest("$Function: F", "$ ('$')\n" + "SIMPLE_ID ('Function')\n" + ": (':')\n" + "WHITE_SPACE (' ')\n" + "SIMPLE_ID ('F')");
    doTest("$global: values", "$ ('$')\n" + "SIMPLE_ID ('global')\n" + ": (':')\n" + "WHITE_SPACE (' ')\n" + "SIMPLE_ID ('values')");
    doTest("$using: values", "$ ('$')\n" + "SIMPLE_ID ('using')\n" + ": (':')\n" + "WHITE_SPACE (' ')\n" + "SIMPLE_ID ('values')");
    doTest("$Variable:Count", "$ ('$')\n" + "SIMPLE_ID ('Variable')\n" + ": (':')\n" + "SIMPLE_ID ('Count')");
    doTest("$Env: Path", "$ ('$')\n" + "SIMPLE_ID ('Env')\n" + ": (':')\n" + "WHITE_SPACE (' ')\n" + "SIMPLE_ID ('Path')");
  }

  public void testBracedVariableId() {
    doTest("${Name with`twhite space and `{punctuation`}}", "${ ('${')\n" + "BRACED_ID ('Name with`twhite space and `{punctuation`}')\n" + "} ('}')");
    doTest("${E:\\File.txt}", "${ ('${')\n" + "BRACED_ID ('E:\\File.txt')\n" + "} ('}')");
  }

  public void testVariableScope() {
    doTest("$function: var_name", "$ ('$')\n" + "SIMPLE_ID ('function')\n" + ": (':')\n" + "WHITE_SPACE (' ')\n" + "SIMPLE_ID ('var_name')\n");
    doTest("$local: var_name", "$ ('$')\n" + "SIMPLE_ID ('local')\n" + ": (':')\n" + "WHITE_SPACE (' ')\n" + "SIMPLE_ID ('var_name')\n");
    doTest("$my_scope:var_name", "$ ('$')\n" + "SIMPLE_ID ('my_scope')\n" + ": (':')\n" + "SIMPLE_ID ('var_name')\n");
    doTest("${my_scope: var_/'name}", "${ ('${')\n" + "SIMPLE_ID ('my_scope')\n" + ": (':')\n" + "WHITE_SPACE (' ')\n" + "BRACED_ID ('var_/'name')" +
        "\n" + "} ('}')");
    doTest("${E: \\File.txt}", "${ ('${')\n" + "SIMPLE_ID ('E')\n" + ": (':')\n" + "WHITE_SPACE (' ')\n" + "BRACED_ID ('\\File.txt')\n" + "} ('}')");
    doTest("${my_scope: File.txt}", "${ ('${')\n" + "SIMPLE_ID ('my_scope')\n" + ": (':')\n" + "WHITE_SPACE (' ')\n" + "BRACED_ID ('File.txt')\n" +
        "} ('}')");
    doTest("${not a_scope: File.txt}", "${ ('${')\n" + "BRACED_ID ('not a_scope: File.txt')\n" + "} ('}')");
    doTest("${not_a_scope : File.txt}", "${ ('${')\n" + "BRACED_ID ('not_a_scope : File.txt')\n" + "} ('}')");
    doTest("${a_scope:File.txt}", "${ ('${')\n" + "SIMPLE_ID ('a_scope')\n" + ": (':')\n" + "BRACED_ID ('File.txt')\n" + "} ('}')");
  }

  public void testExpandableStringLiteral() {
    doTest("\"Hello World!\"", "DQ_OPEN ('\"')\n" +
        "EXPANDABLE_STRING_PART ('Hello World!')\n" +
        "DQ_CLOSE ('\"')");
    doTest("“Hello World!\"", "DQ_OPEN ('“')\n" +
        "EXPANDABLE_STRING_PART ('Hello World!')\n" +
        "DQ_CLOSE ('\"')");
    doTest("\"\"", "DQ_OPEN ('\"')\n" +
        "DQ_CLOSE ('\"')");
    doTest("\"Hello World!”", "DQ_OPEN ('\"')\n" +
        "EXPANDABLE_STRING_PART ('Hello World!')\n" +
        "DQ_CLOSE ('”')");
    doTest("„Hello World!„", "DQ_OPEN ('„')\n" +
        "EXPANDABLE_STRING_PART ('Hello World!')\n" +
        "DQ_CLOSE ('„')");
    doTest("\"I said, \"\"Hello\"\".\"", "DQ_OPEN ('\"')\n" +
        "EXPANDABLE_STRING_PART ('I said, \"\"Hello\"\".')\n" +
        "DQ_CLOSE ('\"')");
    doTest("\"I said, „„Hello„„.\"", "DQ_OPEN ('\"')\n" +
        "EXPANDABLE_STRING_PART ('I said, „„Hello„„.')\n" +
        "DQ_CLOSE ('\"')");
    doTest("\"I said, 'Hello'\"", "DQ_OPEN ('\"')\n" +
        "EXPANDABLE_STRING_PART ('I said, 'Hello'')\n" +
        "DQ_CLOSE ('\"')");
    doTest("\"column1`tcolumn2`nsecond line, `\"Hello`\", ```Q`5`!\"", "DQ_OPEN ('\"')\n" +
        "EXPANDABLE_STRING_PART ('column1`tcolumn2`nsecond line, `\"Hello`\", ```Q`5`!')\n" +
        "DQ_CLOSE ('\"')");
    doTest("\">$a.Length<\"", "DQ_OPEN ('\"')\n" +
        "EXPANDABLE_STRING_PART ('>')\n" +
        "$ ('$')\n" +
        "SIMPLE_ID ('a')\n" +
        "EXPANDABLE_STRING_PART ('.Length<')\n" +
        "DQ_CLOSE ('\"')");
    doTest("\"${Lengt}\"", "DQ_OPEN ('\"')\n" +
        "${ ('${')\n" +
        "BRACED_ID ('Lengt')\n" +
        "} ('}')\n" +
        "DQ_CLOSE ('\"')");
    doTest("\"lonely dollar $ is here$\"", "DQ_OPEN ('\"')\n" +
        "EXPANDABLE_STRING_PART ('lonely dollar ')\n" +
        "EXPANDABLE_STRING_PART ('$')\n" +
        "EXPANDABLE_STRING_PART (' is here')\n" +
        "EXPANDABLE_STRING_PART ('$')\n" +
        "DQ_CLOSE ('\"')");
  }

  public void testVerbatimStringLiteral() {
    doTest("'Hello World!'", "VERBATIM_STRING (''Hello World!'')"); doTest("‘Hello, World'", "VERBATIM_STRING ('‘Hello, World'')");
    doTest("'What‘'s the time?'", "VERBATIM_STRING (''What‘'s the time?'')"); doTest("‘Hello, World'", "VERBATIM_STRING ('‘Hello, World'')");
    doTest("‚Hello, World‚", "VERBATIM_STRING ('‚Hello, World‚')"); doTest("‛Hello, World‛", "VERBATIM_STRING ('‛Hello, World‛')");
    doTest("'What''s the time?'", "VERBATIM_STRING (''What''s the time?'')");
  }

  public void testExpandableHereStringLiteral() {
    doTest("@\"\n" + "\"@", "EXPANDABLE_HERE_STRING_START ('@\"\\n')\n" +
        "EXPANDABLE_HERE_STRING_END ('\"@')");
    doTest("@„\n" + "„@", "EXPANDABLE_HERE_STRING_START ('@„\\n')\n" +
        "EXPANDABLE_HERE_STRING_END ('„@')");
    doTest("@\"\n" + "line 1\n" + "\"@", "EXPANDABLE_HERE_STRING_START ('@\"\\n')\n" +
        "EXPANDABLE_HERE_STRING_PART ('line 1\\n')\n" +
        "EXPANDABLE_HERE_STRING_END ('\"@')");
  }

  public void testVerbatimHereStringLiteral() {
    doTest("@'\n" + "'@", "VERBATIM_HERE_STRING ('@'\\n'@')"); doTest("@‚\n" + "‚@", "VERBATIM_HERE_STRING ('@‚\\n‚@')");
    doTest("@‘\n" + "'@", "VERBATIM_HERE_STRING ('@‘\\n'@')"); doTest("@‛\n" + "‛@", "VERBATIM_HERE_STRING ('@‛\\n‛@')");
    doTest("@'\n" + "line 1\n" + "'@", "VERBATIM_HERE_STRING ('@'\\n" + "line 1\\n" + "'@')");
  }

  public void testDash() throws Exception {
    doTest("-3\n" + "–3\n" + "—3\n" + "―3", "DASH ('-')\n" + "DEC_INTEGER ('3')\n" + "NLS ('\\n')\n" + "DASH ('–')\n" + "DEC_INTEGER ('3')\n" + "NLS ('\\n')\n" + "DASH ('—')\n" + "DEC_INTEGER ('3')\n" + "NLS ('\\n')\n" + "DASH ('―')\n" + "DEC_INTEGER ('3')");
  }

  public void testVariableIdElementAccess() throws Exception {
    doTest("$h1[$true]", "$ ('$')\n" + "SIMPLE_ID ('h1')\n" + "[ ('[')\n" + "$ ('$')\n" + "SIMPLE_ID ('true')\n" + "] (']')");
    doTest("$h1[20.5]", "$ ('$')\n" + "SIMPLE_ID ('h1')\n" + "[ ('[')\n" + "REAL_NUM ('20.5')\n" + "] (']')");
    doTest("$h1[$my_var]", "$ ('$')\n" + "SIMPLE_ID ('h1')\n" + "[ ('[')\n" + "$ ('$')\n" + "SIMPLE_ID ('my_var')\n" + "] (']')");
  }
}
