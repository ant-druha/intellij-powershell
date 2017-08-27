package com.intellij.plugin.powershell.psi.impl

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.plugin.powershell.psi.PowerShellAssignmentStatement
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellPsiElement
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.PsiScopeProcessor

/**
 * Andrey 17/07/17.
 */
class PowerShellFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PowerShellLanguage.INSTANCE), PowerShellPsiElement {
    override fun getFileType(): FileType {
        return PowerShellFileType()
    }

  override fun processDeclarations(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {
    return processDeclarationsImpl(processor, state, lastParent, place)
  }

  private fun processDeclarationsImpl(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {
    val result = HashSet<PowerShellComponent>()
    for (ch in children) {
      if (ch is PowerShellComponent) {
        result.add(ch)
      } else if (ch is PowerShellAssignmentStatement) {
        result += ch.targetVariables
      }
    }
//    result += children.filterIsInstance<PowerShellComponent>()
    return result.none { place.textOffset > it.textOffset && processor.execute(it, state).not() }
  }
}