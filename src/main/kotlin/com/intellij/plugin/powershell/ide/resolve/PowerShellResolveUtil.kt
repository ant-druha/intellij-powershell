package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.PowerShellCallableReference
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellReference
import com.intellij.plugin.powershell.psi.PowerShellVariable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult

/**
 * Andrey 21/08/17.
 */

object PowerShellResolveUtil {

  fun toCandidateInfoArray(elements: List<PsiElement>?): Array<ResolveResult> {
    if (elements == null) return ResolveResult.EMPTY_ARRAY
    return Array(elements.size) { PsiElementResolveResult(elements[it]) }
  }

  fun areNamesEqual(component: PowerShellComponent, reference: PowerShellReference): Boolean {
    val refName = reference.canonicalText

    if (component is PowerShellVariable) {
      val ns = component.getNamespace()
      if (!"function".equals(ns, true) || reference !is PowerShellCallableReference) return component.getQualifiedName() == refName
    }
    return component.name == refName
  }

}