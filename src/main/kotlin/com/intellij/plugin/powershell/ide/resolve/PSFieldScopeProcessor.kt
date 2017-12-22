package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.PowerShellEnumLabelDeclaration
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration
import com.intellij.plugin.powershell.psi.PowerShellMethodDeclarationStatement
import com.intellij.plugin.powershell.psi.PowerShellPropertyDeclarationStatement
import com.intellij.psi.ResolveState

/**
 * Andrey 19/12/17.
 */
class PSFieldScopeProcessor(name: String) : PowerShellMemberScopeProcessor(name) {

  override fun doExecute(psMember: PowerShellMemberDeclaration, state: ResolveState): Boolean {
    if (psMember is PowerShellPropertyDeclarationStatement || psMember is PowerShellEnumLabelDeclaration
        || psMember is PowerShellMethodDeclarationStatement) {

      if (myName.equals(psMember.name, true)) {
        myResult.add(PowerShellResolveResult(psMember))
      }
    }
    return true
  }


}