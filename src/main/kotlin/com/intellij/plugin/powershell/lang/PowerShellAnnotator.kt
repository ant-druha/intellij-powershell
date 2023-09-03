package com.intellij.plugin.powershell.lang

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.plugin.powershell.ide.editor.highlighting.PowerShellSyntaxHighlighter
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.impl.PowerShellPsiImplUtil
import com.intellij.psi.PsiElement

/**
 * Andrey 05/08/17.
 */
class PowerShellAnnotator : Annotator {
  override fun annotate(e: PsiElement, holder: AnnotationHolder) {
    when {
      e is PowerShellCommandName -> {
        val elText = e.text
        if (elText.endsWith(".exe", true) || elText.contains('-') || !elText.contains('.')) {
          createInfoAnnotation(holder, e, PowerShellSyntaxHighlighter.COMMAND_NAME)
        }
      }
      e is PowerShellIdentifier && PowerShellPsiImplUtil.isTypeDeclarationContext(e) -> createInfoAnnotation(holder, e, PowerShellSyntaxHighlighter.TYPE_NAME)
      e is PowerShellReferenceTypeElement -> createInfoAnnotation(holder, e, PowerShellSyntaxHighlighter.TYPE_REFERENCE)
      e is PowerShellTargetVariableExpression -> annotateVariable(holder, e, PowerShellSyntaxHighlighter.VARIABLE_NAME)
      e is PowerShellMemberAccessExpression -> annotateMemberAccess(holder, e, PowerShellSyntaxHighlighter.PROPERTY_REFERENCE)
      e is PowerShellInvocationExpression -> annotateMethodCallName(holder, e, PowerShellSyntaxHighlighter.METHOD_CALL)
      e is PowerShellCallableDeclaration -> annotateFunctionName(holder, e, PowerShellSyntaxHighlighter.METHOD_DECLARATION)
      e is PowerShellLabel -> createInfoAnnotation(holder, e.identifier, PowerShellSyntaxHighlighter.LABEL)
      e is PowerShellLabelReferenceExpression -> createInfoAnnotation(holder, e.identifier, PowerShellSyntaxHighlighter.LABEL)
      e is PowerShellIncompleteDeclaration -> createErrorAnnotation(holder, e, "Incomplete declaration")
      shouldAnnotateAsParameter(e) -> createInfoAnnotation(holder, e, PowerShellSyntaxHighlighter.COMMAND_PARAMETER)
    }
  }

  private fun createErrorAnnotation(holder: AnnotationHolder, e: PsiElement, msg: String) {
    holder.newAnnotation(HighlightSeverity.ERROR, msg).range(e).create()
  }

  private fun shouldAnnotateAsParameter(element: PsiElement): Boolean {
    if (element is PowerShellCommandParameter) return true

    val parameterNode = element.node
    if (parameterNode?.elementType !== PowerShellTypes.CMD_PARAMETER) return false
    val parent = parameterNode?.treeParent ?: return false
    return parent.elementType === PowerShellTypes.FOREACH_STATEMENT || parent.elementType === PowerShellTypes.SWITCH_STATEMENT
  }

  private fun annotateFunctionName(holder: AnnotationHolder, callable: PowerShellCallableDeclaration, attributes: TextAttributesKey) {
    createInfoAnnotation(holder, callable.nameIdentifier, attributes)
  }

  private fun annotateMethodCallName(holder: AnnotationHolder, call: PowerShellInvocationExpression, attributes: TextAttributesKey) {
    createInfoAnnotation(holder, call.identifier, attributes)
  }

  private fun annotateMemberAccess(holder: AnnotationHolder, expression: PowerShellMemberAccessExpression, attributes: TextAttributesKey) {
    createInfoAnnotation(holder, expression.identifier, attributes)
  }

  private fun annotateVariable(holder: AnnotationHolder, variable: PowerShellTargetVariableExpression, attributes: TextAttributesKey) {
    createInfoAnnotation(holder, variable.getScope(), attributes)
    createInfoAnnotation(holder, variable.node.findChildByType(PowerShellTypes.COLON)?.psi, attributes)
    val varId = variable.identifier
    if (!PowerShellTokenTypeSets.KEYWORDS.contains(varId.firstChild?.node?.elementType)) createInfoAnnotation(holder, varId, attributes)
  }

  private fun createInfoAnnotation(holder: AnnotationHolder, element: PsiElement?, attributeKey: TextAttributesKey) {
    if (element != null) {
      holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element).textAttributes(attributeKey).create()
    }
  }
}