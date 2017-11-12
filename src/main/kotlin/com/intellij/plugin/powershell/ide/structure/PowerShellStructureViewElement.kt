package com.intellij.plugin.powershell.ide.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.navigation.ItemPresentation
import com.intellij.plugin.powershell.ide.resolve.PowerShellComponentScopeProcessor
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.impl.PowerShellFile
import com.intellij.psi.ResolveState
import javax.swing.Icon

class PowerShellStructureViewElement(element: PowerShellPsiElement) : PsiTreeElementBase<PowerShellPsiElement>(element),
    ItemPresentation, StructureViewTreeElement {

  private var myIsRoot = false

  constructor(element: PowerShellPsiElement, isRoot: Boolean) : this(element) {
    myIsRoot = isRoot
  }

  override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> {
    val result = mutableListOf<StructureViewTreeElement>()
    val resolveProcessor = PowerShellComponentScopeProcessor()
    val myElement = element
    if (myElement is PowerShellFile) {
      if (!myIsRoot) {
        result.add(PowerShellStructureViewElement(myElement, true))
        return result
      }
      PowerShellResolveUtil.processDeclarationsImpl(myElement, resolveProcessor, ResolveState.initial(), null, null)
    } else if (myElement is PowerShellClassDeclarationStatement) {
      PowerShellResolveUtil.processClassMembers(myElement, resolveProcessor, ResolveState.initial(), null, null)
    }
    val components = resolveProcessor.getResult()
    components.filter {
      it is PowerShellTargetVariableExpression
          || it is PowerShellClassDeclarationStatement
          || it is PowerShellMethodDeclarationStatement
          || it is PowerShellConstructorDeclarationStatement
          || it is PowerShellPropertyDeclarationStatement
          || it is PowerShellEnumDeclarationStatement
          || it is PowerShellFunctionStatement
          || it is PowerShellConfigurationBlock
    }.mapTo(result) { PowerShellStructureViewElement(it, it is PowerShellClassDeclarationStatement && it != myElement) }
    result.sortBy { e -> (e.value as? PowerShellPsiElement)?.textOffset ?: 0 }

    return result
  }

  override fun getIcon(open: Boolean): Icon? {
    return element?.presentation?.getIcon(true)
  }

  override fun getPresentableText(): String? {
    return element?.presentation?.presentableText
  }
}