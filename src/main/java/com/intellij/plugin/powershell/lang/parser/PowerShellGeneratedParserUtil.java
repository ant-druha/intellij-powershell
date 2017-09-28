package com.intellij.plugin.powershell.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.plugin.powershell.psi.PowerShellTypes;
import com.intellij.psi.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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


  public static boolean is_parsing_configuration_block(PsiBuilder b, int l) {
    GeneratedParserUtilBase.ErrorState state = GeneratedParserUtilBase.ErrorState.get(b);
    return findParent(state.currentFrame, "<configuration block>") != null;
  }

  @Nullable
  private static GeneratedParserUtilBase.Frame findParent(@NotNull GeneratedParserUtilBase.Frame frame, @NotNull String frameName) {
    GeneratedParserUtilBase.Frame parent = frame.parentFrame; while (parent != null) {
      if (frameName.equals(parent.name)) {
        return parent;
      } parent = parent.parentFrame;
    }

    return null;
  }
}
