package com.intellij.plugin.powershell.debugger

import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.plugin.powershell.ide.debugger.PowerShellSuspendContext
import com.intellij.plugin.powershell.testFramework.DebuggerTestBase
import com.intellij.plugin.powershell.testFramework.PowerShellTestSession
import com.intellij.xdebugger.XDebuggerTestUtil
import com.jetbrains.rd.util.lifetime.Lifetime
import junit.framework.TestCase

class StepTest: DebuggerTestBase() {

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
        XDebuggerTestUtil.waitFor(
          testSession.sessionListener.pausedSemaphore,
          testSession.waitForBackgroundTimeout.toMillis()
        )
        val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
        TestCase.assertEquals(line, suspendContext.activeExecutionStack.topFrame?.sourcePosition?.line)
        debugSession.stepOver(false)
        XDebuggerTestUtil.waitFor(
          testSession.sessionListener.pausedSemaphore,
          testSession.waitForBackgroundTimeout.toMillis()
        )
        TestCase.assertEquals(
          line + 1,
          (debugSession.suspendContext as PowerShellSuspendContext).activeExecutionStack.topFrame?.sourcePosition?.line
        )
      }
    }
  }

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
        XDebuggerTestUtil.waitFor(
          testSession.sessionListener.pausedSemaphore,
          testSession.waitForBackgroundTimeout.toMillis()
        )
        val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
        TestCase.assertEquals(line, suspendContext.activeExecutionStack.topFrame?.sourcePosition?.line)
        debugSession.stepInto()
        XDebuggerTestUtil.waitFor(
          testSession.sessionListener.pausedSemaphore,
          testSession.waitForBackgroundTimeout.toMillis()
        )
        TestCase.assertEquals(
          stepInLine,
          (debugSession.suspendContext as PowerShellSuspendContext).activeExecutionStack.topFrame?.sourcePosition?.line
        )
      }
    }
  }

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
        XDebuggerTestUtil.waitFor(
          testSession.sessionListener.pausedSemaphore,
          testSession.waitForBackgroundTimeout.toMillis()
        )
        val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
        TestCase.assertEquals(line, suspendContext.activeExecutionStack.topFrame?.sourcePosition?.line)
        debugSession.stepOut()
        XDebuggerTestUtil.waitFor(
          testSession.sessionListener.pausedSemaphore,
          testSession.waitForBackgroundTimeout.toMillis()
        )
        TestCase.assertEquals(
          stepOutLine,
          (debugSession.suspendContext as PowerShellSuspendContext).activeExecutionStack.topFrame?.sourcePosition?.line
        )
      }
    }
  }

  override fun tearDownEdt() {
    FileEditorManagerEx.getInstanceEx(project).closeAllFiles()
  }
}
