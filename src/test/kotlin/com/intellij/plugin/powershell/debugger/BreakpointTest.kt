package com.intellij.plugin.powershell.debugger

import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.plugin.powershell.ide.debugger.PowerShellSuspendContext
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.TempDirTestFixture
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl
import com.intellij.xdebugger.XDebuggerTestUtil
import com.intellij.xdebugger.XTestCompositeNode
import com.jetbrains.rd.util.lifetime.Lifetime
import junit.framework.TestCase
import org.junit.AfterClass

class BreakpointTest: BasePlatformTestCase() {

  override fun getTestDataPath() = "src/test/resources/testData"


  override fun createTempDirTestFixture(): TempDirTestFixture {
    return TempDirTestFixtureImpl()
  }

  fun testBreakpoint() {
    val psiFile = myFixture.configureByFile("debugger/testBreakpoint.ps1")
    val file = psiFile.virtualFile

    val fileLine = 5 // line in file, starting from 1
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
    }
  }

  fun testConditionalBreakpoint()
  {
    val psiFile = myFixture.configureByFile("debugger/testBreakpoint.ps1")
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
      XDebuggerTestUtil.waitFor(
        testSession.sessionListener.pausedSemaphore,
        testSession.waitForBackgroundTimeout.toMillis()
      )
      val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
      val topFrame = suspendContext.activeExecutionStack.topFrame!!
      val children = XTestCompositeNode(topFrame).collectChildren()
      val variableValue = XDebuggerTestUtil.findVar(children, variableName)
      val variableValueNode = XDebuggerTestUtil.computePresentation(variableValue)
      TestCase.assertEquals(value.toString(), variableValueNode.myValue)
    }
  }

  override fun tearDown() {
    FileEditorManagerEx.getInstanceEx(project).closeAllFiles()
    super.tearDown()
  }
}
