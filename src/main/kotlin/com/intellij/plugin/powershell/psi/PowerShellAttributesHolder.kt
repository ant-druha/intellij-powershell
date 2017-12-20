package com.intellij.plugin.powershell.psi

interface PowerShellAttributesHolder {
  fun getAttributeList(): List<PowerShellAttribute>
}