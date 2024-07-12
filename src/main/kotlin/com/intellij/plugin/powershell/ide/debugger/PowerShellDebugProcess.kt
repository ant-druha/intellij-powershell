package com.intellij.plugin.powershell.ide.debugger

import com.intellij.execution.ExecutionResult
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Key
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.breakpoints.XBreakpointHandler
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider

class PowerShellDebugProcess(session: XDebugSession, executionResult: ExecutionResult) : XDebugProcess(session), Disposable {

  val KEY: Key<PowerShellDebugProcess> = Key.create("com.intellij.plugin.powershell.ide.debugger.PowerShellDebugProcess")

  val myProcessHandler = executionResult.processHandler
  init {
    myProcessHandler.putUserData(KEY, this)
  }
  val myExecutionConsole = executionResult.executionConsole
  val myEditorsProvider = PowerShellDebuggerEditorsProvider()
  init {
    com.intellij.openapi.util.Disposer.register(myExecutionConsole, this)
  }

  private val myXBreakpointHandlers = arrayOf<XBreakpointHandler<*>>(
    PowerShellBreakpointHandler(
      this,
      PowerShellBreakpointType::class.java
    ),
  )

  override fun createConsole(): ExecutionConsole {
    return myExecutionConsole
  }
  override fun getEditorsProvider(): XDebuggerEditorsProvider {
    return myEditorsProvider
  }

  override fun getBreakpointHandlers(): Array<XBreakpointHandler<*>> {
    return myXBreakpointHandlers
  }

  override fun dispose() {
    TODO("Not yet implemented")
  }
}
