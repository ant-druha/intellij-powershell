package com.intellij.plugin.powershell.debugger

import com.intellij.plugin.powershell.ide.debugger.PowerShellSuspendContext
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.TempDirTestFixture
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl
import com.intellij.vcs.commit.CommitSessionInfo.Default.session
import com.intellij.xdebugger.XDebuggerTestUtil
import junit.framework.TestCase

class EvaluationTest: BasePlatformTestCase() {
  override fun getTestDataPath() = "src/test/resources/testData"

  override fun createTempDirTestFixture(): TempDirTestFixture {
    return TempDirTestFixtureImpl()
  }

  fun testEvaluation() {
    val psiFile = myFixture.configureByFile("debugger/testBreakpoint.ps1")
    val file = psiFile.virtualFile

    val fileLine = 1 // line in file, starting from 1
    val line = fileLine - 1 // breakpoint line, starting from 0
    val expression = "1 + 2"
    val expectedResult = "3"

    val testSession = PowerShellTestSession(project, file.toNioPath())
    XDebuggerTestUtil.toggleBreakpoint(project, file, line)
    val debugSession = testSession.startDebugSession()
    XDebuggerTestUtil.waitFor(testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis())
    val variableValue = XDebuggerTestUtil.evaluate(debugSession, expression, testSession.waitForBackgroundTimeout.toMillis()).first
    val variableValueNode = XDebuggerTestUtil.computePresentation(variableValue)
    TestCase.assertEquals(expectedResult, variableValueNode.myValue)
    myFixture.projectDisposable.dispose()
  }
}
