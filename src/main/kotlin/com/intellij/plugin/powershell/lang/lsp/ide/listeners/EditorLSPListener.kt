/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.ide.listeners

import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain

class EditorLSPListener : EditorFactoryListener {

  override fun editorReleased(event: EditorFactoryEvent) {
    LSPInitMain.editorClosed(event.editor)
  }

  override fun editorCreated(event: EditorFactoryEvent) {
    LSPInitMain.editorOpened(event.editor)
  }
}