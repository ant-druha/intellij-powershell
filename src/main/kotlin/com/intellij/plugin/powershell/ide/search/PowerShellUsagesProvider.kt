package com.intellij.plugin.powershell.ide.search

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellReferencePsiElement
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.tree.TokenSet

/**
 * Andrey 15/08/17.
 */
class PowerShellUsagesProvider : FindUsagesProvider {

  override fun getWordsScanner(): WordsScanner {
    return DefaultWordsScanner(PowerShellLexerAdapter(),
                               PowerShellTokenTypeSets.IDENTIFIERS,
                               PowerShellTokenTypeSets.COMMENTS,
                               PowerShellTokenTypeSets.STRINGS,
                               TokenSet.EMPTY,
                               PowerShellTokenTypeSets.PROCESS_AS_WORD_TOKENS
    )
  }

  override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
    return psiElement is PowerShellReferencePsiElement || psiElement is PowerShellComponent
  }

  override fun getHelpId(psiElement: PsiElement): String? {
    return null
  }

  override fun getType(element: PsiElement): String {
    val componentType = PowerShellComponentType.typeOf(element)
    return componentType?.toString()?.lowercase() ?: "reference"
  }

  override fun getDescriptiveName(element: PsiElement): String {
    return when (element) {
      is PsiNamedElement ->
        PowerShellComponentType.typeOf(element)?.toString()?.lowercase() + " " + StringUtil.notNullize(element.name)
      is PsiReference -> element.canonicalText
      else -> element.text
    }
  }

  override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
    val name = (element as? PsiNamedElement)?.name
    return name ?: element.text
  }
}
