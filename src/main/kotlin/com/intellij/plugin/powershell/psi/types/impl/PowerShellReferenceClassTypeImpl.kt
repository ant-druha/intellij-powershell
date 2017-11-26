package com.intellij.plugin.powershell.psi.types.impl

import com.intellij.plugin.powershell.lang.resolve.PowerShellTypeReference
import com.intellij.plugin.powershell.psi.types.PowerShellReferenceClassType

class PowerShellReferenceClassTypeImpl(val myReference: PowerShellTypeReference ) : PowerShellReferenceClassType {
  override fun getReference(): PowerShellTypeReference = myReference

  override fun getReferenceName(): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getName(): String {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}