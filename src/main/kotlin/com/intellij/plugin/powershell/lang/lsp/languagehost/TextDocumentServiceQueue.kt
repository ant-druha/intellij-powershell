package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.trace
import com.intellij.util.io.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.TextDocumentService

private val logger = logger<TextDocumentServiceQueue>()

class TextDocumentServiceQueue(private val textDocumentService: () -> TextDocumentService?) {

  private val mutex = Mutex()
  private suspend fun <T> executeTask(id: String, logDescription: Any? = null, task: suspend (TextDocumentService) -> T): T? {
    logger.trace {
      val summary = "$id: operation queued."
      val description = logDescription?.toString()
      if (description == null)
        summary
      else summary + "\n" + description
    }
    return mutex.withLock {
      logger.trace { "$id: taken the lock." }
      withContext(Dispatchers.IO) {
        logger.trace { "$id: executing on the IO context." }
        try {
          textDocumentService()?.let { task(it) } ?: run {
            logger.error("Cannot get the service to perform task $id.")
            null
          }
        } finally {
          logger.trace { "$id: finished." }
        }
      }
    }
  }

  suspend fun didOpen(params: DidOpenTextDocumentParams) {
    executeTask("didOpen notification", params) { service ->
      service.didOpen(params)
    }
  }

  suspend fun didClose(params: DidCloseTextDocumentParams) {
    executeTask("didClose notification", params) { service ->
      service.didClose(params)
    }
  }

  suspend fun didSave(params: DidSaveTextDocumentParams) {
    executeTask("didSave notification", params) { service ->
      service.didSave(params)
    }
  }

  suspend fun didChange(params: DidChangeTextDocumentParams) {
    executeTask("didChange notification", params) { service ->
      service.didChange(params)
    }
  }

  suspend fun completion(params: CompletionParams): Either<List<CompletionItem>, CompletionList>? =
    executeTask("completion request", params) { service ->
      service.completion(params)?.await()
    }
}
