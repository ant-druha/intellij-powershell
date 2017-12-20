package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellBlockBody
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration
import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration

abstract class PowerShellMemberDeclarationImpl(node: ASTNode) : PowerShellAbstractComponent(node), PowerShellMemberDeclaration {
  override fun getContainingClass(): PowerShellTypeDeclaration? = (context as? PowerShellBlockBody)?.context as? PowerShellTypeDeclaration

}