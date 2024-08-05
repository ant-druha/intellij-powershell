package com.intellij.plugin.powershell.ide.debugger

import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.frame.XCompositeNode
import com.intellij.xdebugger.frame.XValueChildrenList
import com.intellij.xdebugger.frame.XValueGroup
import kotlinx.coroutines.CoroutineScope
import org.eclipse.lsp4j.debug.VariablesResponse
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer

class PowerShellVariableGroup(val groupName: String, val variable: VariablesResponse, val parentReference: Int,
                              val server: IDebugProtocolServer,
                              val coroutineScope: CoroutineScope,
                              val xDebugSession: XDebugSession
): XValueGroup(groupName){
  override fun computeChildren(node: XCompositeNode) {
    val list = XValueChildrenList()
    variable.variables.forEach {
      list.add(it.name, PowerShellDebuggerVariableValue(it, parentReference, server, coroutineScope, xDebugSession))
    }
    node.addChildren(list, true)
  }
}
