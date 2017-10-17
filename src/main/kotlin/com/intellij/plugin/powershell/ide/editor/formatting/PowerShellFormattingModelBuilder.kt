package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.formatting.Block
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.FormatterUtil
import com.intellij.psi.formatter.FormattingDocumentModelImpl
import com.intellij.psi.formatter.PsiBasedFormattingModel
import com.intellij.psi.impl.source.tree.TreeUtil

/**
 * Andrey 08/08/17.
 */
class PowerShellFormattingModelBuilder : FormattingModelBuilder {
  override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? {
    return null
  }

  override fun createModel(element: PsiElement?, settings: CodeStyleSettings): FormattingModel {
    val containingFile = element?.getContainingFile()?.viewProvider?.getPsi(PowerShellLanguage.INSTANCE) ?: error(element!!.getContainingFile())
    val astNode = containingFile.node!!
    val codeStyleSettings = settings.getCommonSettings(PowerShellLanguage.INSTANCE)
    val rootBlock = PowerShellBlock(astNode, null, null, settings)
    return PowerShellFormattingModel(containingFile, rootBlock, FormattingDocumentModelImpl.createOn(containingFile))
  }

  private class PowerShellFormattingModel internal constructor(file: PsiFile, rootBlock: Block, documentModel: FormattingDocumentModelImpl)
    : PsiBasedFormattingModel(file, rootBlock, documentModel) {

    override fun replaceWithPsiInLeaf(textRange: TextRange, whiteSpace: String, leafElement: ASTNode): String? {
      if (!myCanModifyAllWhiteSpaces) {
//        if (AppleScriptPsiImplUtil.isWhiteSpaceOrNls(leafElement)) return null
      }

      val elementTypeToUse = TokenType.WHITE_SPACE
      val prevNode = TreeUtil.prevLeaf(leafElement)
//      if (prevNode != null && AppleScriptPsiImplUtil.isWhiteSpaceOrNls(prevNode)) {
//        elementTypeToUse = prevNode.elementType
//      }
      FormatterUtil.replaceWhiteSpace(whiteSpace, leafElement, elementTypeToUse, textRange)
      return whiteSpace
    }
  }
}