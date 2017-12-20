package com.intellij.plugin.powershell.psi.types

import com.intellij.plugin.powershell.psi.PowerShellComponent

interface PowerShellClassType: PowerShellType {
  fun resolve(): PowerShellComponent?
}