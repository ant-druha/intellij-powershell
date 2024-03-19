package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.impl.PowerShellQualifiedReferenceExpression
import com.intellij.plugin.powershell.psi.impl.targetVariables
import com.intellij.plugin.powershell.psi.types.PowerShellArrayClassType
import com.intellij.plugin.powershell.psi.types.PowerShellClassType
import com.intellij.plugin.powershell.psi.types.PowerShellType
import com.intellij.plugin.powershell.psi.types.PowerShellTypeVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.util.Processor

object PowerShellResolveUtil {

  fun toCandidateInfoArray2(elements: List<PowerShellResolveResult>?): Array<PowerShellResolveResult> {
    if (elements == null) return PowerShellResolveResult.EMPTY_ARRAY
    return Array(elements.size) { elements[it] }
  }

  fun processChildDeclarations(context: PsiElement, processor: PsiScopeProcessor, state: ResolveState, lastParent: PsiElement?, place: PsiElement?): Boolean {
    val result = HashSet<PowerShellComponent>()
    collectChildrenDeclarations(context, result, processor, state, lastParent)

    return result.none { (place == null || canBeReferenced(place, it)) && !processor.execute(it, state) }
  }

  fun collectChildrenDeclarations(context: PsiElement, result: HashSet<PowerShellComponent>, processor: PsiScopeProcessor,
                                  state: ResolveState, lastParent: PsiElement?) {
    context.children.forEach { child ->
      if (child === lastParent) return@forEach
      when (child) {
        is PowerShellComponent -> result.add(child)
        is PowerShellAssignmentExpression -> result += child.targetVariables
        !is LeafPsiElement -> {
          collectChildrenDeclarations(child, result, processor, state, null)
        }
      }
    }
  }

  private fun canBeReferenced(place: PsiElement, component: PowerShellComponent): Boolean {
    return place.textOffset > component.textOffset
  }

  fun processClassMembers(clazz: PowerShellClassDeclarationStatement, processor: PowerShellComponentScopeProcessor,
                          state: ResolveState, lastParent: PsiElement?, place: PsiElement?) {
    val classBody = clazz.blockBody ?: return
    processChildDeclarations(classBody, processor, state, lastParent, place)
  }

  fun processEnumMembers(enum: PowerShellEnumDeclarationStatement, processor: PowerShellComponentScopeProcessor,
                         state: ResolveState, lastParent: PsiElement?, place: PsiElement?) {
    val enumBody = enum.blockBody ?: return
    processChildDeclarations(enumBody, processor, state, lastParent, place)
  }

  fun getMemberScopeProcessor(ref: PowerShellQualifiedReferenceExpression): PowerShellMemberScopeProcessor? {
    val name = ref.referenceName ?: return null
    return when (ref) {
      is PowerShellInvocationExpression -> PSMethodScopeProcessor(name)
      is PowerShellMemberAccessExpression -> PSFieldScopeProcessor(name)
      else -> null
    }
  }

  fun hasDefaultConstructor(clazz: PowerShellTypeDeclaration): Boolean {
    val baseClass = clazz.getBaseClass()?.resolve()
    return (baseClass !is PowerShellTypeDeclaration || hasDefaultConstructor(baseClass))
        && !clazz.getMembers().any { it is PowerShellConstructorDeclarationStatement }

  }

  fun processMembersForType(psType: PowerShellType, resolveProcessor: PowerShellMemberScopeProcessor): Boolean {
    val declarationsProvider = Processor<PowerShellMemberScopeProcessor> { processor ->
      psType.accept(object : PowerShellTypeVisitor<Boolean>() {

        override fun visitClassType(o: PowerShellClassType): Boolean {
          return processClassType(o)
        }

        override fun visitArrayClassType(o: PowerShellArrayClassType): Boolean {
          val componentType = o.getComponentType()//TODO[#194]: resolve to the actual array class (PS actually tries to resolve to component type in some cases)
          return if (componentType is PowerShellClassType) {
            processClassType(componentType)
          } else {
            processClassType(o)
          }
        }

        private fun processClassType(o: PowerShellClassType): Boolean {
          val resolved = o.resolve()
          if (resolved is PowerShellTypeDeclaration) {
            return processMembers(resolved, processor)
          }
          return false
        }
      }) ?: false
    }
    return processDeclarations(declarationsProvider, resolveProcessor)
  }

  private fun processDeclarations(declarationsProvider: Processor<PowerShellMemberScopeProcessor>,
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
    //TODO[#195]: create fake PSI element for default constructor?
    return resolveProcessor.getResult().isNotEmpty()
  }

}
