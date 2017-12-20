package com.intellij.plugin.powershell.psi.types.impl

import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.PowerShellTypeVisitor

class PowerShellObjectType: PowerShellType {
  override fun <T> accept(visitor: PowerShellTypeVisitor<T>): T? {
    return visitor.visitType(this)
  }

  override fun getName(): String = "Object"
}