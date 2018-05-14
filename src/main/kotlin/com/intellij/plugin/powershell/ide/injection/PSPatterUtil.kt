package com.intellij.plugin.powershell.ide.injection

import com.intellij.patterns.InitialPatternCondition
import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression
import com.intellij.util.ProcessingContext

object PSPatternUtil {

  fun psCapture(): PSElementPatterns.Capture<PowerShellStringLiteralExpression> {
    val initPatternCond: InitialPatternCondition<PowerShellStringLiteralExpression> = object : InitialPatternCondition<PowerShellStringLiteralExpression>(PowerShellStringLiteralExpression::class.java) {
      override fun accepts(obj: Any?, context: ProcessingContext): Boolean {
        if (obj !is PowerShellStringLiteralExpression) return false
        return obj.text.contains(Regex("script type=\"text/javascript\""))
      }
    }
    return PSElementPatterns.Capture(initPatternCond)
  }

}