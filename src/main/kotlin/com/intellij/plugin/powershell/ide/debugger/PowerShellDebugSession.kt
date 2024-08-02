package com.intellij.plugin.powershell.ide.debugger

import com.intellij.openapi.application.EDT
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.plugin.powershell.lang.debugger.PSDebugClient
import com.intellij.ui.ColoredTextContainer
import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.io.await
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerUtil
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.intellij.xdebugger.breakpoints.XLineBreakpoint
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator
import com.intellij.xdebugger.frame.*
import com.jetbrains.rd.framework.util.adviseSuspend
import com.jetbrains.rd.util.lifetime.Lifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apache.xml.resolver.helpers.FileURL
import org.eclipse.lsp4j.debug.*
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import java.util.*
import java.util.concurrent.TimeUnit
import javax.swing.Icon
import kotlin.collections.HashMap
import kotlin.io.path.Path

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

  fun terminateDebugging()
  {
    coroutineScope.launch {
      server.terminate(TerminateArguments()).await()
    }
  }

  fun restartDebugging()
  {
    coroutineScope.launch {
      server.restart(RestartArguments()).await()
    }
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
  private fun sendBreakpointRequest() {
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
        val setBreakpointsResponse = server.setBreakpoints(breakpointArgs).await()
        val breakpointsResponse: Array<Breakpoint> = setBreakpointsResponse.breakpoints
      }
      }
    }
  }
}

class PowerShellExecutionStack(val stackResponse: StackTraceResponse,
                               val server: IDebugProtocolServer,
                               val coroutineScope: CoroutineScope, val xDebugSession: XDebugSession): XExecutionStack("PowerShell Debug Execution Stack") {
  override fun getTopFrame(): XStackFrame? {
    return stackResponse.stackFrames.firstOrNull()?.let { PowerShellStackFrame(it, server, coroutineScope, xDebugSession) }
  }

  override fun computeStackFrames(firstFrameIndex: Int, container: XStackFrameContainer?) {
    container?.addStackFrames(stackResponse.stackFrames.drop(firstFrameIndex).map { PowerShellStackFrame(it, server, coroutineScope, xDebugSession) }, true)
  }

}

class PowerShellStackFrame(val stack: StackFrame, val server: IDebugProtocolServer, val coroutineScope: CoroutineScope, val xDebugSession: XDebugSession): XStackFrame() {

  override fun getSourcePosition(): XSourcePosition? {
    var file = VfsUtil.findFile(Path(stack.source?.path ?: return null), false)
    return XDebuggerUtil.getInstance().createPosition(file, stack.line - 1, stack.column)
  }

  override fun getEvaluator(): XDebuggerEvaluator {
    return PowershellDebuggerEvaluator(server, this, coroutineScope)
  }

  override fun customizePresentation(component: ColoredTextContainer) {
    if(stack.source == null)
      component.append(stack.name.orEmpty(), SimpleTextAttributes.REGULAR_ATTRIBUTES)
    else
      super.customizePresentation(component)
  }

  override fun computeChildren(node: XCompositeNode) {
    coroutineScope.launch {
      val scopesResponse = server.scopes(ScopesArguments().apply { frameId = stack.id }).await()
      val list = XValueChildrenList()
      val localScope = scopesResponse.scopes.first { scope -> scope.name.lowercase() == "local" }
      val localVariables = server.variables(VariablesArguments().apply {
        variablesReference = localScope.variablesReference
      }).await()
      localVariables.variables.forEach { list.add(it.name, PowerShellDebuggerVariableValue(it, localScope.variablesReference, server, coroutineScope, xDebugSession)) }

      scopesResponse.scopes.filter { x -> x.name.lowercase() != "local" }.forEach {
        val variableRef = it.variablesReference
        val groupName = it.name
        val variables = server.variables(VariablesArguments().apply {
          variablesReference = variableRef
        }).await()
        val group = PowerShellVariableGroup(groupName, variables, variableRef, server, coroutineScope, xDebugSession)
        list.addBottomGroup(group)
      }

      node.addChildren(list, true)
    }
  }
}

