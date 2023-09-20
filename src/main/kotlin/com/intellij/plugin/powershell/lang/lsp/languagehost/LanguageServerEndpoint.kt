/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.ide.actions.ShowSettingsUtilImpl
import com.intellij.notification.*
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.removeUserData
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.plugin.powershell.PowerShellIcons
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.plugin.powershell.ide.PluginProjectDisposableRoot
import com.intellij.plugin.powershell.lang.lsp.client.PSLanguageClientImpl
import com.intellij.plugin.powershell.lang.lsp.ide.DEFAULT_DID_CHANGE_CONFIGURATION_PARAMS
import com.intellij.plugin.powershell.lang.lsp.ide.EditorEventManager
import com.intellij.plugin.powershell.lang.lsp.ide.LSPRequestManager
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.DocumentListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.EditorMouseListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.EditorMouseMotionListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.SelectionListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.settings.PowerShellConfigurable
import com.intellij.plugin.powershell.lang.lsp.util.getTextEditor
import com.intellij.plugin.powershell.lang.lsp.util.isRemotePath
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.launch.LSPLauncher
import org.eclipse.lsp4j.services.LanguageServer
import java.io.File
import java.net.URI
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class LanguageServerEndpoint(private val languageHostConnectionManager: LanguageHostConnectionManager, private val project: Project) : Disposable {
  private val LOG: Logger = Logger.getInstance(javaClass)
  private var client: PSLanguageClientImpl = PSLanguageClientImpl(project)
  private var languageServer: LanguageServer? = null
  private var initializeFuture: CompletableFuture<InitializeResult>? = null
  private var launcherFuture: Future<*>? = null
  private var initializeResult: InitializeResult? = null
  private val connectedEditors: MutableMap<URI, EditorEventManager> = HashMap()
  private val rootPath = project.basePath
  private var capabilitiesAlreadyRequested: Boolean = false
  @Volatile
  private var myStatus: AtomicReference<ServerStatus> = AtomicReference(ServerStatus.STOPPED)
  private var crashCount: Int = 0
  private var myFailedStarts = 0
  private val MAX_FAILED_STARTS = 2

  override fun toString(): String {
    val consoleString = if (isConsoleConnection()) " Console" else ""
    return "[PowerShell Editor Services host$consoleString connection, project: $rootPath]"
  }
//  private val dumpFile = File(FileUtil.toCanonicalPath(PSLanguageHostUtils.getLanguageHostLogsDir() + "/protocol_messages_IJ.log"))
//  private var fileWriter: PrintWriter? = null

  init {
    Disposer.register(PluginProjectDisposableRoot.getInstance(project), this)
  }

  companion object {
    val LANGUAGE_SERVER_ENDPOINT_KEY: Key<LanguageServerEndpoint> = Key.create("Powershell.LanguageServerEndpoint")
    private var installExtensionNotificationShown = false
    private var installPSNotificationShown = false
  }

  fun start() {
    val projectRoot = rootPath ?: project.name
    checkStarted(projectRoot)
    if (initializeFuture != null) {
      val capabilities = getServerCapabilities(projectRoot)
      if (capabilities != null) {
        initializeFuture?.thenRun {
          //todo move it to LanguageHostConnectionManager (notify it when server initialized)
          languageServer?.workspaceService?.didChangeConfiguration(DEFAULT_DID_CHANGE_CONFIGURATION_PARAMS)//notify Editor Services to start REPL loop
        }
      }
    }
  }

  fun connectEditor(editor: Editor?) {
    if (editor == null) {
      LOG.warn("Editor is null for $languageServer")
      return
    }
    val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return
    val uri = VfsUtil.toUri(File(file.path))
    if (!connectedEditors.contains(uri)) {
      checkStarted(editor.document.toString())
      if (initializeFuture != null) {
        val capabilities = getServerCapabilities(editor.document.toString())
        if (capabilities != null) {
          initializeFuture?.thenRun {
            try {
              if (!connectedEditors.contains(uri)) {
                val syncOptions: Either<TextDocumentSyncKind, TextDocumentSyncOptions> = capabilities.textDocumentSync
                val syncKind: TextDocumentSyncKind? = if (syncOptions.isRight) syncOptions.right.change else syncOptions.left
                val mouseListener = EditorMouseListenerImpl()
                val mouseMotionListener = EditorMouseMotionListenerImpl()
                val documentListener = DocumentListenerImpl()
                val selectionListener = SelectionListenerImpl()
                val server = languageServer
                if (server != null) {
                  val requestManager = LSPRequestManager(this, server, client, capabilities)
                  val serverOptions = ServerOptions(syncKind, capabilities.completionProvider, capabilities.signatureHelpProvider, capabilities.codeLensProvider, capabilities.documentOnTypeFormattingProvider, capabilities.documentLinkProvider, capabilities.executeCommandProvider)
                  val manager = EditorEventManager(project, editor, mouseListener, mouseMotionListener, documentListener, selectionListener, requestManager, serverOptions, this)
                  mouseListener.setManager(manager)
                  mouseMotionListener.setManager(manager)
                  documentListener.setManager(manager)
                  selectionListener.setManager(manager)
                  manager.registerListeners()
                  connectedEditors[uri] = manager
                  editor.putUserData(LANGUAGE_SERVER_ENDPOINT_KEY, this)
                  manager.documentOpened()
                  LOG.debug("Created manager for $uri")
                }
              }
            } catch (e: Exception) {
              LOG.error("Error when trying to initialize server capabilities: $e")
            }
          }
        } else {
          LOG.warn("Capabilities are null for $languageServer")
        }
      } else {
        LOG.warn("InitializeFuture is null for $languageServer")
      }
    }
  }

  private fun getServerCapabilities(documentPath: String): ServerCapabilities? {
    if (initializeResult != null) return initializeResult?.capabilities
    try {
      checkStarted(documentPath)
      initializeFuture?.get(if (capabilitiesAlreadyRequested) 0 else 9000 /*Timeout.INIT_TIMEOUT*/, TimeUnit.MILLISECONDS)
    } catch (e: Exception) {
      LOG.warn("PowerShell language host not initialized after 5s\nCheck settings", e)
      stop()
    }
    capabilitiesAlreadyRequested = true
    return if (initializeResult != null) initializeResult?.capabilities
    else null
  }

  override fun dispose() {
    stop()
  }

  private fun stop() {
    val copy = connectedEditors.toMap()
    for (e in copy) {
      disconnectEditor(e.key)
    }

    initializeFuture?.cancel(true)
    initializeFuture = null
    initializeResult = null
    capabilitiesAlreadyRequested = false
    launcherFuture?.cancel(false)
    launcherFuture = null

    try {
      val shutdown: CompletableFuture<Any>? = languageServer?.shutdown()
      shutdown?.get(100, TimeUnit.MILLISECONDS)//otherwise lsp4j stream IOException
      languageServer?.exit()
    } catch (ignored: Exception) {
      LOG.debug("PowerShell language host shutdown exception: $ignored")
    }
    languageServer = null
//    fileWriter?.close()
//    fileWriter = null
    destroyLanguageHostProcess()
    setStatus(ServerStatus.STOPPED)
    LOG.info("Connection to PowerShell language host closed for $rootPath.")
  }

  fun disconnectEditor(uri: URI) {
    val e = connectedEditors.remove(uri)
    if (e != null) {
      e.getEditor().removeUserData(LANGUAGE_SERVER_ENDPOINT_KEY)
      e.removeListeners()
      e.documentClosed()
    }
  }

  private val scheduleStartLock = Any()

  private fun checkStarted(path: String) {
    waitIfStarting(path)//When 'editor opened' notification comes but the server has not been started yet.
    synchronized(scheduleStartLock) {
      val oldStatus = getStatus()
      if (oldStatus == ServerStatus.STOPPED || shouldRetryFailedStart()) {
        //retry n attempts to start if failed
        var launched = scheduleStart()
        while (!launched && shouldRetryFailedStart()) {
          stop()
          launched = scheduleStart()
        }
      }
    }
  }

  private fun waitIfStarting(editorDocument: String) {
    var checkCount = 15
    try {
      LOG.debug("Waiting the language server for the '${project.name}' project to start to connect document: $editorDocument")
      while (getStatus() == ServerStatus.STARTING && checkCount > 0) {
        Thread.sleep(500)
        checkCount--
      }
      if (checkCount <= 0) {
        LOG.warn("Wait time elapsed for the server to start for project '${project.name}'")
      } else if (checkCount != 15) {
        LOG.debug("Language server started for the '${project.name}' project. Can proceed with document: $editorDocument")
      }
    } catch (e: Exception) {
      LOG.warn("Error while waiting the server for the '${project.name}' project to start: $e")
    }
  }

  private fun scheduleStart(): Boolean {
    setStatus(ServerStatus.STARTING)
    if (languageHostConnectionManager.isConnected()) destroyLanguageHostProcess()
    try {
      val (inStream, outStream) = languageHostConnectionManager.establishConnection() //long operation, suspends the thread
      if (inStream == null || outStream == null) {
        LOG.warn("Connection creation to PowerShell language host failed for $rootPath")
        setStatus(ServerStatus.FAILED)
        return false
      }
//      fileWriter = PrintWriter(dumpFile)
      val launcher = LSPLauncher.createClientLauncher(client, inStream, outStream, false, null)
      languageServer = launcher.remoteProxy

      val server = languageServer
      if (server == null) {
        LOG.warn("Language server is null for $launcher")
        setStatus(ServerStatus.FAILED)
        return false
      }
      client.connectServer(server, this@LanguageServerEndpoint)

      launcherFuture = launcher.startListening()
      languageHostConnectionManager.connectServer(this)
      sendInitializeRequest(server)
      LOG.debug("Sent initialize request to server")

      if (isConsoleConnection()) addRemoteFilesChangeListener(server)//register vfs listener for saving remote files

      return true
    } catch (e: Exception) {
      when (e) {
        is PowerShellExtensionError -> {
          LOG.warn("PowerShell extension error: ${e.message}")
          showPowerShellNotConfiguredNotification()
        }
        is PowerShellExtensionNotFound -> {
          LOG.warn("PowerShell extension not found", e)
          showPowerShellNotConfiguredNotification()
        }
        is PowerShellNotInstalled -> {
          LOG.warn("PowerShell is not installed", e)
          showPowerShellNotInstalledNotification()
        }
        else -> {
          LOG.warn("Can not start language server: ", e)
        }
      }
      setStatus(ServerStatus.FAILED)
      return false
    }

  }

  private fun addRemoteFilesChangeListener(server: LanguageServer) {
    val listener = object : AsyncFileListener {
      override fun prepareChange(events: List<VFileEvent>): AsyncFileListener.ChangeApplier? {
        val changed = events.filterIsInstance<VFileContentChangeEvent>()
        if (changed.isEmpty()) return null
        return object : AsyncFileListener.ChangeApplier {
          override fun beforeVfsChange() {
            changed
                .mapNotNull { it.file.canonicalPath?.replaceFirst("/private", "") }
                .filter { isRemotePath(it) }
                .map { Paths.get(it).toUri().toASCIIString() }
                .forEach { uri ->
                  server.textDocumentService?.didSave(DidSaveTextDocumentParams(TextDocumentIdentifier(uri), null))//notify Editor Services to save remote file
                }
          }
        }
      }
    }
    VirtualFileManager.getInstance().addAsyncFileListener(listener, this)
  }

  private fun showPowerShellNotConfiguredNotification() {
    if (installExtensionNotificationShown) return
    val title = MessagesBundle.message("powershell.vs.code.extension.not.installed.title")
    val content = MessagesBundle.message("powershell.vs.code.extension.not.installed.message")
    val installPSExt = Notification("PowerShell.MissingExtension", title, content, NotificationType.INFORMATION)
    installPSExt.setIcon(PowerShellIcons.FILE)
    installPSExt.addAction(BrowseNotificationAction(MessagesBundle.message("powershell.vs.code.extension.install.action"), MessagesBundle.message("powershell.vs.code.extension.install.link")))
    installPSExt.addAction(NotificationAction.createExpiring(MessagesBundle.message("powershell.vs.code.extension.configure.action")) { event, notification ->
      ShowSettingsUtilImpl.showSettingsDialog(project, PowerShellConfigurable.ID, PowerShellConfigurable.NAME)
    })

    Notifications.Bus.notify(installPSExt, project)
    installExtensionNotificationShown = true
  }

  private fun showPowerShellNotInstalledNotification() {
    if (installPSNotificationShown) return
    val title = MessagesBundle.message("powershell.not.installed.title")
    val downloadLink = MessagesBundle.message("powershell.download.link")
    val content = MessagesBundle.message("powershell.not.installed.message")
    val installPSExt = Notification("PowerShell.MissingExtension", title, content, NotificationType.INFORMATION)
    installPSExt.setIcon(PowerShellIcons.FILE)
    installPSExt.addAction(BrowseNotificationAction(MessagesBundle.message("powershell.not.installed.install.action"), downloadLink))

    Notifications.Bus.notify(installPSExt, project)
    myFailedStarts = MAX_FAILED_STARTS
    installPSNotificationShown = true
  }

  private fun sendInitializeRequest(server: LanguageServer) {
    val workspaceClientCapabilities = WorkspaceClientCapabilities()
    workspaceClientCapabilities.applyEdit = true
    workspaceClientCapabilities.didChangeConfiguration = DidChangeConfigurationCapabilities()
    workspaceClientCapabilities.didChangeWatchedFiles = DidChangeWatchedFilesCapabilities()
    workspaceClientCapabilities.executeCommand = ExecuteCommandCapabilities()
    workspaceClientCapabilities.workspaceEdit = WorkspaceEditCapabilities(true)
    workspaceClientCapabilities.symbol = SymbolCapabilities()
    val textDocumentClientCapabilities = TextDocumentClientCapabilities()
    textDocumentClientCapabilities.codeAction = CodeActionCapabilities()
    //textDocumentClientCapabilities.setCodeLens(new CodeLensCapabilities)
    textDocumentClientCapabilities.completion = CompletionCapabilities(CompletionItemCapabilities(false))
    textDocumentClientCapabilities.definition = DefinitionCapabilities()
    textDocumentClientCapabilities.documentHighlight = DocumentHighlightCapabilities()
    textDocumentClientCapabilities.synchronization = SynchronizationCapabilities(false, false, true)
    //textDocumentClientCapabilities.setDocumentLink(new DocumentLinkCapabilities)
    //textDocumentClientCapabilities.setDocumentSymbol(new DocumentSymbolCapabilities)
    textDocumentClientCapabilities.formatting = FormattingCapabilities()
    textDocumentClientCapabilities.hover = HoverCapabilities()
    textDocumentClientCapabilities.onTypeFormatting = OnTypeFormattingCapabilities()
    textDocumentClientCapabilities.rangeFormatting = RangeFormattingCapabilities()
    textDocumentClientCapabilities.references = ReferencesCapabilities()
    textDocumentClientCapabilities.rename = RenameCapabilities()
    textDocumentClientCapabilities.signatureHelp = SignatureHelpCapabilities()
    textDocumentClientCapabilities.synchronization = SynchronizationCapabilities(true, true, true)
    val initParams = InitializeParams()
    initParams.rootUri = rootPath
    initParams.capabilities = ClientCapabilities(workspaceClientCapabilities, textDocumentClientCapabilities, null)
    initParams.initializationOptions = null
    initializeFuture = server.initialize(initParams).thenApply { res ->
      initializeResult = res
      LOG.info("Got server initialize result for $rootPath")
      setStatus(ServerStatus.STARTED)
      return@thenApply res
    }.exceptionally {
          LOG.warn("Server initialization completed exceptionally for $rootPath. Initialize result: $initializeResult, Cause: $it")
          initializeResult = null
          setStatus(ServerStatus.FAILED)
          return@exceptionally null
        }
  }

  private fun shouldRetryFailedStart(): Boolean {
    val thisFailed = getStatus() == ServerStatus.FAILED
    if (thisFailed) myFailedStarts++
    return thisFailed && myFailedStarts <= MAX_FAILED_STARTS
  }

  private fun setStatus(status: ServerStatus) {
    myStatus.set(status)
  }

//  fun getConnectedFiles(): Iterable<URI> {
//    return connectedEditors.keys
//  }

  fun getStatus(): ServerStatus {
    return myStatus.get()
  }


  fun crashed(e: Exception) {
    LOG.warn("Crashed: $e")
    crashCount += 1
    if (crashCount < 5) {
      val editors = connectedEditors.toMap().keys
      stop()
      for (uri in editors) {
        connectEditor(uri)
      }
    } else {
      stop()
    }
  }

  private fun connectEditor(uri: URI) {
    val file = VfsUtil.findFileByURL(uri.toURL()) ?: return
    val editor = getTextEditor(file, project) ?: return
    connectEditor(editor)
  }

  private fun destroyLanguageHostProcess() {
    languageHostConnectionManager.closeConnection()
  }

  fun shutdown() {
    stop()
  }

  private fun isConsoleConnection(): Boolean = languageHostConnectionManager.useConsoleRepl()

}
