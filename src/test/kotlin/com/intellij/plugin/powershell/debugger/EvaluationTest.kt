package com.intellij.plugin.powershell.debugger

import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.plugin.powershell.testFramework.DebuggerTestBase
import com.intellij.plugin.powershell.testFramework.PowerShellTestSession
import com.intellij.xdebugger.XDebuggerTestUtil
import com.jetbrains.rd.util.lifetime.Lifetime
import junit.framework.TestCase

class EvaluationTest: DebuggerTestBase() {

  fun testEvaluation() {
    runInEdt {
      val psiFile = copyAndOpenFile("debugger/testBreakpoint.ps1")
      val file = psiFile.virtualFile

      val fileLine = 1 // line in file, starting from 1
      val line = fileLine - 1 // breakpoint line, starting from 0
      val expression = "1 + 2"
      val expectedResult = "3"

      val testSession = PowerShellTestSession(project, file.toNioPath())
      XDebuggerTestUtil.toggleBreakpoint(project, file, line)
      Lifetime.using { lt ->
        val debugSession = testSession.startDebugSession(lt)
        assertTrue("Pause should be triggered in ${testSession.waitForBackgroundTimeout}", XDebuggerTestUtil.waitFor(
          testSession.sessionListener.pausedSemaphore,
          testSession.waitForBackgroundTimeout.toMillis()
        ))
        val variableValue =
          XDebuggerTestUtil.evaluate(debugSession, expression, testSession.waitForBackgroundTimeout.toMillis()).first
        val variableValueNode = XDebuggerTestUtil.computePresentation(variableValue)
        TestCase.assertEquals(expectedResult, variableValueNode.myValue)
      }
    }
  }

  override fun tearDownEdt() {
    FileEditorManagerEx.getInstanceEx(project).closeAllFiles()
  }
}
