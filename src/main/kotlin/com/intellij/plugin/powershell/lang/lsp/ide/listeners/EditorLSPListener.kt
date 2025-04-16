/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.ide.listeners

import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.plugin.powershell.lang.lsp.LanguageServer

class EditorLSPListener : EditorFactoryListener {

  override fun editorReleased(event: EditorFactoryEvent) {
    LanguageServer.editorClosed(event.editor)
  }

  override fun editorCreated(event: EditorFactoryEvent) {
    LanguageServer.editorOpened(event.editor)
  }
}
