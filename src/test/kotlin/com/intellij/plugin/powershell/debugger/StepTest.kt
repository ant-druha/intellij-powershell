package com.intellij.plugin.powershell.debugger

import com.intellij.plugin.powershell.ide.debugger.PowerShellSuspendContext
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.TempDirTestFixture
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl
import com.intellij.xdebugger.XDebuggerTestUtil
import junit.framework.TestCase

class StepTest: BasePlatformTestCase() {

  override fun getTestDataPath() = "src/test/resources/testData"

  override fun createTempDirTestFixture(): TempDirTestFixture {
    return TempDirTestFixtureImpl()
  }

  fun testStepOver()
  {
    val psiFile = myFixture.configureByFile("debugger/stepTest.ps1")
    val file = psiFile.virtualFile

    val fileLine = 11 // line in file, starting from 1
    val line = fileLine - 1 // breakpoint line, starting from 0

    val testSession = PowerShellTestSession(project, file.toNioPath())
    XDebuggerTestUtil.toggleBreakpoint(project, file, line)
    val debugSession = testSession.startDebugSession()
    XDebuggerTestUtil.waitFor(testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis())
    val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
    TestCase.assertEquals(line, suspendContext.activeExecutionStack.topFrame?.sourcePosition?.line)
    debugSession.stepOver(false)
    XDebuggerTestUtil.waitFor(testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis())
    TestCase.assertEquals(line + 1, (debugSession.suspendContext as PowerShellSuspendContext).activeExecutionStack.topFrame?.sourcePosition?.line)

    myFixture.projectDisposable.dispose()
  }

  fun testStepIn()
  {
    val psiFile = myFixture.configureByFile("debugger/stepTest.ps1")
    val file = psiFile.virtualFile

    val fileLine = 11 // line in file, starting from 1
    val line = fileLine - 1 // breakpoint line, starting from 0

    val stepInLine = 0

    val testSession = PowerShellTestSession(project, file.toNioPath())
    XDebuggerTestUtil.toggleBreakpoint(project, file, line)
    val debugSession = testSession.startDebugSession()
    XDebuggerTestUtil.waitFor(testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis())
    val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
    TestCase.assertEquals(line, suspendContext.activeExecutionStack.topFrame?.sourcePosition?.line)
    debugSession.stepInto()
    XDebuggerTestUtil.waitFor(testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis())
    TestCase.assertEquals(stepInLine, (debugSession.suspendContext as PowerShellSuspendContext).activeExecutionStack.topFrame?.sourcePosition?.line)

    myFixture.projectDisposable.dispose()
  }

  fun testStepOut()
  {
    val psiFile = myFixture.configureByFile("debugger/stepTest.ps1")
    val file = psiFile.virtualFile

    val fileLine = 7 // line in file, starting from 1
    val line = fileLine - 1 // breakpoint line, starting from 0

    val stepOutFileLine = 12
    val stepOutLine = stepOutFileLine - 1

    val testSession = PowerShellTestSession(project, file.toNioPath())
    XDebuggerTestUtil.toggleBreakpoint(project, file, line)
    val debugSession = testSession.startDebugSession()
    XDebuggerTestUtil.waitFor(testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis())
    val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
    TestCase.assertEquals(line, suspendContext.activeExecutionStack.topFrame?.sourcePosition?.line)
    debugSession.stepOut()
    XDebuggerTestUtil.waitFor(testSession.sessionListener.pausedSemaphore, testSession.waitForBackgroundTimeout.toMillis())
    TestCase.assertEquals(stepOutLine, (debugSession.suspendContext as PowerShellSuspendContext).activeExecutionStack.topFrame?.sourcePosition?.line)

    myFixture.projectDisposable.dispose()
  }

  override fun tearDown() {
    myFixture.tearDown()
  }
}
