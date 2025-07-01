package com.intellij.plugin.powershell.ide.debugger

import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.frame.XExecutionStack
import com.intellij.xdebugger.frame.XSuspendContext
import kotlinx.coroutines.CoroutineScope
import org.eclipse.lsp4j.debug.StackTraceResponse
import org.eclipse.lsp4j.debug.Variable
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer

class PowerShellSuspendContext(
  val stack: StackTraceResponse,
  val server: IDebugProtocolServer,
  private val scope: CoroutineScope,
  val threadId: Int = 0,
  val xDebugSession: XDebugSession
): XSuspendContext() {

  val variablesCache: MutableMap<Pair<Int, String>, Variable> = mutableMapOf()
  override fun getExecutionStacks(): Array<XExecutionStack> {
    return arrayOf(PowerShellExecutionStack(stack, server, scope, xDebugSession))
  }

  override fun getActiveExecutionStack(): XExecutionStack {
    return PowerShellExecutionStack(stack, server, scope, xDebugSession)
  }
}
