package com.intellij.plugin.powershell.psi.types.impl

import com.intellij.plugin.powershell.ide.resolve.PsNames
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement
import com.intellij.plugin.powershell.psi.types.PowerShellReferenceClassType
import com.intellij.plugin.powershell.psi.types.PowerShellTypeVisitor

class PowerShellReferenceClassTypeImpl(private val myReference: PowerShellReferenceTypeElement) : PowerShellReferenceClassType {
  override fun <T> accept(visitor: PowerShellTypeVisitor<T>): T? {
    return visitor.visitClassType(this)
  }

  override fun resolve(): PowerShellComponent? {
    return myReference.resolve()
  }

  override fun getReferenceName(): String? {
    return myReference.referenceName
  }

  override fun getName(): String {
    return getReferenceName() ?: PsNames.UNNAMED
  }
}