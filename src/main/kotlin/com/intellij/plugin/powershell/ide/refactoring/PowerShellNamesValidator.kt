package com.intellij.plugin.powershell.ide.refactoring

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter
import com.intellij.plugin.powershell.psi.PowerShellTokenTypeSets
import com.intellij.psi.tree.IElementType

class PowerShellNamesValidator : NamesValidator {
  override fun isKeyword(name: String, project: Project?): Boolean = isKeyword(name)

  override fun isIdentifier(name: String, project: Project?): Boolean = PowerShellNameUtils.isValidName(name, project)

  private fun isKeyword(name: String): Boolean = PowerShellTokenTypeSets.KEYWORDS.contains(getTokenType(name))

  private fun getTokenType(name: String): IElementType? {
    val lexer = PowerShellLexerAdapter()
    lexer.start(name)
    val tt = lexer.tokenType
    lexer.advance()
    return if (lexer.tokenType == null) tt else null
  }

}