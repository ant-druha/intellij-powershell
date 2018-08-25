package com.intellij.plugin.powershell.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiLanguageInjectionHost

interface PowerShellAbstractStringLiteralExpression : PowerShellExpression, PsiLanguageInjectionHost {
  fun hasSubstitutions(): Boolean
  fun isHereString(): Boolean
  fun isExpandable(): Boolean
  fun getQuoteEscapeChar(): Char
  fun getQuoteChar(): Char
  fun getContentRange(): TextRange
  fun getStringContent(): String
}