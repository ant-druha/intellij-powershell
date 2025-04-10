package com.intellij.plugin.powershell.debugger

import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.plugin.powershell.ide.debugger.PowerShellSuspendContext
import com.intellij.plugin.powershell.testFramework.DebuggerTestBase
import com.intellij.plugin.powershell.testFramework.PowerShellTestSession
import com.intellij.plugin.powershell.testFramework.runInEdt
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.xdebugger.XDebuggerTestUtil
import com.jetbrains.rd.util.lifetime.Lifetime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@TestApplication
class StepTest : DebuggerTestBase() {

  @Test
  fun testStepOver() {
    runInEdt {
      val psiFile = copyAndOpenFile("debugger/stepTest.ps1")
      val file = psiFile.virtualFile

      val fileLine = 11 // line in file, starting from 1
      val line = fileLine - 1 // breakpoint line, starting from 0

      val testSession = PowerShellTestSession(project, file.toNioPath())
      XDebuggerTestUtil.toggleBreakpoint(project, file, line)
      Lifetime.using { lt ->
        val debugSession = testSession.startDebugSession(lt)

        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(
            testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis()
          ), "Pause should be triggered in ${testSession.waitForBackgroundTimeout}"
        )

        val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
        Assertions.assertEquals(line, suspendContext.activeExecutionStack.topFrame?.sourcePosition?.line)
        debugSession.stepOver(false)

        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(
            testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis()
          ), "Pause should be triggered in ${testSession.waitForBackgroundTimeout}"
        )
        Assertions.assertEquals(
          line + 1,
          (debugSession.suspendContext as PowerShellSuspendContext).activeExecutionStack.topFrame?.sourcePosition?.line
        )
      }
    }
  }

  @Test
  fun testStepIn() {
    runInEdt {
      val psiFile = copyAndOpenFile("debugger/stepTest.ps1")
      val file = psiFile.virtualFile

      val fileLine = 11 // line in file, starting from 1
      val line = fileLine - 1 // breakpoint line, starting from 0

      val stepInLine = 0

      val testSession = PowerShellTestSession(project, file.toNioPath())
      XDebuggerTestUtil.toggleBreakpoint(project, file, line)
      Lifetime.using { lt ->
        val debugSession = testSession.startDebugSession(lt)

        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(
            testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis()
          ), "Pause should be triggered in ${testSession.waitForBackgroundTimeout}"
        )

        Assertions.assertEquals(
          line,
          (debugSession.suspendContext as PowerShellSuspendContext).activeExecutionStack.topFrame?.sourcePosition?.line
        )

        debugSession.stepInto()

        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(
            testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis()
          ), "Pause should be triggered in ${testSession.waitForBackgroundTimeout}"
        )
        Assertions.assertEquals(
          stepInLine,
          (debugSession.suspendContext as PowerShellSuspendContext).activeExecutionStack.topFrame?.sourcePosition?.line
        )
      }
    }
  }

  @Test
  fun testStepOut() {
    runInEdt {
      val psiFile = copyAndOpenFile("debugger/stepTest.ps1")
      val file = psiFile.virtualFile

      val fileLine = 7 // line in file, starting from 1
      val line = fileLine - 1 // breakpoint line, starting from 0

      val stepOutFileLine = 12
      val stepOutLine = stepOutFileLine - 1

      val testSession = PowerShellTestSession(project, file.toNioPath())
      XDebuggerTestUtil.toggleBreakpoint(project, file, line)
      Lifetime.using { lt ->
        val debugSession = testSession.startDebugSession(lt)
        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(
            testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis()
          ), "Pause should be triggered in ${testSession.waitForBackgroundTimeout}"
        )
        val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
        Assertions.assertEquals(line, suspendContext.activeExecutionStack.topFrame?.sourcePosition?.line)
        debugSession.stepOut()
        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(
            testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis()
          ), "Pause should be triggered in ${testSession.waitForBackgroundTimeout}"
        )
        Assertions.assertEquals(
          stepOutLine,
          (debugSession.suspendContext as PowerShellSuspendContext).activeExecutionStack.topFrame?.sourcePosition?.line
        )
      }
    }
  }

  override fun tearDownInEdt() {
    FileEditorManagerEx.getInstanceEx(project).closeAllFiles()
  }
}
