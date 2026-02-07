/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.ide

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.removeUserData
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.ide.runAndLogException
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.DocumentListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.EditorMouseListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.EditorMouseMotionListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.SelectionListenerImpl
import com.intellij.plugin.powershell.lang.lsp.languagehost.ServerOptions
import com.intellij.plugin.powershell.lang.lsp.util.DocumentUtils.offsetToLSPPos
import com.intellij.plugin.powershell.lang.lsp.util.editorToURIString
import com.intellij.psi.PsiDocumentManager
import com.jetbrains.rd.util.AtomicInteger
import org.eclipse.lsp4j.*

private val logger = logger<EditorEventManager>()

class EditorEventManager(
  private val project: Project,
  private val editor: Editor,
  private val mouseListener: EditorMouseListenerImpl,
  private val mouseMotionListener: EditorMouseMotionListenerImpl,
  private val documentListener: DocumentListenerImpl,
  private val selectionListener: SelectionListenerImpl,
  private val requestManager: LSPRequestManager,
  serverOptions: ServerOptions
) : Disposable.Default {

  private var isOpen: Boolean = false
  private val identifier = TextDocumentIdentifier(editorToURIString(editor))
  private var version = AtomicInteger(-1)
  private val syncKind = serverOptions.syncKind

  private var diagnosticsInfo: List<Diagnostic> = listOf()

  init {
    editor.putUserData(EDITOR_EVENT_MANAGER_KEY, this)
  }

  fun getDiagnostics(): List<Diagnostic> = diagnosticsInfo

  companion object {
    private val EDITOR_EVENT_MANAGER_KEY: Key<EditorEventManager> = Key("Powershell.EditorEventManager")
    fun forEditor(editor: Editor): EditorEventManager? {
      return editor.getUserData(EDITOR_EVENT_MANAGER_KEY)
    }
  }

  fun registerListeners() {
    editor.addEditorMouseListener(mouseListener, this)
    editor.addEditorMouseMotionListener(mouseMotionListener, this)
    editor.document.addDocumentListener(documentListener, this)
    editor.selectionModel.addSelectionListener(selectionListener, this)
  }

  suspend fun documentOpened() {
    if (!editor.isDisposed) {
      if (isOpen) {
        logger.warn("Editor $editor was already open")
      } else {
        requestManager.didOpen(DidOpenTextDocumentParams(TextDocumentItem(identifier.uri, PowerShellLanguage.LSP_ID, incVersion(), editor.document.text)))
        isOpen = true
      }
    }
  }

  suspend fun documentClosed() {
    if (isOpen) {
      requestManager.didClose(DidCloseTextDocumentParams(identifier))
      isOpen = false
      editor.removeUserData(EDITOR_EVENT_MANAGER_KEY)
    } else {
      logger.warn("Editor ${identifier.uri} + was already closed")
    }
  }

  private fun createDidChangeParams(event: DocumentEvent): DidChangeTextDocumentParams? {
    if (editor.isDisposed) return null
    if (event.document != editor.document) {
      logger.error("Wrong document for the EditorEventManager")
      return null
    }

    val syncKind = syncKind ?: TextDocumentSyncKind.None
    val change = when (syncKind) {
      TextDocumentSyncKind.None,
      TextDocumentSyncKind.Incremental -> {
        val newText = event.newFragment
        val offset = event.offset
        val lspPosition: Position = offsetToLSPPos(editor, offset)
        val startLine = lspPosition.line
        val startColumn = lspPosition.character
        val oldText = event.oldFragment

        //if text was deleted/replaced, calculate the end position of inserted/deleted text
        val (endLine, endColumn) = if (oldText.isNotEmpty()) {
          val line = startLine + StringUtil.countNewLines(oldText)
          val oldLines = oldText.toString().split('\n')
          val oldTextLength = if (oldLines.isEmpty()) 0 else oldLines.last().length
          val column = if (oldLines.size == 1) startColumn + oldTextLength else oldTextLength
          Pair(line, column)
        } else Pair(startLine, startColumn) //if insert or no text change, the end position is the same
        val range = Range(Position(startLine, startColumn), Position(endLine, endColumn))
        TextDocumentContentChangeEvent(
          range,
          newText.toString()
        )
      }
      TextDocumentSyncKind.Full -> {
        TextDocumentContentChangeEvent(editor.document.text)
      }
    }

    return DidChangeTextDocumentParams(
      VersionedTextDocumentIdentifier(identifier.uri, incVersion()),
      listOf(change)
    )
  }

  suspend fun documentChanged(event: DocumentEvent) {
    val params = createDidChangeParams(event) ?: return
    requestManager.didChange(params)
  }

  private fun incVersion(): Int = version.incrementAndGet()

  suspend fun completion(pos: Position): CompletionList {
    val completions = CompletionList()
    logger.runAndLogException {
      val res = requestManager.completion(CompletionParams(identifier, pos)) ?: return completions
      if (res.isLeft) {
        completions.items = res.left
      } else if (res.isRight) {
        completions.setIsIncomplete(res.right.isIncomplete)
        completions.items = res.right.items
      }
    }

    return completions
  }

  fun updateDiagnostics(diagnostics: List<Diagnostic>) {
    saveDiagnostics(diagnostics)
    val restartAnalyzerRunnable = Runnable {
      val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
      if (psiFile != null)
        DaemonCodeAnalyzer.getInstance(project).restart(psiFile, "Diagnostic update request from LSP")
    }
    if (ApplicationManager.getApplication().isDispatchThread) {
      restartAnalyzerRunnable.run()
    } else {
      ApplicationManager.getApplication().runReadAction {
        restartAnalyzerRunnable.run()
      }
    }
  }

  /**
   *
   * Saves diagnostics which will then be used by IDE code analyzer and shown in Editor on inspection run
   */
  private fun saveDiagnostics(diagnostics: List<Diagnostic>) {
    diagnosticsInfo = diagnostics
  }

}

val DEFAULT_DID_CHANGE_CONFIGURATION_PARAMS = DidChangeConfigurationParams(PowerShellLanguageServerSettingsWrapper(LanguageServerSettings()))

@Suppress("PropertyName")
data class PowerShellLanguageServerSettingsWrapper(val Powershell: LanguageServerSettings)
@Suppress("PropertyName")
data class LanguageServerSettings(val EnableProfileLoading: Boolean = true, val ScriptAnalysis: ScriptAnalysisSettings = ScriptAnalysisSettings(), val CodeFormatting: CodeFormattingSettings = CodeFormattingSettings())
@Suppress("PropertyName")
data class CodeFormattingSettings(var NewLineAfterOpenBrace: Boolean = true)
@Suppress("PropertyName")
data class ScriptAnalysisSettings(var Enabled: Boolean = true)
