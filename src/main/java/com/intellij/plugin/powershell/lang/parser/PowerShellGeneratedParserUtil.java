package com.intellij.plugin.powershell.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.openapi.util.Key;
import com.intellij.plugin.powershell.psi.PowerShellTypes;
import com.intellij.psi.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.COMMENT;

/**
 * Andrey 28/06/17.
 */
public class PowerShellGeneratedParserUtil {
  private static final Key<Boolean> PARSING_CALL_ARGUMENTS = Key.create("powershell.parsing.call.arguments");

  public static boolean isNotWhiteSpace(PsiBuilder builder, int i) {
    return builder.eof() || !isWhiteSpace(builder, i);
  }

  public static boolean isWhiteSpace(PsiBuilder builder, int i) {
    return builder.eof() || builder.rawLookup(-1) == TokenType.WHITE_SPACE || builder.rawLookup(-1) == COMMENT;
  }

  public static boolean parseCommandArgumentInner(PsiBuilder b, int i, GeneratedParserUtilBase.Parser argumentsCommaListParser) {
    boolean r = argumentsCommaListParser.parse(b, i + 1);
    if (!r) {
      if (!PowerShellParser.argument_separator_condition_no_ws(b, i + 1)) {
        b.advanceLexer();
        r = true;
        while (!PowerShellParser.argument_separator_condition(b, i + 1)) {
          b.advanceLexer();
        }
      }
    }
    return r;
  }

  private static boolean isComment(PsiBuilder builder, int i) {
    return builder.eof() || builder.rawLookup(-1) == COMMENT;
  }

  public static boolean isNotComment(PsiBuilder builder, int i) {
    return builder.eof() || !isComment(builder, i);
  }

  public static boolean isIdentifierBefore(PsiBuilder builder, int i) {
    return builder.rawLookup(-1) == PowerShellTypes.SIMPLE_ID || builder.rawLookup(-1) == PowerShellTypes.GENERIC_ID_PART;
  }


  public static boolean is_parsing_configuration_block(PsiBuilder b, int l) {
    GeneratedParserUtilBase.ErrorState state = GeneratedParserUtilBase.ErrorState.get(b);
    return findParent(state.currentFrame, "<configuration block>") != null;
  }

  public static boolean is_parsing_call_arguments(PsiBuilder b, int l) {
    return b.getUserData(PARSING_CALL_ARGUMENTS) == Boolean.TRUE;
  }

  //private call_arguments ::= ncm nls? LP nls? argument_expression_list? nls? RP | primary_expression
  public static boolean parse_argument_list(PsiBuilder b, int l, GeneratedParserUtilBase.Parser argument_expression_list_parser) {
    boolean oldState = saveState(b, PARSING_CALL_ARGUMENTS, true);
    final boolean result = argument_expression_list_parser.parse(b, l);
    putState(b, PARSING_CALL_ARGUMENTS, oldState);
    return result;
  }

  public static boolean allow_any_expression(PsiBuilder b, int l, GeneratedParserUtilBase.Parser any_expression_parser) {
    boolean oldState = saveState(b, PARSING_CALL_ARGUMENTS, false);
    final boolean result = any_expression_parser.parse(b, l);
    putState(b, PARSING_CALL_ARGUMENTS, oldState);
    return result;
  }

  private static boolean saveState(PsiBuilder b, Key<Boolean> key, boolean value) {
    final boolean oldState = b.getUserData(key) == Boolean.TRUE;
    putState(b, key, value);
    return oldState;
  }

  private static void putState(PsiBuilder b, Key<Boolean> key, boolean value) {
    b.putUserData(key, value);
  }

  @Nullable
  private static GeneratedParserUtilBase.Frame findParent(@NotNull GeneratedParserUtilBase.Frame frame, @NotNull String frameName) {
    GeneratedParserUtilBase.Frame parent = frame.parentFrame;
    while (parent != null) {
      if (frameName.equals(parent.name)) {
        return parent;
      }
      parent = parent.parentFrame;
    }

    return null;
  }
}
