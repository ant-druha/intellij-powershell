package com.intellij.plugin.powershell.lang.lexer.external

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.tree.IElementType

class PowerShellExtLexer : Lexer() {

  private val rpc = LexerRpc()

  override fun start(
    buffer: CharSequence,
    startOffset: Int,
    endOffset: Int,
    initialState: Int
  ) {
    rpc.start(buffer, startOffset, endOffset, initialState)
  }

  override fun getState(): Int = rpc.getState()

  override fun getTokenType(): IElementType? {
    TODO("Not yet implemented")
  }

  override fun getTokenStart(): Int {
    TODO("Not yet implemented")
  }

  override fun getTokenEnd(): Int {
    TODO("Not yet implemented")
  }

  override fun advance() {
    TODO("Not yet implemented")
  }

  override fun getCurrentPosition(): LexerPosition {
    TODO("Not yet implemented")
  }

  override fun restore(position: LexerPosition) {
    TODO("Not yet implemented")
  }

  override fun getBufferSequence(): CharSequence {
    TODO("Not yet implemented")
  }

  override fun getBufferEnd(): Int {
    TODO("Not yet implemented")
  }
}
