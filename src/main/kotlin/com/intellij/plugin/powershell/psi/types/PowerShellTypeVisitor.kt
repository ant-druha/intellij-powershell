package com.intellij.plugin.powershell.psi.types

abstract class PowerShellTypeVisitor<out T> {
  open fun visitClassType(o: PowerShellClassType): T? {
    return visitType(o)
  }

  open fun visitType(o: PowerShellType): T? {
    return null
  }

}