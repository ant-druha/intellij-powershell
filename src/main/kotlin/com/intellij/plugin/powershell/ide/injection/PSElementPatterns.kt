package com.intellij.plugin.powershell.ide.injection

import com.intellij.patterns.InitialPatternCondition
import com.intellij.patterns.PsiElementPattern
import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression

open class PSElementPatterns<T : PowerShellStringLiteralExpression, Self : PSElementPatterns<T, Self>>(condition: InitialPatternCondition<T>) : PsiElementPattern<T, Self>(condition) {

  class Capture<T : PowerShellStringLiteralExpression>(condition: InitialPatternCondition<T>) : PSElementPatterns<T, Capture<T>>(condition)

}