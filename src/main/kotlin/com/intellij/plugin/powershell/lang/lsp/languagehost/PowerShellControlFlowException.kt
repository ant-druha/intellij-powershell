package com.intellij.plugin.powershell.lang.lsp.languagehost

import com.intellij.openapi.diagnostic.ControlFlowException

class PowerShellControlFlowException(message: String?, throwable: Throwable?) : Throwable(message, throwable), ControlFlowException