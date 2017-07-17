package com.intellij.plugin.powershell.lang.lexer

import com.intellij.lexer.FlexAdapter
import com.intellij.plugin.powershell._PwShellLexer

/**
 * Andrey 17/07/17.
 */
class PowerShellLexerAdapter : FlexAdapter(_PwShellLexer()) {
}