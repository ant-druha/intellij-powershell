package com.intellij.plugin.powershell.ide.debugger

import com.intellij.execution.ExecutionResult
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.openapi.util.Key
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.breakpoints.XBreakpointHandler
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider
import com.intellij.xdebugger.frame.XSuspendContext
import com.jetbrains.rd.framework.util.adviseSuspend
import com.jetbrains.rd.util.lifetime.Lifetime
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.atomic.AtomicBoolean

class PowerShellDebugProcess(val xDebugSession: XDebugSession, val executionResult: ExecutionResult, val powerShellDebugSession: PowerShellDebugSession)
  : XDebugProcess(xDebugSession), Disposable {

  private val myBreakpointHandler = PowerShellBreakpointHandler(this, PowerShellBreakpointType::class.java)
  private val myProcessHandler = executionResult.processHandler
  private val disposed = AtomicBoolean(false)

  override fun doGetProcessHandler(): ProcessHandler = myProcessHandler

  private val myExecutionConsole = executionResult.executionConsole
  private val myEditorsProvider = PowerShellDebuggerEditorsProvider(xDebugSession)

  private val myXBreakpointHandlers = arrayOf<XBreakpointHandler<*>>(myBreakpointHandler)

  override fun resume(context: XSuspendContext?) {
    if(context !is PowerShellSuspendContext) {
      return
    }
    powerShellDebugSession.continueDebugging(context)
  }

  override fun startStepOver(context: XSuspendContext?) {
    if(context !is PowerShellSuspendContext) {
      return
    }
    powerShellDebugSession.startStepOver(context)
  }

  override fun startStepInto(context: XSuspendContext?) {
    if(context !is PowerShellSuspendContext) {
      return
    }
    powerShellDebugSession.startStepInto(context)
  }

  override fun startStepOut(context: XSuspendContext?) {
    if(context !is PowerShellSuspendContext) {
      return
    }
    powerShellDebugSession.startStepOut(context)
  }

  override fun startPausing() {
    powerShellDebugSession.startPausing()
  }

  override fun createConsole(): ExecutionConsole {
    return myExecutionConsole
  }

  override fun getEditorsProvider(): XDebuggerEditorsProvider {
    return myEditorsProvider
  }

  override fun getBreakpointHandlers(): Array<XBreakpointHandler<*>> {
    return myXBreakpointHandlers
  }

  override fun stop() {
    dispose()
  }

  override fun dispose() {
    if(!disposed.get()) {
      executionResult.processHandler.destroyProcess()
      disposed.set(true)
    }
  }
}
