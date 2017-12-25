package com.intellij.plugin.powershell.psi.types.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellQualifiedReferenceElement
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement
import com.intellij.plugin.powershell.psi.PowerShellTypes
import com.intellij.plugin.powershell.psi.impl.PowerShellReferencePsiElementImpl
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.psi.PsiElement

open class PowerShellReferenceTypeElementImpl(node: ASTNode) : PowerShellReferencePsiElementImpl(node), PowerShellReferenceTypeElement,
    PowerShellQualifiedReferenceElement<PowerShellReferenceTypeElement> {
  override fun isTypeMemberAccess(): Boolean = false

  override fun getReferenceName(): String? {
    return getNameElement()?.text
  }

  override fun getQualifier(): PowerShellReferenceTypeElement? = findChildByClass(PowerShellReferenceTypeElement::class.java)


  override fun getType(): PowerShellType {
    return PowerShellReferenceClassTypeImpl(this)
  }


  override fun getNameElement(): PsiElement? = findLastChildByType(PowerShellTypes.SIMPLE_ID) ?: lastChild
}