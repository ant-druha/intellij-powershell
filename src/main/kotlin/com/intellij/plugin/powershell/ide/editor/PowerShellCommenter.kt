package com.intellij.plugin.powershell.ide.editor

import com.intellij.lang.CodeDocumentationAwareCommenter
import com.intellij.psi.PsiComment
import com.intellij.psi.tree.IElementType

/**
 * Andrey 15/08/17.
 */
class PowerShellCommenter : CodeDocumentationAwareCommenter {
  override fun getLineCommentTokenType(): IElementType? {
    return null
  }

  override fun getBlockCommentTokenType(): IElementType? {
    return null
  }

  override fun getDocumentationCommentTokenType(): IElementType? {
    return null
  }

  override fun getDocumentationCommentPrefix(): String? {
    return null
  }

  override fun getDocumentationCommentLinePrefix(): String? {
    return null
  }

  override fun getDocumentationCommentSuffix(): String? {
    return null
  }

  override fun isDocumentationComment(element: PsiComment): Boolean {
    return false
  }

  override fun getLineCommentPrefix(): String? {
    return "#"
  }

  override fun getBlockCommentPrefix(): String? {
    return "<#"
  }

  override fun getBlockCommentSuffix(): String? {
    return "#>"
  }

  override fun getCommentedBlockCommentPrefix(): String? {
    return null
  }

  override fun getCommentedBlockCommentSuffix(): String? {
    return null
  }

}