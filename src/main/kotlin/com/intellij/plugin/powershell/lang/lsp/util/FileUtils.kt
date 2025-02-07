package com.intellij.plugin.powershell.lang.lsp.util

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.plugin.powershell.ide.run.join

fun editorToURIString(editor: Editor): String? {
  val file = FileDocumentManager.getInstance().getFile(editor.document)?: return null
  return file.path.toNioPathOrNull()?.toUri()?.toString()
}

fun getTextEditor(file: VirtualFile, project: Project): Editor? {
  return FileEditorManager.getInstance(project).getAllEditors(file).filterIsInstance<TextEditor>().map { e -> e.editor }.firstOrNull()
}

fun isRemotePath(docPath: String?) = docPath != null && docPath.contains(REMOTE_FILES_DIR_PREFIX)

private val REMOTE_FILES_DIR_PREFIX = join(System.getProperty("java.io.tmpdir"), "PSES-")
