package com.intellij.plugin.powershell.ide.debugger

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.ui.ColoredTextContainer
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.io.await
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerUtil
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XStackFrame
import com.intellij.xdebugger.frame.XValueChildrenList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.eclipse.lsp4j.debug.ScopesArguments
import org.eclipse.lsp4j.debug.StackFrame
import org.eclipse.lsp4j.debug.VariablesArguments
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import kotlin.io.path.Path

class PowerShellStackFrame(val stack: StackFrame, val server: IDebugProtocolServer, val coroutineScope: CoroutineScope, val xDebugSession: XDebugSession): XStackFrame() {

  override fun getSourcePosition(): XSourcePosition? {
    val file = VfsUtil.findFile(Path(stack.source?.path ?: return null), false)
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
      localVariables.variables.forEach { list.add(it.name,
          PowerShellDebuggerVariableValue(it, localScope.variablesReference, server, coroutineScope, xDebugSession)
      ) }

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
