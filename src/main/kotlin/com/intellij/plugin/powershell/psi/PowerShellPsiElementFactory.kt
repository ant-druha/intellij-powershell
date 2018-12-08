package com.intellij.plugin.powershell.psi

import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.psi.impl.PowerShellFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil

/**
 * Andrey 16/08/17.
 */
object PowerShellPsiElementFactory {
  fun createIdentifierFromText(project: Project, text: String, functionId: Boolean = false): PowerShellIdentifier? {
    val fileText = if (functionId) {
      "function $text {}"
    } else {
      "class $text {}"
    }
    val file = createFile(project, fileText)
    val component = PsiTreeUtil.findChildOfAnyType(file, PowerShellComponent::class.java)
    return component?.nameIdentifier
  }

  fun createCommandName(project: Project, text: String): PowerShellCommandName? {
    val file = createFile(project, text)
    val component = PsiTreeUtil.findChildOfAnyType(file, PowerShellCommandCallExpression::class.java)
    return component?.commandName
  }

  fun createVariableFromText(project: Project, text: String, bracedVariable: Boolean = false): PowerShellVariable? {
    val varText = if (bracedVariable) "\${$text}" else "$$text"
    val file = createFile(project, varText)
    return PsiTreeUtil.findChildOfAnyType(file, PowerShellTargetVariableExpression::class.java)
  }

  private fun createFile(project: Project, text: String): PowerShellFile {
    val name = "dummy_file" + PowerShellFileType.INSTANCE.defaultExtension
    val stamp = System.currentTimeMillis()
    val factory = PsiFileFactory.getInstance(project)
    return factory.createFileFromText(name, PowerShellFileType.INSTANCE, text, stamp, false) as PowerShellFile
  }

  fun createExpression(project: Project, text: String): PowerShellExpression {
    val file = createFile(project, "\$foo = $text")
    val assignment = PsiTreeUtil.findChildOfType(file, PowerShellAssignmentExpression::class.java) ?: error("text='$text'")
    return assignment.rhsElement as? PowerShellExpression ?: error("text='$text'")
  }
}