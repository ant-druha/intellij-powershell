package com.intellij.plugin.powershell

import com.intellij.plugin.powershell.ide.debugger.PowerShellSuspendContext
import com.intellij.xdebugger.XDebuggerTestUtil
import com.intellij.xdebugger.XTestCompositeNode
import com.intellij.xdebugger.frame.XValue

class PowerShellDebuggerTestUtil {
  companion object {
    fun getVariable(suspendContext: PowerShellSuspendContext, variableName: String): XValue {
      val topFrame = suspendContext.activeExecutionStack.topFrame!!
      val children = XTestCompositeNode(topFrame).collectChildren()
      return XDebuggerTestUtil.findVar(children, variableName)
    }

    fun getChildren(suspendContext: PowerShellSuspendContext, variableName: String): MutableList<XValue> {
      val topFrame = suspendContext.activeExecutionStack.topFrame!!
      return XTestCompositeNode(topFrame).collectChildren()
    }
  }
}
