/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.ide.PluginProjectRoot
import com.intellij.plugin.powershell.lang.lsp.languagehost.EditorServicesLanguageHostStarter
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageServerEndpoint
import com.intellij.plugin.powershell.lang.lsp.languagehost.terminal.PowerShellConsoleTerminalRunner
import com.intellij.plugin.powershell.lang.lsp.util.isRemotePath

@Service(Service.Level.PROJECT)
class LanguageServer(private val project: Project) {
  companion object {
    private val LOG: Logger = logger<LanguageServer>()

    fun getInstance(project: Project): LanguageServer {
      return project.service<LanguageServer>()
    }

    fun editorOpened(editor: Editor) {
      if (!PowerShellSettings.getInstance().state.isUseLanguageServer) return

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
      return findServerForRemoteFile(file, project) ?: getInstance(project).editorLanguageServer.value
    }

    fun editorClosed(editor: Editor) {
      val project = editor.project ?: return
      val vfile = FileDocumentManager.getInstance().getFile(editor.document) ?: return
      if (vfile.fileType !is PowerShellFileType) return
      val server = findServer(vfile, project) ?: return
      server.disconnectEditor(vfile.path.toNioPathOrNull()?.toUri() ?: return)
      LOG.debug("Removed ${vfile.name} script from server: $server")
    }

    private fun findServer(file: VirtualFile, project: Project): LanguageServerEndpoint? {
      return findServerForRemoteFile(file, project)
        ?: getInstance(project).editorLanguageServer.takeIf { it.isInitialized() }?.value
    }

    private fun findServerForRemoteFile(file: VirtualFile, project: Project): LanguageServerEndpoint? {
      val consoleServer = getInstance(project).serverWithConsoleProcess.takeIf { it.isInitialized() }?.value ?: return null
      return if (consoleServer.isRunning && isRemotePath(file.canonicalPath)) consoleServer else null
    }
  }

  val serverWithConsoleProcess: Lazy<LanguageServerEndpoint> = lazy {
    val scope = PluginProjectRoot.getInstance(project).coroutineScope
    LanguageServerEndpoint(scope, PowerShellConsoleTerminalRunner(project), project)
  }

  val editorLanguageServer: Lazy<LanguageServerEndpoint> = lazy {
    val scope = PluginProjectRoot.getInstance(project).coroutineScope
    LanguageServerEndpoint(scope, EditorServicesLanguageHostStarter(project), project)
  }
}
