package com.intellij.plugin.powershell.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellPsiElement

/**
 * Andrey 26/06/17.
 */
open class PowerShellPsiElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), PowerShellPsiElement