package com.intellij.plugin.powershell.debugger

import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.plugin.powershell.ide.debugger.PowerShellSuspendContext
import com.intellij.plugin.powershell.testFramework.DebuggerTestBase
import com.intellij.plugin.powershell.testFramework.PowerShellTestSession
import com.intellij.plugin.powershell.testFramework.runInEdt
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.xdebugger.XDebuggerTestUtil
import com.intellij.xdebugger.XTestCompositeNode
import com.jetbrains.rd.util.lifetime.Lifetime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@TestApplication
class BreakpointTest : DebuggerTestBase() {

  @Test
  fun testBreakpoint() {
    runInEdt {
      val psiFile = copyAndOpenFile("debugger/testBreakpoint.ps1")
      val file = psiFile.virtualFile

      val fileLine = 5 // line in file, starting from 1
      val line = fileLine - 1 // breakpoint line, starting from 0
      val testSession = PowerShellTestSession(project, file.toNioPath())
      XDebuggerTestUtil.toggleBreakpoint(project, file, line)
      Lifetime.using { lt ->
        val debugSession = testSession.startDebugSession(lt)
        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(
            testSession.sessionListener.pausedSemaphore,
            testSession.waitForBackgroundTimeout.toMillis()
          ), "Pause should be triggered in ${testSession.waitForBackgroundTimeout}"
        )
        val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
        Assertions.assertEquals(line, suspendContext.activeExecutionStack.topFrame?.sourcePosition?.line)
      }
    }
  }

  @Test
  fun testBreakpointTwoFiles() {
    runInEdt {
      val psiFile = copyAndOpenFile("debugger/testBreakpointTwoFiles.ps1")
      val psiSecondFile = copyAndOpenFile("debugger/secondFileTest.ps1")

      val file = psiFile.virtualFile

      val fileLine = 4 // line in file, starting from 1
      val line = fileLine - 1 // breakpoint line, starting from 0
      val testSession = PowerShellTestSession(project, file.toNioPath())
      XDebuggerTestUtil.toggleBreakpoint(project, psiSecondFile.virtualFile, line)

      Lifetime.using { lt ->
        val debugSession = testSession.startDebugSession(lt)
        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(
            testSession.sessionListener.pausedSemaphore,
            testSession.waitForBackgroundTimeout.toMillis()
          ), "Pause should be triggered in ${testSession.waitForBackgroundTimeout}"
        )
        val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
        Assertions.assertEquals(
          psiSecondFile.virtualFile.canonicalFile?.toNioPath(),
          suspendContext.activeExecutionStack.topFrame?.sourcePosition?.file?.toNioPath()
        )
        Assertions.assertEquals(line, suspendContext.activeExecutionStack.topFrame?.sourcePosition?.line)
      }
    }
  }

  @Test
  fun testConditionalBreakpoint() {
    runInEdt {
      val psiFile = copyAndOpenFile("debugger/testBreakpoint.ps1")
      val file = psiFile.virtualFile

      val fileLine = 5 // line in file, starting from 1
      val line = fileLine - 1 // breakpoint line, starting from 0
      val variableName = "\$val"
      val value = 2
      val condition = "$variableName -eq $value"

      val testSession = PowerShellTestSession(project, file.toNioPath())
      XDebuggerTestUtil.toggleBreakpoint(project, file, line)
      XDebuggerTestUtil.setBreakpointCondition(project, line, condition)
      Lifetime.using { lt ->
        val debugSession = testSession.startDebugSession(lt)
        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(
            testSession.sessionListener.pausedSemaphore,
            testSession.waitForBackgroundTimeout.toMillis()
          ), "Pause should be triggered in ${testSession.waitForBackgroundTimeout}"
        )
        val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
        val topFrame = suspendContext.activeExecutionStack.topFrame!!
        val children = XTestCompositeNode(topFrame).collectChildren()
        val variableValue = XDebuggerTestUtil.findVar(children, variableName)
        val variableValueNode = XDebuggerTestUtil.computePresentation(variableValue)
        Assertions.assertEquals(value.toString(), variableValueNode.myValue)
      }
    }
  }

  override fun tearDownInEdt() {
    FileEditorManagerEx.getInstanceEx(project).closeAllFiles()
  }
}
