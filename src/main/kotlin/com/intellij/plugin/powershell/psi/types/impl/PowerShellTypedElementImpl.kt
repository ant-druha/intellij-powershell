package com.intellij.plugin.powershell.psi.types.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.impl.PowerShellPsiElementImpl
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement

open class PowerShellTypedElementImpl(node: ASTNode) : PowerShellPsiElementImpl(node), PowerShellTypedElement {

  override fun getType(): PowerShellType = PowerShellObjectType()
}