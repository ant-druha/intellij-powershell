package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.PowerShellAssignmentExpression
import com.intellij.plugin.powershell.psi.PowerShellClassDeclarationStatement
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellEnumDeclarationStatement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.scope.PsiScopeProcessor

/**
 * Andrey 21/08/17.
 */

object PowerShellResolveUtil {

  fun toCandidateInfoArray(elements: List<PsiElement>?): Array<ResolveResult> {
    if (elements == null) return ResolveResult.EMPTY_ARRAY
    return Array(elements.size) { PsiElementResolveResult(elements[it]) }
  }

//  fun areNamesEqual(component: PowerShellComponent, reference: PowerShellReferencePsiElement): Boolean {
//    val refName = reference.canonicalText
//
//    if (component is PowerShellVariable) {
//      val ns = component.getNamespace()
//      if (!"function".equals(ns, true) || reference !is PowerShellCallableReference) return component.getQualifiedName() == refName
//    }
//    return component.name == refName
//  }

  fun processChildDeclarations(context: PsiElement, processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement?): Boolean {
    val result = HashSet<PowerShellComponent>()
    collectChildrenDeclarations(context, result, processor, state, lastParent)

    return result.none { (place == null || canBeReferenced(place, it)) && !processor.execute(it, state) }
  }

  fun collectChildrenDeclarations(context: PsiElement, result: HashSet<PowerShellComponent>, processor: PsiScopeProcessor,
                                  state: ResolveState, lastParent: PsiElement?) {
    context.children.forEach { child ->
      if (child === lastParent) return@forEach
      when (child) {
        is PowerShellComponent -> result.add(child)
        is PowerShellAssignmentExpression -> result += child.targetVariables
        !is LeafPsiElement -> {
          collectChildrenDeclarations(child, result, processor, state, null)
        }
      }
    }
  }

  private fun canBeReferenced(place: PsiElement, component: PowerShellComponent): Boolean {
    return place.textOffset > component.textOffset
  }

  fun processClassMembers(clazz: PowerShellClassDeclarationStatement, processor: PowerShellComponentScopeProcessor,
                          state: ResolveState, lastParent: PsiElement?, place: PsiElement?) {
    val classBody = clazz.blockBody ?: return
    processChildDeclarations(classBody, processor, state, lastParent, place)
  }

  fun processEnumMembers(enum: PowerShellEnumDeclarationStatement, processor: PowerShellComponentScopeProcessor,
                         state: ResolveState, lastParent: PsiElement?, place: PsiElement?) {
    val enumBody = enum.blockBody ?: return
    processChildDeclarations(enumBody, processor, state, lastParent, place)
  }

//  fun getMaxLocalScopeForTargetOrReference(element: PsiElement): PsiElement? {
//    var currentScope: PsiElement?
//    if (element is PowerShellVariable || element is PowerShellReferenceVariable) {
//      //local scope: function/method definition, current file, type declaration
//      currentScope = element.context
//      var maxScopeReached = false
//      while (!maxScopeReached && currentScope != null) {
//        maxScopeReached = isCallableDeclaration(currentScope.node)
//            || currentScope is PowerShellClassDeclarationStatement
//            || currentScope is PowerShellEnumDeclarationStatement
//            || currentScope is PowerShellFile
//        currentScope = if (maxScopeReached) currentScope else currentScope.context
//      }
//      return currentScope
//    }
//    return null
//  }

}