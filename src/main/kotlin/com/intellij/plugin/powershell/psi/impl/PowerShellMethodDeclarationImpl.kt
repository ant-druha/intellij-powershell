package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellBlockBody
import com.intellij.plugin.powershell.psi.PowerShellCallableDeclaration
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.impl.PowerShellObjectType

open class PowerShellMethodDeclarationImpl(node: ASTNode) : PowerShellCallableDeclarationImpl(node), PowerShellCallableDeclaration, PowerShellMemberDeclaration {
  override fun getContainingClass(): PowerShellTypeDeclaration? {
    return (context as? PowerShellBlockBody)?.context as? PowerShellTypeDeclaration
  }

  override fun getReturnType(): PowerShellType? {
    return PowerShellObjectType()//stub
  }
}