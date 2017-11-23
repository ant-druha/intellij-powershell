package com.intellij.plugin.powershell.ide.refactoring

import com.intellij.find.FindManager
import com.intellij.find.impl.FindManagerImpl
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.ide.refactoring.PowerShellNameUtils.nameHasSubExpression
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.LocalSearchScope
import com.intellij.refactoring.rename.RenameDialog
import com.intellij.refactoring.rename.RenamePsiElementProcessor

class PowerShellRenamePsiElementProcessor : RenamePsiElementProcessor() {
  override fun canProcessElement(element: PsiElement): Boolean = element is PowerShellComponent
  override fun findReferences(element: PsiElement?, searchInCommentsAndStrings: Boolean): MutableCollection<PsiReference> {
    if (element is PowerShellComponent && nameHasSubExpression(element)) {//todo it's workaround to resort to findUsage handler because default index does not contain needed tokens
      val findManager = FindManager.getInstance(element.getProject()) as? FindManagerImpl ?: return super.findReferences(element, searchInCommentsAndStrings)
      val findUsagesManager = findManager.findUsagesManager
      val findUsagesHandler = findUsagesManager.getFindUsagesHandler(element, true) ?: return super.findReferences(element, searchInCommentsAndStrings)
      val scope = LocalSearchScope(element.containingFile)
      return findUsagesHandler.findReferencesToHighlight(element, scope)
    } else return super.findReferences(element, searchInCommentsAndStrings)
  }

  override fun createRenameDialog(project: Project, element: PsiElement, nameSuggestionContext: PsiElement?, editor: Editor?): RenameDialog {
    return object : RenameDialog(project, element, nameSuggestionContext, editor) {
      override fun getNewName(): String = nameSuggestionsField.enteredName
    }
  }
}