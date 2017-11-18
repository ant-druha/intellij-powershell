package com.intellij.plugin.powershell.ide.search

import com.intellij.codeInsight.TargetElementEvaluatorEx
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference

class PowerShellTargetElementEvaluator : TargetElementEvaluatorEx {

  override fun getElementByReference(ref: PsiReference, flags: Int): PsiElement? = ref.resolve()

  override fun includeSelfInGotoImplementation(element: PsiElement): Boolean = true

  override fun isIdentifierPart(file: PsiFile?, text: CharSequence?, offset: Int): Boolean {
    if (text == null || file == null) return false
    val elementAt = file.findElementAt(offset)
    return PowerShellTokenTypeSets.IDENTIFIERS.contains(elementAt?.node?.elementType)
  }
}