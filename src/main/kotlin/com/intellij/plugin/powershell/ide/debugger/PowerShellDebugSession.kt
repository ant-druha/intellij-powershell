package com.intellij.plugin.powershell.ide.debugger

import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.logger
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.plugin.powershell.lang.debugger.PSDebugClient
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
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import org.jetbrains.concurrency.await
import java.nio.file.Path
import kotlin.io.path.pathString

class PowerShellDebugSession(
  client: PSDebugClient, val server: IDebugProtocolServer,
  private val session: XDebugSession,
  val coroutineScope: CoroutineScope
) {

  val sendKeyPress = Signal<Unit>()

  private val breakpointMap = mutableMapOf<Path, MutableMap<Int, XLineBreakpoint<XBreakpointProperties<*>>>>()
  private val breakpointsMapMutex = Mutex()

  init {
    client.debugStopped.adviseSuspend(Lifetime.Eternal, Dispatchers.EDT) { args ->
      val stack = server.stackTrace(StackTraceArguments().apply { threadId = args.threadId }).await()
      session.positionReached(PowerShellSuspendContext(stack, server, coroutineScope, args.threadId, session))
    }
    client.sendKeyPress.adviseSuspend(Lifetime.Eternal, Dispatchers.EDT) {
      sendKeyPress.fire(Unit)
    }
    client.terminated.adviseSuspend(Lifetime.Eternal, Dispatchers.IO) {
      logger.info("Debug session has been terminated. Terminating debug server.")
      session.debugProcess.stopAsync().await()
      logger.info("Debug process had been terminated.")
    }
  }

  fun setBreakpoint(filePath: Path, breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
    val path = filePath.toRealPath()
    coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
      breakpointsMapMutex.withLock {
        if (!breakpointMap.containsKey(path))
          breakpointMap[path] = mutableMapOf()
        val bpMap = breakpointMap[path]!!
        bpMap[breakpoint.line] = breakpoint
        sendBreakpointRequest()
      }
    }
  }

  fun removeBreakpoint(filePath: Path, breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
    val path = filePath.toRealPath()
    coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
      breakpointsMapMutex.withLock {
        val bpMap = breakpointMap[path] ?: return@launch
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

  private suspend fun sendBreakpointRequest(breakpointMap: Map<Path, MutableMap<Int, XLineBreakpoint<XBreakpointProperties<*>>>>) {
    for ((file, breakpointsInFile) in breakpointMap) {
      val breakpointArgs = SetBreakpointsArguments()
      val source = Source()
      source.path = file.pathString
      breakpointArgs.source = source

      breakpointArgs.breakpoints = breakpointsInFile.map {
        val breakpoint = it.value
        SourceBreakpoint().apply {
          line = breakpoint.line + 1 // ide breakpoints line numbering starts from 0, while PSES start from 1
          condition = breakpoint.conditionExpression?.expression
          logMessage = breakpoint.logExpressionObject?.expression
        }
      }.toTypedArray()
      try {
        val setBreakpointsResponse = server.setBreakpoints(breakpointArgs).await()
        val responseMap = setBreakpointsResponse.breakpoints.associateBy { x -> x.line - 1 }
        breakpointsInFile.forEach {
          val bp = responseMap[it.value.line]
          if (bp?.isVerified == true) {
            session.setBreakpointVerified(it.value)
            logger.info("Set breakpoint at $file:${bp.line} successfully.")
          } else {
            session.setBreakpointInvalid(
              it.value,
              bp?.message.nullize(nullizeSpaces = true)
                ?: MessagesBundle.message("powershell.debugger.breakpoints.invalidBreakPoint")
            )
            logger.info("Invalid breakpoint at $file:${bp?.line}: ${bp?.message}")
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
