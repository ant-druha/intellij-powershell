/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.client

import com.intellij.openapi.diagnostic.Logger
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageServerEndpoint
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.services.LanguageClient
import org.eclipse.lsp4j.services.LanguageServer
import java.util.concurrent.CompletableFuture

class PSLanguageClientImpl : LanguageClient {
  private val LOG: Logger = Logger.getInstance(javaClass)
  private var server: LanguageServer? = null
  private var serverEndpoint: LanguageServerEndpoint? = null


  fun connectServer(server: LanguageServer, serverEndpoint: LanguageServerEndpoint) {
    this.server = server
    this.serverEndpoint = serverEndpoint
  }

  /**
   * The workspace/applyEdit request is sent from the server to the client to modify resource on the client side.
   */
  override fun applyEdit(params: ApplyWorkspaceEditParams): CompletableFuture<ApplyWorkspaceEditResponse> {
    throw UnsupportedOperationException()
  }

  /**
   * The client/registerCapability request is sent from the server to the client
   * to register for a new capability on the client side.
   * Not all clients need to support dynamic capability registration.
   * A client opts in via the ClientCapabilities.dynamicRegistration property
   */
  override fun registerCapability(params: RegistrationParams): CompletableFuture<Void> {
    throw UnsupportedOperationException()
  }

  /**
   * The client/unregisterCapability request is sent from the server to the client
   * to unregister a previously register capability.
   */
  override fun unregisterCapability(params: UnregistrationParams): CompletableFuture<Void> {
    throw UnsupportedOperationException()
  }

  /**
   * The telemetry notification is sent from the server to the client to ask
   * the client to log a telemetry event.
   */
  override fun telemetryEvent(`object`: Any) {
  }

  /**
   * Diagnostics notifications are sent from the server to the client to
   * signal results of validation runs.
   */
  override fun publishDiagnostics(diagnostics: PublishDiagnosticsParams) {
  }

  /**
   * The show message notification is sent from a server to a client to ask
   * the client to display a particular message in the user interface.
   */
  override fun showMessage(messageParams: MessageParams) {
  }

  /**
   * The show message request is sent from a server to a client to ask the
   * client to display a particular message in the user interface. In addition
   * to the show message notification the request allows to pass actions and
   * to wait for an answer from the client.
   */
  override fun showMessageRequest(requestParams: ShowMessageRequestParams): CompletableFuture<MessageActionItem> {
    TODO("Not implemented")
  }

  /**
   * The log message notification is send from the server to the client to ask
   * the client to log a particular message.
   */
  override fun logMessage(message: MessageParams) {
  }
}