package com.intellij.plugin.powershell.ide.debugger

import com.intellij.ui.IconManager
import com.intellij.ui.PlatformIcons
import com.intellij.util.io.await
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XExpression
import com.intellij.xdebugger.frame.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.eclipse.lsp4j.debug.SetVariableArguments
import org.eclipse.lsp4j.debug.Variable
import org.eclipse.lsp4j.debug.VariablesArguments
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import javax.swing.Icon

class PowerShellDebuggerVariableValue(val variable: Variable, val parentReference: Int?,
                                      val server: IDebugProtocolServer,
                                      val coroutineScope: CoroutineScope, val xDebugSession: XDebugSession
) : XNamedValue(variable.name ?: "") {

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
    val icon: Icon = IconManager.getInstance().getPlatformIcon(PlatformIcons.Variable)
    node.setPresentation(icon, variable.type, variable.value, variable.variablesReference != 0)
  }

  override fun computeChildren(node: XCompositeNode) {
    coroutineScope.launch {
      if (variable.variablesReference != 0) {
        val list = XValueChildrenList()
        server.variables(VariablesArguments().apply { variablesReference = variable.variablesReference })
          .await().variables.forEach { list.add(it.name,
                PowerShellDebuggerVariableValue(it, variable.variablesReference, server, coroutineScope, xDebugSession)
            ) }
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
}
