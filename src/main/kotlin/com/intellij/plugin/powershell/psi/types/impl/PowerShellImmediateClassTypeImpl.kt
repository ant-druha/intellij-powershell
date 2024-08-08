package com.intellij.plugin.powershell.psi.types.impl

import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration
import com.intellij.plugin.powershell.psi.types.PowerShellImmediateClassType
import com.intellij.plugin.powershell.psi.types.PowerShellTypeVisitor

class PowerShellImmediateClassTypeImpl(private val myClass: PowerShellTypeDeclaration) : PowerShellImmediateClassType {
  override fun <T> accept(visitor: PowerShellTypeVisitor<T>): T? {
    return visitor.visitClassType(this)
  }

  override fun resolve(): PowerShellComponent {
    return myClass
  }

  override fun getName(): String {
    return myClass.name ?: "<Unnamed>"
  }
}
