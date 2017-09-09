package com.intellij.plugin.powershell.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.plugin.powershell.psi.PowerShellTypes;
import com.intellij.psi.TokenType;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMMENT;

/**
 * Andrey 28/06/17.
 */
public class PowerShellGeneratedParserUtil {

  public static boolean isNotWhiteSpace(PsiBuilder builder, int i) {
    return builder.eof() || !isWhiteSpace(builder, i);
  }

  public static boolean isWhiteSpace(PsiBuilder builder, int i) {
    return builder.eof() || builder.rawLookup(-1) == TokenType.WHITE_SPACE || builder.rawLookup(-1) == COMMENT;
  }

  public static boolean isIdentifierBefore(PsiBuilder builder, int i) {
    return builder.rawLookup(-1) == PowerShellTypes.SIMPLE_ID || builder.rawLookup(-1) == PowerShellTypes.GENERIC_ID_PART;
  }
}
