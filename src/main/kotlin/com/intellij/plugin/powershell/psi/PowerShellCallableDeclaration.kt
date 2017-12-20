package com.intellij.plugin.powershell.psi

import com.intellij.plugin.powershell.psi.types.PowerShellType

interface PowerShellCallableDeclaration : PowerShellComponent {
  fun getReturnType(): PowerShellType?
}