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
import com.intellij.plugin.powershell.lang.lsp.languagehost.EditorServicesLanguageHostStarter
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageServerEndpoint
import com.intellij.plugin.powershell.lang.lsp.languagehost.ServerStatus
import com.intellij.plugin.powershell.lang.lsp.languagehost.terminal.PowerShellConsoleTerminalRunner
import com.intellij.plugin.powershell.lang.lsp.util.isRemotePath
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

@State(name = "PowerShellSettings", storages = [Storage(file = "powerShellSettings.xml", roamingType = RoamingType.DISABLED)])
class LSPInitMain : ApplicationComponent, PersistentStateComponent<LSPInitMain.PowerShellExtensionInfo> {

  data class PowerShellExtensionInfo(var editorServicesStartupScript: String = "",
                                     var powerShellExtensionPath: String? = null,
                                     var editorServicesModuleVersion: String? = null,
                                     var isUseLanguageServer: Boolean = true) {
    constructor(isUseLanguageServer: Boolean) : this("", null, null, isUseLanguageServer)
  }

  private var myPowerShellExtensionInfo: PowerShellExtensionInfo = PowerShellExtensionInfo()
  private val myDisposable = Disposer.newDisposable()

  override fun loadState(powerShellExtensionInfo: PowerShellExtensionInfo) {
    myPowerShellExtensionInfo = powerShellExtensionInfo
  }

  override fun getState(): LSPInitMain.PowerShellExtensionInfo {
    return myPowerShellExtensionInfo
  }

  fun getPowerShellInfo(): LSPInitMain.PowerShellExtensionInfo {
    return myPowerShellExtensionInfo
  }

  fun setPSExtensionInfo(info: PowerShellExtensionInfo?) {
    myPowerShellExtensionInfo = info ?: PowerShellExtensionInfo()
  }

  companion object {
    private val LOG: Logger = Logger.getInstance(LSPInitMain::class.java)
    private val psEditorLanguageServer = mutableMapOf<Project, LanguageServerEndpoint>()
    private var psConsoleLanguageServer = mutableMapOf<Project, LanguageServerEndpoint>()

    fun getServerWithConsoleProcess(project: Project): LanguageServerEndpoint {
      synchronized(psConsoleLanguageServer) {
        var server = psConsoleLanguageServer[project]
        if (server == null) {
          server = LanguageServerEndpoint(PowerShellConsoleTerminalRunner(project), project)
          psConsoleLanguageServer[project] = server
        }
        return server
      }
    }

    private fun getEditorLanguageServer(project: Project): LanguageServerEndpoint {
      synchronized(psEditorLanguageServer) {
        var server = psEditorLanguageServer[project]
        if (server == null) {
          server = LanguageServerEndpoint(EditorServicesLanguageHostStarter(project), project)
          psEditorLanguageServer[project] = server
        }
        return server
      }
    }

    fun editorOpened(editor: Editor) {
      val lspMain = ApplicationManager.getApplication().getComponent(LSPInitMain::class.java)
      if (!lspMain.myPowerShellExtensionInfo.isUseLanguageServer) return

      val project = editor.project ?: return
      val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
      if (file == null || file.fileType !is PowerShellFileType && !isRemotePath(file.virtualFile?.path)) return
      ApplicationManager.getApplication().executeOnPooledThread {
        val server = getServer(file, project)
        server.connectEditor(editor)
        LOG.info("Registered ${file.virtualFile.path} script for server: $server")
      }
    }

    private fun getServer(file: PsiFile, project: Project): LanguageServerEndpoint {
      return findServerForRemoteFile(file, project) ?: getEditorLanguageServer(project)
    }

    fun editorClosed(editor: Editor) {
      val project = editor.project ?: return
      val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
      if (file?.fileType !is PowerShellFileType) return
      val server = findServer(file, project) ?: return
      server.disconnectEditor(VfsUtil.toUri(file.virtualFile))
      LOG.debug("Removed ${file.name} script from server: $server")
    }

    private fun findServer(file: PsiFile, project: Project): LanguageServerEndpoint? {
      return findServerForRemoteFile(file, project) ?: psEditorLanguageServer[project]
    }

    private fun findServerForRemoteFile(file: PsiFile, project: Project): LanguageServerEndpoint? {
      val consoleServer = psConsoleLanguageServer[project]
      return if (consoleServer?.getStatus() == ServerStatus.STARTED && isRemotePath(file.virtualFile.canonicalPath)) consoleServer else null
    }

  }

  override fun initComponent() {
    EditorFactory.getInstance().addEditorFactoryListener(EditorLSPListener(), myDisposable)
    LOG.debug("PluginMain init finished")
  }

  override fun disposeComponent() {
    super.disposeComponent()
    psEditorLanguageServer.clear()
    Disposer.dispose(myDisposable)
  }
}