/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.ide

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageServerEndpoint
import com.intellij.plugin.powershell.lang.lsp.languagehost.ServerStatus
import com.intellij.plugin.powershell.lang.lsp.languagehost.TextDocumentServiceQueue
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import java.util.concurrent.CancellationException

class LSPRequestManager(
  private val serverEndpoint: LanguageServerEndpoint,
  private val capabilities: ServerCapabilities,
  private val queue: TextDocumentServiceQueue
) {

  private val documentSyncOptions: TextDocumentSyncOptions? = if (capabilities.textDocumentSync.isRight) capabilities.textDocumentSync.right else null

  private fun checkStatus(): Boolean = serverEndpoint.getStatus() == ServerStatus.STARTED

  suspend fun didClose(params: DidCloseTextDocumentParams) {
    if (checkStatus()) {
      handleServerError {
        if (documentSyncOptions == null || documentSyncOptions.openClose) queue.didClose(params)
      }
    }
  }

  suspend fun didOpen(params: DidOpenTextDocumentParams) {
    if (checkStatus()) {
      handleServerError {
        if (documentSyncOptions == null || documentSyncOptions.openClose) queue.didOpen(params)
      }
    }
  }

  suspend fun didChange(params: DidChangeTextDocumentParams) {
    if (checkStatus()) {
      handleServerError {
        if (documentSyncOptions == null || documentSyncOptions.change != null) queue.didChange(params)
      }
    }
  }

  suspend fun completion(params: TextDocumentPositionParams): Either<List<CompletionItem>, CompletionList>? {
    if (checkStatus()) {
      return handleServerError {
        if (capabilities.completionProvider != null)
          queue.completion(params)
        else null
      }
    }

    return null
  }

  private suspend fun <T> handleServerError(action: suspend () -> T): T? {
    try {
      return action()
    } catch (e: Throwable) {
      if (e is ProcessCanceledException || e is CancellationException) {
        throw e
      }

      thisLogger().error(e)
      serverEndpoint.crashed(e)
      return null
    }
  }
}
