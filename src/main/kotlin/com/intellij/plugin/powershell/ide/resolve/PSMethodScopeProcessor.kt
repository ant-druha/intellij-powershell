package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.PowerShellConstructorDeclarationStatement
import com.intellij.plugin.powershell.psi.PowerShellMemberDeclaration
import com.intellij.plugin.powershell.psi.PowerShellMethodDeclarationStatement
import com.intellij.psi.ResolveState

/**
 * Andrey 19/12/17.
 */
class PSMethodScopeProcessor(private val myName: String) : PowerShellMemberScopeProcessor() {

  override fun doExecute(psMember: PowerShellMemberDeclaration, state: ResolveState): Boolean {
    if (psMember is PowerShellMethodDeclarationStatement) {
      if (myName.equals((psMember as PowerShellMemberDeclaration).name, true)) {
        myResult.add(PowerShellResolveResult(psMember))
        return false
      }
    }
    if (PsNames.CONSTRUCTOR_CALL.equals(myName, true)) {
      val memberName = psMember.name
      val clazz = psMember.getContainingClass()
      if (memberName != null && memberName.equals(clazz?.name, true)) {
        assert(psMember is PowerShellConstructorDeclarationStatement)
        myResult.add(PowerShellResolveResult(psMember))
        return false
      }
    }
    return true
  }


}