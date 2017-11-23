package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration
import com.intellij.psi.PsiElement

open class PowerShellMemberDeclarationImpl(node: ASTNode): PowerShellAbstractComponent(node), PowerShellMemberDeclaration {
  override fun getContainingClass(): PsiElement? = context
}