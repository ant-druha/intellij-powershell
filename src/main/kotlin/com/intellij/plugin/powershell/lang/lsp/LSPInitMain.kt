/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.EditorLSPListener
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageServerEndpoint
import com.intellij.psi.PsiDocumentManager

@State(name = "PowerShellSettings", storages = arrayOf(Storage(file = "powerShellSettings.xml", roamingType = RoamingType.PER_OS)))
class LSPInitMain : ApplicationComponent, PersistentStateComponent<LSPInitMain.State> {

  data class State(var editorServicesStartupScript: String = "")

  private var myState: State? = null
  private val myDisposable = Disposer.newDisposable()

  override fun loadState(state: State) {
    myState = state
  }

  override fun getState(): LSPInitMain.State {
    return myState ?: LSPInitMain.State("")
  }

  fun setPSEditorServicesPath(path: String) {
    myState = LSPInitMain.State(path)
  }

  companion object {
    private val LOG: Logger = Logger.getInstance(LSPInitMain::class.java)
    private val projectToLanguageServer = mutableMapOf<Project, LanguageServerEndpoint>()

    private fun getServer(project: Project): LanguageServerEndpoint {
      synchronized(this, {
        var server = projectToLanguageServer[project]
        if (server == null) {
          server = LanguageServerEndpoint(project)
          projectToLanguageServer[project] = server
        }
        return server
      })
    }

    fun editorOpened(editor: Editor) {
      val project = editor.project ?: return
      val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
      if (file?.fileType !is PowerShellFileType) return
      ApplicationManager.getApplication().executeOnPooledThread {
        val server = getServer(project)
        server.connectEditor(editor)
        LOG.info("Registered ${file.name} script for PowerShell language host service.")
      }
    }

    fun editorClosed(editor: Editor) {
      val project = editor.project ?: return
      val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
      if (file?.fileType !is PowerShellFileType) return
      val server = projectToLanguageServer[project] ?: return
      server.disconnectEditor(VfsUtil.toUri(file.virtualFile))
      LOG.info("Removed ${file.name} script from PowerShell language host service.")
    }

  }

  override fun initComponent() {
    EditorFactory.getInstance().addEditorFactoryListener(EditorLSPListener(), myDisposable)
    LOG.info("PluginMain init finished")
  }

  override fun disposeComponent() {
    super.disposeComponent()
    projectToLanguageServer.clear()
    Disposer.dispose(myDisposable)
  }
}