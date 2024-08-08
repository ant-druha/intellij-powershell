package com.intellij.plugin.powershell.psi.types.impl

import com.intellij.plugin.powershell.ide.resolve.PsNames
import com.intellij.plugin.powershell.psi.PowerShellArrayTypeElement
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.types.PowerShellArrayClassType
import com.intellij.plugin.powershell.psi.types.PowerShellClassType
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.PowerShellTypeVisitor

class PowerShellArrayClassTypeImpl(private val psiElement: PowerShellArrayTypeElement) : PowerShellArrayClassType {

  override fun getComponentType(): PowerShellType {
    return psiElement.referenceTypeElement.getType()
  }

  override fun getReferenceName(): String {
    return "[" + getName() + "]"
  }

  override fun resolve(): PowerShellComponent? {//TODO[#194] resolve to the actual array class
    val componentType = getComponentType()
    return (componentType as? PowerShellClassType)?.resolve()
  }

  override fun <T> accept(visitor: PowerShellTypeVisitor<T>): T? {
    return visitor.visitArrayClassType(this)
  }

  override fun getName(): String {
    return psiElement.referenceTypeElement.referenceName ?: PsNames.UNNAMED
  }

}
