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
  fun createIdentifierFromText(project: Project, text: String): PowerShellIdentifier? {
    val file = createFile(project, text)
    return PsiTreeUtil.findChildOfAnyType(file, PowerShellIdentifier::class.java)
  }

  private fun createFile(project: Project, text: String): PowerShellFile {
    val name = "dummy_file" + PowerShellFileType.INSTANCE.defaultExtension
    val stamp = System.currentTimeMillis()
    val factory = PsiFileFactory.getInstance(project)
    return factory.createFileFromText(name, PowerShellFileType.INSTANCE, text, stamp, false) as PowerShellFile
  }
}