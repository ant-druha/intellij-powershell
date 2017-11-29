package com.intellij.plugin.powershell.ide.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.navigation.ItemPresentation
import com.intellij.plugin.powershell.ide.resolve.PowerShellComponentScopeProcessor
import com.intellij.plugin.powershell.ide.resolve.PowerShellResolveUtil
import com.intellij.plugin.powershell.psi.PowerShellClassDeclarationStatement
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellEnumDeclarationStatement
import com.intellij.plugin.powershell.psi.PowerShellPsiElement
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
    when (myElement) {
      is PowerShellFile -> {
        if (!myIsRoot) {
          result.add(PowerShellStructureViewElement(myElement, true))
        }
        PowerShellResolveUtil.processChildDeclarations(myElement, resolveProcessor, ResolveState.initial(), null, null)
      }
      is PowerShellClassDeclarationStatement -> PowerShellResolveUtil.processClassMembers(myElement, resolveProcessor, ResolveState.initial(), null, null)
      is PowerShellEnumDeclarationStatement -> PowerShellResolveUtil.processEnumMembers(myElement, resolveProcessor, ResolveState.initial(), null, null)
    }
    val declarations: List<PowerShellComponent> = resolveProcessor.getResult()
    declarations.filter { d -> PowerShellStructureViewModel.getSuitableClasses().any { it.isInstance(d) } }
        .mapTo(result) { PowerShellStructureViewElement(it, isRoot(it) && it != myElement) }
    result.sortBy { e -> (e.value as? PowerShellPsiElement)?.textOffset ?: 0 }

    return result
  }

  private fun isRoot(e: PowerShellPsiElement) = e is PowerShellClassDeclarationStatement || e is PowerShellEnumDeclarationStatement

  override fun getIcon(open: Boolean): Icon? {
    return element?.presentation?.getIcon(true)
  }

  override fun getPresentableText(): String? {
    return element?.presentation?.presentableText
  }
}