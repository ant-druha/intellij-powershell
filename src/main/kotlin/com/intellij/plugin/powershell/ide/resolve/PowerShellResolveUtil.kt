package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

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

  fun processDeclarationsImpl(context: PsiElement, processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement?): Boolean {
    val result = HashSet<PowerShellComponent>()
    for (ch in context.children) {
      if (ch is PowerShellComponent) {
        result.add(ch)
      } else if (ch is PowerShellAssignmentExpression) {
        result += ch.targetVariables
      }
    }

    return result.none { (place == null || canBeReferenced(place, it)) && !processor.execute(it, state) }
  }

  private fun canBeReferenced(place: PsiElement, component: PowerShellComponent): Boolean {
    return place.textOffset > component.textOffset
  }

  fun processClassMembers(clazz: PowerShellClassDeclarationStatement, processor: PowerShellComponentScopeProcessor,
                          state: ResolveState, lastParent: PsiElement?, place: PsiElement?) {
    val classBody = clazz.blockBody ?: return
    processDeclarationsImpl(classBody, processor, state, lastParent, place)
  }

  fun processEnumMembers(enum: PowerShellEnumDeclarationStatement, processor: PowerShellComponentScopeProcessor,
                          state: ResolveState, lastParent: PsiElement?, place: PsiElement?) {//todo common interface for class/enum
    val enumBody = enum.blockBody ?: return
    processDeclarationsImpl(enumBody, processor, state, lastParent, place)
  }

}