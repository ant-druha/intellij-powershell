package com.intellij.plugin.powershell.psi.impl

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.plugin.powershell.psi.PwShellPsiElement
import com.intellij.psi.FileViewProvider

/**
 * Andrey 17/07/17.
 */
class PowerShellFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PowerShellLanguage.INSTANCE), PwShellPsiElement {
    override fun getFileType(): FileType {
        return PowerShellFileType()
    }
}