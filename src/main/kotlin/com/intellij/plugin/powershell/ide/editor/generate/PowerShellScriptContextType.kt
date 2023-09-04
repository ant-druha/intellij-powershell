package com.intellij.plugin.powershell.ide.editor.generate

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.psi.PowerShellBlockBody
import com.intellij.plugin.powershell.psi.PowerShellCommandName
import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.impl.PowerShellFile
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace


abstract class PowerShellScriptContextType(presentableName: String) : TemplateContextType(presentableName) {

  override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
    val file = templateActionContext.file
    val place = file.findElementAt(templateActionContext.startOffset)
    return if (place == null || place is PsiWhiteSpace || place is PsiComment) {
      false
    } else file is PowerShellFile && isInContext(place)
  }

  protected open fun isInContext(place: PsiElement) = true

  override fun createHighlighter(): SyntaxHighlighter? =
      SyntaxHighlighterFactory.getSyntaxHighlighter(PowerShellFileType.INSTANCE, null, null)
}

open class PowerShellLanguageContext(presentableName: String) :
    PowerShellScriptContextType(presentableName) {
  @Suppress("unused")
  constructor() : this("PowerShell")
}

class PowerShellDeclarationContext : PowerShellLanguageContext("Declaration") {

  override fun isInContext(place: PsiElement): Boolean = isDeclarationContext(place)

  private fun isDeclarationContext(place: PsiElement): Boolean {
    val psiElement = place.context ?: return false

    return psiElement.context is PowerShellComponent || psiElement.context is PowerShellFile
  }
}

class PowerShellStatementContext : PowerShellLanguageContext("Statement") {

  override fun isInContext(place: PsiElement): Boolean = isStatementContext(place)

  private fun isStatementContext(place: PsiElement): Boolean {
    val psiElement = place.context ?: return false
    val elementContext = psiElement.context
    if (elementContext is PowerShellCommandName) return true

    return elementContext is PowerShellBlockBody || elementContext is PowerShellFile
  }
}