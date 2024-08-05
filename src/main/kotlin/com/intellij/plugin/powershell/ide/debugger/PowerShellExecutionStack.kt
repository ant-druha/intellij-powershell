package com.intellij.plugin.powershell.ide.debugger

import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.frame.XExecutionStack
import com.intellij.xdebugger.frame.XStackFrame
import kotlinx.coroutines.CoroutineScope
import org.eclipse.lsp4j.debug.StackTraceResponse
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer

class PowerShellExecutionStack(val stackResponse: StackTraceResponse,
                               val server: IDebugProtocolServer,
                               val coroutineScope: CoroutineScope, val xDebugSession: XDebugSession
): XExecutionStack("PowerShell Debug Execution Stack") {
  override fun getTopFrame(): XStackFrame? {
    return stackResponse.stackFrames.firstOrNull()?.let {
        PowerShellStackFrame(
            it,
            server,
            coroutineScope,
            xDebugSession
        )
    }
  }

  override fun computeStackFrames(firstFrameIndex: Int, container: XStackFrameContainer?) {
    container?.addStackFrames(stackResponse.stackFrames.drop(firstFrameIndex).map {
        PowerShellStackFrame(
            it,
            server,
            coroutineScope,
            xDebugSession
        )
    }, true)
  }
}
