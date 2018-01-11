/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.languagehost

import org.eclipse.lsp4j.*

class ServerOptions(internal val syncKind: TextDocumentSyncKind?, internal val completionProvider: CompletionOptions?,
                    internal val signatureHelpProvider: SignatureHelpOptions, internal val codeLensProvider: CodeLensOptions?,
                    internal val documentOnTypeFormattingProvider: DocumentOnTypeFormattingOptions?,
                    internal val documentLinkProvider: DocumentLinkOptions?, internal val executeCommandProvider: ExecuteCommandOptions?)