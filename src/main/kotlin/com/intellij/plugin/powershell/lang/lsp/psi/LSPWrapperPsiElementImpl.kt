package com.intellij.plugin.powershell.lang.lsp.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.ide.search.PowerShellComponentType
import com.intellij.plugin.powershell.psi.PowerShellIdentifier
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.FakePsiElement
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionItemKind
import javax.swing.Icon

class LSPWrapperPsiElementImpl(private val myName: String, private val myParent: PsiElement, kind: CompletionItemKind?) : FakePsiElement(), LSPWrapperPsiElement {
  private val myKind: CompletionItemKind = kind ?: CompletionItemKind.Text
  private var myType: PowerShellComponentType = PowerShellComponentType.typeOf(this) ?: PowerShellComponentType.TEXT
  private var myCompletionItem: CompletionItem? = null

  constructor(completionItem: CompletionItem, parent: PsiElement) : this(if (StringUtil.isEmpty(completionItem.label)) completionItem.insertText else completionItem.label, parent, completionItem.kind) {
    myCompletionItem = completionItem
  }

  override fun getCompletionKind(): CompletionItemKind = myKind

  override fun getType(): PowerShellComponentType = myType

  override fun setType(type: PowerShellComponentType) {
    myType = type
  }

  override fun getCompletionItem(): CompletionItem? = myCompletionItem

  override fun getDocumentation(): String? {
    val doc = myCompletionItem?.documentation
    return if (StringUtil.isEmpty(doc)) myCompletionItem?.detail else doc
  }

  override fun getNameIdentifier(): PowerShellIdentifier? = null

  override fun getPresentation(): ItemPresentation {
    return object : ItemPresentation {
      override fun getLocationString(): String? = null
      override fun getIcon(unused: Boolean): Icon = getType().getIcon()
      override fun getPresentableText(): String? = name
    }
  }

  override fun getName(): String? = myName
  override fun getParent(): PsiElement? = myParent

}