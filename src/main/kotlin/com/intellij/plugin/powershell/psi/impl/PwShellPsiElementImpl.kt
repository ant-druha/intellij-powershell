package com.intellij.plugin.powershell.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PwShellPsiElement

/**
 * Andrey 26/06/17.
 */
open class PwShellPsiElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), PwShellPsiElement