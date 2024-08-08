package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.openapi.project.Project
import java.io.InputStream
import java.io.OutputStream

interface LanguageHostConnectionManager {
  suspend fun establishConnection(): Pair<InputStream?, OutputStream?>
  suspend fun establishDebuggerConnection(): Pair<InputStream, OutputStream>?
  fun closeConnection()
  fun isConnected(): Boolean
  fun getProcess(): Process?
  fun createProcess(project: Project, command: List<String>, environment: Map<String, String>?): Process
  fun connectServer(server: LanguageServerEndpoint) {}
  fun useConsoleRepl(): Boolean = false
}
