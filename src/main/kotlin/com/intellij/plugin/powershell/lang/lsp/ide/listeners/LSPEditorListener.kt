/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.ide.listeners

import com.intellij.openapi.diagnostic.Logger
import com.intellij.plugin.powershell.lang.lsp.ide.EditorEventManager

open class LSPEditorListener {
  protected var editorManager: EditorEventManager? = null
  private val LOG: Logger = Logger.getInstance(javaClass)

  fun setManager(manager: EditorEventManager) {
    this.editorManager = manager
  }

  protected fun checkManager(): Boolean {
    return if (editorManager == null) {
      LOG.error("Manager is null")
      false
    } else true
  }
}