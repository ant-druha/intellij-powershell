package com.intellij.plugin.powershell.psi

import com.intellij.psi.PsiElement

interface PowerShellMemberDeclaration: PowerShellComponent {
  fun getContainingClass(): PsiElement?
}