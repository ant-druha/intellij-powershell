package com.intellij.plugin.powershell.lang

import com.intellij.lang.LanguageBraceMatching
import com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellPairedBraceMatcher
import com.intellij.plugin.powershell.lang.parser.PowerShellParserDefinition
import com.intellij.testFramework.ParsingTestCase

class PowerShellParserTest : ParsingTestCase("parser", "ps1", PowerShellParserDefinition()) {

  override fun setUp() {
    super.setUp()
    addExplicitExtension(LanguageBraceMatching.INSTANCE, PowerShellLanguage.INSTANCE, PowerShellPairedBraceMatcher())
  }

  override fun getTestDataPath() =  "src/test/resources/testData"
  override fun includeRanges() = true

  fun testCallInvocationOperator() { doTest(true) }
  fun testVerbatimCommandArgument() { doTest(true) }
  fun testPathExpression() { doTest(true) }
  fun testCommandCallExpression() { doTest(true) }
  fun testStdCmdlets() { doTest(true) }
  fun testMemberElementAccess() { doTest(true) }
  fun testTypeMemberAccess() { doTest(true) }
  fun testOperators() { doTest(true) }
  fun testNullCoalesceOperator() { doTest(true) }
  fun testLabeledStatement() { doTest(true) }
  fun testStatements() { doTest(true) }
  fun testFunctionMultilineDefinition() { doTest(true) }
  fun testDesiredSateConfiguration() { doTest(true) }
  fun testArrayStatement() { doTest(true) }
  fun testAttributes() { doTest(true) }
  fun testClosures() { doTest(true) }
  fun testModules() { doTest(true) }
  fun testHashTables() { doTest(true) }
  fun testExpandableString() { doTest(true) }
  fun testVerbatimString() { doTest(true) }
  fun testTypeLiteral() { doTest(true) }
  fun testClassDeclaration() { doTest(true) }
  fun testEnumDeclaration() { doTest(true) }
  fun testParamBlock() { doTest(true) }
  fun testComment() { doTest(true) }
  fun testNewLineThenPipeOperator() { doTest(true) }
}
