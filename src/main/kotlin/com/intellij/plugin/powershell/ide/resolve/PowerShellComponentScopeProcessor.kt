package com.intellij.plugin.powershell.ide.resolve

import com.intellij.openapi.util.Key
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellPsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

/**
 * Andrey 18/08/17.
 */
class PowerShellComponentScopeProcessor : PsiScopeProcessor {
  private val myResult: ArrayList<PowerShellPsiElement> = ArrayList()

  override fun handleEvent(event: PsiScopeProcessor.Event, associated: Any?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun <T : Any?> getHint(hintKey: Key<T>): T? {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun execute(element: PsiElement, state: ResolveState): Boolean {
    if (element is PowerShellComponent) {
      myResult.add(element)
    }
    return true
  }

  fun getResult(): List<PowerShellPsiElement> {
    return myResult
  }

}