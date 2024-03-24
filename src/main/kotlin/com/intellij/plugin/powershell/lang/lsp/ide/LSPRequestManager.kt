/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.ide

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageServerEndpoint
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

  private fun checkStatus(): Boolean = serverEndpoint.isRunning

  suspend fun didClose(params: DidCloseTextDocumentParams) {
    serviceCall {
      if (documentSyncOptions == null || documentSyncOptions.openClose) queue.didClose(params)
    }
  }

  suspend fun didOpen(params: DidOpenTextDocumentParams) {
    serviceCall {
      if (documentSyncOptions == null || documentSyncOptions.openClose) queue.didOpen(params)
    }
  }

  suspend fun didChange(params: DidChangeTextDocumentParams) {
    serviceCall {
      if (documentSyncOptions == null || documentSyncOptions.change != null) queue.didChange(params)
    }
  }

  suspend fun completion(params: CompletionParams): Either<List<CompletionItem>, CompletionList>? =
    serviceCall {
      if (capabilities.completionProvider != null)
        queue.completion(params)
      else null
    }

  private suspend fun <T> serviceCall(action: suspend () -> T): T? {
    if (!checkStatus()) {
      logger.error("ServerEndpoint does not report isRunning == true")
      return null
    }

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

private val logger = logger<LSPRequestManager>()
