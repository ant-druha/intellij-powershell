package com.intellij.plugin.powershell.lang.lsp.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VfsUtil
import java.net.URI


fun editorToURIString(editor: Editor): String? {
  val file = FileDocumentManager.getInstance().getFile(editor.document)?: return null
  return VfsUtil.toUri(file).toString()
}

fun editorToURI(editor: Editor): URI? {
  val file = FileDocumentManager.getInstance().getFile(editor.document)?: return null
  return VfsUtil.toUri(file)
}