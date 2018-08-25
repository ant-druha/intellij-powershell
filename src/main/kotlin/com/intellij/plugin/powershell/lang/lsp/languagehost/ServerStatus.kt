/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.languagehost

enum class ServerStatus {
  STARTED, FAILED, STARTING, STOPPED
}