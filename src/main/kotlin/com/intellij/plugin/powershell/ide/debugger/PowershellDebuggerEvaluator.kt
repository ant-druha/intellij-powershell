package com.intellij.plugin.powershell.ide.debugger

import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.util.io.await
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.ExpressionInfo
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.eclipse.lsp4j.debug.EvaluateArguments
import org.eclipse.lsp4j.debug.Variable
import org.eclipse.lsp4j.debug.services.IDebugProtocolServer
import org.jetbrains.concurrency.Promise

class PowershellDebuggerEvaluator(val server: IDebugProtocolServer, val stackFrame: PowerShellStackFrame, val coroutineScope: CoroutineScope): XDebuggerEvaluator() {
  override fun evaluate(expression: String, callback: XEvaluationCallback, expressionPosition: XSourcePosition?) {
    coroutineScope.launch {
      val result = server.evaluate(EvaluateArguments().apply {
        this.expression = expression
        frameId = stackFrame.stack.id
      }).await()
      val variable : Variable = Variable().apply {
        value = result.result
        evaluateName = expression
        variablesReference = result.variablesReference
        type = result.type
        presentationHint = result.presentationHint
      }
      callback.evaluated(PowerShellDebuggerVariableValue(variable, null, server, coroutineScope, stackFrame.xDebugSession))
    }
  }

}
