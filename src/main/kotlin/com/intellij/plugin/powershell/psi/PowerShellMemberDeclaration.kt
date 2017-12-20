package com.intellij.plugin.powershell.psi

interface PowerShellMemberDeclaration : PowerShellComponent {
  fun getContainingClass(): PowerShellTypeDeclaration?
}