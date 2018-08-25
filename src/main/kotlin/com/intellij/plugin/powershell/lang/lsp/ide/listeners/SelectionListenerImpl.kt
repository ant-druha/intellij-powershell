/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.ide.listeners

import com.intellij.openapi.editor.event.SelectionEvent
import com.intellij.openapi.editor.event.SelectionListener

class SelectionListenerImpl : LSPEditorListener(), SelectionListener {
  override fun selectionChanged(e: SelectionEvent?) {

  }
}