package com.intellij.plugin.powershell.debugger

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.TempDirTestFixture
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl
import com.intellij.xdebugger.XDebuggerTestUtil
import com.jetbrains.rd.util.lifetime.Lifetime
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
    Lifetime.using { lt ->
      val debugSession = testSession.startDebugSession(lt)
      XDebuggerTestUtil.waitFor(
        testSession.sessionListener.pausedSemaphore,
        testSession.waitForBackgroundTimeout.toMillis()
      )
      val variableValue =
        XDebuggerTestUtil.evaluate(debugSession, expression, testSession.waitForBackgroundTimeout.toMillis()).first
      val variableValueNode = XDebuggerTestUtil.computePresentation(variableValue)
      TestCase.assertEquals(expectedResult, variableValueNode.myValue)
      myFixture.projectDisposable.dispose()
    }
  }
}
