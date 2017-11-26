package com.intellij.plugin.powershell.psi.types.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.lang.resolve.PowerShellTypeReference
import com.intellij.plugin.powershell.psi.PowerShellQualifiedReferenceElement
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement
import com.intellij.plugin.powershell.psi.PowerShellTypes
import com.intellij.plugin.powershell.psi.impl.PowerShellReferencePsiElementImpl
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference

open class PowerShellReferenceTypeElementImpl(node: ASTNode) : PowerShellReferencePsiElementImpl(node), PowerShellReferenceTypeElement,
    PowerShellQualifiedReferenceElement<PowerShellReferenceTypeElement> {

  override fun getQualifier(): PowerShellReferenceTypeElement? = findChildByClass(PowerShellReferenceTypeElement::class.java)


  override fun getReference(): PsiReference? {
    return super.getReference()
  }

  override fun getQualifiedName(): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getType(): PowerShellType {
    return PowerShellReferenceClassTypeImpl(PowerShellTypeReference())
  }


  override fun getNameElement(): PsiElement? = findLastChildByType(PowerShellTypes.SIMPLE_ID) ?: lastChild
}