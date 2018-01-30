package com.intellij.plugin.powershell.lang.lsp.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.net.URI


fun editorToURIString(editor: Editor): String? {
  val file = FileDocumentManager.getInstance().getFile(editor.document)?: return null
  return VfsUtil.toUri(file).toString()
}

fun editorToURI(editor: Editor): URI? {
  val file = FileDocumentManager.getInstance().getFile(editor.document)?: return null
  return VfsUtil.toUri(file)
}

fun getTextEditor(file: VirtualFile, project: Project): Editor? {
  return FileEditorManager.getInstance(project).getAllEditors(file).filterIsInstance<TextEditor>().map { e -> e.editor }.firstOrNull()
}