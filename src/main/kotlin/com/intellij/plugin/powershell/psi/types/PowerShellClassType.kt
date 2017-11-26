package com.intellij.plugin.powershell.psi.types

interface PowerShellClassType: PowerShellType {
  fun getReferenceName(): String
}