package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellIdentifier
import com.intellij.plugin.powershell.psi.PowerShellPsiElementFactory
import com.intellij.psi.PsiElement


/**
 * Andrey 15/08/17.
 */
abstract class PowerShellAbstractComponent(node: ASTNode) : PowerShellPsiElementImpl(node), PowerShellComponent {

  override fun getName(): String? {
    return nameIdentifier?.text
  }

  override fun getTextOffset(): Int {
    return nameIdentifier?.textOffset ?: super.getTextOffset()
  }

  override fun getNameIdentifier(): PsiElement? {
    return findChildByClass(PowerShellIdentifier::class.java)
  }

  override fun setName(name: String): PsiElement {
    val identifier = nameIdentifier
    val identifierNew = PowerShellPsiElementFactory.createIdentifierFromText(project, name)
    if (identifierNew != null && identifier != null) {
      node.replaceChild(identifier.node, identifierNew.node)
    }
    return this
  }
}