class PowerShellDebuggerVariableValue(val variable: Variable, val parentReference: Int?,
                                      val server: IDebugProtocolServer,
                                      val coroutineScope: CoroutineScope, val xDebugSession: XDebugSession) : XNamedValue(variable.name ?: "") {

  init {
    xDebugSession.suspendContext?.let {
      val variablesCache = (xDebugSession.suspendContext as PowerShellSuspendContext).variablesCache
      variablesCache.getOrDefault((Pair(parentReference, variable.name)), null)?.let {
        variable.value = it.value
        variable.type = it.type ?: variable.type
        variable.variablesReference = variable.variablesReference
        variable.namedVariables = it.namedVariables
        variable.indexedVariables = it.indexedVariables
      }
    }
  }

  override fun computePresentation(node: XValueNode, place: XValuePlace) {
    //val kind = variable.presentationHint.kind
    var icon: Icon? = IconManager.getInstance().getPlatformIcon(PlatformIcons.Variable)

    node.setPresentation(icon, variable.type, variable.value, variable.variablesReference != 0)
  }

  override fun computeChildren(node: XCompositeNode) {
    coroutineScope.launch {
      if (variable.variablesReference != 0) {
        val list = XValueChildrenList()
        server.variables(VariablesArguments().apply { variablesReference = variable.variablesReference })
          .await().variables.forEach { list.add(it.name, PowerShellDebuggerVariableValue(it, variable.variablesReference, server, coroutineScope, xDebugSession)) }
        node.addChildren(list, true)
      }
    }
  }

  override fun getEvaluationExpression(): String? {
    return variable.evaluateName
  }

  override fun getModifier(): XValueModifier {
    return object : XValueModifier() {
      override fun getInitialValueEditorText(): String? {
        return variable.value
      }

      override fun setValue(expression: XExpression, callback: XModificationCallback) {
        if(parentReference !is Int)
          return
        coroutineScope.launch {
          val variablesCache = (xDebugSession.suspendContext as PowerShellSuspendContext).variablesCache
          try {
            val response = server.setVariable(SetVariableArguments().apply {
              variablesReference = parentReference
              name = variable.name
              value = expression.expression
            }).await()
            variable.value = response.value
            variable.type = response.type ?: variable.type
            variable.variablesReference = response.variablesReference ?: variable.variablesReference
            variable.namedVariables = response.namedVariables ?: variable.namedVariables
            variable.indexedVariables = response.indexedVariables ?: variable.indexedVariables
            variablesCache[(Pair(parentReference, variable.name))] = variable

            callback.valueModified()
          } catch (e: Exception) {
            callback.errorOccurred(e.message ?: e.javaClass.simpleName)
          }
        }
      }
    }
  }

  override fun computeSourcePosition(navigatable: XNavigatable) {
    //navigatable.setSourcePosition(PowerShellSourcePosition())
  }
}

class PowerShellVariableGroup(val groupName: String, val variable: VariablesResponse, val parentReference: Int,
                              val server: IDebugProtocolServer,
                              val coroutineScope: CoroutineScope,
                              val xDebugSession: XDebugSession): XValueGroup(groupName){
  override fun computeChildren(node: XCompositeNode) {
    val list = XValueChildrenList()
    variable.variables.forEach {
      list.add(it.name, PowerShellDebuggerVariableValue(it, parentReference, server, coroutineScope, xDebugSession))
    }
    node.addChildren(list, true)
  }
}

class PowerShellSuspendContext(val stack: StackTraceResponse, val server: IDebugProtocolServer,
                               val coroutineScope: CoroutineScope,
                               val threadId: Int = 0, val xDebugSession: XDebugSession):XSuspendContext(){

  val variablesCache: MutableMap<Pair<Int, String>, Variable> = mutableMapOf()
  override fun getExecutionStacks(): Array<XExecutionStack> {
    return arrayOf(PowerShellExecutionStack(stack, server, coroutineScope, xDebugSession))
  }

  override fun getActiveExecutionStack(): XExecutionStack {
    return PowerShellExecutionStack(stack, server, coroutineScope, xDebugSession)
  }
}
