package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellCallableReference
import com.intellij.plugin.powershell.psi.PowerShellCommandName
import com.intellij.psi.PsiElement

/**
 * Andrey 24/08/17.
 */
open class PowerShellCallableReferenceExpression(node: ASTNode) : PowerShellReferencePsiElementImpl(node), PowerShellCallableReference {

  override fun getElement(): PsiElement = getNameElement() ?: super.getElement()

  override fun getNameElement(): PsiElement? = findChildByClass(PowerShellCommandName::class.java)?.identifier

  override fun getCanonicalText(): String = getNameElement()?.text ?: super.getCanonicalText()
}
