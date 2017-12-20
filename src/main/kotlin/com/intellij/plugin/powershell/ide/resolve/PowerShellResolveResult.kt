package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.psi.ResolveResult

/**
 * Andrey 19/08/17.
 */
class PowerShellResolveResult(private val myElement: PowerShellComponent) : ResolveResult {

  override fun getElement(): PowerShellComponent {
    return myElement
  }

  private val myIsValid: Boolean = true

  override fun isValidResult(): Boolean {
    return myIsValid
  }

  companion object {
    val EMPTY_ARRAY = arrayOf<PowerShellResolveResult>()
  }
}