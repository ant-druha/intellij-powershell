/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.ide

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.removeUserData
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.DocumentListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.EditorMouseListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.EditorMouseMotionListenerImpl
import com.intellij.plugin.powershell.lang.lsp.ide.listeners.SelectionListenerImpl
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageServerEndpoint
import com.intellij.plugin.powershell.lang.lsp.languagehost.ServerOptions
import com.intellij.plugin.powershell.lang.lsp.util.DocumentUtils.offsetToLSPPos
import com.intellij.plugin.powershell.lang.lsp.util.editorToURIString
import com.intellij.psi.PsiDocumentManager
import org.eclipse.lsp4j.*
import java.util.*
import java.util.concurrent.TimeUnit

class EditorEventManager(private val project: Project, private val editor: Editor, private val mouseListener: EditorMouseListenerImpl,
                         private val mouseMotionListener: EditorMouseMotionListenerImpl, private val documentListener: DocumentListenerImpl,
                         private val selectionListener: SelectionListenerImpl, private val requestManager: LSPRequestManager,
                         private val serverOptions: ServerOptions, private val languageServerEndpoint: LanguageServerEndpoint) {
  private val LOG: Logger = Logger.getInstance(javaClass)
  private var isOpen: Boolean = false
  private val identifier = TextDocumentIdentifier(editorToURIString(editor))
  private var version: Int = -1
  private val changesParams = DidChangeTextDocumentParams(VersionedTextDocumentIdentifier(), Collections.singletonList(TextDocumentContentChangeEvent()))
  private val syncKind = serverOptions.syncKind

  private val completionTriggers = if (serverOptions.completionProvider?.triggerCharacters != null)
    serverOptions.completionProvider.triggerCharacters?.filter { c -> "." != c }
  else emptySet<String>()
  private val signatureTriggers = if (serverOptions.signatureHelpProvider.triggerCharacters != null)
    serverOptions.signatureHelpProvider.triggerCharacters.toSet()
  else emptySet<String>()

  private var diagnosticsInfo: List<Diagnostic> = listOf()

  init {
    changesParams.textDocument.uri = identifier.uri
    editor.putUserData(EDITOR_EVENT_MANAGER_KEY, this)
  }

  fun getEditor(): Editor {
    return editor
  }

  fun getDiagnostics(): List<Diagnostic> = diagnosticsInfo

  companion object {
    private val EDITOR_EVENT_MANAGER_KEY: Key<EditorEventManager> = Key("Powershell.EditorEventManager")
    fun forEditor(editor: Editor): EditorEventManager? {
      return editor.getUserData(EDITOR_EVENT_MANAGER_KEY)
    }
  }

  fun registerListeners() {
    editor.addEditorMouseListener(mouseListener)
    editor.addEditorMouseMotionListener(mouseMotionListener)
    editor.document.addDocumentListener(documentListener)
    editor.selectionModel.addSelectionListener(selectionListener)
  }

  fun documentOpened() {
    if (!editor.isDisposed) {
      if (isOpen) {
        LOG.warn("Editor $editor was already open")
      } else {
        requestManager.didOpen(DidOpenTextDocumentParams(TextDocumentItem(identifier.uri, PowerShellLanguage.INSTANCE.id, incVersion(), editor.document.text)))
        isOpen = true
      }
    }
  }

  fun removeListeners() {
    editor.removeEditorMouseMotionListener(mouseMotionListener)
    editor.document.removeDocumentListener(documentListener)
    editor.removeEditorMouseListener(mouseListener)
    editor.selectionModel.removeSelectionListener(selectionListener)
  }


  fun documentClosed() {
    if (isOpen) {
      requestManager.didClose(DidCloseTextDocumentParams(identifier))
      isOpen = false
      editor.removeUserData(EDITOR_EVENT_MANAGER_KEY)
    } else {
      LOG.warn("Editor ${identifier.uri} + was already closed")
    }
  }

  fun documentChanged(event: DocumentEvent) {
    if (!editor.isDisposed) {
      if (event.document == editor.document) {
        changesParams.textDocument.version = incVersion()
        when (syncKind) {
          null,
          TextDocumentSyncKind.None,
          TextDocumentSyncKind.Incremental -> {
            val changeEvent = changesParams.contentChanges[0]
            val newText = event.newFragment
            val offset = event.offset
            val newTextLength = event.newLength
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
            changeEvent.range = range
            changeEvent.rangeLength = newTextLength
            changeEvent.text = newText.toString()
          }
          TextDocumentSyncKind.Full -> {
            changesParams.contentChanges[0].text = editor.document.text
          }
        }
        requestManager.didChange(changesParams)
      } else {
        LOG.error("Wrong document for the EditorEventManager")
      }
    }
  }

  private fun incVersion(): Int {
    version++
    return version - 1
  }

  fun completion(pos: Position): CompletionList {
    val request = requestManager.completion(TextDocumentPositionParams(identifier, pos))
    val result = CompletionList()
    if (request == null) return result
    return try {
      val res = request.get(500, TimeUnit.MILLISECONDS)
      if (res != null) {
        if (res.isLeft) {
          result.items = res.left
        } else if (res.isRight) {
          result.setIsIncomplete(res.right.isIncomplete)
          result.items = res.right.items
        }
        result
      } else result
    } catch (e: Exception) {
      LOG.warn("Error on completion request: $e")
      result
    }
  }

  fun updateDiagnostics(diagnostics: List<Diagnostic>) {
    saveDiagnostics(diagnostics)
    val restartAnalyzerRunnable = Runnable {
      val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
      if (psiFile != null) DaemonCodeAnalyzer.getInstance(project).restart(psiFile)
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

data class PowerShellLanguageServerSettingsWrapper(val Powershell: LanguageServerSettings)
data class LanguageServerSettings(val EnableProfileLoading: Boolean = true, val ScriptAnalysis: ScriptAnalysisSettings = ScriptAnalysisSettings(), val CodeFormatting: CodeFormattingSettings = CodeFormattingSettings())
data class CodeFormattingSettings(var NewLineAfterOpenBrace: Boolean = true)
data class ScriptAnalysisSettings(var Enabled: Boolean = true)
