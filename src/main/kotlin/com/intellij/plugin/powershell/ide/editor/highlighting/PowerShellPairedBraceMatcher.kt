package com.intellij.plugin.powershell.ide.editor.highlighting

import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.plugin.powershell.lang.PowerShellLanguage
import com.intellij.plugin.powershell.psi.PowerShellTypes.*
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

class PowerShellPairedBraceMatcher : PairedBraceMatcherAdapter(MyPairedBraceMatcher(), PowerShellLanguage.INSTANCE) {

  private class MyPairedBraceMatcher : PairedBraceMatcher {
    private val PAIRS = arrayOf(
      BracePair(LCURLY, RCURLY, true),
      BracePair(BRACED_VAR_START, RCURLY, false),
      BracePair(LP, RP, false),
      BracePair(SQBR_L, SQBR_R, false)
    )

    override fun getPairs(): Array<BracePair> {
      return PAIRS
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
      return true
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
      return openingBraceOffset
    }
  }
}
