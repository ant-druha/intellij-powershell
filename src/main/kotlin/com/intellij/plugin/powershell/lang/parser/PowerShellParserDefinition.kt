package com.intellij.plugin.powershell.lang.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.plugin.powershell.lang.lexer.PowerShellLexerAdapter
import com.intellij.plugin.powershell.psi.PowerShellTypes
import com.intellij.plugin.powershell.psi.impl.PowerShellFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

/**
 * Andrey 17/07/17.
 */
class PowerShellParserDefinition : ParserDefinition {

  private val FILE = IFileElementType(PowerShellLanguage.INSTANCE)

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return PowerShellFile(viewProvider)
    }

    override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    override fun getStringLiteralElements(): TokenSet {
      return TokenSet.create(PowerShellTypes.STRING_LITERAL_EXPRESSION)
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun getWhitespaceTokens(): TokenSet {
        return TokenSet.create(TokenType.WHITE_SPACE)
    }

    override fun createLexer(project: Project?): Lexer {
        return PowerShellLexerAdapter()
    }

    override fun createElement(node: ASTNode?): PsiElement {
        return PowerShellTypes.Factory.createElement(node)
    }

    override fun getCommentTokens(): TokenSet {
        return TokenSet.create(PowerShellTypes.COMMENT)
    }

    override fun createParser(project: Project?): PsiParser {
      return PowerShellParser()
    }

}