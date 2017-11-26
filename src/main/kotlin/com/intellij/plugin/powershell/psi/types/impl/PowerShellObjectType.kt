package com.intellij.plugin.powershell.psi.types.impl

import com.intellij.plugin.powershell.psi.types.PowerShellType

class PowerShellObjectType: PowerShellType {
  override fun getName(): String = "Object"
}