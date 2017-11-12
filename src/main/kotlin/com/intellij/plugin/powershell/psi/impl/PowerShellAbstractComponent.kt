package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.plugin.powershell.ide.search.PowerShellComponentType
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellIdentifier
import com.intellij.plugin.powershell.psi.PowerShellPsiElementFactory
import com.intellij.psi.PsiElement
import javax.swing.Icon


/**
 * Andrey 15/08/17.
 */
abstract class PowerShellAbstractComponent(node: ASTNode) : PowerShellPsiElementImpl(node), PowerShellComponent {

  override fun getPresentation(): ItemPresentation {
    return object : ItemPresentation {
      override fun getLocationString(): String? {
        return null
      }

      override fun getIcon(unused: Boolean): Icon? {
        return PowerShellComponentType.typeOf(this@PowerShellAbstractComponent)?.getIcon()
      }

      override fun getPresentableText(): String? {
        return name
      }
    }
  }

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