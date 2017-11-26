package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.*
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.scope.BaseScopeProcessor
import com.intellij.psi.util.PsiTreeUtil
import java.util.*

/**
 * Andrey 21/08/17.
 */
class PowerShellResolver<T> : ResolveCache.AbstractResolver<T, List<PsiElement>> where T : PowerShellReferencePsiElement {
  companion object {
    val INSTANCE = PowerShellResolver<PowerShellReferencePsiElement>()
  }

  private fun resolveReference(ref: T): List<PsiElement> {
    val result = ArrayList<PowerShellComponent>()
    val resolveProcessor = when (ref) {
      is PowerShellCallableReference -> //  $function: F  # invokes function F -> also callable reference (when ns== "function")
        PowerShellCallableResolveProcessor(ref)
      is PowerShellReferenceTypeElement -> PowerShellTypeResolveProcessor(ref)
      else -> PowerShellVariableResolveProcessor(ref)
    }
    PsiTreeUtil.treeWalkUp(resolveProcessor, ref, null, ResolveState.initial())
    val res = resolveProcessor.getResult()
    if (res != null) result.add(res)
    return result
  }

  override fun resolve(ref: T, incompleteCode: Boolean): List<PsiElement> {

    return resolveReference(ref)
  }
}

abstract class PowerShellResolveProcessor<out R : PowerShellReferencePsiElement>(protected val myRef: R) : BaseScopeProcessor() {
  protected var myResult: PowerShellComponent? = null //todo should navigate to closest declaration (of function or variable)

  fun getResult(): PowerShellComponent? {
    return myResult
  }

  protected fun setResult(element: PowerShellComponent): Boolean {
    if (myResult == null || myResult!!.textOffset > element.textOffset) {
      myResult = element
    }
    return true
  }

}

class PowerShellCallableResolveProcessor(ref: PowerShellCallableReference) : PowerShellResolveProcessor<PowerShellCallableReference>(ref) {

  override fun execute(element: PsiElement, state: ResolveState): Boolean {
    if (element is PowerShellComponent) {
      if (element.name.equals(myRef.canonicalText, true) && (element !is PowerShellVariable || "function".equals(element.getScopeName(), true))) {
//      if (PowerShellResolveUtil.areNamesEqual(element, myRef)) {
        return setResult(element)
      }
//      }
    }
    return true
  }

}

class PowerShellVariableResolveProcessor(ref: PowerShellReferencePsiElement) : PowerShellResolveProcessor<PowerShellReferencePsiElement>(ref) {

  override fun execute(element: PsiElement, state: ResolveState): Boolean {
    if (element is PowerShellComponent) {
      val refName = myRef.canonicalText
      if (element is PowerShellVariable) {
        if (element.getQualifiedName().equals(refName, true)) {
          return setResult(element)
        }
      } else if (element.name.equals(refName, true)) {
        return setResult(element)
      }
    }
    return true
  }

}

class PowerShellTypeResolveProcessor(ref: PowerShellReferenceTypeElement) : PowerShellResolveProcessor<PowerShellReferencePsiElement>(ref) {

  override fun execute(element: PsiElement, state: ResolveState): Boolean {
    if (element is PowerShellClassDeclarationStatement || element is PowerShellEnumDeclarationStatement) {
      val refName = myRef.canonicalText
      if ((element as PowerShellComponent).name.equals(refName, true)) {
        return setResult(element)
      }
    }
    return true
  }

}
