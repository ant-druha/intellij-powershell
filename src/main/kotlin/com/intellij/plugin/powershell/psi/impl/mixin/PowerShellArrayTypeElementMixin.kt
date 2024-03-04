package com.intellij.plugin.powershell.psi.impl.mixin

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellArrayTypeElement
import com.intellij.plugin.powershell.psi.impl.PowerShellTypeElementImplGen
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.impl.PowerShellArrayClassTypeImpl

abstract class PowerShellArrayTypeElementMixin(
  node: ASTNode?
) : PowerShellTypeElementImplGen(node), PowerShellArrayTypeElement {

  override fun getType(): PowerShellType {
    return PowerShellArrayClassTypeImpl(this)
  }
}
