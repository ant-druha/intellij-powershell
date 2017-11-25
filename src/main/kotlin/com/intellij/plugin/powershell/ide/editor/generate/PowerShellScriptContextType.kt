package com.intellij.plugin.powershell.ide.editor.generate

import com.intellij.codeInsight.template.EverywhereContextType
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
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace


abstract class PowerShellScriptContextType(id: String, presentableName: String, baseContext: Class<out TemplateContextType>)
  : TemplateContextType(id, presentableName, baseContext) {

  override fun isInContext(file: PsiFile, offset: Int): Boolean {
    val place = file.findElementAt(offset)
    return if (place == null || place is PsiWhiteSpace || place is PsiComment) {
      false
    } else file is PowerShellFile && isInContext(place)
  }

  protected open fun isInContext(place: PsiElement) = true

  override fun createHighlighter(): SyntaxHighlighter? =
      SyntaxHighlighterFactory.getSyntaxHighlighter(PowerShellFileType.INSTANCE, null, null)
}

open class PowerShellLanguageContext(id: String, presentableName: String, baseContext: Class<out TemplateContextType>) :
    PowerShellScriptContextType(id, presentableName, baseContext) {
  @Suppress("unused")
  constructor() : this("powershell.context.file", "PowerShell", EverywhereContextType::class.java)
}

class PowerShellDeclarationContext : PowerShellLanguageContext("powershell.context.declaration", "Declaration", PowerShellLanguageContext::class.java) {

  override fun isInContext(place: PsiElement): Boolean = isDeclarationContext(place)

  private fun isDeclarationContext(place: PsiElement): Boolean {
    val psiElement = place.context ?: return false

    return psiElement.context is PowerShellComponent || psiElement.context is PowerShellFile
  }
}

class PowerShellStatementContext : PowerShellLanguageContext("powershell.context.statement", "Statement", PowerShellLanguageContext::class.java) {

  override fun isInContext(place: PsiElement): Boolean = isStatementContext(place)

  private fun isStatementContext(place: PsiElement): Boolean {
    val psiElement = place.context ?: return false
    val elementContext = psiElement.context
    if (elementContext is PowerShellCommandName) return true

    return elementContext is PowerShellBlockBody || elementContext is PowerShellFile
  }
}