package com.intellij.plugin.powershell.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.ide.resolve.PowerShellMemberScopeProcessor
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveResult
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.ide.resolve.PsNames
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.types.PowerShellClassType
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.PowerShellTypeVisitor
import com.intellij.plugin.powershell.psi.types.impl.PowerShellImmediateClassTypeImpl
import com.intellij.psi.ResolveState
import com.intellij.util.Processor

abstract class PowerShellQualifiedReferenceExpression(node: ASTNode) : PowerShellQualifiedReferenceElementImpl<PowerShellExpression>(node), PowerShellQualifiedReferenceElement<PowerShellExpression>, PowerShellExpression {

  override fun getType(): PowerShellType {
    val resolved = resolve()
    if (resolved != null) {
      return inferTypeFromResolved(resolved)
    }
    return PowerShellType.UNKNOWN
  }

  private fun inferTypeFromResolved(resolved: PowerShellComponent): PowerShellType {
    //todo create function type and return function image here
    if (resolved is PowerShellAttributesHolder) resolved.getAttributeList().mapNotNull { it.typeLiteralExpression }.forEach { return it.getType() }

    if (resolved is PowerShellConstructorDeclarationStatement) {
      //this means that this is a call 'new' -> can infer from qualifier
      assert(PsNames.CONSTRUCTOR_CALL.equals(referenceName, true))
      val resolvedQualifier = resolveQualifier()
      if (resolvedQualifier is PowerShellTypeDeclaration) return PowerShellImmediateClassTypeImpl(resolvedQualifier)

//      //or infer from containing class? but can resolve to e.g. base class
//      val containingClass = resolved.getContainingClass()
//      if (containingClass != null) return PowerShellImmediateClassTypeImpl(containingClass)
    }
    if (resolved is PowerShellEnumLabelDeclaration) {
      val containingClass = resolved.getContainingClass()
      if (containingClass != null) return PowerShellImmediateClassTypeImpl(containingClass)
    }
    return PowerShellType.UNKNOWN
  }

  override fun getQualifier(): PowerShellExpression? = findChildByClass(PowerShellExpression::class.java)

  override fun multiResolve(incompleteCode: Boolean): Array<PowerShellResolveResult> {
    val qType = getQualifierType()
    val resolveProcessor = PowerShellResolveUtil.getMemberScopeProcessor(this)
    if (resolveProcessor != null && qType != null && qType != PowerShellType.UNKNOWN) {
      if (processTypeMembers(qType, incompleteCode, resolveProcessor)) return extractResults(resolveProcessor)
    }
    return super.multiResolve(incompleteCode)
  }

  private fun extractResults(resolveProcessor: PowerShellMemberScopeProcessor): Array<PowerShellResolveResult> {
    val res = resolveProcessor.getResult()
    return Array(res.size) { res[it] }
  }

  private fun processTypeMembers(psType: PowerShellType, incompleteCode: Boolean, resolveProcessor: PowerShellMemberScopeProcessor): Boolean {

    val declarationsProvider = Processor<PowerShellMemberScopeProcessor> { processor ->
      psType.accept(object : PowerShellTypeVisitor<Boolean>() {

        override fun visitClassType(o: PowerShellClassType): Boolean {
          val resolved = o.resolve()
          if (resolved is PowerShellTypeDeclaration) {
            return processMembers(resolved, processor)
          }
          return false
        }
      }) ?: false
    }
    return processDeclarations(declarationsProvider, incompleteCode, resolveProcessor)
  }

  private fun processDeclarations(declarationsProvider: Processor<PowerShellMemberScopeProcessor>,
                                  incompleteCode: Boolean,
                                  resolveProcessor: PowerShellMemberScopeProcessor): Boolean {
    return declarationsProvider.process(resolveProcessor)
  }

  private fun processMembers(clazz: PowerShellTypeDeclaration, resolveProcessor: PowerShellMemberScopeProcessor): Boolean {
    val members = clazz.getMembers()
    members.forEach {
      if (!resolveProcessor.doExecute(it, ResolveState.initial())) return true
    }
    val baseClass = clazz.getBaseClass()
    if (baseClass != null) {
      val baseDeclaration = baseClass.resolve()
      if (baseDeclaration is PowerShellTypeDeclaration && processMembers(baseDeclaration, resolveProcessor)) return true
    }
    //todo process default constructor (navigate to class)
    if (hasDefaultConstructor(clazz)) {

    }
    return false
  }

  private fun hasDefaultConstructor(clazz: PowerShellTypeDeclaration): Boolean {
    val baseClass = clazz.getBaseClass()?.resolve()
    return baseClass is PowerShellTypeDeclaration && hasDefaultConstructor(baseClass) || !clazz.getMembers().any { it is PowerShellConstructorDeclarationStatement }

  }

  private fun processDefaultConstructor(clazz: PowerShellTypeDeclaration, resolveProcessor: PowerShellMemberScopeProcessor): Boolean {
    clazz.getMembers().filter { it is PowerShellConstructorDeclarationStatement }
    return false
  }


  private fun resolveQualifier(): PowerShellComponent? {
    val q = qualifier //can be literal expression, parenthesized/sub/script_block expression, target_variable, so..
    //does not make sense to search for a reference to resolve? better to get type of it (since it is an expression -> have a type
    if (q != null) {
      val qualifierType = q.getType()
      if (qualifierType is PowerShellClassType) {
        return qualifierType.resolve()
      }
    }
    return null
  }

  private fun getQualifierType(): PowerShellType? {
    return qualifier?.getType()
  }

}