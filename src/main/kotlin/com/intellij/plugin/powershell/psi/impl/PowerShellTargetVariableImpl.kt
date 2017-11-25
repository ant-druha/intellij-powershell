package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolver
import com.intellij.plugin.powershell.ide.search.PowerShellComponentType
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.PowerShellTypes.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.ResolveResult
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.tree.TokenSet
import javax.swing.Icon

/**
 * Andrey 18/08/17.
 */
open class PowerShellTargetVariableImpl(node: ASTNode) : PowerShellAbstractComponent(node), PowerShellVariable, PowerShellReferencePsiElement, PsiPolyVariantReference {
  override fun getNameElement(): PsiElement? = nameIdentifier

  override fun getQualifiedName(): String {
    val ns = getNamespace() ?: "Variable"
    return ns + ":" + name
  }


  override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
    val elements = ResolveCache.getInstance(project).resolveWithCaching(this, PowerShellResolver.INSTANCE, true, incompleteCode)
    return PowerShellResolveUtil.toCandidateInfoArray(elements)
  }

  override fun getElement(): PsiElement = this

  override fun resolve(): PsiElement? {
    val res = multiResolve(false)
    return if (res.isEmpty()) this else res[0].element
  }

  override fun getVariants(): Array<Any> = emptyArray() //todo list all existing variables + all functions of type: "$Function: funName"

  override fun getRangeInElement(): TextRange = TextRange(getPrefixNode().textLength, node.textLength - (getSuffixNode()?.textLength ?: 0))

  override fun getCanonicalText(): String {
    val varName = nameIdentifier?.text
    val ns = getNamespace() ?: "Variable"
    return ns + ":" + varName
  }

  override fun handleElementRename(newElementName: String?): PsiElement =
      if (newElementName != null) setName(newElementName) else this

  override fun setName(name: String): PsiElement {
    val identifier = getNameElement()
    val variable = PowerShellPsiElementFactory.createVariableFromText(project, name, isBracedVariable())
    val identifierNew = variable?.nameIdentifier
    if (identifierNew != null && identifier != null) {
      node.replaceChild(identifier.node, identifierNew.node)
    }
    return this
  }

  private fun isBracedVariable() = firstChild.node.elementType === BRACED_VAR_START

  override fun bindToElement(element: PsiElement): PsiElement = this

  override fun isSoft(): Boolean = false

  override fun isReferenceTo(element: PsiElement?): Boolean = element != null && element == resolve()
//    return isReferenceToTarget(element)

  //if we resolve several definitions to closes target
//  private fun isReferenceToTarget(element: PsiElement?): Boolean {
//    if (element == null) return false
//    val target = resolve()
//    if (target == this || target == null) return false
//    val targetName = (target as? PowerShellTargetVariableExpression)?.name
//    val theirName = (element as? PowerShellTargetVariableExpression)?.name
//    if (StringUtil.isNotEmpty(targetName) && theirName == targetName) {
//      val ourScopeOwner = PowerShellResolveUtil.getMaxLocalScopeForTargetOrReference(target)
//      val theirScopeOwner = PowerShellResolveUtil.getMaxLocalScopeForTargetOrReference(element)
//      if (resolvesToSameLocal(element, ourScopeOwner, theirScopeOwner)) {
//        return true
//      }
//    }
//    return false
//  }
//
//  private fun resolvesToSameLocal(element: PsiElement, ourScopeOwner: PsiElement?, theirScopeOwner: PsiElement?): Boolean {
//    return ourScopeOwner === theirScopeOwner
//  }

  override fun getQualifier(): PsiElement? {
    val dot = findChildByType<PsiElement>(PowerShellTypes.DOT)
    return dot?.prevSibling //todo
  }

  override fun getPresentation(): ItemPresentation {
    return object : ItemPresentation {
      override fun getLocationString(): String? = getNamespace()

      override fun getIcon(unused: Boolean): Icon? = getIcon(0)

      override fun getPresentableText(): String? = getPrefix() + name + (getSuffix() ?: "")
    }
  }

  override fun getIcon(flags: Int): Icon? = PowerShellComponentType.VARIABLE.getIcon()

  override fun getNamespace(): String? = findChildByType<PsiElement>(SIMPLE_ID)?.text

  override fun getPrefix(): String = getPrefixNode().text

  private fun getPrefixNode(): ASTNode = node.findChildByType(TokenSet.create(DS, AT, BRACED_VAR_START)) ?: error("null for" + text)

  override fun getSuffix(): String? = getSuffixNode()?.text

  private fun getSuffixNode(): ASTNode? = node.findChildByType(RCURLY)

  override fun getReference(): PowerShellReferencePsiElement? {
    return if (getNamespace().equals("function", true) && !isLhsAssignmentTarget()) {
      //if context is PowerShellAssignmentStatement, then this is function declaration
      object : PowerShellCallableReferenceExpression(node) {
        override fun getNameElement(): PsiElement? = nameIdentifier
      }
    } else this
//    return PowerShellReferencePsiElementImpl(node)
  }

  fun isLhsAssignmentTarget(): Boolean = context?.firstChild == this && context is PowerShellAssignmentExpression

  override fun processDeclarations(processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement): Boolean =
      super.processDeclarations(processor, state, lastParent, place)
}

