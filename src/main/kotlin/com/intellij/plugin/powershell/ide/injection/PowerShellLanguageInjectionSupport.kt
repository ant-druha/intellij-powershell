package com.intellij.plugin.powershell.ide.injection

import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression
import com.intellij.psi.PsiLanguageInjectionHost
import org.intellij.plugins.intelliLang.inject.AbstractLanguageInjectionSupport
import org.intellij.plugins.intelliLang.inject.TemporaryPlacesRegistry
import org.jetbrains.annotations.NonNls


@NonNls
val POWERSHELL_SUPPORT_ID = "PowerShell"

class PowerShellLanguageInjectionSupport : AbstractLanguageInjectionSupport() {
  override fun getId(): String = POWERSHELL_SUPPORT_ID
  override fun useDefaultInjector(host: PsiLanguageInjectionHost?): Boolean = false

  override fun removeInjectionInPlace(psiElement: PsiLanguageInjectionHost?): Boolean {
    if (psiElement !is PowerShellStringLiteralExpression) return false
    val project = psiElement.project
    TemporaryPlacesRegistry.getInstance(project).removeHostWithUndo(project, psiElement)
    return true
  }

  override fun getPatternClasses() = arrayOf(PSPatterns::class.java) //arrayOf()


  override fun isApplicableTo(host: PsiLanguageInjectionHost?): Boolean = host is PowerShellStringLiteralExpression
}