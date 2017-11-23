package com.intellij.plugin.powershell.ide.refactoring

import com.intellij.ide.DataManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.lang._PowerShellLexer
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellPsiElementFactory
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets
import com.intellij.plugin.powershell.psi.PowerShellTypes
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.refactoring.rename.PsiElementRenameHandler

object PowerShellNameUtils {

  fun isValidName(name: String, project: Project?): Boolean = isValidIdentifier(name, project)

  fun nameHasSubExpression(element: PowerShellComponent): Boolean {
    val name = element.name ?: return false
    val lexer = PowerShellLexerAdapter()
    lexer.start(name)
    lexer.tokenType
    lexer.advance()
    val tt2 = lexer.tokenType
    return tt2 == PowerShellTypes.DS
  }

  fun isValidName(name: String, element: PsiElement): Boolean = when {
    isBracedVariable(element) -> PowerShellTokenTypeSets.BRACED_VARIABLE_IDENTIFIERS.contains(getTokenType(name, _PowerShellLexer.VAR_BRACED))
    isSimpleVariable(element) -> PowerShellTokenTypeSets.SIMPLE_VARIABLE_IDENTIFIERS.contains(getTokenType(name, _PowerShellLexer.VAR_SIMPLE))
    isFunctionStatement(element) -> {
      val tt = getTokenType(name, _PowerShellLexer.FUNCTION_ID)
      if (PowerShellTokenTypeSets.FUNCTION_IDENTIFIERS.contains(tt)) {
        true
      } else {
        val identifierNew = PowerShellPsiElementFactory.createIdentifierFromText(element.project, name, true)
        identifierNew != null
      }
    }
    isMember(element) -> PowerShellTokenTypeSets.MEMBER_IDENTIFIERS.contains(getTokenType(name))
    else -> PowerShellTokenTypeSets.IDENTIFIERS.contains(getTokenType(name))
  }

  private fun isValidIdentifier(name: String, project: Project?): Boolean {
    if (project != null) {
      val elementToRename = getElementToRename(project)
      if (elementToRename != null) return isValidName(name, elementToRename)
    }
    return PowerShellTokenTypeSets.IDENTIFIERS.contains(getTokenType(name))
  }

  private fun getElementToRename(project: Project): PsiElement? {
    val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return null
    val dataContext = DataManager.getInstance().getDataContext(editor.component)
    return PsiElementRenameHandler.getElement(dataContext)
  }

  private fun getTokenType(name: String): IElementType? {
    val lexer = PowerShellLexerAdapter()
    lexer.start(name)
    val tt = lexer.tokenType
    lexer.advance()
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