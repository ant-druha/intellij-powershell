package com.intellij.plugin.powershell.ide.debugger

import com.intellij.execution.ExecutionResult
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.openapi.util.Key
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpointHandler
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider
import com.intellij.xdebugger.frame.XSuspendContext
import com.jetbrains.rd.framework.util.adviseSuspend
import com.jetbrains.rd.util.lifetime.Lifetime
import kotlinx.coroutines.Dispatchers
import org.eclipse.lsp4j.debug.ContinueArguments

class PowerShellDebugProcess(val xDebugSession: XDebugSession, val executionResult: ExecutionResult, val clientSession: PowerShellDebugSession)
  : XDebugProcess(xDebugSession), Disposable { // TODO: Dispose

  companion object {
    val KEY: Key<PowerShellDebugProcess> =
      Key.create("com.intellij.plugin.powershell.ide.debugger.PowerShellDebugProcess")
  }

  val myBreakpointHandler = PowerShellBreakpointHandler(this, PowerShellBreakpointType::class.java)
  val myProcessHandler = executionResult.processHandler

  init {
    myProcessHandler.putUserData(KEY, this)
  }

  override fun doGetProcessHandler(): ProcessHandler = myProcessHandler

  val myExecutionConsole = executionResult.executionConsole
  val myEditorsProvider = PowerShellDebuggerEditorsProvider(xDebugSession)

  init {
    com.intellij.openapi.util.Disposer.register(myExecutionConsole, this)
    myBreakpointHandler.registerBreakpointEvent.adviseSuspend(Lifetime.Eternal, Dispatchers.EDT) {
      pair -> clientSession.setBreakpoint(pair.first, pair.second)
    }
    myBreakpointHandler.unregisterBreakpointEvent.adviseSuspend(Lifetime.Eternal, Dispatchers.EDT) {
      pair -> clientSession.removeBreakpoint(pair.first, pair.second)
    }
  }

  private val myXBreakpointHandlers = arrayOf<XBreakpointHandler<*>>(myBreakpointHandler)

  override fun resume(context: XSuspendContext?) {
    if(context !is PowerShellSuspendContext) {
      return
    }
    clientSession.continueDebugging(context)
  }

  override fun stop() {
    clientSession.terminateDebugging()
  }

  override fun startStepOver(context: XSuspendContext?) {
    if(context !is PowerShellSuspendContext) {
      return
    }
    clientSession.startStepOver(context)
  }

  override fun startStepInto(context: XSuspendContext?) {
    if(context !is PowerShellSuspendContext) {
      return
    }
    clientSession.startStepInto(context)
  }

  override fun startStepOut(context: XSuspendContext?) {
    if(context !is PowerShellSuspendContext) {
      return
    }
    clientSession.startStepOut(context)
  }

  override fun startPausing() {
    clientSession.startPausing()
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

  override fun dispose() {
    clientSession.terminateDebugging()
  }
}
