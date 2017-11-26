package com.intellij.plugin.powershell.lang

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
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
    }
  }

  private fun annotateFunctionName(holder: AnnotationHolder, callable: PowerShellCallableDeclaration, attributes: TextAttributesKey) {
    createInfoAnnotation(holder, callable.nameIdentifier, attributes)
  }

  private fun annotateMethodCallName(holder: AnnotationHolder, call: PowerShellInvocationExpression, attributes: TextAttributesKey) {
    createInfoAnnotation(holder, call.member, attributes)
  }

  private fun annotateMemberAccess(holder: AnnotationHolder, expression: PowerShellMemberAccessExpression, attributes: TextAttributesKey) {
    createInfoAnnotation(holder, expression.member, attributes)
  }

  private fun annotateVariable(holder: AnnotationHolder, variable: PowerShellTargetVariableExpression, attributes: TextAttributesKey) {
    createInfoAnnotation(holder, variable.getScope(), attributes)
    createInfoAnnotation(holder, variable.node.findChildByType(PowerShellTypes.COLON)?.psi, attributes)
    val varId = variable.identifier
    if (!PowerShellTokenTypeSets.KEYWORDS.contains(varId.firstChild?.node?.elementType)) createInfoAnnotation(holder, varId, attributes)
  }

  private fun createInfoAnnotation(holder: AnnotationHolder, element: PsiElement?, attributeKey: TextAttributesKey) {
    if (element != null) {
      holder.createInfoAnnotation(element, null).textAttributes = attributeKey
    }
  }
}