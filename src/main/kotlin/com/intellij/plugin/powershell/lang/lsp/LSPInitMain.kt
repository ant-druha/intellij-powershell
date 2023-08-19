/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.ide.run.findPsExecutable
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.EditorLSPListener
import com.intellij.plugin.powershell.lang.lsp.languagehost.EditorServicesLanguageHostStarter
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageServerEndpoint
import com.intellij.plugin.powershell.lang.lsp.languagehost.ServerStatus
import com.intellij.plugin.powershell.lang.lsp.languagehost.terminal.PowerShellConsoleTerminalRunner
import com.intellij.plugin.powershell.lang.lsp.util.isRemotePath
import com.intellij.psi.PsiDocumentManager
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@State(name = "PowerShellSettings", storages = [Storage(file = "powerShellSettings.xml", roamingType = RoamingType.DISABLED)])
class LSPInitMain : PersistentStateComponent<LSPInitMain.PowerShellInfo>, Disposable {

  override fun initializeComponent() {
    EditorFactory.getInstance().addEditorFactoryListener(EditorLSPListener(), this)
    ApplicationManager.getApplication().messageBus.connect(this)
      .subscribe<ProjectManagerListener>(ProjectManager.TOPIC, object : ProjectManagerListener {
        override fun projectClosed(project: Project) {
          psEditorLanguageServer.remove(project)
          psConsoleLanguageServer.remove(project)
        }
      })

    Disposer.register(ApplicationManager.getApplication(), this)

    LOG.debug("PluginMain init finished")
  }

  data class PowerShellInfo(
    var editorServicesStartupScript: String = "",
    var powerShellExePath: String? = null,
    var powerShellVersion: String? = null,
    var powerShellExtensionPath: String? = null,
    var editorServicesModuleVersion: String? = null,
    var isUseLanguageServer: Boolean = true
  ) {
    constructor(isUseLanguageServer: Boolean, powerShellExePath: String?) :
      this("", powerShellExePath, null, null, null, isUseLanguageServer)
  }

  fun getPowerShellExecutable(): String {
    val psExecutable = myPowerShellInfo.powerShellExePath ?: findPsExecutable()
    myPowerShellInfo.powerShellExePath = psExecutable
    return psExecutable;
  }

  private var myPowerShellInfo: PowerShellInfo = PowerShellInfo()

  override fun loadState(powerShellInfo: PowerShellInfo) {
    myPowerShellInfo = powerShellInfo
  }

  override fun getState(): PowerShellInfo {
    return myPowerShellInfo
  }

  companion object {
    private val LOG: Logger = Logger.getInstance(LSPInitMain::class.java)
    private val psEditorLanguageServer = ConcurrentHashMap<Project, LanguageServerEndpoint>()
    private val psConsoleLanguageServer = ConcurrentHashMap<Project, LanguageServerEndpoint>()

    fun getServerWithConsoleProcess(project: Project): LanguageServerEndpoint {
      return psConsoleLanguageServer.computeIfAbsent(project) { LanguageServerEndpoint(PowerShellConsoleTerminalRunner(it), it) }
    }

    private fun getEditorLanguageServer(project: Project): LanguageServerEndpoint {
      return psEditorLanguageServer.computeIfAbsent(project) { LanguageServerEndpoint(EditorServicesLanguageHostStarter(it), it) }
    }

    fun editorOpened(editor: Editor) {
      val lspMain = ApplicationManager.getApplication().getComponent(LSPInitMain::class.java)
      if (!lspMain.myPowerShellInfo.isUseLanguageServer) return

      val project = editor.project ?: return
      val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
      if (file == null || file.fileType !is PowerShellFileType && !isRemotePath(file.virtualFile?.path)) return
      ApplicationManager.getApplication().executeOnPooledThread {
        val server = getServer(file.virtualFile, project)
        server.connectEditor(editor)
        LOG.info("Registered ${file.virtualFile.path} script for server: $server")
      }
    }

    private fun getServer(file: VirtualFile, project: Project): LanguageServerEndpoint {
      return findServerForRemoteFile(file, project) ?: getEditorLanguageServer(project)
    }

    fun editorClosed(editor: Editor) {
      val project = editor.project ?: return
      val vfile = FileDocumentManager.getInstance().getFile(editor.document) ?: return
      if (!project.isDisposed) {
        val file = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
        if (file?.fileType !is PowerShellFileType) return
      }
      val server = findServer(vfile, project) ?: return
      server.disconnectEditor(VfsUtil.toUri(File(vfile.path)))
      LOG.debug("Removed ${vfile.name} script from server: $server")
    }

    private fun findServer(file: VirtualFile, project: Project): LanguageServerEndpoint? {
      return findServerForRemoteFile(file, project) ?: psEditorLanguageServer[project]
    }

    private fun findServerForRemoteFile(file: VirtualFile, project: Project): LanguageServerEndpoint? {
      val consoleServer = psConsoleLanguageServer[project] ?: return null
      return if (consoleServer.getStatus() == ServerStatus.STARTED && isRemotePath(file.canonicalPath)) consoleServer else null
    }

  }

  override fun dispose() {
    psEditorLanguageServer.clear()
    psConsoleLanguageServer.clear()
  }
}