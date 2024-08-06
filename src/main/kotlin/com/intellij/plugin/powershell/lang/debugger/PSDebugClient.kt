package com.intellij.plugin.powershell.lang.debugger

import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.frame.XSuspendContext
import com.jetbrains.rd.util.reactive.Signal
import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification
import java.util.concurrent.CompletableFuture

class PSDebugClient(session: XDebugSession): IDebugProtocolClient {
  private val debugSession = session

  val debugStopped = Signal<StoppedEventArguments?>()
  val sendKeyPress = Signal<Unit>()

  override fun breakpoint(args: BreakpointEventArguments?) {
    super.breakpoint(args)
  }

  override fun stopped(args: StoppedEventArguments?) {
    super.stopped(args)
    debugStopped.fire(args)
  }

  override fun continued(args: ContinuedEventArguments?) {
    super.continued(args)
  }

  override fun exited(args: ExitedEventArguments?) {
    super.exited(args)
  }

  override fun terminated(args: TerminatedEventArguments?) {
    super.terminated(args)
  }

  override fun thread(args: ThreadEventArguments?) {
    super.thread(args)
  }

  override fun output(args: OutputEventArguments?) {
    super.output(args)
  }

  override fun module(args: ModuleEventArguments?) {
    super.module(args)
  }

  override fun loadedSource(args: LoadedSourceEventArguments?) {
    super.loadedSource(args)
  }

  override fun process(args: ProcessEventArguments?) {
    super.process(args)
  }

  override fun capabilities(args: CapabilitiesEventArguments?) {
    super.capabilities(args)
  }

  override fun progressStart(args: ProgressStartEventArguments?) {
    super.progressStart(args)
  }

  override fun progressUpdate(args: ProgressUpdateEventArguments?) {
    super.progressUpdate(args)
  }

  override fun progressEnd(args: ProgressEndEventArguments?) {
    super.progressEnd(args)
  }

  override fun invalidated(args: InvalidatedEventArguments?) {
    super.invalidated(args)
  }

  override fun memory(args: MemoryEventArguments?) {
    super.memory(args)
  }

  override fun runInTerminal(args: RunInTerminalRequestArguments?): CompletableFuture<RunInTerminalResponse> {
    return super.runInTerminal(args)
  }

  override fun startDebugging(args: StartDebuggingRequestArguments?): CompletableFuture<Void> {
    return super.startDebugging(args)
  }

  @JsonNotification("powerShell/sendKeyPress")
  fun sendKeyPress(){
    sendKeyPress.fire(Unit)
  }
}
