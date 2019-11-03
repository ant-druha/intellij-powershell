package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.PowerShellTypeDeclaration
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

/**
 * Andrey 24/12/17.
 */
open class PowerShellClassScopeProcessor : PsiScopeProcessor {
  private val myResult: ArrayList<PowerShellResolveResult> = ArrayList()

  override fun execute(element: PsiElement, state: ResolveState): Boolean {
    if (element is PowerShellTypeDeclaration) {
      return doExecute(element, state)
    }
    return true
  }

  open fun doExecute(pstype: PowerShellTypeDeclaration, state: ResolveState): Boolean {
    myResult.add(PowerShellResolveResult(pstype))
    return true
  }

  fun getResult(): List<PowerShellResolveResult> {
    return myResult
  }

}