package com.intellij.plugin.powershell.psi

/**
 * Andrey 18/08/17.
 */
interface PowerShellVariable : PowerShellQualifiedElement, PowerShellComponent, PowerShellReference {
  override fun getReference(): PowerShellReference?
  fun getNamespace(): String?
  fun getPrefix(): String
  fun getSuffix(): String?
}