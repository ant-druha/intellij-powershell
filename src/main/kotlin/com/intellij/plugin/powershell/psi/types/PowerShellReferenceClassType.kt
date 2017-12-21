package com.intellij.plugin.powershell.psi.types

interface PowerShellReferenceClassType : PowerShellClassType {
  fun getReferenceName(): String?
}