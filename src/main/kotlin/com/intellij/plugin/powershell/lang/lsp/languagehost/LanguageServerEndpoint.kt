/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.collaboration.async.launchNow
import com.intellij.ide.actions.ShowSettingsUtilImpl
import com.intellij.notification.*
import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.diagnostic.runAndLogException
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
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
import com.intellij.util.io.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.launch.LSPLauncher
import org.eclipse.lsp4j.services.LanguageServer
import org.jetbrains.annotations.TestOnly
import java.io.File
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class LanguageServerEndpoint(
  private val coroutineScope: CoroutineScope,
  private val languageHostConnectionManager: LanguageHostConnectionManager,
  private val project: Project
) : Disposable {

  private val LOG: Logger = Logger.getInstance(javaClass)
  private var client: PSLanguageClientImpl = PSLanguageClientImpl(project)
  private var languageServer: LanguageServer? = null
  private val textDocumentServiceQueue: TextDocumentServiceQueue

  private val serverInitializationLock = Any()
  private var serverInitialization: Deferred<InitializeResult?>? = null

  private val connectedEditors = ConcurrentHashMap<URI, Deferred<EditorEventManager?>>()
  private val rootPath = project.basePath
  private var crashCount: Int = 0
  private val myFailedStarts = AtomicInteger()
  private val MAX_FAILED_STARTS = 2

  override fun toString(): String {
    val consoleString = if (isConsoleConnection()) " Console" else ""
    return "[PowerShell Editor Services host$consoleString connection, project: $rootPath]"
  }
//  private val dumpFile = File(FileUtil.toCanonicalPath(PSLanguageHostUtils.getLanguageHostLogsDir() + "/protocol_messages_IJ.log"))
//  private var fileWriter: PrintWriter? = null

  init {
    Disposer.register(PluginProjectDisposableRoot.getInstance(project), this)
    textDocumentServiceQueue = TextDocumentServiceQueue { languageServer?.textDocumentService }
  }

  companion object {
    private var installExtensionNotificationShown = false
    private var installPSNotificationShown = false
  }

  fun start() {
    coroutineScope.launchNow {
      checkStarted()
      val capabilities = getServerCapabilities()
      if (capabilities != null) {
        //todo move it to LanguageHostConnectionManager (notify it when server initialized)
        languageServer?.workspaceService?.didChangeConfiguration(DEFAULT_DID_CHANGE_CONFIGURATION_PARAMS)//notify Editor Services to start REPL loop
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
    connectedEditors.computeIfAbsent(uri) {
      coroutineScope.async {
        checkStarted()
        val capabilities = getServerCapabilities()
        if (capabilities != null) {
          LOG.runAndLogException {
            val syncOptions: Either<TextDocumentSyncKind, TextDocumentSyncOptions> = capabilities.textDocumentSync
            val syncKind: TextDocumentSyncKind? = if (syncOptions.isRight) syncOptions.right.change else syncOptions.left
            val mouseListener = EditorMouseListenerImpl()
            val mouseMotionListener = EditorMouseMotionListenerImpl()
            val documentListener = DocumentListenerImpl(coroutineScope)
            val selectionListener = SelectionListenerImpl()
            val server = languageServer
            if (server != null) {
              val requestManager = LSPRequestManager(
                this@LanguageServerEndpoint,
                capabilities,
                textDocumentServiceQueue
              )
              val serverOptions = ServerOptions(syncKind, capabilities.completionProvider, capabilities.signatureHelpProvider, capabilities.codeLensProvider, capabilities.documentOnTypeFormattingProvider, capabilities.documentLinkProvider, capabilities.executeCommandProvider)
              val manager = EditorEventManager(project, editor, mouseListener, mouseMotionListener, documentListener, selectionListener, requestManager, serverOptions)
              mouseListener.setManager(manager)
              mouseMotionListener.setManager(manager)
              documentListener.setManager(manager)
              selectionListener.setManager(manager)
              manager.registerListeners()
              coroutineScope.launchNow { manager.documentOpened() }
              LOG.debug("Created manager for $uri")
              return@async manager
            }
          }
        } else {
          LOG.warn("Capabilities are null for $languageServer")
        }

        return@async null
      }
    }
  }

  private suspend fun getServerCapabilities(): ServerCapabilities? {
    checkStarted()
    val initialization = synchronized(serverInitializationLock) { serverInitialization }
    return initialization?.await()?.capabilities
  }

  override fun dispose() {
    stop()
  }

  private fun stop() {
    val copy = connectedEditors.toMap()
    for (e in copy) {
      disconnectEditor(e.key)
    }

    synchronized(serverInitializationLock) {
      serverInitialization?.cancel()
      serverInitialization = null
    }

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
    LOG.info("Connection to PowerShell language host closed for $rootPath.")
  }

  fun disconnectEditor(uri: URI) {
    val deferred = connectedEditors.remove(uri)
    if (deferred != null) {
      coroutineScope.launchNow {
        val manager = deferred.await()
        manager?.removeListeners()
        manager?.documentClosed()
      }
    }
  }

  private fun onStartFailure() {
    if (shouldRetryFailedStart()) {
      synchronized(serverInitializationLock) {
        serverInitialization?.cancel()
        serverInitialization = scheduleStart()
      }
    }
  }

  private suspend fun checkStarted() {
    synchronized(serverInitializationLock) {
      if (serverInitialization == null) {
        serverInitialization = scheduleStart()
      }
    }

    LOG.runAndLogException {
      serverInitialization?.await()
    }
  }

  private fun scheduleStart(): Deferred<InitializeResult?> {
    if (languageHostConnectionManager.isConnected()) destroyLanguageHostProcess()

    return coroutineScope.async job@{
      try {
        val (inStream, outStream) = languageHostConnectionManager.establishConnection() //long operation, suspends the thread
        if (inStream == null || outStream == null) {
          LOG.warn("Connection creation to PowerShell language host failed for $rootPath")
          onStartFailure()
          return@job null
        }

        val launcher = LSPLauncher.createClientLauncher(client, inStream, outStream, false, null)
        languageServer = launcher.remoteProxy

        val server = languageServer
        if (server == null) {
          LOG.warn("Language server is null for $launcher")
          onStartFailure()
          return@job null
        }
        client.connectServer(server, this@LanguageServerEndpoint)

        launcher.startListening().await()
        languageHostConnectionManager.connectServer(this@LanguageServerEndpoint)
        val result = async { initialize(server) }
        LOG.debug("Sent initialize request to server")

        if (isConsoleConnection()) addRemoteFilesChangeListener()//register vfs listener for saving remote files
        return@job result.await()
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

        onStartFailure()
        return@job null
      }
    }
  }

  private fun addRemoteFilesChangeListener() {
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
                  coroutineScope.launchNow {
                    textDocumentServiceQueue.didSave(DidSaveTextDocumentParams(TextDocumentIdentifier(uri), null))
                  }
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
    installPSExt.addAction(NotificationAction.createSimpleExpiring(MessagesBundle.message("powershell.vs.code.extension.configure.action")) {
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
    myFailedStarts.set(MAX_FAILED_STARTS)
    installPSNotificationShown = true
  }

  private suspend fun initialize(server: LanguageServer): InitializeResult {
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
    val result = server.initialize(initParams).await()
    LOG.info("Got server initialize result for $rootPath")
    return result
  }

  private fun shouldRetryFailedStart(): Boolean {
    val counter = myFailedStarts.incrementAndGet()
    return counter <= MAX_FAILED_STARTS
  }

  val isRunning: Boolean
    get() = synchronized(serverInitializationLock) { serverInitialization } != null

  fun crashed(e: Throwable) {
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

  @TestOnly
  suspend fun waitForInit() {
    start()
    while (synchronized(serverInitializationLock) { serverInitialization } == null) {
      delay(50)
    }
    this.serverInitialization!!.await()
  }

  @TestOnly
  suspend fun waitForEditorConnect(filePath: Path) {
    while (!connectedEditors.containsKey(filePath.toUri())) {
      delay(50)
    }
  }
}
