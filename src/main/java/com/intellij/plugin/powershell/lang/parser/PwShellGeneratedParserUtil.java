package com.intellij.plugin.powershell.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.TokenType;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMMENT;

/**
 * Andrey 28/06/17.
 */
public class PwShellGeneratedParserUtil {

  public static boolean isNotWhiteSpace(PsiBuilder builder, int i) {
    return builder.eof() || !isWhiteSpace(builder, i);
  }

  public static boolean isWhiteSpace(PsiBuilder builder, int i) {
    return builder.eof() || builder.rawLookup(-1) == TokenType.WHITE_SPACE || builder.rawLookup(-1) == COMMENT;
  }
}
