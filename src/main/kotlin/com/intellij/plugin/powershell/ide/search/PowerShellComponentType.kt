package com.intellij.plugin.powershell.ide.search

import com.intellij.plugin.powershell.psi.PowerShellFunctionStatement
import com.intellij.plugin.powershell.psi.PowerShellVariable
import com.intellij.psi.PsiElement

/**
 * Andrey 24/08/17.
 */
enum class PowerShellComponentType(s: String) {

  FUNCTION("function"),
  VARIABLE("function");

  companion object {
    fun typeOf(element: PsiElement): PowerShellComponentType? {
      return when (element) {
        is PowerShellFunctionStatement -> FUNCTION
        is PowerShellVariable -> VARIABLE
        else -> null
      }
    }
  }

}