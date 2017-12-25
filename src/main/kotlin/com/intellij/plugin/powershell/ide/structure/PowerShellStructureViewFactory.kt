package com.intellij.plugin.powershell.ide.structure

import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.plugin.powershell.psi.impl.PowerShellFile
import com.intellij.psi.PsiFile

class PowerShellStructureViewFactory : PsiStructureViewFactory {
  override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
    if (psiFile !is PowerShellFile) return null

    return object : TreeBasedStructureViewBuilder() {
      override fun createStructureViewModel(editor: Editor?): StructureViewModel {
        return PowerShellStructureViewModel(psiFile, editor)
      }

      override fun isRootNodeShown() = true
    }
  }
}