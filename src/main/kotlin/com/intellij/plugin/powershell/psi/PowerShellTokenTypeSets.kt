package com.intellij.plugin.powershell.psi

import com.intellij.plugin.powershell.psi.PowerShellTypes.*
import com.intellij.psi.tree.TokenSet

/**
 * Andrey 17/07/17.
 */
object PowerShellTokenTypeSets {

  val KEYWORDS = TokenSet.create(BEGIN, BREAK, CATCH, CLASS, CONTINUE, DATA, DEFINE, DO, DYNAMICPARAM, ELSE, ELSEIF, END, EXIT, FILTER, FINALLY, FOR,
      FOREACH, FROM, FUNCTION, IF, IN, INLINESCRIPT, PARALLEL, PARAM, PROCESS, RETURN, SWITCH, THROW, TRAP, TRY, UNTIL, USING, VAR, WHILE, WORKFLOW)
  val LINE_COMMENT = TokenSet.create(COMMENT)
  val STRINGS = TokenSet.create(STRING_DQ, STRING_SQ)
  val NUMBERS = TokenSet.create(DIGITS)
}