package com.intellij.plugin.powershell.psi.types

import com.intellij.plugin.powershell.lang.resolve.PowerShellTypeReference

interface PowerShellReferenceClassType : PowerShellClassType {
  fun getReference(): PowerShellTypeReference
}