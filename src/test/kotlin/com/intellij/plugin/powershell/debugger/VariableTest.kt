package com.intellij.plugin.powershell.debugger

import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.util.Pair
import com.intellij.plugin.powershell.PowerShellDebuggerTestUtil
import com.intellij.plugin.powershell.ide.debugger.PowerShellSuspendContext
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.plugin.powershell.testFramework.DebuggerTestBase
import com.intellij.plugin.powershell.testFramework.PowerShellTestSession
import com.intellij.plugin.powershell.testFramework.runInEdt
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.xdebugger.XDebuggerTestUtil
import com.intellij.xdebugger.XTestCompositeNode
import com.intellij.xdebugger.frame.XValueModifier.XModificationCallback
import com.intellij.xdebugger.impl.breakpoints.XExpressionImpl
import com.jetbrains.rd.util.lifetime.Lifetime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.Semaphore

@TestApplication
class VariableTest : DebuggerTestBase() {

  @Test
  fun testPrimitiveVariable() {
    runInEdt {
      val psiFile = copyAndOpenFile("debugger/variableTest.ps1")
      val file = psiFile.virtualFile

      val fileLine = 2 // line in file, starting from 1
      val line = fileLine - 1 // breakpoint line, starting from 0
      val variableName = "\$myPrimitiveVar"
      val value = 69

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
        val topFrame = suspendContext.activeExecutionStack.topFrame!!
        val children = XTestCompositeNode(topFrame).collectChildren()
        val variableValue = XDebuggerTestUtil.findVar(children, variableName)
        val variableValueNode = XDebuggerTestUtil.computePresentation(variableValue)

        Assertions.assertEquals(value.toString(), variableValueNode.myValue)
      }
    }
  }

  @Test
  fun testComplexVariable() {
    runInEdt {
      val psiFile = copyAndOpenFile("debugger/variableTest.ps1")
      val file = psiFile.virtualFile

      val fileLine = 5 // line in file, starting from 1
      val line = fileLine - 1 // breakpoint line, starting from 0
      val variableName = "\$myComplexVar"
      val nestedFieldName = "NestedField"
      val nestedFieldValue = 123

      val testSession = PowerShellTestSession(project, file.toNioPath())
      val millis = testSession.waitForBackgroundTimeout.toMillis()

      XDebuggerTestUtil.toggleBreakpoint(project, file, line)
      Lifetime.using { lt ->
        val debugSession = testSession.startDebugSession(lt)

        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(testSession.sessionListener.pausedSemaphore, millis),
          "Pause should be triggered in $millis ms."
        )

        val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
        val topFrame = suspendContext.activeExecutionStack.topFrame!!
        val children = XTestCompositeNode(topFrame).collectChildren(millis)
        val variableValue = XDebuggerTestUtil.findVar(children, variableName)
        val variableChildren = XTestCompositeNode(variableValue).collectChildren(millis)

        Assertions.assertTrue(
          variableChildren.isNotEmpty(),
          "variableChildren.size (${variableChildren.size}) is not greater than zero"
        )

        val nestedField = XDebuggerTestUtil.findVar(variableChildren, nestedFieldName)
        val nestedFieldValueNode = XDebuggerTestUtil.computePresentation(nestedField)
        Assertions.assertEquals(nestedFieldValue.toString(), nestedFieldValueNode.myValue)
      }
    }
  }

  @Test
  fun testSetVariable() {
    runInEdt {
      val psiFile = copyAndOpenFile("debugger/variableTest.ps1")
      val file = psiFile.virtualFile

      val fileLine = 2 // line in file, starting from 1
      val line = fileLine - 1 // breakpoint line, starting from 0
      val secondFileLine = 5 // line in file, starting from 1
      val secondLine = secondFileLine - 1 // breakpoint line, starting from 0

      val variableName = "\$myPrimitiveVar"
      val value = 123
      val setValueExpression = XExpressionImpl("$value", PowerShellLanguage.INSTANCE, "")

      val testSession = PowerShellTestSession(project, file.toNioPath())
      val millis = testSession.waitForBackgroundTimeout.toMillis()

      XDebuggerTestUtil.toggleBreakpoint(project, file, line)
      XDebuggerTestUtil.toggleBreakpoint(project, file, secondLine)

      Lifetime.using { lt ->
        val debugSession = testSession.startDebugSession(lt)

        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(testSession.sessionListener.pausedSemaphore, millis),
          "Pause should be triggered in $millis ms."
        )

        val suspendContext = debugSession.suspendContext as PowerShellSuspendContext
        val variableValue = PowerShellDebuggerTestUtil.getVariable(suspendContext, variableName)
        val callback = XTestModificationCallback()
        variableValue.modifier!!.setValue(setValueExpression, callback)
        callback.waitFor(millis)
        debugSession.resume()

        Assertions.assertTrue(
          XDebuggerTestUtil.waitFor(
            testSession.sessionListener.pausedSemaphore,
            testSession.waitForBackgroundTimeout.toMillis()
          ), "Pause should be triggered in ${testSession.waitForBackgroundTimeout}"
        )
        val resultVariable =
          PowerShellDebuggerTestUtil.getVariable(debugSession.suspendContext as PowerShellSuspendContext, variableName)
        val variableValueNode = XDebuggerTestUtil.computePresentation(resultVariable)
        Assertions.assertEquals(value.toString(), variableValueNode.myValue)
      }
    }
  }

  override fun tearDownInEdt() {
    FileEditorManagerEx.getInstanceEx(project).closeAllFiles()
  }
}

class XTestModificationCallback : XModificationCallback {
  private var myErrorMessage: String? = null
  private val myFinished = Semaphore(0)


  override fun errorOccurred(errorMessage: String) {
    myErrorMessage = errorMessage
    myFinished.release()
  }

  override fun valueModified() {
    myFinished.release()
  }

  fun waitFor(timeoutInMilliseconds: Long): Pair<Boolean, String?> {
    Assertions.assertTrue(XDebuggerTestUtil.waitFor(myFinished, timeoutInMilliseconds), "timed out")
    return Pair.create(true, myErrorMessage)
  }
}
