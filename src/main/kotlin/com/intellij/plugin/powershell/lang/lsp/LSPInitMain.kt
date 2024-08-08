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
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.ide.PluginProjectRoot
import com.intellij.plugin.powershell.ide.run.findPsExecutable
import com.intellij.plugin.powershell.lang.lsp.languagehost.EditorServicesLanguageHostStarter
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageServerEndpoint
import com.intellij.plugin.powershell.lang.lsp.languagehost.terminal.PowerShellConsoleTerminalRunner
import com.intellij.plugin.powershell.lang.lsp.util.isRemotePath
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@State(name = "PowerShellSettings", storages = [Storage(value = "powerShellSettings.xml", roamingType = RoamingType.DISABLED)])
class LSPInitMain : PersistentStateComponent<LSPInitMain.PowerShellInfo>, Disposable {

  private val psEditorLanguageServer = ConcurrentHashMap<Project, LanguageServerEndpoint>()
  private val psConsoleLanguageServer = ConcurrentHashMap<Project, LanguageServerEndpoint>()

  override fun initializeComponent() {
    ApplicationManager.getApplication().messageBus.connect(this)
      .subscribe<ProjectManagerListener>(ProjectManager.TOPIC, object : ProjectManagerListener {
        override fun projectClosed(project: Project) {
          psEditorLanguageServer.remove(project)
          psConsoleLanguageServer.remove(project)
        }
      })

    LOG.debug("PluginMain init finished")
  }

  data class PowerShellInfo(
    var editorServicesStartupScript: String = "",
    var powerShellExePath: String? = null,
    var powerShellVersion: String? = null,
    var powerShellExtensionPath: String? = null,
    var editorServicesModuleVersion: String? = null,
    var isUseLanguageServer: Boolean = true
  )

  fun getPowerShellExecutable(): String {
    val psExecutable = myPowerShellInfo.powerShellExePath ?: findPsExecutable()
    myPowerShellInfo.powerShellExePath = psExecutable
    return psExecutable
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

    @JvmStatic
    fun getInstance(): LSPInitMain {
      return ApplicationManager.getApplication().getService(LSPInitMain::class.java)
    }

    fun getServerWithConsoleProcess(project: Project): LanguageServerEndpoint {
      return getInstance().psConsoleLanguageServer.computeIfAbsent(project) { prj ->
        val scope = PluginProjectRoot.getInstance(project).coroutineScope
        LanguageServerEndpoint(scope, PowerShellConsoleTerminalRunner(prj), prj)
      }
    }

    fun getEditorLanguageServer(project: Project): LanguageServerEndpoint {
      return getInstance().psEditorLanguageServer.computeIfAbsent(project) { prj ->
        val scope = PluginProjectRoot.getInstance(project).coroutineScope
        LanguageServerEndpoint(scope, EditorServicesLanguageHostStarter(prj), prj)
      }
    }

    fun editorOpened(editor: Editor) {
      val lspMain = getInstance()
      if (!lspMain.myPowerShellInfo.isUseLanguageServer) return

      val project = editor.project ?: return
      val file = FileDocumentManager.getInstance().getFile(editor.document)
      if (file == null || file.fileType !is PowerShellFileType && !isRemotePath(file.path)) return
      ApplicationManager.getApplication().executeOnPooledThread {
        val server = getServer(file, project)
        server.connectEditor(editor)
        LOG.info("Registered ${file.path} script for server: $server")
      }
    }

    private fun getServer(file: VirtualFile, project: Project): LanguageServerEndpoint {
      return findServerForRemoteFile(file, project) ?: getEditorLanguageServer(project)
    }

    fun editorClosed(editor: Editor) {
      val project = editor.project ?: return
      val vfile = FileDocumentManager.getInstance().getFile(editor.document) ?: return
      if (vfile.fileType !is PowerShellFileType) return
      val server = findServer(vfile, project) ?: return
      server.disconnectEditor(VfsUtil.toUri(File(vfile.path)))
      LOG.debug("Removed ${vfile.name} script from server: $server")
    }

    private fun findServer(file: VirtualFile, project: Project): LanguageServerEndpoint? {
      return findServerForRemoteFile(file, project) ?: getInstance().psEditorLanguageServer[project]
    }

    private fun findServerForRemoteFile(file: VirtualFile, project: Project): LanguageServerEndpoint? {
      val consoleServer = getInstance().psConsoleLanguageServer[project] ?: return null
      return if (consoleServer.isRunning && isRemotePath(file.canonicalPath)) consoleServer else null
    }
  }

  override fun dispose() {
    psEditorLanguageServer.clear()
    psConsoleLanguageServer.clear()
  }
}
