package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellCallableReference
import com.intellij.plugin.powershell.psi.PowerShellCommandName
import com.intellij.psi.PsiElement

/**
 * Andrey 24/08/17.
 */
open class PowerShellCallableReferenceExpression(node: ASTNode) : PowerShellReferenceImpl(node), PowerShellCallableReference {

  override fun getElement(): PsiElement {
    return getNameElement() ?: super.getElement()
  }

  override fun getNameElement(): PsiElement? {//todo delete and implement this in getElement()?
    return findChildByClass(PowerShellCommandName::class.java)
  }

  override fun getCanonicalText(): String {
    val commandName = getNameElement()
    return if (commandName != null) commandName.text else super.getCanonicalText()
  }
}
