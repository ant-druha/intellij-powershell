package com.intellij.plugin.powershell.ide.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.openapi.editor.Editor
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.impl.PowerShellFile
import com.intellij.psi.PsiElement

class PowerShellStructureViewModel(file: PowerShellFile, editor: Editor?) :
    StructureViewModelBase(file, editor, PowerShellStructureViewElement(file)), StructureViewModel.ElementInfoProvider {

  init {
    withSuitableClasses(*PowerShellStructureViewModel.getSuitableClasses())
  }

  companion object {
    fun getSuitableClasses(): Array<Class<out PsiElement>> {
      return arrayOf(PowerShellFile::class.java,
          PowerShellClassDeclarationStatement::class.java,
          PowerShellEnumDeclarationStatement::class.java,
          PowerShellMethodDeclarationStatement::class.java,
          PowerShellConstructorDeclarationStatement::class.java,
          PowerShellPropertyDeclarationStatement::class.java,
          PowerShellTargetVariableExpression::class.java,
          PowerShellFunctionStatement::class.java,
          PowerShellEnumLabelDeclaration::class.java,
          PowerShellConfigurationBlock::class.java
      )
    }
  }

  override fun shouldEnterElement(element: Any?): Boolean {
    return element is PowerShellClassDeclarationStatement
  }

  override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean {
    return false
  }

  override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean {
    return false
  }
}