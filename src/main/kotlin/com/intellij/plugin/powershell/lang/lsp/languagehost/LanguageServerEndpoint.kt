/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.plugin.powershell.lang.lsp.client.PSLanguageClientImpl
import com.intellij.plugin.powershell.lang.lsp.ide.EditorEventManager
import com.intellij.plugin.powershell.lang.lsp.ide.LSPRequestManager
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.DocumentListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.EditorMouseListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.EditorMouseMotionListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.SelectionListenerImpl
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.launch.LSPLauncher
import org.eclipse.lsp4j.services.LanguageServer
import java.io.File
import java.io.PrintWriter
import java.net.URI
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class LanguageServerEndpoint(val project: Project) {
  private val LOG: Logger = Logger.getInstance(javaClass)
  private var client: PSLanguageClientImpl = PSLanguageClientImpl()
  private var languageServer: LanguageServer? = null
  private var initializeFuture: CompletableFuture<InitializeResult>? = null
  private var launcherFuture: Future<*>? = null
  private var initializeResult: InitializeResult? = null
  private val connectedEditors: MutableMap<URI, EditorEventManager> = HashMap()
  private val rootPath = project.basePath
  private var capabilitiesAlreadyRequested: Boolean = false
  @Volatile private var myStatus: ServerStatus = ServerStatus.STOPPED
  private var languageHostStarter: LanguageHostStarter? = null
  private var crashCount: Int = 0
  private var myFailedStarts = 0

  private val dumpFile = File(FileUtil.toCanonicalPath(PSLanguageHostUtils.getLanguageHostLogsDir() + "/protocol_messages_IJ.log"))
  private var fileWriter: PrintWriter? = null

  init {
    dumpFile.parentFile.mkdirs()
    addShutdownHook()
  }

  private fun addShutdownHook() {
    ProjectManager.getInstance().addProjectManagerListener(project, object : ProjectManagerListener {
      override fun projectClosed(project: Project?) {
        stop()
      }
    })
  }

  companion object {
    private val uriToLanguageServerConnection = mutableMapOf<URI, LanguageServerEndpoint>()
    private val editorToLanguageServerConnection = mutableMapOf<Editor, LanguageServerEndpoint>()
  }

  fun connectEditor(editor: Editor?) {
    if (editor == null) {
      LOG.warn("Editor is null for " + languageServer)
      return
    }
    val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return
    val uri = VfsUtil.toUri(file)
    if (!connectedEditors.contains(uri)) {
      checkStarted()
      if (initializeFuture != null) {
        val capabilities = getServerCapabilities()
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
                  editorToLanguageServerConnection[editor] = this
                  uriToLanguageServerConnection[uri] = this
                  manager.documentOpened()
                  LOG.debug("Created manager for $uri")
                }
              }
            } catch (e: Exception) {
              LOG.error("Error when trying to initialize server capabilities: $e")
            }
          }
        } else {
          LOG.warn("Capabilities are null for " + languageServer)
        }
      } else {
        LOG.warn("InitializeFuture is null for " + languageServer)
      }
    }
  }

  private fun getServerCapabilities(): ServerCapabilities? {
    if (initializeResult != null) return initializeResult?.capabilities
    try {
      checkStarted()
      initializeFuture?.get(if (capabilitiesAlreadyRequested) 0 else 5000 /*Timeout.INIT_TIMEOUT*/, TimeUnit.MILLISECONDS)
    } catch (e: Exception) {
      LOG.warn("PowerShell language host not initialized after 5s\nCheck settings", e)
      stop()
    }
    capabilitiesAlreadyRequested = true
    return if (initializeResult != null) initializeResult?.capabilities
    else null
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
    launcherFuture?.cancel(true)
    launcherFuture = null

    try {
      val shutdown: CompletableFuture<Any>? = languageServer?.shutdown()
      shutdown?.get(100, TimeUnit.MILLISECONDS)//otherwise lsp4j stream IOException
    } catch (ignored: Exception) {
      LOG.debug("PowerShell language host shutdown exception: $ignored")
    }
    languageServer?.exit()
    languageServer = null
    fileWriter?.close()
    fileWriter = null
    destroyLanguageHostProcess()
    setStatus(ServerStatus.STOPPED)
    LOG.info("Connection to PowerShell language host closed for $rootPath.")
  }

  fun disconnectEditor(uri: URI) {
    val e = connectedEditors.remove(uri)
    uriToLanguageServerConnection.remove(uri)
    if (e != null) {
      editorToLanguageServerConnection.remove(e.getEditor())
      e.removeListeners()
      e.documentClosed()
    }
  }

  private val scheduleStartLock = Any()

  private fun checkStarted() {
    waitIfStarting()//When 'editor opened' notification comes but the server has not been started yet.
    synchronized(scheduleStartLock, {
      val oldStatus = getStatus()
      if (oldStatus == ServerStatus.STOPPED || shouldRetryFailedStart()) {
        //retry n attempts to start if failed
        var launched = scheduleStart(oldStatus)
        while (!launched && shouldRetryFailedStart()) {
          stop()
          launched = scheduleStart(getStatus())
        }
      }
    })
  }

  private fun waitIfStarting() {
    var checkCount = 15
    try {
      while (getStatus() == ServerStatus.STARTING && checkCount > 0) {
        LOG.info("Waiting the language server for the '${project.name}' project to start... $checkCount.")
        Thread.sleep(500)
        checkCount--
      }
      if (checkCount <=0) {
        LOG.warn("Wait time elapsed for the server to start for project '${project.name}'")
      }
    } catch (e: Exception) {
      LOG.warn("Error while waiting the server for the '${project.name}' project to start: $e")
    }
  }

  private fun scheduleStart(oldStatus: ServerStatus): Boolean {
    setStatus(ServerStatus.STARTING)
    if (languageHostStarter != null) languageHostStarter?.closeConnection()
    if (languageHostStarter == null || oldStatus == ServerStatus.FAILED) {
      languageHostStarter = LanguageHostStarter()
    }
    try {
      val (inStream, outStream) = languageHostStarter!!.establishConnection() //long operation, suspends the thread
      if (inStream == null || outStream == null) {
        LOG.warn("Connection creation to PowerShell language host failed for $rootPath")
        setStatus(ServerStatus.FAILED)
        return false
      }
      fileWriter = PrintWriter(dumpFile)
      val launcher = LSPLauncher.createClientLauncher(client, inStream, outStream, false, fileWriter)
      languageServer = launcher.remoteProxy

      val server = languageServer
      if (server == null) {
        LOG.warn("Language server is null for $launcher")
        setStatus(ServerStatus.FAILED)
        return false
      }
      client.connectServer(server, this@LanguageServerEndpoint)

      launcherFuture = launcher.startListening()
      sendInitializeRequest(server)
      return true
    } catch (e: PSEditorServicesScriptNotFound) {
      LOG.warn("PowerShell editor services language host is not found")
      setStatus(ServerStatus.FAILED)
      return false
    }

  }

  private fun sendInitializeRequest(server: LanguageServer) {
    val workspaceClientCapabilities = WorkspaceClientCapabilities()
    workspaceClientCapabilities.applyEdit = true
    //workspaceClientCapabilities.setDidChangeConfiguration(new DidChangeConfigurationCapabilities)
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
    return thisFailed && myFailedStarts <= 2
  }

  private fun setStatus(status: ServerStatus) {
    myStatus = status
  }

//  fun getConnectedFiles(): Iterable<URI> {
//    return connectedEditors.keys
//  }

  fun getStatus(): ServerStatus {
    return myStatus
  }


  fun crashed(e: Exception) {
    LOG.error("Crashed: $e")
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
    val editors = FileEditorManager.getInstance(project).getAllEditors(file)
        .filterIsInstance<TextEditor>().map { e -> e.editor }
    if (editors.isNotEmpty()) {
      connectEditor(editors.first())
    }
  }

  private fun destroyLanguageHostProcess() {
    languageHostStarter?.closeConnection()
    languageHostStarter = null
  }

}