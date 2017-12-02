package com.intellij.plugin.powershell.psi.impl

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.ide.resolve.PowerShellComponentResolveProcessor
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolver
import com.intellij.plugin.powershell.psi.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.tree.IElementType
import java.util.*

/**
 * Andrey 18/08/17.
 */
abstract class PowerShellReferencePsiElementImpl(node: ASTNode) : PowerShellPsiElementImpl(node), PowerShellReferencePsiElement, PsiPolyVariantReference {

  override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
    val elements = ResolveCache.getInstance(project).resolveWithCaching(this, PowerShellResolver.INSTANCE, true, incompleteCode)
    return PowerShellResolveUtil.toCandidateInfoArray(elements)
  }

  override fun getNameElement(): PsiElement? = findChildByClass(PowerShellIdentifier::class.java)

  override fun getElement(): PsiElement = this

  override fun getReference(): PsiReference? = this

  override fun resolve(): PsiElement? {
    val res = multiResolve(false)
    return if (res.isNotEmpty()) res[0].element else null
  }

  override fun getVariants(): Array<Any> {
    val elements = ResolveCache.getInstance(project)
        .resolveWithCaching(this, PowerShellComponentResolveProcessor.INSTANCE, true, true) ?: return emptyArray()

    val lookupElements = ArrayList<LookupElement>()

    for (e in elements) {
      addLookupElement(e, lookupElements)
    }

    for (keyword in PowerShellTokenTypeSets.KEYWORDS.types) {
      addKeyword(keyword, lookupElements)
    }
    return lookupElements.toArray()
  }

  private fun addKeyword(kw: IElementType, elements: ArrayList<LookupElement>) {
    elements.add(LookupElementBuilder.create(kw.toString().toLowerCase()).bold())
  }

  private fun addLookupElement(e: PsiElement, lookupElements: ArrayList<LookupElement>) {
    val icon = e.getIcon(0)
    val res: LookupElement = when (e) {
      is PowerShellVariable -> {
        val lookupString = e.presentation.presentableText ?: e.text
        LookupElementBuilder.create(e, lookupString).withIcon(icon).withPresentableText(lookupString)
      }
      is PowerShellFunctionStatement -> LookupElementBuilder.create(e).withIcon(icon)
      is PowerShellComponent -> LookupElementBuilder.create(e).withIcon(icon)
      else -> LookupElementBuilder.create(e)
    }
    lookupElements.add(res)
  }

  override fun getRangeInElement(): TextRange {
    val refRange = element.textRange
    return TextRange(refRange.startOffset - textRange.startOffset, refRange.endOffset - textRange.startOffset)
  }

//  override fun getTextRange(): TextRange { //todo
//    val start = textOffset
//    return TextRange(start, start + textLength)
//  }

//  override fun getTextOffset(): Int {
//    return getNameElement()?.textOffset ?: super.getTextOffset()
//  }

  override fun getCanonicalText(): String = node.text

  override fun handleElementRename(newElementName: String?): PsiElement {
    val nameElement = getNameElement()
    if (newElementName == null || nameElement == null) return this

    val identifierNew = PowerShellPsiElementFactory.createIdentifierFromText(project, newElementName, true)
    if (identifierNew != null) nameElement.replace(identifierNew)
    return this
  }


  override fun bindToElement(element: PsiElement): PsiElement = this

  override fun isSoft(): Boolean = false

  override fun isReferenceTo(element: PsiElement?): Boolean = element != null && resolve() == element
}
