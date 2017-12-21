package com.intellij.plugin.powershell.lang.resolve

import com.intellij.plugin.powershell.psi.PowerShellExpression
import com.intellij.plugin.powershell.psi.PowerShellReferenceTypeElement
import com.intellij.plugin.powershell.psi.PowerShellTargetVariableExpression
import com.intellij.plugin.powershell.psi.PowerShellTypeLiteralExpression
import com.intellij.plugin.powershell.psi.types.PowerShellType

object PowerShellTypeUtil {

//  fun createType(expression: PowerShellExpression): PowerShellType {
//    if (expression is PowerShellTypeLiteralExpression) {
//      val typeElement = expression.typeElement
//      if (typeElement is PowerShellReferenceTypeElement) {
//        return PowerShellReferenceClassTypeImpl(typeElement)
//
//      }
//    }
//    return PowerShellType.UNNAMED
//  }

  fun inferExpressionType(e: PowerShellExpression): PowerShellType {
    when (e) {
      is PowerShellTypeLiteralExpression -> {
//        return e.typeElement.getType() //SOE could happen if not reference
        val typE = e.typeElement //SOE could happen if not reference
        if (typE is PowerShellReferenceTypeElement) return typE.getType()
      }
      is PowerShellTargetVariableExpression -> {
        val resolved = e.resolve()
        if (resolved is PowerShellTargetVariableExpression) {
          return resolved.getType() //
        }
      }
    }
    return PowerShellType.UNKNOWN
  }
}