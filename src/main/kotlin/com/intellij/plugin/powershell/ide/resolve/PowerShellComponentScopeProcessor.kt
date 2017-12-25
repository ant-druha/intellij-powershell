package com.intellij.plugin.powershell.ide.resolve

import com.intellij.plugin.powershell.psi.PowerShellComponent
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.scope.BaseScopeProcessor
import java.util.HashMap
import kotlin.collections.ArrayList

/**
 * Andrey 18/08/17.
 */
class PowerShellComponentScopeProcessor : BaseScopeProcessor() {
  private val myResult: ArrayList<PowerShellComponent> = ArrayList()
  private val myCollectedVariables = HashMap<String, PowerShellTargetVariableExpression>()

  override fun execute(element: PsiElement, state: ResolveState): Boolean {
    if (element is PowerShellTargetVariableExpression) {
      val name = element.getQualifiedName()
      val oldVar = myCollectedVariables[name]
      if (oldVar == null || oldVar.textOffset > element.textOffset) {
        myCollectedVariables.put(name, element)
      } else if (element.containingFile !== oldVar.containingFile) {
        myCollectedVariables.put(name, element)//should not happen if the file is the same
        // if there already variable with the same name defined it should be located in the same local context
        throw AssertionError("Elements are defined in different files")
      }
    } else if (element is PowerShellComponent) {
      myResult.add(element)
    }
    return true
  }

  fun getResult(): List<PowerShellComponent> {
    myResult.addAll(myCollectedVariables.values)
    return myResult
  }

}