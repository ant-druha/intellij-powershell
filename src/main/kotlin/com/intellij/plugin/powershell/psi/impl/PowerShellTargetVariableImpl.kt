package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveResult
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolver
import com.intellij.plugin.powershell.ide.resolve.PsNames
import com.intellij.plugin.powershell.ide.search.PowerShellComponentType
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.PowerShellTypes.*
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.impl.PowerShellImmediateClassTypeImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import javax.swing.Icon

/**
 * Andrey 18/08/17.
 */
open class PowerShellTargetVariableImpl(node: ASTNode) : PowerShellAbstractComponent(node), PowerShellVariable, PowerShellReferencePsiElement, PsiPolyVariantReference {

  override fun getType(): PowerShellType {
    //in case of $this variable resolves to class declaration
    if (isInstanceThis()) {
      val enclosingType = resolve()
      if (enclosingType is PowerShellTypeDeclaration) return PowerShellImmediateClassTypeImpl(enclosingType)
    }
    val resolved = findInitialDefinition()
    val context = resolved.context
    if (context is PowerShellCastExpression) {
      return context.castType
    }
    val initializer = findInitializer(resolved)
    if (initializer is PowerShellExpression) {
      return initializer.getType()
    }
    return (initializer as? PowerShellExpression)?.getType() ?: PowerShellType.UNKNOWN
  }

  private fun isInstanceThis(): Boolean {
    return PsNames.INSTANCE_THIS.equals(name, true)
  }

  private fun findInitializer(resolved: PowerShellVariable): PowerShellPsiElement? {
    if (this == resolved) return null
    val assignment = PsiTreeUtil.getParentOfType(resolved, PowerShellAssignmentExpression::class.java, true, PowerShellBlockBody::class.java)
    return assignment?.rhsElement
  }

  //resolve is needed to find the initialization point (with the initializer) as we can have variables as reference
  //type initializer info //do an annotation check only for properties (! or parameters !)
  private fun findInitialDefinition(): PowerShellVariable {
    return resolve() as? PowerShellVariable ?: this
  }

  override fun getNameElement(): PsiElement? = nameIdentifier

  override fun getQualifiedName(): String {
    val ns = getScopeName() ?: "Variable"
    return ns + ":" + getShortName()
  }

  private fun getShortName(): String? = nameIdentifier?.text


  override fun multiResolve(incompleteCode: Boolean): Array<PowerShellResolveResult> {
    val elements = ResolveCache.getInstance(project).resolveWithCaching(this, PowerShellResolver.INSTANCE, true, incompleteCode)
    return PowerShellResolveUtil.toCandidateInfoArray2(elements)
  }

  override fun getName(): String? = getShortName()

  private fun getExplicitName(): String {
    var ns = getScopeName()
    ns = if (StringUtil.isNotEmpty(ns)) "$ns:" else ""
    return ns + getShortName()
  }

  override fun getElement(): PsiElement = this

  override fun resolve(): PowerShellComponent? {
    if (isInstanceThis()) return PsiTreeUtil.findFirstContext(this, false) { c -> c is PowerShellTypeDeclaration } as? PowerShellComponent
    val res = multiResolve(false)
    return if (res.isEmpty()) this else res[0].element
  }

  override fun getVariants(): Array<Any> = emptyArray()

  override fun getRangeInElement(): TextRange = TextRange(getPrefixNode().textLength, node.textLength - (getSuffixNode()?.textLength ?: 0))

  override fun getCanonicalText(): String {
    val varName = getShortName()
    val ns = getScopeName() ?: "Variable"
    return "$ns:$varName"
  }

  override fun handleElementRename(newElementName: String): PsiElement = setName(newElementName)

  override fun setName(name: String): PsiElement {
    val identifier = getNameElement()
    val variable = PowerShellPsiElementFactory.createVariableFromText(project, name, isBracedVariable())
    val identifierNew = variable?.getNameElement()
    if (identifierNew != null && identifier != null) {
      node.replaceChild(identifier.node, identifierNew.node)
    }
    return this
  }

  override fun isBracedVariable() = firstChild.node.elementType === BRACED_VAR_START

  override fun bindToElement(element: PsiElement): PsiElement = this

  override fun isSoft(): Boolean = false

  override fun isReferenceTo(element: PsiElement): Boolean = element == resolve()
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

  override fun getPresentation(): ItemPresentation {
    return object : ItemPresentation {
      override fun getLocationString(): String? = getScopeName()

      override fun getIcon(unused: Boolean): Icon? = getIcon(0)

      override fun getPresentableText(): String = getPrefix() + getExplicitName() + (getSuffix() ?: "")
    }
  }

  override fun getIcon(flags: Int): Icon? = PowerShellComponentType.VARIABLE.getIcon()

  override fun getScopeName(): String? = getScope()?.text

  override fun getScope(): PsiElement? = findChildByType(SIMPLE_ID)

  override fun getPrefix(): String = getPrefixNode().text

  private fun getPrefixNode(): ASTNode = node.findChildByType(TokenSet.create(DS, AT, BRACED_VAR_START)) ?: error("null for$text")

  override fun getSuffix(): String? = getSuffixNode()?.text

  private fun getSuffixNode(): ASTNode? = node.findChildByType(RCURLY)

  override fun getReference(): PowerShellReferencePsiElement? {
    return if (getScopeName().equals("function", true) && !isLhsAssignmentTarget()) {
      //if context is PowerShellAssignmentStatement, then this is function declaration
      object : PowerShellCommandCallExpressionImpl(node) {
        override fun getNameElement(): PsiElement? = this@PowerShellTargetVariableImpl.getNameElement()
        override fun getRangeInElement(): TextRange = this@PowerShellTargetVariableImpl.rangeInElement
      }
    } else this
  }

  private fun isLhsAssignmentTarget(): Boolean = context?.firstChild == this && context is PowerShellAssignmentExpression

}

