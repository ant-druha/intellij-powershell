package com.intellij.plugin.powershell.psi.impl

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.UnfairTextRange
import com.intellij.plugin.powershell.ide.resolve.PowerShellComponentResolveProcessor
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolver
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellFunctionStatement
import com.intellij.plugin.powershell.psi.PowerShellReference
import com.intellij.plugin.powershell.psi.PowerShellVariable
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import java.util.*

/**
 * Andrey 18/08/17.
 */
open class PowerShellReferenceImpl(node: ASTNode) : PowerShellPsiElementImpl(node), PowerShellReference, PsiPolyVariantReference {

  override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
    val elements = ResolveCache.getInstance(project).resolveWithCaching(this, PowerShellResolver.INSTANCE, true, incompleteCode)
    return PowerShellResolveUtil.toCandidateInfoArray(elements)
  }

  override fun getElement(): PsiElement = this

  override fun getReference(): PsiReference? = this

  override fun resolve(): PsiElement? {
    val res = multiResolve(false)
    return if (res.isNotEmpty()) res[0].element else null
  }

  override fun getVariants(): Array<Any> {
    val elements = ResolveCache.getInstance(project)
        .resolveWithCaching(this, PowerShellComponentResolveProcessor.INSTANCE, true, true) ?: return emptyArray<Any>()

    val lookupElements = ArrayList<LookupElement>()

    for (e in elements) {
      addLookupElement(e, lookupElements)
    }

    return lookupElements.toArray()
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
    val refs = PsiTreeUtil.getChildrenOfType(this, PowerShellReference::class.java)
    if (refs != null && refs.isNotEmpty()) {
      val lastRefRange = refs[refs.size - 1].textRange
      return UnfairTextRange(lastRefRange.startOffset - textRange.startOffset, lastRefRange.endOffset - textRange.endOffset)
    }
    return UnfairTextRange(0, textRange.endOffset - textRange.startOffset)
  }

  override fun getCanonicalText(): String = node.text

  override fun handleElementRename(newElementName: String?): PsiElement =
      if (this is PsiNamedElement && newElementName != null) setName(newElementName) else this

  override fun bindToElement(element: PsiElement): PsiElement = this

  override fun isSoft(): Boolean = false

  override fun isReferenceTo(element: PsiElement?): Boolean = element != null && resolve() == element
}
