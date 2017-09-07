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

}
