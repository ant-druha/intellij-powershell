package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.UnfairTextRange
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolver
import com.intellij.plugin.powershell.psi.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.ResolveResult
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.PlatformIcons
import javax.swing.Icon

/**
 * Andrey 18/08/17.
 */
open class PowerShellTargetVariableImpl(node: ASTNode) : PowerShellAbstractComponent(node), PowerShellVariable, PowerShellReference, PsiPolyVariantReference {

  override fun getQualifiedName(): String {
    val varName = nameIdentifier?.text
    val ns = getNamespace() ?: "Variable"
    return ns + ":" + varName
  }


  override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
    val elements = ResolveCache.getInstance(project).resolveWithCaching(this, PowerShellResolver.INSTANCE, true, incompleteCode)
    return PowerShellResolveUtil.toCandidateInfoArray(elements)
  }

  override fun getElement(): PsiElement {
    return nameIdentifier ?: this
//    return this
  }

  override fun resolve(): PsiElement? {
    val res = multiResolve(false)
    return if (res.isEmpty()) this else res[0].element
  }

  override fun getVariants(): Array<Any> {
    return emptyArray<Any>() //todo list all existing variables + all functions of type: "$Function: funName"
  }

  override fun getRangeInElement(): TextRange {
    val refs = PsiTreeUtil.getChildrenOfType(this, PowerShellReference::class.java)
    val nameIdRange = nameIdentifier?.textRange ?: textRange
    if (refs != null && refs.isNotEmpty()) {
      val lastRefRange = refs[refs.size - 1].textRange
      return UnfairTextRange(lastRefRange.startOffset - nameIdRange.startOffset, lastRefRange.endOffset - nameIdRange.endOffset)
    }
//    return UnfairTextRange(nameIdRange.startOffset - this.textRange.startOffset, nameIdRange.endOffset - nameIdRange.startOffset)
//    return UnfairTextRange(0, nameIdRange.endOffset - nameIdRange.startOffset)
    return UnfairTextRange(0, this.textRange.endOffset - this.textRange.startOffset)
  }

  override fun getCanonicalText(): String {
    val varName = nameIdentifier?.text
    val ns = getNamespace() ?: "Variable"
    return ns + ":" + varName
//    val varName = nameIdentifier?.text ?: ""
//    val ns = getNamespace()
//    return if (ns != null) ns + ":" + varName else varName
  }

  override fun handleElementRename(newElementName: String?): PsiElement {
    return if (newElementName != null) setName(newElementName) else this
  }

  override fun bindToElement(element: PsiElement): PsiElement {
    return this
  }

  override fun isSoft(): Boolean {
    return false
  }

  override fun isReferenceTo(element: PsiElement?): Boolean {
    return element != null && element == resolve()
  }

  override fun getQualifier(): PsiElement? {
    val dot = findChildByType<PsiElement>(PowerShellTypes.DOT)
    return dot?.prevSibling //todo
  }

  override fun getPresentation(): ItemPresentation? {
    return object : ItemPresentation {
      override fun getLocationString(): String? {
        return getNamespace()
      }

      override fun getIcon(unused: Boolean): Icon? {
        return PlatformIcons.VARIABLE_ICON
      }

      override fun getPresentableText(): String? {
        return node.text
      }
    }
  }

  override fun getIcon(flags: Int): Icon? {
    return /*super.getIcon(flags) ?:*/ PlatformIcons.VARIABLE_ICON
  }

  override fun getNamespace(): String? {
    //todo implicit "Variable" namespace
    return findChildByType<PsiElement>(PowerShellTokenTypeSets.IDENTIFIERS)?.text
  }

  override fun getName(): String? {
//    val varName = nameIdentifier?.text
//    val ns = getNamespace()
//    return if (ns != null) ns + ":" + varName else varName
    return super.getName()
  }

  override fun getReference(): PowerShellReference? {
    return if (getNamespace().equals("function", true) && !isLhsAssignmentTarget()) {
      //if context is PowerShellAssignmentStatement, then this is function declaration
      object : PowerShellCallableReferenceExpression(node) {
        override fun getNameElement(): PsiElement? {
          return nameIdentifier
        }
      }
    } else this
//    return this
//    return PowerShellReferenceImpl(node)//todo implement reference here?
  }

  fun isLhsAssignmentTarget(): Boolean {
    return context?.firstChild == this && context is PowerShellAssignmentExpression
  }

  override fun processDeclarations(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean {

    return super.processDeclarations(processor, state, lastParent, place)
  }
}

