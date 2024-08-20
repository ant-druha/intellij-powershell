package com.intellij.plugin.powershell.lang.debugger

import com.jetbrains.rd.util.reactive.Signal
import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.services.IDebugProtocolClient
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification
import java.util.concurrent.CompletableFuture
import com.intellij.openapi.diagnostic.logger

class PSDebugClient: IDebugProtocolClient {

  val debugStopped = Signal<StoppedEventArguments>()
  val sendKeyPress = Signal<Unit>()

  override fun breakpoint(args: BreakpointEventArguments) {
    logger.trace("BreakpointEventArguments: $args")
    super.breakpoint(args)
  }

  override fun stopped(args: StoppedEventArguments) {
    logger.trace("StoppedEventArguments: $args")
    super.stopped(args)
    debugStopped.fire(args)
  }

  override fun continued(args: ContinuedEventArguments) {
    logger.trace("ContinuedEventArguments: $args")
    super.continued(args)
  }

  override fun exited(args: ExitedEventArguments) {
    logger.trace("ExitedEventArguments: $args")
    super.exited(args)
  }

  override fun terminated(args: TerminatedEventArguments) {
    logger.trace("TerminatedEventArguments: $args")
    super.terminated(args)
  }

  override fun thread(args: ThreadEventArguments) {
    logger.trace("ThreadEventArguments: $args")
    super.thread(args)
  }

  override fun output(args: OutputEventArguments) {
    logger.trace("OutputEventArguments: $args")
    super.output(args)
  }

  override fun module(args: ModuleEventArguments) {
    logger.trace("ModuleEventArguments: $args")
    super.module(args)
  }

  override fun loadedSource(args: LoadedSourceEventArguments) {
    logger.trace("LoadedSourceEventArguments: $args")
    super.loadedSource(args)
  }

  override fun process(args: ProcessEventArguments) {
    logger.trace("ProcessEventArguments: $args")
    super.process(args)
  }

  override fun capabilities(args: CapabilitiesEventArguments) {
    logger.trace("CapabilitiesEventArguments: $args")
    super.capabilities(args)
  }

  override fun progressStart(args: ProgressStartEventArguments) {
    logger.trace("ProgressStartEventArguments: $args")
    super.progressStart(args)
  }

  override fun progressUpdate(args: ProgressUpdateEventArguments) {
    logger.trace("ProgressUpdateEventArguments: $args")
    super.progressUpdate(args)
  }

  override fun progressEnd(args: ProgressEndEventArguments) {
    logger.trace("ProgressEndEventArguments: $args")
    super.progressEnd(args)
  }

  override fun invalidated(args: InvalidatedEventArguments) {
    logger.trace("InvalidatedEventArguments: $args")
    super.invalidated(args)
  }

  override fun memory(args: MemoryEventArguments) {
    logger.trace("MemoryEventArguments: $args")
    super.memory(args)
  }

  override fun runInTerminal(args: RunInTerminalRequestArguments): CompletableFuture<RunInTerminalResponse> {
    logger.trace("RunInTerminalRequestArguments: $args")
    return super.runInTerminal(args)
  }

  override fun startDebugging(args: StartDebuggingRequestArguments): CompletableFuture<Void> {
    logger.trace("StartDebuggingRequestArguments: $args")
    return super.startDebugging(args)
  }

  @JsonNotification("powerShell/sendKeyPress")
  fun sendKeyPress(){
    logger.trace("JsonNotification SendKeyPress")
    sendKeyPress.fire(Unit)
  }
}

private val logger = logger<PSDebugClient>()
