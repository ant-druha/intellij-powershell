package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

/**
 * Andrey 19/12/17.
 */
open class PowerShellMemberScopeProcessor : PsiScopeProcessor {
  protected val myResult: ArrayList<PowerShellResolveResult> = ArrayList()

  override fun execute(element: PsiElement, state: ResolveState): Boolean {
    if (element is PowerShellMemberDeclaration) {
      return doExecute(element, state)
    }
    return true
  }

  open fun doExecute(psMember: PowerShellMemberDeclaration, state: ResolveState): Boolean {
    myResult.add(PowerShellResolveResult(psMember))
    return true
  }

  fun getResult(): List<PowerShellResolveResult> {
    return myResult
  }

}