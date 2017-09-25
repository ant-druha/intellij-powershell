package com.intellij.plugin.powershell.lang;

import com.intellij.plugin.powershell.lang.parser.PowerShellParserDefinition;
import com.intellij.testFramework.ParsingTestCase;

import java.io.File;

/**
 * Andrey 06/09/17.
 */
public class PowerShellParserTest extends ParsingTestCase {
  private static final String TEST_DATA_PATH = "src" + File.separatorChar + "test" + File.separatorChar + "resources" + File.separatorChar +
      "testData";

  public PowerShellParserTest() {
    super("parser", "ps1", new PowerShellParserDefinition());
  }

  @Override
  protected String getTestDataPath() { return TEST_DATA_PATH; }

  @Override
  protected boolean includeRanges() { return true; }

  public void testCallInvocationOperator() throws Exception { doTest(true); }

  public void testVerbatimCommandArgument() throws Exception { doTest(true); }

  public void testPathExpression() throws Exception { doTest(true); }

  public void testCommandCallExpression() throws Exception { doTest(true); }

  public void testStdCmdlets() throws Exception { doTest(true); }

  public void testMemberElementAccess() throws Exception { doTest(true); }

  public void testOperators() throws Exception { doTest(true); }

  public void testLabeledStatement() throws Exception { doTest(true); }

  public void testStatements() throws Exception { doTest(true); }

  public void testArrayStatement() throws Exception { doTest(true); }

  public void testAttributes() throws Exception { doTest(true); }

  public void testClosures() throws Exception { doTest(true); }

  public void testModules() throws Exception { doTest(true); }

  public void testHashTables() throws Exception { doTest(true); }

  public void testTypeLiteral() throws Exception { doTest(true); }

}
