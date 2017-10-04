package com.intellij.plugin.powershell.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellAssignmentExpression
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellPsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

/**
 * Andrey 26/06/17.
 */
open class PowerShellPsiElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), PowerShellPsiElement {
  override fun processDeclarations(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {
    return processDeclarationsImpl(processor, state, lastParent, place)
  }

  private fun processDeclarationsImpl(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {
    val result = HashSet<PowerShellComponent>()
    for (ch in children) {
      if (ch is PowerShellComponent) {
        result.add(ch)
      } else if (ch is PowerShellAssignmentExpression) {
        result += ch.targetVariables
      }
    }
//    return result.none { processor.execute(it, state).not() }
    return result.none { place.textOffset > it.textOffset && processor.execute(it, state).not() }
  }
}