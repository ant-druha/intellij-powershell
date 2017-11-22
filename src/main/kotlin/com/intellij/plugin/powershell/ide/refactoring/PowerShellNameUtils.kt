package com.intellij.plugin.powershell.ide.refactoring

import com.intellij.ide.DataManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.refactoring.rename.PsiElementRenameHandler

object PowerShellNameUtils {

  fun isValidName(name: String, project: Project?): Boolean = isIdentifier(name, project)

  fun isValidName(name: String, element: PsiElement): Boolean {
    if (isBracedVariable(element)) {
      return !name.contains(Regex("(.*[}].*)|(.*[`]$)"))
    }
//    else if (element is PowerShellVariable) {
//      val tt = getTokenType("\$", name)
//      val tt = getTokenType(name, _PowerShellLexer.VAR_SIMPLE)
//      return PowerShellTokenTypeSets.SIMPLE_IDENTIFIERS.contains(tt)
//    }
    return PowerShellTokenTypeSets.IDENTIFIERS.contains(getTokenType(name))
  }

  private fun isIdentifier(name: String, project: Project?): Boolean {
    if (project != null && isRenamingBracedVariable(project)) {
      return !name.contains(Regex("(.*[}].*)|(.*[`])"))
    }
    return PowerShellTokenTypeSets.IDENTIFIERS.contains(getTokenType(name))
  }

  private fun isRenamingBracedVariable(project: Project): Boolean {
    val elementToRename: PsiElement?
    val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return false

    val dataContext = DataManager.getInstance().getDataContext(editor.component)
    elementToRename = PsiElementRenameHandler.getElement(dataContext)
    return isBracedVariable(elementToRename)
  }

  private fun getTokenType(name: String): IElementType? {
    val lexer = PowerShellLexerAdapter()

    lexer.start(name)
    val tt = lexer.tokenType
    lexer.advance()
    return if (lexer.tokenType == null) tt else null
  }

  private fun getTokenType(prefix: CharSequence = "", name: String): IElementType? {
    val lexer = PowerShellLexerAdapter()
    if (prefix.isNotEmpty()) {
      val text = prefix.toString() + name
      lexer.start(text, 0, prefix.length)
      lexer.start(text, prefix.length + 1, text.length)
    } else {
      lexer.start(name)
    }
    val tt = lexer.tokenType
    return if (lexer.tokenType == null) tt else null
  }

  private fun getTokenType(name: String, state: Int): IElementType? {
    val lexer = PowerShellLexerAdapter()

    lexer.start(name, 0, name.length, state)
    val tt = lexer.tokenType
    lexer.advance()
    return if (lexer.tokenType == null) tt else null
  }

}