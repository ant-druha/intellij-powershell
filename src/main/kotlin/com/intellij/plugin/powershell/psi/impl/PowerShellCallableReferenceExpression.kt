package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.psi.PowerShellCallableReference
import com.intellij.plugin.powershell.psi.PowerShellCommandName
import com.intellij.psi.PsiElement

/**
 * Andrey 24/08/17.
 */
open class PowerShellCallableReferenceExpression(node: ASTNode) : PowerShellReferencePsiElementImpl(node), PowerShellCallableReference {

  override fun getElement(): PsiElement = getNameElement() ?: super.getElement()

  override fun getRangeInElement(): TextRange {
    val refElement = getNameElement() ?: return super.getRangeInElement()

    val refRange = refElement.textRange
    return TextRange(textRange.startOffset - refRange.startOffset, refRange.endOffset - refRange.startOffset)
  }

  override fun getNameElement(): PsiElement? = findChildByClass(PowerShellCommandName::class.java)

  override fun getCanonicalText(): String = getNameElement()?.text ?: super.getCanonicalText()
}
