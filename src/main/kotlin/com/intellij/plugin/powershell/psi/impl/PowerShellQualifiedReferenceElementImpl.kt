package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.psi.PowerShellPsiElement
import com.intellij.plugin.powershell.psi.PowerShellQualifiedReferenceElement
import com.intellij.plugin.powershell.psi.PowerShellReferenceIdentifier
import com.intellij.plugin.powershell.psi.PowerShellTypes
import com.intellij.psi.PsiElement

abstract class PowerShellQualifiedReferenceElementImpl<out Q : PowerShellPsiElement>(node: ASTNode) : PowerShellReferencePsiElementImpl(node), PowerShellQualifiedReferenceElement<Q> {

  override fun isTypeMemberAccess(): Boolean {
    return findChildByType<PsiElement>(PowerShellTypes.COLON2) != null
  }

  override fun getNavigationElement(): PsiElement {
    return getNameElement() ?: super.getNavigationElement()
  }

  override fun getReferenceName(): String? {
    return getNameElement()?.text
  }

  override fun getNameElement(): PsiElement? {
    return findChildByClass(PowerShellReferenceIdentifier::class.java)
  }

  override fun getRangeInElement(): TextRange {
    val nameRange = getNameElement()?.textRange ?: textRange
    return TextRange(nameRange.startOffset - textRange.startOffset, nameRange.endOffset - textRange.startOffset)
  }

  override fun getTextOffset(): Int {
    return getNameElement()?.textOffset ?: super.getTextOffset()
  }
}