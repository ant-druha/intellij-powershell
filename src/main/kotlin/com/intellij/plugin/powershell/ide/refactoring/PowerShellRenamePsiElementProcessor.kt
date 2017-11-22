package com.intellij.plugin.powershell.ide.refactoring

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.psi.PsiElement
import com.intellij.refactoring.rename.RenameDialog
import com.intellij.refactoring.rename.RenamePsiElementProcessor

class PowerShellRenamePsiElementProcessor: RenamePsiElementProcessor() {
  override fun canProcessElement(element: PsiElement): Boolean = element is PowerShellComponent

  override fun createRenameDialog(project: Project, element: PsiElement, nameSuggestionContext: PsiElement?, editor: Editor?): RenameDialog {
    return object : RenameDialog(project, element, nameSuggestionContext, editor) {
      override fun getNewName(): String = nameSuggestionsField.enteredName
    }
  }
}