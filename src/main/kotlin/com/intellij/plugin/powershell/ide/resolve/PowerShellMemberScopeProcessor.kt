package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.BaseScopeProcessor

/**
 * Andrey 19/12/17.
 */
abstract class PowerShellMemberScopeProcessor(protected val myName: String) : BaseScopeProcessor() {
  protected val myResult: ArrayList<PowerShellResolveResult> = ArrayList()

  override fun execute(element: PsiElement, state: ResolveState): Boolean {
    if (element is PowerShellMemberDeclaration) {
      return doExecute(element, state)
    }
    return true
  }

  abstract fun doExecute(psMember: PowerShellMemberDeclaration, state: ResolveState): Boolean

  fun getResult(): List<PowerShellResolveResult> {
    return myResult
  }

}