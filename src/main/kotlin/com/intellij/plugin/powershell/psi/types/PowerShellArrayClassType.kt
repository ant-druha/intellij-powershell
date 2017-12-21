package com.intellij.plugin.powershell.psi.types

interface PowerShellArrayClassType : PowerShellReferenceClassType {
  fun getComponentType(): PowerShellType
}