package com.intellij.plugin.powershell.ide.debugger

import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.plugin.powershell.lang.debugger.PSDebugClient
import com.intellij.util.io.await
import com.intellij.util.text.nullize
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.intellij.xdebugger.breakpoints.XLineBreakpoint
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.Signal
import com.jetbrains.rd.util.threading.coroutines.adviseSuspend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer

class PowerShellDebugSession(
  client: PSDebugClient, val server: IDebugProtocolServer,
  private val session: XDebugSession,
  val coroutineScope: CoroutineScope
) {

  private val sendKeyPress = Signal<Unit>()

  private val breakpointMap = mutableMapOf<String, MutableMap<Int, XLineBreakpoint<XBreakpointProperties<*>>>>() // todo: Path as key
  private val breakpointsMapMutex = Mutex()

  init {
    client.debugStopped.adviseSuspend(Lifetime.Eternal, Dispatchers.EDT) { args ->
      val stack = server.stackTrace(StackTraceArguments().apply { threadId = args!!.threadId }).await()
      thisLogger().info(stack.toString())
      session.positionReached(PowerShellSuspendContext(stack, server, coroutineScope, args!!.threadId, session))
    }
    client.sendKeyPress.adviseSuspend(Lifetime.Eternal, Dispatchers.EDT) {
      sendKeyPress.fire(Unit)
    }
  }

  fun setBreakpoint(filePath: String, breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
    coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
      breakpointsMapMutex.withLock {
        if (!breakpointMap.containsKey(filePath))
          breakpointMap[filePath] = mutableMapOf()
        val bpMap = breakpointMap[filePath]!!
        bpMap[breakpoint.line] = breakpoint
        sendBreakpointRequest()
      }
    }
  }

  fun removeBreakpoint(fileURL: String, breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
    coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
      breakpointsMapMutex.withLock {
        val bpMap = breakpointMap[fileURL] ?: return@launch
        if (!bpMap.containsKey(breakpoint.line))
          return@launch
        bpMap.remove(breakpoint.line)
        sendBreakpointRequest()
      }
    }
  }

  fun continueDebugging(context: PowerShellSuspendContext) {
    continueDebugging(context.threadId)
  }

  private fun continueDebugging(threadId: Int) {
    coroutineScope.launch {
      server.continue_(ContinueArguments().apply { this.threadId = threadId }).await()
    }
  }

  fun startStepOver(context: PowerShellSuspendContext) {
    startStepOver(context.threadId)
  }

  private fun startStepOver(threadId: Int) {
    coroutineScope.launch {
      server.next(NextArguments().apply { this.threadId = threadId }).await()
    }
  }

  fun startStepInto(context: PowerShellSuspendContext) {
    startStepInto(context.threadId)
  }

  private fun startStepInto(threadId: Int) {
    coroutineScope.launch {
      server.stepIn(StepInArguments().apply { this.threadId = threadId }).await()
    }
  }

  fun startStepOut(context: PowerShellSuspendContext) {
    startStepOut(context.threadId)
  }

  private fun startStepOut(threadId: Int) {
    coroutineScope.launch {
      server.stepOut(StepOutArguments().apply { this.threadId = threadId }).await()
    }
  }

  fun startPausing() {
    coroutineScope.launch {
      server.pause(PauseArguments().apply { threadId = 0 }).await()
    }
  }

  private suspend fun sendBreakpointRequest() {
    sendBreakpointRequest(breakpointMap)
  }

  private suspend fun sendBreakpointRequest(breakpointMap: Map<String, MutableMap<Int, XLineBreakpoint<XBreakpointProperties<*>>>>) {
    for (breakpointMapEntry in breakpointMap) {
      val breakpointArgs = SetBreakpointsArguments()
      val source = Source()
      source.path = breakpointMapEntry.key
      breakpointArgs.source = source

      breakpointArgs.breakpoints = breakpointMapEntry.value.map {
        val bp = it.value
        SourceBreakpoint().apply {
          line = bp.line + 1 // ide breakpoints line numbering starts from 0, while PSES start from 1
          condition = bp.conditionExpression?.expression
          logMessage = bp.logExpressionObject?.expression
        }
      }.toTypedArray()
      try {
        val setBreakpointsResponse = server.setBreakpoints(breakpointArgs).await()
        val responseMap = setBreakpointsResponse.breakpoints.associateBy { x -> x.line - 1 }
        breakpointMapEntry.value.forEach {
          val bp = responseMap[it.value.line]
          if (bp?.isVerified == true) {
            logger.info("Set breakpoint at ${breakpointMapEntry.key}:${bp.line} successfully.")
          } else {
            session.setBreakpointInvalid(
              it.value,
              bp?.message.nullize(nullizeSpaces = true)
                ?: MessagesBundle.message("powershell.debugger.breakpoints.invalidBreakPoint")
            )
            logger.info("Invalid breakpoint at ${breakpointMapEntry.key}:${bp?.line}: ${bp?.message}")
          }
        }
      } catch (e: Throwable) {
        logger.error(e)
        session.reportMessage(
          e.message ?: e.javaClass.simpleName,
          com.intellij.openapi.ui.MessageType.ERROR
        )
      }
    }
  }
}

private val logger = logger<PowerShellDebugSession>()
