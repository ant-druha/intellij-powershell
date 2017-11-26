package com.intellij.plugin.powershell.psi.types

import com.intellij.plugin.powershell.psi.PowerShellPsiElement

interface PowerShellTypedElement : PowerShellPsiElement {
  fun getType(): PowerShellType
}