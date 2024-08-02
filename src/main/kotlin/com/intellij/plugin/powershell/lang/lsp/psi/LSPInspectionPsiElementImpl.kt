package com.intellij.plugin.powershell.lang.lsp.psi

import com.intellij.lang.Language
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.FakePsiElement

class LSPInspectionPsiElementImpl(private val myContainingFile: PsiFile, private val myRange: TextRange) : FakePsiElement(), LSPInspectionPsiElement {

  constructor(parent: PsiFile, range: TextRange, name: String) : this(parent, range) {
    myName = name
  }

  override fun getContainingFile(): PsiFile {
    return myContainingFile
  }
  override fun getLanguage(): Language= PowerShellLanguage.INSTANCE

  override fun getTextRange(): TextRange =  myRange

  override fun isPhysical(): Boolean = true

  private var myName: String? = null

  override fun toString(): String {
    return "Inspection LSP element. Text: $text; File: ${myContainingFile.name}"
  }

  override fun getPresentation(): ItemPresentation {
    return this
  }

  override fun getName(): String? {
    return myName ?: super.getName()
  }

  override fun getParent(): PsiElement = myContainingFile
}
