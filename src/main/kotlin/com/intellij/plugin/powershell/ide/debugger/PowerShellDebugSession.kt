package com.intellij.plugin.powershell.ide.debugger

import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.plugin.powershell.lang.debugger.PSDebugClient
import com.intellij.util.io.await
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.intellij.xdebugger.breakpoints.XLineBreakpoint
import com.jetbrains.rd.framework.util.adviseSuspend
import com.jetbrains.rd.util.lifetime.Lifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer

class PowerShellDebugSession(val client: PSDebugClient, val server: IDebugProtocolServer,
                             val session: XDebugSession,
                             val coroutineScope: CoroutineScope,
                             val xDebugSession: XDebugSession) {

  val breakpointMap = mutableMapOf<String, MutableMap<Int, XLineBreakpoint<XBreakpointProperties<*>>>>()

  init{
    client.debugStopped.adviseSuspend(Lifetime.Eternal, Dispatchers.EDT){
        args ->
      val stack = server.stackTrace(StackTraceArguments().apply { threadId = args!!.threadId }).await()
      thisLogger().info(stack.toString())
      session.positionReached(PowerShellSuspendContext(stack, server, coroutineScope, args!!.threadId, xDebugSession))
    }
  }

  fun setBreakpoint(fileURL: String, breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
    if(!breakpointMap.containsKey(fileURL))
      breakpointMap[fileURL] = mutableMapOf()
    val bpMap = breakpointMap[fileURL]!!
    bpMap[breakpoint.line] = breakpoint

    sendBreakpointRequest()
  }

  fun removeBreakpoint(fileURL: String, breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
    if(!breakpointMap.containsKey(fileURL))
      breakpointMap[fileURL] = mutableMapOf()
    val bpMap = breakpointMap[fileURL]!!
    if(bpMap.containsKey(breakpoint.line))
      bpMap.remove(breakpoint.line)

    sendBreakpointRequest()
  }

  fun continueDebugging(context: PowerShellSuspendContext)
  {
    continueDebugging(context.threadId)
  }

  fun continueDebugging(threadId: Int)
  {
    coroutineScope.launch {
      server.continue_(ContinueArguments().apply { this.threadId = threadId }).await()
    }
  }

  fun startStepOver(context: PowerShellSuspendContext)
  {
    startStepOver(context.threadId)
  }

  fun startStepOver(threadId: Int) {
    coroutineScope.launch {
      server.next(NextArguments().apply { this.threadId = threadId }).await()
    }
  }

  fun startStepInto(context: PowerShellSuspendContext)
  {
    startStepInto(context.threadId)
  }

  fun startStepInto(threadId: Int) {
    coroutineScope.launch {
      server.stepIn(StepInArguments().apply { this.threadId = threadId }).await()
    }
  }
  fun startStepOut(context: PowerShellSuspendContext)
  {
    startStepOut(context.threadId)
  }

  fun startStepOut(threadId: Int) {
    coroutineScope.launch {
      server.stepOut(StepOutArguments().apply { this.threadId = threadId }).await()
    }
  }

  fun startPausing() {
    coroutineScope.launch {
      server.pause(PauseArguments().apply { threadId = 0 }).await()
    }
  }

  private val linearizer = Mutex()

  fun muteBreakpoints(sourcePath: String)
  {
    coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
      linearizer.withLock {
        val breakpointArgs = SetBreakpointsArguments().apply {
          this.source = Source().apply { this.path = sourcePath }
          this.breakpoints = emptyArray()
        }
        server.setBreakpoints(breakpointArgs).await()
      }
    }
  }

  fun unmuteBreakpoints(sourcePath: String) {
    sendBreakpointRequest() // TODO make for separate file
  }

  private fun sendBreakpointRequest() {
    sendBreakpointRequest(breakpointMap)
  }

  fun sendBreakpointRequest(breakpointMap: Map<String, MutableMap<Int, XLineBreakpoint<XBreakpointProperties<*>>>>) {
    coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
      linearizer.withLock {
        for (breakpointMapEntry in breakpointMap) {
          val breakpointArgs = SetBreakpointsArguments()
          val source: Source = Source()
          source.path = breakpointMapEntry.key
          breakpointArgs.source = source

          breakpointArgs.breakpoints = breakpointMapEntry.value.map {
            val bp = it.value
            SourceBreakpoint().apply {
              line = bp.line + 1
              condition = bp.conditionExpression?.expression
              logMessage = bp.logExpressionObject?.expression
            }
          }.toTypedArray()
          try {
            val setBreakpointsResponse = server.setBreakpoints(breakpointArgs).await()
            val responseSet = setBreakpointsResponse.breakpoints.map { x -> x.line - 1 }.toHashSet()
            breakpointMapEntry.value.forEach {
              if(!responseSet.contains(it.key))
                session.setBreakpointInvalid(it.value, MessagesBundle.message("powershell.debugger.breakpoints.invalidBreakPoint"))
            }
          } catch (e: Throwable) {
            session.reportMessage(
              e.message ?: e.javaClass.simpleName,
              com.intellij.openapi.ui.MessageType.ERROR
            )
          }
        }
      }
    }
  }
}

private val logger = logger<PowerShellDebugSession>()
