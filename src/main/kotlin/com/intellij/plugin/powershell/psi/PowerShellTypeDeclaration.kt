package com.intellij.plugin.powershell.psi

interface PowerShellTypeDeclaration : PowerShellComponent {
  fun getMembers(): List<PowerShellMemberDeclaration>
  fun getBaseClass(): PowerShellReferenceTypeElement?
}