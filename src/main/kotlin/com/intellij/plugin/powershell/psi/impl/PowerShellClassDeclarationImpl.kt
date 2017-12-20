package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellBlockBody
import com.intellij.plugin.powershell.psi.PowerShellClassDeclaration
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement
import com.intellij.psi.util.PsiTreeUtil

open class PowerShellClassDeclarationImpl(node: ASTNode) : PowerShellAbstractComponent(node), PowerShellClassDeclaration {
  override fun getBaseClass(): PowerShellReferenceTypeElement? {
    return findChildByClass(PowerShellReferenceTypeElement::class.java)
  }

  override fun getMembers(): List<PowerShellMemberDeclaration> {
    val result = mutableListOf<PowerShellMemberDeclaration>()
    val classBody = findChildByClass(PowerShellBlockBody::class.java)
    result.addAll(PsiTreeUtil.findChildrenOfType(classBody, PowerShellMemberDeclaration::class.java))
    return result
  }
}