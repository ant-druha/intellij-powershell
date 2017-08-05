package com.intellij.plugin.powershell.psi.impl

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.plugin.powershell.psi.PowerShellPsiElement
import com.intellij.psi.FileViewProvider

/**
 * Andrey 17/07/17.
 */
class PowerShellFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PowerShellLanguage.INSTANCE), PowerShellPsiElement {
    override fun getFileType(): FileType {
        return PowerShellFileType()
    }
}