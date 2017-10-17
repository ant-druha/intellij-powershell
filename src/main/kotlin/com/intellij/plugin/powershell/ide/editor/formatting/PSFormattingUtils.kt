package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.lang.ASTNode
import com.intellij.plugin.powershell.psi.PowerShellPsiUtil

object PSFormattingUtils {
  fun canAddSpace(node: ASTNode): Boolean =
      !PowerShellPsiUtil.isLabel(node) && !PowerShellPsiUtil.isVariableScope(node) && !PowerShellPsiUtil.isPathExpression(node)

}