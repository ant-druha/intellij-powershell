package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.PowerShellReference
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil

/**
 * Andrey 19/08/17.
 */
class PowerShellComponentResolveProcessor : ResolveCache.AbstractResolver<PowerShellReference, List<PsiElement>> {

  companion object {
    val INSTANCE = PowerShellComponentResolveProcessor()
  }

  override fun resolve(ref: PowerShellReference, incompleteCode: Boolean): List<PsiElement> {

    // local
    val maxScope = getMaxScope(ref.element)
    val resolveProcessor = PowerShellComponentScopeProcessor()
    PsiTreeUtil.treeWalkUp(resolveProcessor, ref.element, maxScope, ResolveState.initial())
    val results = resolveProcessor.getResult()
    return results.toList()
  }

  private fun getMaxScope(context: PsiElement): PsiElement? {
    return null
  }


}