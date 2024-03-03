// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.*;
import static com.intellij.plugin.powershell.lang.parser.PowerShellGeneratedParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PowerShellParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return top_level_element(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(ARRAY_TYPE_ELEMENT, GENERIC_TYPE_ELEMENT, REFERENCE_TYPE_ELEMENT, TYPE_ELEMENT),
    create_token_set_(ADDITIVE_EXPRESSION, ARRAY_EXPRESSION, ARRAY_LITERAL_EXPRESSION, ASSIGNMENT_EXPRESSION,
      BITWISE_EXPRESSION, CAST_EXPRESSION, COMMAND_CALL_EXPRESSION, COMPARISON_EXPRESSION,
      ELEMENT_ACCESS_EXPRESSION, EXPRESSION, FORMAT_EXPRESSION, HASH_LITERAL_EXPRESSION,
      INTEGER_LITERAL_EXPRESSION, INVOCATION_EXPRESSION, KEY_EXPRESSION, LABEL_REFERENCE_EXPRESSION,
      LOGICAL_EXPRESSION, MEMBER_ACCESS_EXPRESSION, MULTIPLICATIVE_EXPRESSION, PARENTHESIZED_EXPRESSION,
      PATH_EXPRESSION, POST_DECREMENT_EXPRESSION, POST_INCREMENT_EXPRESSION, RANGE_EXPRESSION,
      REAL_LITERAL_EXPRESSION, SCRIPT_BLOCK_EXPRESSION, STRING_LITERAL_EXPRESSION, SUB_EXPRESSION,
      TARGET_VARIABLE_EXPRESSION, TYPE_LITERAL_EXPRESSION, UNARY_EXPRESSION),
  };

  /* ********************************************************** */
  // 'begin'
  //  | 'break'
  //  | 'catch'
  //  | 'continue'
  //  | 'data'
  //  | 'exit'
  //  | 'define'
  //  | 'dynamicparam'
  //  | 'else'
  //  | 'end'
  //  | 'finally'
  //  | 'foreach'
  //  | 'hidden'
  //  | 'in'
  //  | 'inlinescript'
  //  | 'parallel'
  //  | 'param'
  //  | 'process'
  //  | 'static'
  //  | 'this'
  //  | 'until'
  //  | 'workflow'
  static boolean allowed_identifier_keywords(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "allowed_identifier_keywords")) return false;
    boolean r;
    r = consumeToken(b, BEGIN);
    if (!r) r = consumeToken(b, BREAK);
    if (!r) r = consumeToken(b, CATCH);
    if (!r) r = consumeToken(b, CONTINUE);
    if (!r) r = consumeToken(b, DATA);
    if (!r) r = consumeToken(b, EXIT);
    if (!r) r = consumeToken(b, DEFINE);
    if (!r) r = consumeToken(b, DYNAMICPARAM);
    if (!r) r = consumeToken(b, ELSE);
    if (!r) r = consumeToken(b, END);
    if (!r) r = consumeToken(b, FINALLY);
    if (!r) r = consumeToken(b, FOREACH);
    if (!r) r = consumeToken(b, HIDDEN);
    if (!r) r = consumeToken(b, IN);
    if (!r) r = consumeToken(b, INLINESCRIPT);
    if (!r) r = consumeToken(b, PARALLEL);
    if (!r) r = consumeToken(b, PARAM);
    if (!r) r = consumeToken(b, PROCESS);
    if (!r) r = consumeToken(b, STATIC);
    if (!r) r = consumeToken(b, THIS);
    if (!r) r = consumeToken(b, UNTIL);
    if (!r) r = consumeToken(b, WORKFLOW);
    return r;
  }

  /* ********************************************************** */
  // <<parse_argument_list expression_list_rule>>
  static boolean argument_expression_list(PsiBuilder b, int l) {
    return parse_argument_list(b, l + 1, PowerShellParser::expression_list_rule);
  }

  /* ********************************************************** */
  // COMMA | LP | RP | LCURLY | RCURLY | sep | DS | PIPE | AMP | BACKTICK | DQ_OPEN | DQ_CLOSE | nls | <<eof>>
  static boolean argument_separation_token_no_ws(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_separation_token_no_ws")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, LP);
    if (!r) r = consumeToken(b, RP);
    if (!r) r = consumeToken(b, LCURLY);
    if (!r) r = consumeToken(b, RCURLY);
    if (!r) r = sep(b, l + 1);
    if (!r) r = consumeToken(b, DS);
    if (!r) r = consumeToken(b, PIPE);
    if (!r) r = consumeToken(b, AMP);
    if (!r) r = consumeToken(b, BACKTICK);
    if (!r) r = consumeToken(b, DQ_OPEN);
    if (!r) r = consumeToken(b, DQ_CLOSE);
    if (!r) r = nls(b, l + 1);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // &argument_separator_token
  static boolean argument_separator_condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_separator_condition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = argument_separator_token(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // &argument_separation_token_no_ws
  static boolean argument_separator_condition_no_ws(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_separator_condition_no_ws")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = argument_separation_token_no_ws(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // argument_separation_token_no_ws | ws
  static boolean argument_separator_token(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_separator_token")) return false;
    boolean r;
    r = argument_separation_token_no_ws(b, l + 1);
    if (!r) r = ws(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // AT nws LP nls? statement_list_rule? nls? RP
  public static boolean array_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expression")) return false;
    if (!nextTokenIsFast(b, AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, AT);
    r = r && nws(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && array_expression_3(b, l + 1);
    r = r && array_expression_4(b, l + 1);
    r = r && array_expression_5(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, ARRAY_EXPRESSION, r);
    return r;
  }

  // nls?
  private static boolean array_expression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expression_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // statement_list_rule?
  private static boolean array_expression_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expression_4")) return false;
    statement_list_rule(b, l + 1);
    return true;
  }

  // nls?
  private static boolean array_expression_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expression_5")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // array_type_name nls? dimension? SQBR_R
  public static boolean array_type_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_TYPE_ELEMENT, "<array type element>");
    r = array_type_name(b, l + 1);
    r = r && array_type_element_1(b, l + 1);
    r = r && array_type_element_2(b, l + 1);
    r = r && consumeToken(b, SQBR_R);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls?
  private static boolean array_type_element_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_element_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // dimension?
  private static boolean array_type_element_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_element_2")) return false;
    dimension(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // reference_type_element SQBR_L
  static boolean array_type_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_name")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = reference_type_element(b, l + 1);
    r = r && consumeToken(b, SQBR_L);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EQ | EQ_DASH | EQ_PLUS | EQ_STAR | EQ_DIV | EQ_PERS
  static boolean assignment_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_operator")) return false;
    boolean r;
    r = consumeToken(b, EQ);
    if (!r) r = consumeToken(b, EQ_DASH);
    if (!r) r = consumeToken(b, EQ_PLUS);
    if (!r) r = consumeToken(b, EQ_STAR);
    if (!r) r = consumeToken(b, EQ_DIV);
    if (!r) r = consumeToken(b, EQ_PERS);
    return r;
  }

  /* ********************************************************** */
  // SQBR_L nls? attribute_name LP nls? attribute_arguments? nls? RP nls? SQBR_R | type_literal_expression
  public static boolean attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute")) return false;
    if (!nextTokenIs(b, SQBR_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_0(b, l + 1);
    if (!r) r = type_literal_expression(b, l + 1);
    exit_section_(b, m, ATTRIBUTE, r);
    return r;
  }

  // SQBR_L nls? attribute_name LP nls? attribute_arguments? nls? RP nls? SQBR_R
  private static boolean attribute_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SQBR_L);
    r = r && attribute_0_1(b, l + 1);
    r = r && attribute_name(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && attribute_0_4(b, l + 1);
    r = r && attribute_0_5(b, l + 1);
    r = r && attribute_0_6(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && attribute_0_8(b, l + 1);
    r = r && consumeToken(b, SQBR_R);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean attribute_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean attribute_0_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0_4")) return false;
    nls(b, l + 1);
    return true;
  }

  // attribute_arguments?
  private static boolean attribute_0_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0_5")) return false;
    attribute_arguments(b, l + 1);
    return true;
  }

  // nls?
  private static boolean attribute_0_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0_6")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean attribute_0_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0_8")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // simple_name_identifier '=' nls? expression | simple_name_identifier | expression
  public static boolean attribute_argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_argument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATTRIBUTE_ARGUMENT, "<attribute argument>");
    r = attribute_argument_0(b, l + 1);
    if (!r) r = simple_name_identifier(b, l + 1);
    if (!r) r = expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // simple_name_identifier '=' nls? expression
  private static boolean attribute_argument_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_argument_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = simple_name_identifier(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && attribute_argument_0_2(b, l + 1);
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean attribute_argument_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_argument_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parse_argument_list attribute_arguments_rule>>
  static boolean attribute_arguments(PsiBuilder b, int l) {
    return parse_argument_list(b, l + 1, PowerShellParser::attribute_arguments_rule);
  }

  /* ********************************************************** */
  // attribute_argument (nls? COMMA nls? attribute_argument)*
  static boolean attribute_arguments_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_arguments_rule")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_argument(b, l + 1);
    r = r && attribute_arguments_rule_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nls? COMMA nls? attribute_argument)*
  private static boolean attribute_arguments_rule_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_arguments_rule_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!attribute_arguments_rule_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "attribute_arguments_rule_1", c)) break;
    }
    return true;
  }

  // nls? COMMA nls? attribute_argument
  private static boolean attribute_arguments_rule_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_arguments_rule_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_arguments_rule_1_0_0(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && attribute_arguments_rule_1_0_2(b, l + 1);
    r = r && attribute_argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean attribute_arguments_rule_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_arguments_rule_1_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean attribute_arguments_rule_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_arguments_rule_1_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (attribute nls?)+ !(nws member_access_operator)
  static boolean attribute_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_list")) return false;
    if (!nextTokenIs(b, SQBR_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_list_0(b, l + 1);
    r = r && attribute_list_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (attribute nls?)+
  private static boolean attribute_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_list_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_list_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!attribute_list_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "attribute_list_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // attribute nls?
  private static boolean attribute_list_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_list_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute(b, l + 1);
    r = r && attribute_list_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean attribute_list_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_list_0_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // !(nws member_access_operator)
  private static boolean attribute_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_list_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !attribute_list_1_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nws member_access_operator
  private static boolean attribute_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nws(b, l + 1);
    r = r && member_access_operator(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // type_element
  static boolean attribute_name(PsiBuilder b, int l) {
    return type_element(b, l + 1);
  }

  /* ********************************************************** */
  // COLON nls? base_class_name
  static boolean base_class_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "base_class_clause")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && base_class_clause_1(b, l + 1);
    r = r && base_class_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean base_class_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "base_class_clause_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // type_element
  static boolean base_class_name(PsiBuilder b, int l) {
    return type_element(b, l + 1);
  }

  /* ********************************************************** */
  // COLON 'base' method_argument_expression_list
  static boolean base_constructor_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "base_constructor_call")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && consumeToken(b, "base");
    r = r && method_argument_expression_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // OP_C | OP_NOT | OP_BNOT | OP_BAND | OP_BOR | OP_BXOR | OP_AND | OP_OR | OP_XOR
  static boolean binary_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_operator")) return false;
    boolean r;
    r = consumeToken(b, OP_C);
    if (!r) r = consumeToken(b, OP_NOT);
    if (!r) r = consumeToken(b, OP_BNOT);
    if (!r) r = consumeToken(b, OP_BAND);
    if (!r) r = consumeToken(b, OP_BOR);
    if (!r) r = consumeToken(b, OP_BXOR);
    if (!r) r = consumeToken(b, OP_AND);
    if (!r) r = consumeToken(b, OP_OR);
    if (!r) r = consumeToken(b, OP_XOR);
    return r;
  }

  /* ********************************************************** */
  // nls? block_body_content nls? | nls
  public static boolean block_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, BLOCK_BODY, "<block body>");
    r = block_body_0(b, l + 1);
    if (!r) r = nls(b, l + 1);
    exit_section_(b, l, m, r, false, PowerShellParser::block_body_recover);
    return r;
  }

  // nls? block_body_content nls?
  private static boolean block_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block_body_0_0(b, l + 1);
    r = r && block_body_content(b, l + 1);
    r = r && block_body_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean block_body_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean block_body_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (statement_list | statement_terminators)+
  static boolean block_body_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body_content")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = block_body_content_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!block_body_content_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "block_body_content", c)) break;
    }
    exit_section_(b, l, m, r, false, PowerShellParser::statement_block_recover);
    return r;
  }

  // statement_list | statement_terminators
  private static boolean block_body_content_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body_content_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_list(b, l + 1);
    if (!r) r = statement_terminators(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !RCURLY
  static boolean block_body_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeTokenFast(b, RCURLY);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // reserved_statement_keywords | sep | nls | RCURLY
  static boolean block_body_stop_tokens(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body_stop_tokens")) return false;
    boolean r;
    r = reserved_statement_keywords(b, l + 1);
    if (!r) r = sep(b, l + 1);
    if (!r) r = nls(b, l + 1);
    if (!r) r = consumeToken(b, RCURLY);
    return r;
  }

  /* ********************************************************** */
  // 'dynamicparam' | 'begin' | 'process' | 'end'
  static boolean block_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_name")) return false;
    boolean r;
    r = consumeToken(b, DYNAMICPARAM);
    if (!r) r = consumeToken(b, BEGIN);
    if (!r) r = consumeToken(b, PROCESS);
    if (!r) r = consumeToken(b, END);
    return r;
  }

  /* ********************************************************** */
  // attribute_list? nls? 'param' nls? LP nls? script_parameter_list? nls? RP
  public static boolean block_parameter_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_parameter_clause")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BLOCK_PARAMETER_CLAUSE, "<block parameter clause>");
    r = block_parameter_clause_0(b, l + 1);
    r = r && block_parameter_clause_1(b, l + 1);
    r = r && consumeToken(b, PARAM);
    p = r; // pin = 3
    r = r && report_error_(b, block_parameter_clause_3(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LP)) && r;
    r = p && report_error_(b, block_parameter_clause_5(b, l + 1)) && r;
    r = p && report_error_(b, block_parameter_clause_6(b, l + 1)) && r;
    r = p && report_error_(b, block_parameter_clause_7(b, l + 1)) && r;
    r = p && consumeToken(b, RP) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // attribute_list?
  private static boolean block_parameter_clause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_parameter_clause_0")) return false;
    attribute_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean block_parameter_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_parameter_clause_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean block_parameter_clause_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_parameter_clause_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean block_parameter_clause_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_parameter_clause_5")) return false;
    nls(b, l + 1);
    return true;
  }

  // script_parameter_list?
  private static boolean block_parameter_clause_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_parameter_clause_6")) return false;
    script_parameter_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean block_parameter_clause_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_parameter_clause_7")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // BRACED_VAR_START (nws declaration_scope)? variable_name_braced RCURLY
  static boolean braced_variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "braced_variable")) return false;
    if (!nextTokenIs(b, BRACED_VAR_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACED_VAR_START);
    r = r && braced_variable_1(b, l + 1);
    r = r && variable_name_braced(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nws declaration_scope)?
  private static boolean braced_variable_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "braced_variable_1")) return false;
    braced_variable_1_0(b, l + 1);
    return true;
  }

  // nws declaration_scope
  private static boolean braced_variable_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "braced_variable_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nws(b, l + 1);
    r = r && declaration_scope(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ('break' | 'continue') label_reference_expression?
  static boolean break_continue_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "break_continue_statement")) return false;
    if (!nextTokenIs(b, "", BREAK, CONTINUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = break_continue_statement_0(b, l + 1);
    r = r && break_continue_statement_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'break' | 'continue'
  private static boolean break_continue_statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "break_continue_statement_0")) return false;
    boolean r;
    r = consumeToken(b, BREAK);
    if (!r) r = consumeToken(b, CONTINUE);
    return r;
  }

  // label_reference_expression?
  private static boolean break_continue_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "break_continue_statement_1")) return false;
    label_reference_expression(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ncm nls? parenthesized_argument_list | primary_expression
  static boolean call_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_arguments")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = call_arguments_0(b, l + 1);
    if (!r) r = primary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ncm nls? parenthesized_argument_list
  private static boolean call_arguments_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_arguments_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ncm(b, l + 1);
    r = r && call_arguments_0_1(b, l + 1);
    r = r && parenthesized_argument_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean call_arguments_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_arguments_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // type_literal_expression unary_expression
  public static boolean cast_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cast_expression")) return false;
    if (!nextTokenIsFast(b, SQBR_L)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, CAST_EXPRESSION, null);
    r = type_literal_expression(b, l + 1);
    r = r && unary_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'catch' nls? catch_type_list? nls? statement_block
  public static boolean catch_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clause")) return false;
    if (!nextTokenIs(b, CATCH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CATCH_CLAUSE, null);
    r = consumeToken(b, CATCH);
    p = r; // pin = 1
    r = r && report_error_(b, catch_clause_1(b, l + 1));
    r = p && report_error_(b, catch_clause_2(b, l + 1)) && r;
    r = p && report_error_(b, catch_clause_3(b, l + 1)) && r;
    r = p && statement_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean catch_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clause_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // catch_type_list?
  private static boolean catch_clause_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clause_2")) return false;
    catch_type_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean catch_clause_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clause_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // catch_clause (nls? catch_clause)*
  static boolean catch_clauses(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clauses")) return false;
    if (!nextTokenIs(b, CATCH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = catch_clause(b, l + 1);
    r = r && catch_clauses_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nls? catch_clause)*
  private static boolean catch_clauses_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clauses_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!catch_clauses_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "catch_clauses_1", c)) break;
    }
    return true;
  }

  // nls? catch_clause
  private static boolean catch_clauses_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clauses_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = catch_clauses_1_0_0(b, l + 1);
    r = r && catch_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean catch_clauses_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clauses_1_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // type_literal_expression nls? ( ',' nls? type_literal_expression nls? )*
  static boolean catch_type_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list")) return false;
    if (!nextTokenIsFast(b, SQBR_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_literal_expression(b, l + 1);
    r = r && catch_type_list_1(b, l + 1);
    r = r && catch_type_list_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean catch_type_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // ( ',' nls? type_literal_expression nls? )*
  private static boolean catch_type_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!catch_type_list_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "catch_type_list_2", c)) break;
    }
    return true;
  }

  // ',' nls? type_literal_expression nls?
  private static boolean catch_type_list_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && catch_type_list_2_0_1(b, l + 1);
    r = r && type_literal_expression(b, l + 1);
    r = r && catch_type_list_2_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean catch_type_list_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_2_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean catch_type_list_2_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_2_0_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LCURLY class_block_body? RCURLY
  static boolean class_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_block")) return false;
    if (!nextTokenIs(b, LCURLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LCURLY);
    p = r; // pin = 1
    r = r && report_error_(b, class_block_1(b, l + 1));
    r = p && consumeToken(b, RCURLY) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // class_block_body?
  private static boolean class_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_block_1")) return false;
    class_block_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? (class_member_declaration_list | statement_terminators)+ nls? | nls
  public static boolean class_block_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_block_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BLOCK_BODY, "<class block body>");
    r = class_block_body_0(b, l + 1);
    if (!r) r = nls(b, l + 1);
    exit_section_(b, l, m, r, false, PowerShellParser::block_body_recover);
    return r;
  }

  // nls? (class_member_declaration_list | statement_terminators)+ nls?
  private static boolean class_block_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_block_body_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_block_body_0_0(b, l + 1);
    r = r && class_block_body_0_1(b, l + 1);
    r = r && class_block_body_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean class_block_body_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_block_body_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // (class_member_declaration_list | statement_terminators)+
  private static boolean class_block_body_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_block_body_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_block_body_0_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!class_block_body_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "class_block_body_0_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // class_member_declaration_list | statement_terminators
  private static boolean class_block_body_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_block_body_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_member_declaration_list(b, l + 1);
    if (!r) r = statement_terminators(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean class_block_body_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_block_body_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // attribute_list? 'class' class_name (nls? base_class_clause)? (nls? interface_list)? nls? class_block
  public static boolean class_declaration_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_declaration_statement")) return false;
    if (!nextTokenIs(b, "<class declaration statement>", CLASS, SQBR_L)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_DECLARATION_STATEMENT, "<class declaration statement>");
    r = class_declaration_statement_0(b, l + 1);
    r = r && consumeToken(b, CLASS);
    r = r && class_name(b, l + 1);
    r = r && class_declaration_statement_3(b, l + 1);
    r = r && class_declaration_statement_4(b, l + 1);
    r = r && class_declaration_statement_5(b, l + 1);
    r = r && class_block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // attribute_list?
  private static boolean class_declaration_statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_declaration_statement_0")) return false;
    attribute_list(b, l + 1);
    return true;
  }

  // (nls? base_class_clause)?
  private static boolean class_declaration_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_declaration_statement_3")) return false;
    class_declaration_statement_3_0(b, l + 1);
    return true;
  }

  // nls? base_class_clause
  private static boolean class_declaration_statement_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_declaration_statement_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_declaration_statement_3_0_0(b, l + 1);
    r = r && base_class_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean class_declaration_statement_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_declaration_statement_3_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // (nls? interface_list)?
  private static boolean class_declaration_statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_declaration_statement_4")) return false;
    class_declaration_statement_4_0(b, l + 1);
    return true;
  }

  // nls? interface_list
  private static boolean class_declaration_statement_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_declaration_statement_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = class_declaration_statement_4_0_0(b, l + 1);
    r = r && interface_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean class_declaration_statement_4_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_declaration_statement_4_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean class_declaration_statement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_declaration_statement_5")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // property_declaration_statement | constructor_declaration_statement | method_declaration_statement | incomplete_declaration
  static boolean class_member_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_member_declaration")) return false;
    boolean r;
    r = property_declaration_statement(b, l + 1);
    if (!r) r = constructor_declaration_statement(b, l + 1);
    if (!r) r = method_declaration_statement(b, l + 1);
    if (!r) r = incomplete_declaration(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // class_member_declaration (statement_terminators class_member_declaration)*
  static boolean class_member_declaration_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_member_declaration_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = class_member_declaration(b, l + 1);
    r = r && class_member_declaration_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, PowerShellParser::top_level_recover);
    return r;
  }

  // (statement_terminators class_member_declaration)*
  private static boolean class_member_declaration_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_member_declaration_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!class_member_declaration_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "class_member_declaration_list_1", c)) break;
    }
    return true;
  }

  // statement_terminators class_member_declaration
  private static boolean class_member_declaration_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_member_declaration_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_terminators(b, l + 1);
    r = r && class_member_declaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // simple_name_identifier
  static boolean class_name(PsiBuilder b, int l) {
    return simple_name_identifier(b, l + 1);
  }

  /* ********************************************************** */
  // parse_command_argument
  public static boolean command_argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_argument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMAND_ARGUMENT, "<command argument>");
    r = parse_command_argument(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // command_argument ( (COMMA | EQ) command_argument)* | array_literal_expression
  static boolean command_argument_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_argument_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_argument_list_0(b, l + 1);
    if (!r) r = expression(b, l + 1, 7);
    exit_section_(b, m, null, r);
    return r;
  }

  // command_argument ( (COMMA | EQ) command_argument)*
  private static boolean command_argument_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_argument_list_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_argument(b, l + 1);
    r = r && command_argument_list_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( (COMMA | EQ) command_argument)*
  private static boolean command_argument_list_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_argument_list_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!command_argument_list_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "command_argument_list_0_1", c)) break;
    }
    return true;
  }

  // (COMMA | EQ) command_argument
  private static boolean command_argument_list_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_argument_list_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_argument_list_0_1_0_0(b, l + 1);
    r = r && command_argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA | EQ
  private static boolean command_argument_list_0_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_argument_list_0_1_0_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, EQ);
    return r;
  }

  /* ********************************************************** */
  // command_invocation_operator ( (command_module command_name_expr command_element* ) | command_name_expr command_element* ) | command_name_expression command_element*
  public static boolean command_call_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_call_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, COMMAND_CALL_EXPRESSION, "<command call expression>");
    r = command_call_expression_0(b, l + 1);
    if (!r) r = command_call_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // command_invocation_operator ( (command_module command_name_expr command_element* ) | command_name_expr command_element* )
  private static boolean command_call_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_call_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_invocation_operator(b, l + 1);
    r = r && command_call_expression_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (command_module command_name_expr command_element* ) | command_name_expr command_element*
  private static boolean command_call_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_call_expression_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_call_expression_0_1_0(b, l + 1);
    if (!r) r = command_call_expression_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // command_module command_name_expr command_element*
  private static boolean command_call_expression_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_call_expression_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_module(b, l + 1);
    r = r && command_name_expr(b, l + 1);
    r = r && command_call_expression_0_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // command_element*
  private static boolean command_call_expression_0_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_call_expression_0_1_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!command_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "command_call_expression_0_1_0_2", c)) break;
    }
    return true;
  }

  // command_name_expr command_element*
  private static boolean command_call_expression_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_call_expression_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_name_expr(b, l + 1);
    r = r && command_call_expression_0_1_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // command_element*
  private static boolean command_call_expression_0_1_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_call_expression_0_1_1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!command_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "command_call_expression_0_1_1_1", c)) break;
    }
    return true;
  }

  // command_name_expression command_element*
  private static boolean command_call_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_call_expression_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_name_expression(b, l + 1);
    r = r && command_call_expression_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // command_element*
  private static boolean command_call_expression_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_call_expression_1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!command_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "command_call_expression_1_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // command_parameter | redirection | command_argument_list
  static boolean command_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_element")) return false;
    boolean r;
    r = command_parameter(b, l + 1);
    if (!r) r = redirection(b, l + 1);
    if (!r) r = command_argument_list(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // '&' | DOT
  static boolean command_invocation_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_invocation_operator")) return false;
    if (!nextTokenIs(b, "", AMP, DOT)) return false;
    boolean r;
    r = consumeToken(b, AMP);
    if (!r) r = consumeToken(b, DOT);
    return r;
  }

  /* ********************************************************** */
  // primary_expression
  static boolean command_module(PsiBuilder b, int l) {
    return primary_expression(b, l + 1);
  }

  /* ********************************************************** */
  // command_name_identifier
  public static boolean command_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMAND_NAME, "<command name>");
    r = command_name_identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // generic_token_chars | allowed_identifier_keywords
  public static boolean command_name_char_tokens(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_name_char_tokens")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, REFERENCE_IDENTIFIER, "<command name char tokens>");
    r = generic_token_chars(b, l + 1);
    if (!r) r = allowed_identifier_keywords(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // primary_expression | command_name_expression
  static boolean command_name_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_name_expr")) return false;
    boolean r;
    r = primary_expression(b, l + 1);
    if (!r) r = command_name_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // path_expression | command_name
  static boolean command_name_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_name_expression")) return false;
    boolean r;
    r = path_expression(b, l + 1);
    if (!r) r = command_name(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // generic_token_with_sub_expr | generic_token_part
  public static boolean command_name_identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_name_identifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, IDENTIFIER, "<command name identifier>");
    r = generic_token_with_sub_expr(b, l + 1);
    if (!r) r = generic_token_part(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CMD_PARAMETER | binary_operator
  public static boolean command_parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_parameter")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMAND_PARAMETER, "<command parameter>");
    r = consumeToken(b, CMD_PARAMETER);
    if (!r) r = binary_operator(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !is_id requires_comment | !is_id SINGLE_LINE_COMMENT | DELIMITED_COMMENT
  public static boolean comment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMENT, "<comment>");
    r = comment_0(b, l + 1);
    if (!r) r = comment_1(b, l + 1);
    if (!r) r = consumeToken(b, DELIMITED_COMMENT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // !is_id requires_comment
  private static boolean comment_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = comment_0_0(b, l + 1);
    r = r && requires_comment(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !is_id
  private static boolean comment_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !is_id(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // !is_id SINGLE_LINE_COMMENT
  private static boolean comment_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = comment_1_0(b, l + 1);
    r = r && consumeToken(b, SINGLE_LINE_COMMENT);
    exit_section_(b, m, null, r);
    return r;
  }

  // !is_id
  private static boolean comment_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !is_id(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // OP_C
  static boolean comparison_operator(PsiBuilder b, int l) {
    return consumeToken(b, OP_C);
  }

  /* ********************************************************** */
  // DOT (SIMPLE_ID | allowed_identifier_keywords)
  public static boolean compound_type_identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_type_identifier")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, REFERENCE_TYPE_ELEMENT, null);
    r = consumeToken(b, DOT);
    r = r && compound_type_identifier_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SIMPLE_ID | allowed_identifier_keywords
  private static boolean compound_type_identifier_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_type_identifier_1")) return false;
    boolean r;
    r = consumeToken(b, SIMPLE_ID);
    if (!r) r = allowed_identifier_keywords(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // attribute_list? 'configuration' nls? configuration_name nls? configuration_block_block
  public static boolean configuration_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONFIGURATION_BLOCK, "<configuration block>");
    r = configuration_block_0(b, l + 1);
    r = r && consumeToken(b, CONFIGURATION);
    p = r; // pin = 2
    r = r && report_error_(b, configuration_block_2(b, l + 1));
    r = p && report_error_(b, configuration_name(b, l + 1)) && r;
    r = p && report_error_(b, configuration_block_4(b, l + 1)) && r;
    r = p && configuration_block_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, PowerShellParser::configuration_block_recover);
    return r || p;
  }

  // attribute_list?
  private static boolean configuration_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_0")) return false;
    attribute_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean configuration_block_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_2")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean configuration_block_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_4")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LCURLY configuration_block_body? RCURLY
  static boolean configuration_block_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_block")) return false;
    if (!nextTokenIs(b, LCURLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LCURLY);
    p = r; // pin = 1
    r = r && report_error_(b, configuration_block_block_1(b, l + 1));
    r = p && consumeToken(b, RCURLY) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // configuration_block_body?
  private static boolean configuration_block_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_block_1")) return false;
    configuration_block_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // top_script_block
  public static boolean configuration_block_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, BLOCK_BODY, "<configuration block body>");
    r = top_script_block(b, l + 1);
    exit_section_(b, l, m, r, false, PowerShellParser::configuration_block_body_recover);
    return r;
  }

  /* ********************************************************** */
  // !(nls? RCURLY statement_terminators? ('configuration' | <<eof>> | SQBR_L | statement_stop_tokens))
  static boolean configuration_block_body_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_body_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !configuration_block_body_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls? RCURLY statement_terminators? ('configuration' | <<eof>> | SQBR_L | statement_stop_tokens)
  private static boolean configuration_block_body_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_body_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = configuration_block_body_recover_0_0(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    r = r && configuration_block_body_recover_0_2(b, l + 1);
    r = r && configuration_block_body_recover_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean configuration_block_body_recover_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_body_recover_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // statement_terminators?
  private static boolean configuration_block_body_recover_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_body_recover_0_2")) return false;
    statement_terminators(b, l + 1);
    return true;
  }

  // 'configuration' | <<eof>> | SQBR_L | statement_stop_tokens
  private static boolean configuration_block_body_recover_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_body_recover_0_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, CONFIGURATION);
    if (!r) r = eof(b, l + 1);
    if (!r) r = consumeTokenFast(b, SQBR_L);
    if (!r) r = statement_stop_tokens(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // configuration_block (statement_terminators configuration_block)* statement_terminators?
  static boolean configuration_block_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_list")) return false;
    if (!nextTokenIs(b, "", CONFIGURATION, SQBR_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = configuration_block(b, l + 1);
    r = r && configuration_block_list_1(b, l + 1);
    r = r && configuration_block_list_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (statement_terminators configuration_block)*
  private static boolean configuration_block_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!configuration_block_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "configuration_block_list_1", c)) break;
    }
    return true;
  }

  // statement_terminators configuration_block
  private static boolean configuration_block_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_terminators(b, l + 1);
    r = r && configuration_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_terminators?
  private static boolean configuration_block_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_list_2")) return false;
    statement_terminators(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !(statement_terminators? ('configuration' | <<eof>> | SQBR_L| statement_stop_tokens))
  static boolean configuration_block_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !configuration_block_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // statement_terminators? ('configuration' | <<eof>> | SQBR_L| statement_stop_tokens)
  private static boolean configuration_block_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = configuration_block_recover_0_0(b, l + 1);
    r = r && configuration_block_recover_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_terminators?
  private static boolean configuration_block_recover_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_recover_0_0")) return false;
    statement_terminators(b, l + 1);
    return true;
  }

  // 'configuration' | <<eof>> | SQBR_L| statement_stop_tokens
  private static boolean configuration_block_recover_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "configuration_block_recover_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, CONFIGURATION);
    if (!r) r = eof(b, l + 1);
    if (!r) r = consumeTokenFast(b, SQBR_L);
    if (!r) r = statement_stop_tokens(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // function_name
  static boolean configuration_name(PsiBuilder b, int l) {
    return function_name(b, l + 1);
  }

  /* ********************************************************** */
  // class_name method_argument_expression_list nls? base_constructor_call? nls? method_block
  public static boolean constructor_declaration_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_declaration_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONSTRUCTOR_DECLARATION_STATEMENT, "<constructor declaration statement>");
    r = class_name(b, l + 1);
    r = r && method_argument_expression_list(b, l + 1);
    r = r && constructor_declaration_statement_2(b, l + 1);
    r = r && constructor_declaration_statement_3(b, l + 1);
    r = r && constructor_declaration_statement_4(b, l + 1);
    r = r && method_block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls?
  private static boolean constructor_declaration_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_declaration_statement_2")) return false;
    nls(b, l + 1);
    return true;
  }

  // base_constructor_call?
  private static boolean constructor_declaration_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_declaration_statement_3")) return false;
    base_constructor_call(b, l + 1);
    return true;
  }

  // nls?
  private static boolean constructor_declaration_statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constructor_declaration_statement_4")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // command_name_expr
  static boolean data_command(PsiBuilder b, int l) {
    return command_name_expr(b, l + 1);
  }

  /* ********************************************************** */
  // '-supportedcommand' nls? data_commands_list
  static boolean data_commands_allowed(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_allowed")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "-supportedcommand");
    r = r && data_commands_allowed_1(b, l + 1);
    r = r && data_commands_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean data_commands_allowed_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_allowed_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // data_command ( nls? ',' nls? data_command )*
  static boolean data_commands_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = data_command(b, l + 1);
    r = r && data_commands_list_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( nls? ',' nls? data_command )*
  private static boolean data_commands_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!data_commands_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "data_commands_list_1", c)) break;
    }
    return true;
  }

  // nls? ',' nls? data_command
  private static boolean data_commands_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = data_commands_list_1_0_0(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && data_commands_list_1_0_2(b, l + 1);
    r = r && data_command(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean data_commands_list_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list_1_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean data_commands_list_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list_1_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // simple_name_identifier
  static boolean data_name(PsiBuilder b, int l) {
    return simple_name_identifier(b, l + 1);
  }

  /* ********************************************************** */
  // 'data' nls? data_name? nls? data_commands_allowed? nls? statement_block
  public static boolean data_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_statement")) return false;
    if (!nextTokenIs(b, DATA)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DATA_STATEMENT, null);
    r = consumeToken(b, DATA);
    p = r; // pin = 1
    r = r && report_error_(b, data_statement_1(b, l + 1));
    r = p && report_error_(b, data_statement_2(b, l + 1)) && r;
    r = p && report_error_(b, data_statement_3(b, l + 1)) && r;
    r = p && report_error_(b, data_statement_4(b, l + 1)) && r;
    r = p && report_error_(b, data_statement_5(b, l + 1)) && r;
    r = p && statement_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean data_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // data_name?
  private static boolean data_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_statement_2")) return false;
    data_name(b, l + 1);
    return true;
  }

  // nls?
  private static boolean data_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_statement_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // data_commands_allowed?
  private static boolean data_statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_statement_4")) return false;
    data_commands_allowed(b, l + 1);
    return true;
  }

  // nls?
  private static boolean data_statement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_statement_5")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // DEC_INTEGER
  static boolean decimal_integer_literal(PsiBuilder b, int l) {
    return consumeToken(b, DEC_INTEGER);
  }

  /* ********************************************************** */
  // 'hidden' | 'static'
  static boolean declaration_modifier_attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declaration_modifier_attribute")) return false;
    if (!nextTokenIs(b, "", HIDDEN, STATIC)) return false;
    boolean r;
    r = consumeToken(b, HIDDEN);
    if (!r) r = consumeToken(b, STATIC);
    return r;
  }

  /* ********************************************************** */
  // 'global:' | 'local:' | 'private:' | 'script:' | 'using:' | 'workflow:' | variable_namespace
  static boolean declaration_scope(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declaration_scope")) return false;
    boolean r;
    r = consumeToken(b, "global:");
    if (!r) r = consumeToken(b, "local:");
    if (!r) r = consumeToken(b, "private:");
    if (!r) r = consumeToken(b, "script:");
    if (!r) r = consumeToken(b, "using:");
    if (!r) r = consumeToken(b, "workflow:");
    if (!r) r = variable_namespace(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // COMMA+
  static boolean dimension(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dimension")) return false;
    if (!nextTokenIs(b, COMMA)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMA)) break;
      if (!empty_element_parsed_guard_(b, "dimension", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'do' nls? statement_block nls? ( 'while' | 'until' ) nls? LP nls? while_condition nls? RP
  public static boolean do_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement")) return false;
    if (!nextTokenIs(b, DO)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DO_STATEMENT, null);
    r = consumeToken(b, DO);
    p = r; // pin = 1
    r = r && report_error_(b, do_statement_1(b, l + 1));
    r = p && report_error_(b, statement_block(b, l + 1)) && r;
    r = p && report_error_(b, do_statement_3(b, l + 1)) && r;
    r = p && report_error_(b, do_statement_4(b, l + 1)) && r;
    r = p && report_error_(b, do_statement_5(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, LP)) && r;
    r = p && report_error_(b, do_statement_7(b, l + 1)) && r;
    r = p && report_error_(b, while_condition(b, l + 1)) && r;
    r = p && report_error_(b, do_statement_9(b, l + 1)) && r;
    r = p && consumeToken(b, RP) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean do_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean do_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // 'while' | 'until'
  private static boolean do_statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement_4")) return false;
    boolean r;
    r = consumeToken(b, WHILE);
    if (!r) r = consumeToken(b, UNTIL);
    return r;
  }

  // nls?
  private static boolean do_statement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement_5")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean do_statement_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement_7")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean do_statement_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement_9")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // DQ_CLOSE
  static boolean dq_close(PsiBuilder b, int l) {
    return consumeToken(b, DQ_CLOSE);
  }

  /* ********************************************************** */
  // DQ_OPEN
  static boolean dq_open(PsiBuilder b, int l) {
    return consumeToken(b, DQ_OPEN);
  }

  /* ********************************************************** */
  // nws SQBR_L nls? expression nls? SQBR_R
  public static boolean element_access_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_access_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, ELEMENT_ACCESS_EXPRESSION, "<element access expression>");
    r = nws(b, l + 1);
    r = r && consumeToken(b, SQBR_L);
    r = r && element_access_expression_2(b, l + 1);
    r = r && expression(b, l + 1, -1);
    r = r && element_access_expression_4(b, l + 1);
    r = r && consumeToken(b, SQBR_R);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls?
  private static boolean element_access_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_access_expression_2")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean element_access_expression_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_access_expression_4")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'else' nls? statement_block
  public static boolean else_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "else_clause")) return false;
    if (!nextTokenIs(b, ELSE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ELSE_CLAUSE, null);
    r = consumeToken(b, ELSE);
    p = r; // pin = 1
    r = r && report_error_(b, else_clause_1(b, l + 1));
    r = p && statement_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean else_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "else_clause_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'elseif' nls? LP nls? pipeline nls? RP nls? statement_block
  public static boolean elseif_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause")) return false;
    if (!nextTokenIs(b, ELSEIF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ELSEIF_CLAUSE, null);
    r = consumeToken(b, ELSEIF);
    p = r; // pin = 1
    r = r && report_error_(b, elseif_clause_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LP)) && r;
    r = p && report_error_(b, elseif_clause_3(b, l + 1)) && r;
    r = p && report_error_(b, pipeline(b, l + 1)) && r;
    r = p && report_error_(b, elseif_clause_5(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, RP)) && r;
    r = p && report_error_(b, elseif_clause_7(b, l + 1)) && r;
    r = p && statement_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean elseif_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean elseif_clause_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean elseif_clause_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause_5")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean elseif_clause_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause_7")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LCURLY enum_block_body? RCURLY
  static boolean enum_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_block")) return false;
    if (!nextTokenIs(b, LCURLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LCURLY);
    p = r; // pin = 1
    r = r && report_error_(b, enum_block_1(b, l + 1));
    r = p && consumeToken(b, RCURLY) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // enum_block_body?
  private static boolean enum_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_block_1")) return false;
    enum_block_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? (label_list | statement_terminators)+ nls? | nls
  public static boolean enum_block_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_block_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BLOCK_BODY, "<enum block body>");
    r = enum_block_body_0(b, l + 1);
    if (!r) r = nls(b, l + 1);
    exit_section_(b, l, m, r, false, PowerShellParser::block_body_recover);
    return r;
  }

  // nls? (label_list | statement_terminators)+ nls?
  private static boolean enum_block_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_block_body_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = enum_block_body_0_0(b, l + 1);
    r = r && enum_block_body_0_1(b, l + 1);
    r = r && enum_block_body_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean enum_block_body_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_block_body_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // (label_list | statement_terminators)+
  private static boolean enum_block_body_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_block_body_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = enum_block_body_0_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!enum_block_body_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "enum_block_body_0_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // label_list | statement_terminators
  private static boolean enum_block_body_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_block_body_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = label_list(b, l + 1);
    if (!r) r = statement_terminators(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean enum_block_body_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_block_body_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'enum' enum_name nls? enum_block
  static boolean enum_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_declaration")) return false;
    if (!nextTokenIs(b, ENUM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ENUM);
    r = r && enum_name(b, l + 1);
    r = r && enum_declaration_2(b, l + 1);
    r = r && enum_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean enum_declaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_declaration_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // enum_declaration | enum_with_flags_declaration
  public static boolean enum_declaration_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_declaration_statement")) return false;
    if (!nextTokenIs(b, "<enum declaration statement>", ENUM, SQBR_L)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ENUM_DECLARATION_STATEMENT, "<enum declaration statement>");
    r = enum_declaration(b, l + 1);
    if (!r) r = enum_with_flags_declaration(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // label_identifier_expression (EQ expression)?
  public static boolean enum_label_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_label_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ENUM_LABEL_DECLARATION, "<enum label declaration>");
    r = label_identifier_expression(b, l + 1);
    r = r && enum_label_declaration_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (EQ expression)?
  private static boolean enum_label_declaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_label_declaration_1")) return false;
    enum_label_declaration_1_0(b, l + 1);
    return true;
  }

  // EQ expression
  private static boolean enum_label_declaration_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_label_declaration_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // simple_name_identifier
  static boolean enum_name(PsiBuilder b, int l) {
    return simple_name_identifier(b, l + 1);
  }

  /* ********************************************************** */
  // attribute /*SQBR_L SIMPLE_ID LP RP SQBR_R */nls? enum_declaration
  static boolean enum_with_flags_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_with_flags_declaration")) return false;
    if (!nextTokenIs(b, SQBR_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute(b, l + 1);
    r = r && enum_with_flags_declaration_1(b, l + 1);
    r = r && enum_declaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean enum_with_flags_declaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enum_with_flags_declaration_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ( expandable_string_content | EXPANDABLE_HERE_STRING_PART )+
  static boolean expandable_here_string_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandable_here_string_content")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expandable_here_string_content_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!expandable_here_string_content_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expandable_here_string_content", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // expandable_string_content | EXPANDABLE_HERE_STRING_PART
  private static boolean expandable_here_string_content_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandable_here_string_content_0")) return false;
    boolean r;
    r = expandable_string_content(b, l + 1);
    if (!r) r = consumeToken(b, EXPANDABLE_HERE_STRING_PART);
    return r;
  }

  /* ********************************************************** */
  // EXPANDABLE_HERE_STRING_START expandable_here_string_content? EXPANDABLE_HERE_STRING_END
  static boolean expandable_here_string_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandable_here_string_literal")) return false;
    if (!nextTokenIs(b, EXPANDABLE_HERE_STRING_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EXPANDABLE_HERE_STRING_START);
    r = r && expandable_here_string_literal_1(b, l + 1);
    r = r && consumeToken(b, EXPANDABLE_HERE_STRING_END);
    exit_section_(b, m, null, r);
    return r;
  }

  // expandable_here_string_content?
  private static boolean expandable_here_string_literal_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandable_here_string_literal_1")) return false;
    expandable_here_string_content(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ( string_literal_entry | target_variable_expression | sub_expression )+
  static boolean expandable_string_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandable_string_content")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expandable_string_content_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!expandable_string_content_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expandable_string_content", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // string_literal_entry | target_variable_expression | sub_expression
  private static boolean expandable_string_content_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandable_string_content_0")) return false;
    boolean r;
    r = string_literal_entry(b, l + 1);
    if (!r) r = target_variable_expression(b, l + 1);
    if (!r) r = sub_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // dq_open expandable_string_content? dq_close
  static boolean expandable_string_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandable_string_literal")) return false;
    if (!nextTokenIs(b, DQ_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = dq_open(b, l + 1);
    r = r && expandable_string_literal_1(b, l + 1);
    r = r && dq_close(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // expandable_string_content?
  private static boolean expandable_string_literal_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expandable_string_literal_1")) return false;
    expandable_string_content(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // expression ( nls? COMMA nls? expression)*
  static boolean expression_list_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_list_rule")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1, -1);
    r = r && expression_list_rule_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( nls? COMMA nls? expression)*
  private static boolean expression_list_rule_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_list_rule_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expression_list_rule_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expression_list_rule_1", c)) break;
    }
    return true;
  }

  // nls? COMMA nls? expression
  private static boolean expression_list_rule_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_list_rule_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression_list_rule_1_0_0(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && expression_list_rule_1_0_2(b, l + 1);
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean expression_list_rule_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_list_rule_1_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean expression_list_rule_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_list_rule_1_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // prefix_op nls? unary_expression | cast_expression
  static boolean expression_with_unary_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_with_unary_operator")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression_with_unary_operator_0(b, l + 1);
    if (!r) r = cast_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // prefix_op nls? unary_expression
  private static boolean expression_with_unary_operator_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_with_unary_operator_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = prefix_op(b, l + 1);
    r = r && expression_with_unary_operator_0_1(b, l + 1);
    r = r && unary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean expression_with_unary_operator_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_with_unary_operator_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // OP_FR
  static boolean file_redirection_operator(PsiBuilder b, int l) {
    return consumeToken(b, OP_FR);
  }

  /* ********************************************************** */
  // 'finally' nls? statement_block
  public static boolean finally_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "finally_clause")) return false;
    if (!nextTokenIs(b, FINALLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FINALLY_CLAUSE, null);
    r = consumeToken(b, FINALLY);
    p = r; // pin = 1
    r = r && report_error_(b, finally_clause_1(b, l + 1));
    r = p && statement_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean finally_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "finally_clause_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // break_continue_statement | throw_return_exit_statement
  public static boolean flow_control_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "flow_control_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FLOW_CONTROL_STATEMENT, "<flow control statement>");
    r = break_continue_statement(b, l + 1);
    if (!r) r = throw_return_exit_statement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LP nls? (for_initializer sep | sep?)
  //                   nls? (for_condition sep | sep?)
  //                   nls? for_iterator? nls? RP
  public static boolean for_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause")) return false;
    if (!nextTokenIs(b, LP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && for_clause_1(b, l + 1);
    r = r && for_clause_2(b, l + 1);
    r = r && for_clause_3(b, l + 1);
    r = r && for_clause_4(b, l + 1);
    r = r && for_clause_5(b, l + 1);
    r = r && for_clause_6(b, l + 1);
    r = r && for_clause_7(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, FOR_CLAUSE, r);
    return r;
  }

  // nls?
  private static boolean for_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // for_initializer sep | sep?
  private static boolean for_clause_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = for_clause_2_0(b, l + 1);
    if (!r) r = for_clause_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // for_initializer sep
  private static boolean for_clause_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = for_initializer(b, l + 1);
    r = r && sep(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sep?
  private static boolean for_clause_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_2_1")) return false;
    sep(b, l + 1);
    return true;
  }

  // nls?
  private static boolean for_clause_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // for_condition sep | sep?
  private static boolean for_clause_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = for_clause_4_0(b, l + 1);
    if (!r) r = for_clause_4_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // for_condition sep
  private static boolean for_clause_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = for_condition(b, l + 1);
    r = r && sep(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sep?
  private static boolean for_clause_4_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_4_1")) return false;
    sep(b, l + 1);
    return true;
  }

  // nls?
  private static boolean for_clause_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_5")) return false;
    nls(b, l + 1);
    return true;
  }

  // for_iterator?
  private static boolean for_clause_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_6")) return false;
    for_iterator(b, l + 1);
    return true;
  }

  // nls?
  private static boolean for_clause_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_clause_7")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // pipeline
  static boolean for_condition(PsiBuilder b, int l) {
    return pipeline(b, l + 1);
  }

  /* ********************************************************** */
  // pipeline
  static boolean for_initializer(PsiBuilder b, int l) {
    return pipeline(b, l + 1);
  }

  /* ********************************************************** */
  // pipeline
  static boolean for_iterator(PsiBuilder b, int l) {
    return pipeline(b, l + 1);
  }

  /* ********************************************************** */
  // 'for' nls? for_clause nls? statement_block
  public static boolean for_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement")) return false;
    if (!nextTokenIs(b, FOR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOR_STATEMENT, null);
    r = consumeToken(b, FOR);
    p = r; // pin = 1
    r = r && report_error_(b, for_statement_1(b, l + 1));
    r = p && report_error_(b, for_clause(b, l + 1)) && r;
    r = p && report_error_(b, for_statement_3(b, l + 1)) && r;
    r = p && statement_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean for_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean for_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LP nls? target_variable_expression nls? 'in' nls? pipeline nls? RP
  static boolean foreach_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_clause")) return false;
    if (!nextTokenIs(b, LP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && foreach_clause_1(b, l + 1);
    r = r && target_variable_expression(b, l + 1);
    r = r && foreach_clause_3(b, l + 1);
    r = r && consumeToken(b, IN);
    r = r && foreach_clause_5(b, l + 1);
    r = r && pipeline(b, l + 1);
    r = r && foreach_clause_7(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean foreach_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_clause_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean foreach_clause_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_clause_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean foreach_clause_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_clause_5")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean foreach_clause_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_clause_7")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '-parallel' throttlelimit_parameter | throttlelimit_parameter '-parallel' | '-parallel' | throttlelimit_parameter
  static boolean foreach_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_parameters")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = foreach_parameters_0(b, l + 1);
    if (!r) r = foreach_parameters_1(b, l + 1);
    if (!r) r = consumeToken(b, "-parallel");
    if (!r) r = throttlelimit_parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '-parallel' throttlelimit_parameter
  private static boolean foreach_parameters_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_parameters_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "-parallel");
    r = r && throttlelimit_parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // throttlelimit_parameter '-parallel'
  private static boolean foreach_parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_parameters_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = throttlelimit_parameter(b, l + 1);
    r = r && consumeToken(b, "-parallel");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'foreach' nls? foreach_parameters? nls? foreach_clause nls?
  //                                 nls? statement_block
  public static boolean foreach_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement")) return false;
    if (!nextTokenIs(b, FOREACH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOREACH_STATEMENT, null);
    r = consumeToken(b, FOREACH);
    p = r; // pin = 1
    r = r && report_error_(b, foreach_statement_1(b, l + 1));
    r = p && report_error_(b, foreach_statement_2(b, l + 1)) && r;
    r = p && report_error_(b, foreach_statement_3(b, l + 1)) && r;
    r = p && report_error_(b, foreach_clause(b, l + 1)) && r;
    r = p && report_error_(b, foreach_statement_5(b, l + 1)) && r;
    r = p && report_error_(b, foreach_statement_6(b, l + 1)) && r;
    r = p && statement_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean foreach_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // foreach_parameters?
  private static boolean foreach_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_2")) return false;
    foreach_parameters(b, l + 1);
    return true;
  }

  // nls?
  private static boolean foreach_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean foreach_statement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_5")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean foreach_statement_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_6")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '-f'
  static boolean format_operator(PsiBuilder b, int l) {
    return consumeToken(b, "-f");
  }

  /* ********************************************************** */
  // LCURLY function_block_body? RCURLY
  static boolean function_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_block")) return false;
    if (!nextTokenIs(b, LCURLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LCURLY);
    p = r; // pin = 1
    r = r && report_error_(b, function_block_1(b, l + 1));
    r = p && consumeToken(b, RCURLY) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // function_block_body?
  private static boolean function_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_block_1")) return false;
    function_block_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? attribute_list nls? top_script_block | top_script_block
  public static boolean function_block_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_block_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, BLOCK_BODY, "<function block body>");
    r = function_block_body_0(b, l + 1);
    if (!r) r = top_script_block(b, l + 1);
    exit_section_(b, l, m, r, false, PowerShellParser::block_body_recover);
    return r;
  }

  // nls? attribute_list nls? top_script_block
  private static boolean function_block_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_block_body_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_block_body_0_0(b, l + 1);
    r = r && attribute_list(b, l + 1);
    r = r && function_block_body_0_2(b, l + 1);
    r = r && top_script_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean function_block_body_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_block_body_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean function_block_body_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_block_body_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // primary_expression | identifier
  static boolean function_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_name")) return false;
    boolean r;
    r = primary_expression(b, l + 1);
    if (!r) r = identifier(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // parameter_clause
  static boolean function_parameter_declaration(PsiBuilder b, int l) {
    return parameter_clause(b, l + 1);
  }

  /* ********************************************************** */
  // declaration_scope nws
  static boolean function_scope(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_scope")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = declaration_scope(b, l + 1);
    r = r && nws(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ('function' | 'filter') nls? function_statement_tail
  public static boolean function_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement")) return false;
    if (!nextTokenIs(b, "<function statement>", FILTER, FUNCTION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _COLLAPSE_, FUNCTION_STATEMENT, "<function statement>");
    r = function_statement_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, function_statement_1(b, l + 1));
    r = p && function_statement_tail(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // 'function' | 'filter'
  private static boolean function_statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_0")) return false;
    boolean r;
    r = consumeToken(b, FUNCTION);
    if (!r) r = consumeToken(b, FILTER);
    return r;
  }

  // nls?
  private static boolean function_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // function_scope? function_name nls? function_parameter_declaration? nls? function_block
  static boolean function_statement_tail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_tail")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_statement_tail_0(b, l + 1);
    r = r && function_name(b, l + 1);
    r = r && function_statement_tail_2(b, l + 1);
    r = r && function_statement_tail_3(b, l + 1);
    r = r && function_statement_tail_4(b, l + 1);
    r = r && function_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // function_scope?
  private static boolean function_statement_tail_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_tail_0")) return false;
    function_scope(b, l + 1);
    return true;
  }

  // nls?
  private static boolean function_statement_tail_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_tail_2")) return false;
    nls(b, l + 1);
    return true;
  }

  // function_parameter_declaration?
  private static boolean function_statement_tail_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_tail_3")) return false;
    function_parameter_declaration(b, l + 1);
    return true;
  }

  // nls?
  private static boolean function_statement_tail_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_tail_4")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // path_expression | primary_expression | command_name_char_tokens
  static boolean generic_chars_or_path_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_chars_or_path_expression")) return false;
    boolean r;
    r = path_expression(b, l + 1);
    if (!r) r = primary_expression(b, l + 1);
    if (!r) r = command_name_char_tokens(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // generic_id_part_tokens_start | DOT | PATH_SEP | DIV | CMD_PARAMETER | PLUS | DASH | STAR | PERS
  static boolean generic_id_part_tokens(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_id_part_tokens")) return false;
    boolean r;
    r = generic_id_part_tokens_start(b, l + 1);
    if (!r) r = consumeToken(b, DOT);
    if (!r) r = consumeToken(b, PATH_SEP);
    if (!r) r = consumeToken(b, DIV);
    if (!r) r = consumeToken(b, CMD_PARAMETER);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, DASH);
    if (!r) r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, PERS);
    return r;
  }

  /* ********************************************************** */
  // SIMPLE_ID | GENERIC_ID_PART | STAR | PERS | allowed_identifier_keywords | DEC_INTEGER | REAL_NUM
  static boolean generic_id_part_tokens_start(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_id_part_tokens_start")) return false;
    boolean r;
    r = consumeToken(b, SIMPLE_ID);
    if (!r) r = consumeToken(b, GENERIC_ID_PART);
    if (!r) r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, PERS);
    if (!r) r = allowed_identifier_keywords(b, l + 1);
    if (!r) r = consumeToken(b, DEC_INTEGER);
    if (!r) r = consumeToken(b, REAL_NUM);
    return r;
  }

  /* ********************************************************** */
  // generic_token_with_sub_expr | generic_token_part | allowed_identifier_keywords
  static boolean generic_identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_identifier")) return false;
    boolean r;
    r = generic_token_with_sub_expr(b, l + 1);
    if (!r) r = generic_token_part(b, l + 1);
    if (!r) r = allowed_identifier_keywords(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // generic_id_part_tokens_start generic_token_chars_tail?
  static boolean generic_token_chars(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token_chars")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = generic_id_part_tokens_start(b, l + 1);
    r = r && generic_token_chars_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // generic_token_chars_tail?
  private static boolean generic_token_chars_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token_chars_1")) return false;
    generic_token_chars_tail(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ( nws generic_id_part_tokens )+
  static boolean generic_token_chars_tail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token_chars_tail")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = generic_token_chars_tail_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!generic_token_chars_tail_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "generic_token_chars_tail", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // nws generic_id_part_tokens
  private static boolean generic_token_chars_tail_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token_chars_tail_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nws(b, l + 1);
    r = r && generic_id_part_tokens(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (expandable_string_literal | verbatim_here_string_literal | target_variable_expression | generic_token_chars) generic_token_chars_tail?
  static boolean generic_token_part(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token_part")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = generic_token_part_0(b, l + 1);
    r = r && generic_token_part_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // expandable_string_literal | verbatim_here_string_literal | target_variable_expression | generic_token_chars
  private static boolean generic_token_part_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token_part_0")) return false;
    boolean r;
    r = expandable_string_literal(b, l + 1);
    if (!r) r = verbatim_here_string_literal(b, l + 1);
    if (!r) r = target_variable_expression(b, l + 1);
    if (!r) r = generic_token_chars(b, l + 1);
    return r;
  }

  // generic_token_chars_tail?
  private static boolean generic_token_part_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token_part_1")) return false;
    generic_token_chars_tail(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // generic_token_with_subexpr_start statement_list? RP nws command_name
  static boolean generic_token_with_sub_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token_with_sub_expr")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = generic_token_with_subexpr_start(b, l + 1);
    r = r && generic_token_with_sub_expr_1(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && nws(b, l + 1);
    r = r && command_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_list?
  private static boolean generic_token_with_sub_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token_with_sub_expr_1")) return false;
    statement_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // generic_token_part nws DS nws LP
  static boolean generic_token_with_subexpr_start(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token_with_subexpr_start")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = generic_token_part(b, l + 1);
    r = r && nws(b, l + 1);
    r = r && consumeToken(b, DS);
    r = r && nws(b, l + 1);
    r = r && consumeToken(b, LP);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // type_element nls? (',' nls? type_element)*
  static boolean generic_type_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_arguments")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_element(b, l + 1);
    r = r && generic_type_arguments_1(b, l + 1);
    r = r && generic_type_arguments_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean generic_type_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_arguments_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // (',' nls? type_element)*
  private static boolean generic_type_arguments_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_arguments_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!generic_type_arguments_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "generic_type_arguments_2", c)) break;
    }
    return true;
  }

  // ',' nls? type_element
  private static boolean generic_type_arguments_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_arguments_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && generic_type_arguments_2_0_1(b, l + 1);
    r = r && type_element(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean generic_type_arguments_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_arguments_2_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // generic_type_name nls? generic_type_arguments SQBR_R
  public static boolean generic_type_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GENERIC_TYPE_ELEMENT, "<generic type element>");
    r = generic_type_name(b, l + 1);
    r = r && generic_type_element_1(b, l + 1);
    r = r && generic_type_arguments(b, l + 1);
    r = r && consumeToken(b, SQBR_R);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls?
  private static boolean generic_type_element_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_element_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // reference_type_element SQBR_L
  static boolean generic_type_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_name")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = reference_type_element(b, l + 1);
    r = r && consumeToken(b, SQBR_L);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // key_expression '=' nls? statement
  static boolean hash_entry(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_entry")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = key_expression(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && hash_entry_2(b, l + 1);
    r = r && statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean hash_entry_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_entry_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // hash_entry ( statement_terminators hash_entry )*
  static boolean hash_entry_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_entry_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = hash_entry(b, l + 1);
    r = r && hash_entry_list_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( statement_terminators hash_entry )*
  private static boolean hash_entry_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_entry_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!hash_entry_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "hash_entry_list_1", c)) break;
    }
    return true;
  }

  // statement_terminators hash_entry
  private static boolean hash_entry_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_entry_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_terminators(b, l + 1);
    r = r && hash_entry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (hash_entry_list | statement_terminators )+
  public static boolean hash_literal_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, BLOCK_BODY, "<hash literal body>");
    r = hash_literal_body_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!hash_literal_body_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "hash_literal_body", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // hash_entry_list | statement_terminators
  private static boolean hash_literal_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_body_0")) return false;
    boolean r;
    r = hash_entry_list(b, l + 1);
    if (!r) r = statement_terminators(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // <<allow_any_expression hash_literal_body>>
  static boolean hash_literal_body_rule(PsiBuilder b, int l) {
    return allow_any_expression(b, l + 1, PowerShellParser::hash_literal_body);
  }

  /* ********************************************************** */
  // AT nws LCURLY nls? hash_literal_body_rule? nls? RCURLY
  public static boolean hash_literal_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_expression")) return false;
    if (!nextTokenIsFast(b, AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, AT);
    r = r && nws(b, l + 1);
    r = r && consumeToken(b, LCURLY);
    r = r && hash_literal_expression_3(b, l + 1);
    r = r && hash_literal_expression_4(b, l + 1);
    r = r && hash_literal_expression_5(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, HASH_LITERAL_EXPRESSION, r);
    return r;
  }

  // nls?
  private static boolean hash_literal_expression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_expression_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // hash_literal_body_rule?
  private static boolean hash_literal_expression_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_expression_4")) return false;
    hash_literal_body_rule(b, l + 1);
    return true;
  }

  // nls?
  private static boolean hash_literal_expression_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_expression_5")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // HEX_INTEGER
  static boolean hexadecimal_integer_literal(PsiBuilder b, int l) {
    return consumeToken(b, HEX_INTEGER);
  }

  /* ********************************************************** */
  // generic_identifier
  public static boolean identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, IDENTIFIER, "<identifier>");
    r = generic_identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'if' nls? LP nls? pipeline nls? RP nls? statement_block (nls? elseif_clause)* (nls? else_clause)?
  public static boolean if_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_STATEMENT, null);
    r = consumeToken(b, IF);
    p = r; // pin = 1
    r = r && report_error_(b, if_statement_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LP)) && r;
    r = p && report_error_(b, if_statement_3(b, l + 1)) && r;
    r = p && report_error_(b, pipeline(b, l + 1)) && r;
    r = p && report_error_(b, if_statement_5(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, RP)) && r;
    r = p && report_error_(b, if_statement_7(b, l + 1)) && r;
    r = p && report_error_(b, statement_block(b, l + 1)) && r;
    r = p && report_error_(b, if_statement_9(b, l + 1)) && r;
    r = p && if_statement_10(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean if_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean if_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean if_statement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_5")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean if_statement_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_7")) return false;
    nls(b, l + 1);
    return true;
  }

  // (nls? elseif_clause)*
  private static boolean if_statement_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_9")) return false;
    while (true) {
      int c = current_position_(b);
      if (!if_statement_9_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "if_statement_9", c)) break;
    }
    return true;
  }

  // nls? elseif_clause
  private static boolean if_statement_9_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_9_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = if_statement_9_0_0(b, l + 1);
    r = r && elseif_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean if_statement_9_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_9_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // (nls? else_clause)?
  private static boolean if_statement_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_10")) return false;
    if_statement_10_0(b, l + 1);
    return true;
  }

  // nls? else_clause
  private static boolean if_statement_10_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_10_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = if_statement_10_0_0(b, l + 1);
    r = r && else_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean if_statement_10_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_10_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // attribute
  public static boolean incomplete_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "incomplete_declaration")) return false;
    if (!nextTokenIs(b, SQBR_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute(b, l + 1);
    exit_section_(b, m, INCOMPLETE_DECLARATION, r);
    return r;
  }

  /* ********************************************************** */
  // 'inlinescript' statement_block
  public static boolean inlinescript_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inlinescript_statement")) return false;
    if (!nextTokenIs(b, INLINESCRIPT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INLINESCRIPT);
    r = r && statement_block(b, l + 1);
    exit_section_(b, m, INLINESCRIPT_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // decimal_integer_literal | hexadecimal_integer_literal
  public static boolean integer_literal_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "integer_literal_expression")) return false;
    if (!nextTokenIsFast(b, DEC_INTEGER, HEX_INTEGER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INTEGER_LITERAL_EXPRESSION, "<integer literal expression>");
    r = decimal_integer_literal(b, l + 1);
    if (!r) r = hexadecimal_integer_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // COMMA nls? interface_name (nls? COMMA nls? interface_name)*
  static boolean interface_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interface_list")) return false;
    if (!nextTokenIs(b, COMMA)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && interface_list_1(b, l + 1);
    r = r && interface_name(b, l + 1);
    r = r && interface_list_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean interface_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interface_list_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // (nls? COMMA nls? interface_name)*
  private static boolean interface_list_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interface_list_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!interface_list_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "interface_list_3", c)) break;
    }
    return true;
  }

  // nls? COMMA nls? interface_name
  private static boolean interface_list_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interface_list_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = interface_list_3_0_0(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && interface_list_3_0_2(b, l + 1);
    r = r && interface_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean interface_list_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interface_list_3_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean interface_list_3_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "interface_list_3_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // type_element
  static boolean interface_name(PsiBuilder b, int l) {
    return type_element(b, l + 1);
  }

  /* ********************************************************** */
  // nws member_access_operator nls? member_name call_arguments
  public static boolean invocation_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invocation_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, INVOCATION_EXPRESSION, "<invocation expression>");
    r = nws(b, l + 1);
    r = r && member_access_operator(b, l + 1);
    r = r && invocation_expression_2(b, l + 1);
    r = r && member_name(b, l + 1);
    r = r && call_arguments(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls?
  private static boolean invocation_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invocation_expression_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // <<is_parsing_configuration_block>>
  static boolean is_dsc(PsiBuilder b, int l) {
    return is_parsing_configuration_block(b, l + 1);
  }

  /* ********************************************************** */
  // <<isIdentifierBefore>>
  static boolean is_id(PsiBuilder b, int l) {
    return isIdentifierBefore(b, l + 1);
  }

  /* ********************************************************** */
  // <<is_parsing_call_arguments>>
  static boolean is_in_call_arguments(PsiBuilder b, int l) {
    return is_parsing_call_arguments(b, l + 1);
  }

  /* ********************************************************** */
  // simple_name_identifier | unary_expression
  public static boolean key_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "key_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, KEY_EXPRESSION, "<key expression>");
    r = simple_name_identifier(b, l + 1);
    if (!r) r = unary_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ':' nws label_name
  public static boolean label(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && nws(b, l + 1);
    r = r && label_name(b, l + 1);
    exit_section_(b, m, LABEL, r);
    return r;
  }

  /* ********************************************************** */
  // simple_name_identifier | unary_expression
  static boolean label_identifier_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_identifier_expression")) return false;
    boolean r;
    r = simple_name_identifier(b, l + 1);
    if (!r) r = unary_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // enum_label_declaration (statement_terminators enum_label_declaration)*
  static boolean label_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = enum_label_declaration(b, l + 1);
    r = r && label_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, PowerShellParser::top_level_recover);
    return r;
  }

  // (statement_terminators enum_label_declaration)*
  private static boolean label_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!label_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "label_list_1", c)) break;
    }
    return true;
  }

  // statement_terminators enum_label_declaration
  private static boolean label_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_terminators(b, l + 1);
    r = r && enum_label_declaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // SIMPLE_ID
  public static boolean label_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_name")) return false;
    if (!nextTokenIs(b, SIMPLE_ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SIMPLE_ID);
    exit_section_(b, m, IDENTIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // label_identifier_expression
  public static boolean label_reference_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_reference_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, LABEL_REFERENCE_EXPRESSION, "<label reference expression>");
    r = label_identifier_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // switch_statement
  //                               | foreach_statement
  //                               | for_statement
  //                               | while_statement
  //                               | do_statement
  static boolean labeled_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "labeled_statement")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = switch_statement(b, l + 1);
    if (!r) r = foreach_statement(b, l + 1);
    if (!r) r = for_statement(b, l + 1);
    if (!r) r = while_statement(b, l + 1);
    if (!r) r = do_statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // real_literal_expression | integer_literal_expression | string_literal_expression
  static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    r = real_literal_expression(b, l + 1);
    if (!r) r = integer_literal_expression(b, l + 1);
    if (!r) r = string_literal_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // nws member_access_operator nls? member_name
  public static boolean member_access_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_access_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, MEMBER_ACCESS_EXPRESSION, "<member access expression>");
    r = nws(b, l + 1);
    r = r && member_access_operator(b, l + 1);
    r = r && member_access_expression_2(b, l + 1);
    r = r && member_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls?
  private static boolean member_access_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_access_expression_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // DOT | COLON2
  static boolean member_access_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_access_operator")) return false;
    if (!nextTokenIs(b, "", COLON2, DOT)) return false;
    boolean r;
    r = consumeToken(b, DOT);
    if (!r) r = consumeToken(b, COLON2);
    return r;
  }

  /* ********************************************************** */
  // declaration_modifier_attribute | attribute
  static boolean member_declaration_attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_declaration_attribute")) return false;
    boolean r;
    r = declaration_modifier_attribute(b, l + 1);
    if (!r) r = attribute(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // simple_name_reference
  //                         | string_literal_expression
  // //->                        | string_literal_with_subexpression
  //                         | expression_with_unary_operator //prefix_op unary_expression //
  //                         | value
  static boolean member_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_name")) return false;
    boolean r;
    r = simple_name_reference(b, l + 1);
    if (!r) r = string_literal_expression(b, l + 1);
    if (!r) r = expression_with_unary_operator(b, l + 1);
    if (!r) r = value(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // OP_MR
  static boolean merging_redirection_operator(PsiBuilder b, int l) {
    return consumeToken(b, OP_MR);
  }

  /* ********************************************************** */
  // LP nls? parameter_list? nls? RP
  public static boolean method_argument_expression_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_argument_expression_list")) return false;
    if (!nextTokenIs(b, LP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && method_argument_expression_list_1(b, l + 1);
    r = r && method_argument_expression_list_2(b, l + 1);
    r = r && method_argument_expression_list_3(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, PARAMETER_CLAUSE, r);
    return r;
  }

  // nls?
  private static boolean method_argument_expression_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_argument_expression_list_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // parameter_list?
  private static boolean method_argument_expression_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_argument_expression_list_2")) return false;
    parameter_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean method_argument_expression_list_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_argument_expression_list_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // statement_block
  static boolean method_block(PsiBuilder b, int l) {
    return statement_block(b, l + 1);
  }

  /* ********************************************************** */
  // (member_declaration_attribute nls?)* method_name method_argument_expression_list nls? method_block
  public static boolean method_declaration_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_declaration_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, METHOD_DECLARATION_STATEMENT, "<method declaration statement>");
    r = method_declaration_statement_0(b, l + 1);
    r = r && method_name(b, l + 1);
    r = r && method_argument_expression_list(b, l + 1);
    r = r && method_declaration_statement_3(b, l + 1);
    r = r && method_block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (member_declaration_attribute nls?)*
  private static boolean method_declaration_statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_declaration_statement_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!method_declaration_statement_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "method_declaration_statement_0", c)) break;
    }
    return true;
  }

  // member_declaration_attribute nls?
  private static boolean method_declaration_statement_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_declaration_statement_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = member_declaration_attribute(b, l + 1);
    r = r && method_declaration_statement_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean method_declaration_statement_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_declaration_statement_0_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean method_declaration_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_declaration_statement_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // simple_name_identifier
  static boolean method_name(PsiBuilder b, int l) {
    return simple_name_identifier(b, l + 1);
  }

  /* ********************************************************** */
  // block_name nls? statement_block
  static boolean named_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = block_name(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, named_block_1(b, l + 1));
    r = p && statement_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean named_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // named_block (statement_terminators named_block )*
  static boolean named_block_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = named_block(b, l + 1);
    r = r && named_block_list_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (statement_terminators named_block )*
  private static boolean named_block_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!named_block_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "named_block_list_1", c)) break;
    }
    return true;
  }

  // statement_terminators named_block
  private static boolean named_block_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_terminators(b, l + 1);
    r = r && named_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // <<isNotComment>>
  static boolean ncm(PsiBuilder b, int l) {
    return isNotComment(b, l + 1);
  }

  /* ********************************************************** */
  // LF
  static boolean new_line_char(PsiBuilder b, int l) {
    return consumeToken(b, LF);
  }

  /* ********************************************************** */
  // (comment? NLS comment?)+
  static boolean nls(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nls")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nls_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!nls_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "nls", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // comment? NLS comment?
  private static boolean nls_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nls_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nls_0_0(b, l + 1);
    r = r && consumeToken(b, NLS);
    r = r && nls_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // comment?
  private static boolean nls_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nls_0_0")) return false;
    comment(b, l + 1);
    return true;
  }

  // comment?
  private static boolean nls_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nls_0_2")) return false;
    comment(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // node_token nls? command_argument_list+
  public static boolean node_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "node_block")) return false;
    if (!nextTokenIs(b, SIMPLE_ID)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NODE_BLOCK, null);
    r = node_token(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, node_block_1(b, l + 1));
    r = p && node_block_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean node_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "node_block_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // command_argument_list+
  private static boolean node_block_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "node_block_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_argument_list(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!command_argument_list(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "node_block_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // SIMPLE_ID
  static boolean node_token(PsiBuilder b, int l) {
    return consumeToken(b, SIMPLE_ID);
  }

  /* ********************************************************** */
  // <<isNotWhiteSpace>>
  static boolean nws(PsiBuilder b, int l) {
    return isNotWhiteSpace(b, l + 1);
  }

  /* ********************************************************** */
  // '-join' | '–join' | '—join' | '―join'
  static boolean op_join_text(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_join_text")) return false;
    boolean r;
    r = consumeToken(b, "-join");
    if (!r) r = consumeToken(b, "–join");
    if (!r) r = consumeToken(b, "—join");
    if (!r) r = consumeToken(b, "―join");
    return r;
  }

  /* ********************************************************** */
  // '-split' | '–split' | '—split' | '―split'
  static boolean op_split_text(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_split_text")) return false;
    boolean r;
    r = consumeToken(b, "-split");
    if (!r) r = consumeToken(b, "–split");
    if (!r) r = consumeToken(b, "—split");
    if (!r) r = consumeToken(b, "―split");
    return r;
  }

  /* ********************************************************** */
  // 'parallel' statement_block
  public static boolean parallel_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parallel_statement")) return false;
    if (!nextTokenIs(b, PARALLEL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PARALLEL);
    r = r && statement_block(b, l + 1);
    exit_section_(b, m, PARALLEL_STATEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // LP nls? parameter_list? nls? RP
  public static boolean parameter_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_clause")) return false;
    if (!nextTokenIs(b, LP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && parameter_clause_1(b, l + 1);
    r = r && parameter_clause_2(b, l + 1);
    r = r && parameter_clause_3(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, PARAMETER_CLAUSE, r);
    return r;
  }

  // nls?
  private static boolean parameter_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_clause_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // parameter_list?
  private static boolean parameter_clause_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_clause_2")) return false;
    parameter_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean parameter_clause_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_clause_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // script_parameter_list
  static boolean parameter_list(PsiBuilder b, int l) {
    return script_parameter_list(b, l + 1);
  }

  /* ********************************************************** */
  // LP nls? argument_expression_list? nls? RP
  public static boolean parenthesized_argument_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_argument_list")) return false;
    if (!nextTokenIs(b, LP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LP);
    r = r && parenthesized_argument_list_1(b, l + 1);
    r = r && parenthesized_argument_list_2(b, l + 1);
    r = r && parenthesized_argument_list_3(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, PARENTHESIZED_ARGUMENT_LIST, r);
    return r;
  }

  // nls?
  private static boolean parenthesized_argument_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_argument_list_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // argument_expression_list?
  private static boolean parenthesized_argument_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_argument_list_2")) return false;
    argument_expression_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean parenthesized_argument_list_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_argument_list_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LP nls? <<allow_any_expression pipeline>> nls? RP
  public static boolean parenthesized_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_expression")) return false;
    if (!nextTokenIsFast(b, LP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, LP);
    r = r && parenthesized_expression_1(b, l + 1);
    r = r && allow_any_expression(b, l + 1, PowerShellParser::pipeline);
    r = r && parenthesized_expression_3(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, PARENTHESIZED_EXPRESSION, r);
    return r;
  }

  // nls?
  private static boolean parenthesized_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_expression_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean parenthesized_expression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_expression_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parseCommandArgumentInner generic_chars_or_path_expression>>
  static boolean parse_command_argument(PsiBuilder b, int l) {
    return parseCommandArgumentInner(b, l + 1, PowerShellParser::generic_chars_or_path_expression);
  }

  /* ********************************************************** */
  // is_dsc node_block
  static boolean parse_node_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parse_node_block")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = is_dsc(b, l + 1);
    r = r && node_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // is_dsc resource_block
  static boolean parse_resource_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parse_resource_block")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = is_dsc(b, l + 1);
    r = r && resource_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // path_item nws (
  //                               (
  //                               ((   (PATH_SEP nws COLON2 nws) (path_item nws PATH_SEP nws COLON2 nws)*  //provider+ drive?
  //                                   (nws path_item nws (COLON|DS) nws PATH_SEP)? //drive
  //                                )
  //                               |
  //                                (
  //                                   ((COLON|DS) nws PATH_SEP)     // or drive
  //                                )
  //                                )
  //                               (nws path_item) (nws PATH_SEP nws path_item)* (nws PATH_SEP)? // container+
  //                               )//provider and/or drive and container+
  //                               | (PATH_SEP nws path_item)+ //just container+
  //                               | (COLON|DS) (nws PATH_SEP)?     // just drive
  //                               | PATH_SEP //just PATH_SEP
  //                                 ) | PATH_SEP (nws (path_expression | path_item))?
  //                                   | relative_path_item
  public static boolean path_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PATH_EXPRESSION, "<path expression>");
    r = path_expression_0(b, l + 1);
    if (!r) r = path_expression_1(b, l + 1);
    if (!r) r = relative_path_item(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // path_item nws (
  //                               (
  //                               ((   (PATH_SEP nws COLON2 nws) (path_item nws PATH_SEP nws COLON2 nws)*  //provider+ drive?
  //                                   (nws path_item nws (COLON|DS) nws PATH_SEP)? //drive
  //                                )
  //                               |
  //                                (
  //                                   ((COLON|DS) nws PATH_SEP)     // or drive
  //                                )
  //                                )
  //                               (nws path_item) (nws PATH_SEP nws path_item)* (nws PATH_SEP)? // container+
  //                               )//provider and/or drive and container+
  //                               | (PATH_SEP nws path_item)+ //just container+
  //                               | (COLON|DS) (nws PATH_SEP)?     // just drive
  //                               | PATH_SEP //just PATH_SEP
  //                                 )
  private static boolean path_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = path_item(b, l + 1);
    r = r && nws(b, l + 1);
    r = r && path_expression_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (
  //                               ((   (PATH_SEP nws COLON2 nws) (path_item nws PATH_SEP nws COLON2 nws)*  //provider+ drive?
  //                                   (nws path_item nws (COLON|DS) nws PATH_SEP)? //drive
  //                                )
  //                               |
  //                                (
  //                                   ((COLON|DS) nws PATH_SEP)     // or drive
  //                                )
  //                                )
  //                               (nws path_item) (nws PATH_SEP nws path_item)* (nws PATH_SEP)? // container+
  //                               )//provider and/or drive and container+
  //                               | (PATH_SEP nws path_item)+ //just container+
  //                               | (COLON|DS) (nws PATH_SEP)?     // just drive
  //                               | PATH_SEP
  private static boolean path_expression_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = path_expression_0_2_0(b, l + 1);
    if (!r) r = path_expression_0_2_1(b, l + 1);
    if (!r) r = path_expression_0_2_2(b, l + 1);
    if (!r) r = consumeTokenFast(b, PATH_SEP);
    exit_section_(b, m, null, r);
    return r;
  }

  // ((   (PATH_SEP nws COLON2 nws) (path_item nws PATH_SEP nws COLON2 nws)*  //provider+ drive?
  //                                   (nws path_item nws (COLON|DS) nws PATH_SEP)? //drive
  //                                )
  //                               |
  //                                (
  //                                   ((COLON|DS) nws PATH_SEP)     // or drive
  //                                )
  //                                )
  //                               (nws path_item) (nws PATH_SEP nws path_item)* (nws PATH_SEP)?
  private static boolean path_expression_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = path_expression_0_2_0_0(b, l + 1);
    r = r && path_expression_0_2_0_1(b, l + 1);
    r = r && path_expression_0_2_0_2(b, l + 1);
    r = r && path_expression_0_2_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (   (PATH_SEP nws COLON2 nws) (path_item nws PATH_SEP nws COLON2 nws)*  //provider+ drive?
  //                                   (nws path_item nws (COLON|DS) nws PATH_SEP)? //drive
  //                                )
  //                               |
  //                                (
  //                                   ((COLON|DS) nws PATH_SEP)     // or drive
  //                                )
  private static boolean path_expression_0_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = path_expression_0_2_0_0_0(b, l + 1);
    if (!r) r = path_expression_0_2_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (PATH_SEP nws COLON2 nws) (path_item nws PATH_SEP nws COLON2 nws)*  //provider+ drive?
  //                                   (nws path_item nws (COLON|DS) nws PATH_SEP)?
  private static boolean path_expression_0_2_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = path_expression_0_2_0_0_0_0(b, l + 1);
    r = r && path_expression_0_2_0_0_0_1(b, l + 1);
    r = r && path_expression_0_2_0_0_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PATH_SEP nws COLON2 nws
  private static boolean path_expression_0_2_0_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, PATH_SEP);
    r = r && nws(b, l + 1);
    r = r && consumeToken(b, COLON2);
    r = r && nws(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (path_item nws PATH_SEP nws COLON2 nws)*
  private static boolean path_expression_0_2_0_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_0_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!path_expression_0_2_0_0_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "path_expression_0_2_0_0_0_1", c)) break;
    }
    return true;
  }

  // path_item nws PATH_SEP nws COLON2 nws
  private static boolean path_expression_0_2_0_0_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_0_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = path_item(b, l + 1);
    r = r && nws(b, l + 1);
    r = r && consumeToken(b, PATH_SEP);
    r = r && nws(b, l + 1);
    r = r && consumeToken(b, COLON2);
    r = r && nws(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nws path_item nws (COLON|DS) nws PATH_SEP)?
  private static boolean path_expression_0_2_0_0_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_0_0_2")) return false;
    path_expression_0_2_0_0_0_2_0(b, l + 1);
    return true;
  }

  // nws path_item nws (COLON|DS) nws PATH_SEP
  private static boolean path_expression_0_2_0_0_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_0_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nws(b, l + 1);
    r = r && path_item(b, l + 1);
    r = r && nws(b, l + 1);
    r = r && path_expression_0_2_0_0_0_2_0_3(b, l + 1);
    r = r && nws(b, l + 1);
    r = r && consumeToken(b, PATH_SEP);
    exit_section_(b, m, null, r);
    return r;
  }

  // COLON|DS
  private static boolean path_expression_0_2_0_0_0_2_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_0_0_2_0_3")) return false;
    boolean r;
    r = consumeTokenFast(b, COLON);
    if (!r) r = consumeTokenFast(b, DS);
    return r;
  }

  // (COLON|DS) nws PATH_SEP
  private static boolean path_expression_0_2_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = path_expression_0_2_0_0_1_0(b, l + 1);
    r = r && nws(b, l + 1);
    r = r && consumeToken(b, PATH_SEP);
    exit_section_(b, m, null, r);
    return r;
  }

  // COLON|DS
  private static boolean path_expression_0_2_0_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_0_1_0")) return false;
    boolean r;
    r = consumeTokenFast(b, COLON);
    if (!r) r = consumeTokenFast(b, DS);
    return r;
  }

  // nws path_item
  private static boolean path_expression_0_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nws(b, l + 1);
    r = r && path_item(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nws PATH_SEP nws path_item)*
  private static boolean path_expression_0_2_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!path_expression_0_2_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "path_expression_0_2_0_2", c)) break;
    }
    return true;
  }

  // nws PATH_SEP nws path_item
  private static boolean path_expression_0_2_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nws(b, l + 1);
    r = r && consumeToken(b, PATH_SEP);
    r = r && nws(b, l + 1);
    r = r && path_item(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nws PATH_SEP)?
  private static boolean path_expression_0_2_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_3")) return false;
    path_expression_0_2_0_3_0(b, l + 1);
    return true;
  }

  // nws PATH_SEP
  private static boolean path_expression_0_2_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nws(b, l + 1);
    r = r && consumeToken(b, PATH_SEP);
    exit_section_(b, m, null, r);
    return r;
  }

  // (PATH_SEP nws path_item)+
  private static boolean path_expression_0_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = path_expression_0_2_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!path_expression_0_2_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "path_expression_0_2_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // PATH_SEP nws path_item
  private static boolean path_expression_0_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, PATH_SEP);
    r = r && nws(b, l + 1);
    r = r && path_item(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COLON|DS) (nws PATH_SEP)?
  private static boolean path_expression_0_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = path_expression_0_2_2_0(b, l + 1);
    r = r && path_expression_0_2_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COLON|DS
  private static boolean path_expression_0_2_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_2_0")) return false;
    boolean r;
    r = consumeTokenFast(b, COLON);
    if (!r) r = consumeTokenFast(b, DS);
    return r;
  }

  // (nws PATH_SEP)?
  private static boolean path_expression_0_2_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_2_1")) return false;
    path_expression_0_2_2_1_0(b, l + 1);
    return true;
  }

  // nws PATH_SEP
  private static boolean path_expression_0_2_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_0_2_2_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nws(b, l + 1);
    r = r && consumeToken(b, PATH_SEP);
    exit_section_(b, m, null, r);
    return r;
  }

  // PATH_SEP (nws (path_expression | path_item))?
  private static boolean path_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, PATH_SEP);
    r = r && path_expression_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nws (path_expression | path_item))?
  private static boolean path_expression_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_1_1")) return false;
    path_expression_1_1_0(b, l + 1);
    return true;
  }

  // nws (path_expression | path_item)
  private static boolean path_expression_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nws(b, l + 1);
    r = r && path_expression_1_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // path_expression | path_item
  private static boolean path_expression_1_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_expression_1_1_0_1")) return false;
    boolean r;
    r = path_expression(b, l + 1);
    if (!r) r = path_item(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // path_item_name
  public static boolean path_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_item")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, PATH_ITEM, "<path item>");
    r = path_item_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // relative_path | path_name_tokens | allowed_identifier_keywords | target_variable_expression
  static boolean path_item_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_item_name")) return false;
    boolean r;
    r = relative_path(b, l + 1);
    if (!r) r = path_name_tokens(b, l + 1);
    if (!r) r = allowed_identifier_keywords(b, l + 1);
    if (!r) r = target_variable_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // generic_id_part_tokens_start | CMD_PARAMETER | PLUS | DASH
  static boolean path_name_part_tokens_start(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_name_part_tokens_start")) return false;
    boolean r;
    r = generic_id_part_tokens_start(b, l + 1);
    if (!r) r = consumeToken(b, CMD_PARAMETER);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, DASH);
    return r;
  }

  /* ********************************************************** */
  // path_name_part_tokens_start (nws (path_name_part_tokens_start | DOT))*
  static boolean path_name_tokens(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_name_tokens")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = path_name_part_tokens_start(b, l + 1);
    r = r && path_name_tokens_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nws (path_name_part_tokens_start | DOT))*
  private static boolean path_name_tokens_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_name_tokens_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!path_name_tokens_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "path_name_tokens_1", c)) break;
    }
    return true;
  }

  // nws (path_name_part_tokens_start | DOT)
  private static boolean path_name_tokens_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_name_tokens_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nws(b, l + 1);
    r = r && path_name_tokens_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // path_name_part_tokens_start | DOT
  private static boolean path_name_tokens_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "path_name_tokens_1_0_1")) return false;
    boolean r;
    r = path_name_part_tokens_start(b, l + 1);
    if (!r) r = consumeToken(b, DOT);
    return r;
  }

  /* ********************************************************** */
  // expression redirection* pipeline_tail? &statement_end | command_call_expression verbatim_command_argument? pipeline_tail?
  static boolean pipeline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pipeline_0(b, l + 1);
    if (!r) r = pipeline_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // expression redirection* pipeline_tail? &statement_end
  private static boolean pipeline_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1, -1);
    r = r && pipeline_0_1(b, l + 1);
    r = r && pipeline_0_2(b, l + 1);
    r = r && pipeline_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // redirection*
  private static boolean pipeline_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!redirection(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "pipeline_0_1", c)) break;
    }
    return true;
  }

  // pipeline_tail?
  private static boolean pipeline_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_0_2")) return false;
    pipeline_tail(b, l + 1);
    return true;
  }

  // &statement_end
  private static boolean pipeline_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_0_3")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = statement_end(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // command_call_expression verbatim_command_argument? pipeline_tail?
  private static boolean pipeline_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_call_expression(b, l + 1);
    r = r && pipeline_1_1(b, l + 1);
    r = r && pipeline_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // verbatim_command_argument?
  private static boolean pipeline_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_1_1")) return false;
    verbatim_command_argument(b, l + 1);
    return true;
  }

  // pipeline_tail?
  private static boolean pipeline_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_1_2")) return false;
    pipeline_tail(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ( PIPE nls? command_call_expression )+
  public static boolean pipeline_tail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_tail")) return false;
    if (!nextTokenIs(b, PIPE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, PIPELINE, null);
    r = pipeline_tail_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!pipeline_tail_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "pipeline_tail", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PIPE nls? command_call_expression
  private static boolean pipeline_tail_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_tail_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && pipeline_tail_0_1(b, l + 1);
    r = r && command_call_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean pipeline_tail_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_tail_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // MM
  public static boolean post_decrement_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "post_decrement_expression")) return false;
    if (!nextTokenIsFast(b, MM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, POST_DECREMENT_EXPRESSION, null);
    r = consumeTokenFast(b, MM);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PP
  public static boolean post_increment_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "post_increment_expression")) return false;
    if (!nextTokenIsFast(b, PP)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, POST_INCREMENT_EXPRESSION, null);
    r = consumeTokenFast(b, PP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // DS | QMARK | HAT
  public static boolean predefined_var_id(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predefined_var_id")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IDENTIFIER, "<predefined var id>");
    r = consumeToken(b, DS);
    if (!r) r = consumeToken(b, QMARK);
    if (!r) r = consumeToken(b, HAT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // OP_BNOT | EXCL_MARK | OP_NOT | MM | PP | op_split_text | op_join_text | COMMA | PLUS | DASH
  static boolean prefix_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_op")) return false;
    boolean r;
    r = consumeToken(b, OP_BNOT);
    if (!r) r = consumeToken(b, EXCL_MARK);
    if (!r) r = consumeToken(b, OP_NOT);
    if (!r) r = consumeToken(b, MM);
    if (!r) r = consumeToken(b, PP);
    if (!r) r = op_split_text(b, l + 1);
    if (!r) r = op_join_text(b, l + 1);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, DASH);
    return r;
  }

  /* ********************************************************** */
  // value (invocation_expression | member_access_expression | element_access_expression | post_increment_expression | post_decrement_expression)*
  static boolean primary_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expression")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value(b, l + 1);
    r = r && primary_expression_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (invocation_expression | member_access_expression | element_access_expression | post_increment_expression | post_decrement_expression)*
  private static boolean primary_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expression_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!primary_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "primary_expression_1", c)) break;
    }
    return true;
  }

  // invocation_expression | member_access_expression | element_access_expression | post_increment_expression | post_decrement_expression
  private static boolean primary_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expression_1_0")) return false;
    boolean r;
    r = invocation_expression(b, l + 1);
    if (!r) r = member_access_expression(b, l + 1);
    if (!r) r = element_access_expression(b, l + 1);
    if (!r) r = post_increment_expression(b, l + 1);
    if (!r) r = post_decrement_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (member_declaration_attribute nls?)* property_definition_expression
  public static boolean property_declaration_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_declaration_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY_DECLARATION_STATEMENT, "<property declaration statement>");
    r = property_declaration_statement_0(b, l + 1);
    r = r && property_definition_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (member_declaration_attribute nls?)*
  private static boolean property_declaration_statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_declaration_statement_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!property_declaration_statement_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "property_declaration_statement_0", c)) break;
    }
    return true;
  }

  // member_declaration_attribute nls?
  private static boolean property_declaration_statement_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_declaration_statement_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = member_declaration_attribute(b, l + 1);
    r = r && property_declaration_statement_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean property_declaration_statement_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_declaration_statement_0_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // expression
  static boolean property_definition_expression(PsiBuilder b, int l) {
    return expression(b, l + 1, -1);
  }

  /* ********************************************************** */
  // REAL_NUM
  public static boolean real_literal_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "real_literal_expression")) return false;
    if (!nextTokenIsFast(b, REAL_NUM)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, REAL_NUM);
    exit_section_(b, m, REAL_LITERAL_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // command_argument_list | primary_expression
  static boolean redirected_file_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "redirected_file_name")) return false;
    boolean r;
    r = command_argument_list(b, l + 1);
    if (!r) r = primary_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // merging_redirection_operator | file_redirection_operator  redirected_file_name
  public static boolean redirection(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "redirection")) return false;
    if (!nextTokenIs(b, "<redirection>", OP_FR, OP_MR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, REDIRECTION, "<redirection>");
    r = merging_redirection_operator(b, l + 1);
    if (!r) r = redirection_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // file_redirection_operator  redirected_file_name
  private static boolean redirection_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "redirection_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = file_redirection_operator(b, l + 1);
    r = r && redirected_file_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // simple_type_identifier compound_type_identifier*
  public static boolean reference_type_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reference_type_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, REFERENCE_TYPE_ELEMENT, "<reference type element>");
    r = simple_type_identifier(b, l + 1);
    r = r && reference_type_element_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // compound_type_identifier*
  private static boolean reference_type_element_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reference_type_element_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!compound_type_identifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "reference_type_element_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // DOT | DOT_DOT | PATH_SEP
  static boolean relative_path(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relative_path")) return false;
    boolean r;
    r = consumeToken(b, DOT);
    if (!r) r = consumeToken(b, DOT_DOT);
    if (!r) r = consumeToken(b, PATH_SEP);
    return r;
  }

  /* ********************************************************** */
  // relative_path
  public static boolean relative_path_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "relative_path_item")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PATH_ITEM, "<relative path item>");
    r = relative_path(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // REQUIRES_COMMENT_START ws command_argument_list+
  static boolean requires_comment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "requires_comment")) return false;
    if (!nextTokenIs(b, REQUIRES_COMMENT_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, REQUIRES_COMMENT_START);
    r = r && ws(b, l + 1);
    r = r && requires_comment_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // command_argument_list+
  private static boolean requires_comment_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "requires_comment_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_argument_list(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!command_argument_list(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "requires_comment_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'class'|'data'|'do'|'elseif'|'exit'|'filter'|'for'|'foreach'|'from'|'function'|'if'|'return'|'switch'|'throw'
  // |'trap'|'try'|'using'|'var'|'while'|'configuration'|'enum'
  static boolean reserved_statement_keywords(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reserved_statement_keywords")) return false;
    boolean r;
    r = consumeToken(b, CLASS);
    if (!r) r = consumeToken(b, DATA);
    if (!r) r = consumeToken(b, DO);
    if (!r) r = consumeToken(b, ELSEIF);
    if (!r) r = consumeToken(b, EXIT);
    if (!r) r = consumeToken(b, FILTER);
    if (!r) r = consumeToken(b, FOR);
    if (!r) r = consumeToken(b, FOREACH);
    if (!r) r = consumeToken(b, FROM);
    if (!r) r = consumeToken(b, FUNCTION);
    if (!r) r = consumeToken(b, IF);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, SWITCH);
    if (!r) r = consumeToken(b, THROW);
    if (!r) r = consumeToken(b, TRAP);
    if (!r) r = consumeToken(b, TRY);
    if (!r) r = consumeToken(b, USING);
    if (!r) r = consumeToken(b, VAR);
    if (!r) r = consumeToken(b, WHILE);
    if (!r) r = consumeToken(b, CONFIGURATION);
    if (!r) r = consumeToken(b, ENUM);
    return r;
  }

  /* ********************************************************** */
  // resource_type nls? ( resource_name nls? resource_block_tail | resource_block_tail )
  public static boolean resource_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_block")) return false;
    if (!nextTokenIs(b, SIMPLE_ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = resource_type(b, l + 1);
    r = r && resource_block_1(b, l + 1);
    r = r && resource_block_2(b, l + 1);
    exit_section_(b, m, RESOURCE_BLOCK, r);
    return r;
  }

  // nls?
  private static boolean resource_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_block_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // resource_name nls? resource_block_tail | resource_block_tail
  private static boolean resource_block_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_block_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = resource_block_2_0(b, l + 1);
    if (!r) r = resource_block_tail(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // resource_name nls? resource_block_tail
  private static boolean resource_block_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_block_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = resource_name(b, l + 1);
    r = r && resource_block_2_0_1(b, l + 1);
    r = r && resource_block_tail(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean resource_block_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_block_2_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // hash_literal_body
  public static boolean resource_block_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_block_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, BLOCK_BODY, "<resource block body>");
    r = hash_literal_body(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LCURLY nls? resource_block_body? nls? RCURLY
  static boolean resource_block_tail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_block_tail")) return false;
    if (!nextTokenIs(b, LCURLY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LCURLY);
    r = r && resource_block_tail_1(b, l + 1);
    r = r && resource_block_tail_2(b, l + 1);
    r = r && resource_block_tail_3(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean resource_block_tail_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_block_tail_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // resource_block_body?
  private static boolean resource_block_tail_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_block_tail_2")) return false;
    resource_block_body(b, l + 1);
    return true;
  }

  // nls?
  private static boolean resource_block_tail_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_block_tail_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // function_name
  static boolean resource_name(PsiBuilder b, int l) {
    return function_name(b, l + 1);
  }

  /* ********************************************************** */
  // SIMPLE_ID
  static boolean resource_type(PsiBuilder b, int l) {
    return consumeToken(b, SIMPLE_ID);
  }

  /* ********************************************************** */
  // top_script_block
  public static boolean script_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, BLOCK_BODY, "<script block>");
    r = top_script_block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // named_block_list | (statement_list | statement_terminators)+
  static boolean script_block_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block_body")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = named_block_list(b, l + 1);
    if (!r) r = script_block_body_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (statement_list | statement_terminators)+
  private static boolean script_block_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block_body_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = script_block_body_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!script_block_body_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "script_block_body_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_list | statement_terminators
  private static boolean script_block_body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block_body_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_list(b, l + 1);
    if (!r) r = statement_terminators(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LCURLY script_block_rule? RCURLY
  public static boolean script_block_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block_expression")) return false;
    if (!nextTokenIsFast(b, LCURLY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, LCURLY);
    r = r && script_block_expression_1(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, SCRIPT_BLOCK_EXPRESSION, r);
    return r;
  }

  // script_block_rule?
  private static boolean script_block_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block_expression_1")) return false;
    script_block_rule(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // <<allow_any_expression script_block>>
  static boolean script_block_rule(PsiBuilder b, int l) {
    return allow_any_expression(b, l + 1, PowerShellParser::script_block);
  }

  /* ********************************************************** */
  // attribute_list? nls? target_variable_expression nls? script_parameter_default?
  static boolean script_parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = script_parameter_0(b, l + 1);
    r = r && script_parameter_1(b, l + 1);
    r = r && target_variable_expression(b, l + 1);
    r = r && script_parameter_3(b, l + 1);
    r = r && script_parameter_4(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // attribute_list?
  private static boolean script_parameter_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_0")) return false;
    attribute_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean script_parameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean script_parameter_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // script_parameter_default?
  private static boolean script_parameter_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_4")) return false;
    script_parameter_default(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '=' nls? expression
  static boolean script_parameter_default(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_default")) return false;
    if (!nextTokenIs(b, EQ)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && script_parameter_default_1(b, l + 1);
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean script_parameter_default_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_default_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parse_argument_list script_parameter_list_rule>>
  static boolean script_parameter_list(PsiBuilder b, int l) {
    return parse_argument_list(b, l + 1, PowerShellParser::script_parameter_list_rule);
  }

  /* ********************************************************** */
  // script_parameter (nls? COMMA nls? script_parameter)*
  static boolean script_parameter_list_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_list_rule")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = script_parameter(b, l + 1);
    r = r && script_parameter_list_rule_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nls? COMMA nls? script_parameter)*
  private static boolean script_parameter_list_rule_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_list_rule_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!script_parameter_list_rule_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "script_parameter_list_rule_1", c)) break;
    }
    return true;
  }

  // nls? COMMA nls? script_parameter
  private static boolean script_parameter_list_rule_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_list_rule_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = script_parameter_list_rule_1_0_0(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && script_parameter_list_rule_1_0_2(b, l + 1);
    r = r && script_parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean script_parameter_list_rule_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_list_rule_1_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean script_parameter_list_rule_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_list_rule_1_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // SEMI |  NLS | new_line_char | comment
  static boolean sep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sep")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = consumeToken(b, NLS);
    if (!r) r = new_line_char(b, l + 1);
    if (!r) r = comment(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // 'sequence' statement_block
  public static boolean sequence_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SEQUENCE_STATEMENT, "<sequence statement>");
    r = consumeToken(b, "sequence");
    r = r && statement_block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SIMPLE_ID | allowed_identifier_keywords
  public static boolean simple_name_identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_name_identifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IDENTIFIER, "<simple name identifier>");
    r = consumeToken(b, SIMPLE_ID);
    if (!r) r = allowed_identifier_keywords(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SIMPLE_ID | allowed_identifier_keywords
  public static boolean simple_name_reference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_name_reference")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, REFERENCE_IDENTIFIER, "<simple name reference>");
    r = consumeToken(b, SIMPLE_ID);
    if (!r) r = allowed_identifier_keywords(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SIMPLE_ID | allowed_identifier_keywords
  public static boolean simple_type_identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_type_identifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, REFERENCE_TYPE_ELEMENT, "<reference type element>");
    r = consumeToken(b, SIMPLE_ID);
    if (!r) r = allowed_identifier_keywords(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (DS | AT) nws (declaration_scope nws)? variable_name_simple
  static boolean simple_variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_variable")) return false;
    if (!nextTokenIs(b, "", AT, DS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = simple_variable_0(b, l + 1);
    r = r && nws(b, l + 1);
    r = r && simple_variable_2(b, l + 1);
    r = r && variable_name_simple(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // DS | AT
  private static boolean simple_variable_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_variable_0")) return false;
    boolean r;
    r = consumeToken(b, DS);
    if (!r) r = consumeToken(b, AT);
    return r;
  }

  // (declaration_scope nws)?
  private static boolean simple_variable_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_variable_2")) return false;
    simple_variable_2_0(b, l + 1);
    return true;
  }

  // declaration_scope nws
  private static boolean simple_variable_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_variable_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = declaration_scope(b, l + 1);
    r = r && nws(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // if_statement
  //                       | (label nls?)? labeled_statement
  //                       | class_declaration_statement
  //                       | enum_declaration_statement
  //                       | function_statement
  //                       | workflow_statement
  //                       | flow_control_statement //sep
  //                       | trap_pipeline
  //                       | try_statement
  //                       | data_statement
  //                       | using_statement
  //                       | inlinescript_statement
  //                       | parallel_statement
  //                       | sequence_statement
  //                       | parse_resource_block
  //                       | parse_node_block
  //                       | pipeline
  static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = if_statement(b, l + 1);
    if (!r) r = statement_1(b, l + 1);
    if (!r) r = class_declaration_statement(b, l + 1);
    if (!r) r = enum_declaration_statement(b, l + 1);
    if (!r) r = function_statement(b, l + 1);
    if (!r) r = workflow_statement(b, l + 1);
    if (!r) r = flow_control_statement(b, l + 1);
    if (!r) r = trap_pipeline(b, l + 1);
    if (!r) r = try_statement(b, l + 1);
    if (!r) r = data_statement(b, l + 1);
    if (!r) r = using_statement(b, l + 1);
    if (!r) r = inlinescript_statement(b, l + 1);
    if (!r) r = parallel_statement(b, l + 1);
    if (!r) r = sequence_statement(b, l + 1);
    if (!r) r = parse_resource_block(b, l + 1);
    if (!r) r = parse_node_block(b, l + 1);
    if (!r) r = pipeline(b, l + 1);
    exit_section_(b, l, m, r, false, PowerShellParser::statement_recover);
    return r;
  }

  // (label nls?)? labeled_statement
  private static boolean statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_1_0(b, l + 1);
    r = r && labeled_statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (label nls?)?
  private static boolean statement_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1_0")) return false;
    statement_1_0_0(b, l + 1);
    return true;
  }

  // label nls?
  private static boolean statement_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = label(b, l + 1);
    r = r && statement_1_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean statement_1_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1_0_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LCURLY block_body? RCURLY
  static boolean statement_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_block")) return false;
    if (!nextTokenIs(b, LCURLY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LCURLY);
    r = r && statement_block_1(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // block_body?
  private static boolean statement_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_block_1")) return false;
    block_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !block_body_stop_tokens
  static boolean statement_block_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_block_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !block_body_stop_tokens(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // sep | RCURLY | RP | <<eof>>
  static boolean statement_end(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_end")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sep(b, l + 1);
    if (!r) r = consumeToken(b, RCURLY);
    if (!r) r = consumeToken(b, RP);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // statement ( statement_terminators statement )*
  static boolean statement_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_list")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = statement(b, l + 1);
    p = r; // pin = 1
    r = r && statement_list_1(b, l + 1);
    exit_section_(b, l, m, r, p, PowerShellParser::top_level_recover);
    return r || p;
  }

  // ( statement_terminators statement )*
  private static boolean statement_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "statement_list_1", c)) break;
    }
    return true;
  }

  // statement_terminators statement
  private static boolean statement_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_terminators(b, l + 1);
    r = r && statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // <<allow_any_expression statement_list>>
  static boolean statement_list_rule(PsiBuilder b, int l) {
    return allow_any_expression(b, l + 1, PowerShellParser::statement_list);
  }

  /* ********************************************************** */
  // !statement_stop_tokens
  static boolean statement_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !statement_stop_tokens(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // reserved_statement_keywords | sep | nls | RCURLY | LCURLY | RP | SQBR_L
  static boolean statement_stop_tokens(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_stop_tokens")) return false;
    boolean r;
    r = reserved_statement_keywords(b, l + 1);
    if (!r) r = sep(b, l + 1);
    if (!r) r = nls(b, l + 1);
    if (!r) r = consumeToken(b, RCURLY);
    if (!r) r = consumeToken(b, LCURLY);
    if (!r) r = consumeToken(b, RP);
    if (!r) r = consumeToken(b, SQBR_L);
    return r;
  }

  /* ********************************************************** */
  // sep+
  static boolean statement_terminators(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_terminators")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sep(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!sep(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "statement_terminators", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // EXPANDABLE_STRING_PART
  static boolean string_literal_entry(PsiBuilder b, int l) {
    return consumeToken(b, EXPANDABLE_STRING_PART);
  }

  /* ********************************************************** */
  // expandable_string_literal | verbatim_string_literal | expandable_here_string_literal | verbatim_here_string_literal
  public static boolean string_literal_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_literal_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING_LITERAL_EXPRESSION, "<string literal expression>");
    r = expandable_string_literal(b, l + 1);
    if (!r) r = verbatim_string_literal(b, l + 1);
    if (!r) r = expandable_here_string_literal(b, l + 1);
    if (!r) r = verbatim_here_string_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // DS nws LP nls? statement_list_rule? nls? RP
  public static boolean sub_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_expression")) return false;
    if (!nextTokenIsFast(b, DS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, DS);
    r = r && nws(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && sub_expression_3(b, l + 1);
    r = r && sub_expression_4(b, l + 1);
    r = r && sub_expression_5(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, SUB_EXPRESSION, r);
    return r;
  }

  // nls?
  private static boolean sub_expression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_expression_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // statement_list_rule?
  private static boolean sub_expression_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_expression_4")) return false;
    statement_list_rule(b, l + 1);
    return true;
  }

  // nls?
  private static boolean sub_expression_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_expression_5")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? '{' switch_body? '}'
  static boolean switch_block_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_block_body")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = switch_block_body_0(b, l + 1);
    r = r && consumeToken(b, LCURLY);
    r = r && switch_block_body_2(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean switch_block_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_block_body_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // switch_body?
  private static boolean switch_block_body_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_block_body_2")) return false;
    switch_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? switch_clause+ nls? | nls
  public static boolean switch_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BLOCK_BODY, "<switch body>");
    r = switch_body_0(b, l + 1);
    if (!r) r = nls(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls? switch_clause+ nls?
  private static boolean switch_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_body_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = switch_body_0_0(b, l + 1);
    r = r && switch_body_0_1(b, l + 1);
    r = r && switch_body_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean switch_body_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_body_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // switch_clause+
  private static boolean switch_body_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_body_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = switch_clause(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!switch_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "switch_body_0_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean switch_body_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_body_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // switch_clause_condition nls? switch_clause_block statement_terminators?
  static boolean switch_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_clause")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = switch_clause_condition(b, l + 1);
    r = r && switch_clause_1(b, l + 1);
    r = r && switch_clause_block(b, l + 1);
    r = r && switch_clause_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean switch_clause_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_clause_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // statement_terminators?
  private static boolean switch_clause_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_clause_3")) return false;
    statement_terminators(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // statement_block
  public static boolean switch_clause_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_clause_block")) return false;
    if (!nextTokenIs(b, LCURLY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_block(b, l + 1);
    exit_section_(b, m, SWITCH_CLAUSE_BLOCK, r);
    return r;
  }

  /* ********************************************************** */
  // parse_command_argument | primary_expression
  static boolean switch_clause_condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_clause_condition")) return false;
    boolean r;
    r = parse_command_argument(b, l + 1);
    if (!r) r = primary_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // LP nls? pipeline nls? RP | '-file' nls? switch_filename
  static boolean switch_condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_condition")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = switch_condition_0(b, l + 1);
    if (!r) r = switch_condition_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LP nls? pipeline nls? RP
  private static boolean switch_condition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_condition_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, LP);
    r = r && switch_condition_0_1(b, l + 1);
    r = r && pipeline(b, l + 1);
    r = r && switch_condition_0_3(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean switch_condition_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_condition_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean switch_condition_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_condition_0_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // '-file' nls? switch_filename
  private static boolean switch_condition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_condition_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, "-file");
    r = r && switch_condition_1_1(b, l + 1);
    r = r && switch_filename(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean switch_condition_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_condition_1_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // parse_command_argument | primary_expression
  static boolean switch_filename(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_filename")) return false;
    boolean r;
    r = parse_command_argument(b, l + 1);
    if (!r) r = primary_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // '-regex'
  //                              | '-wildcard'
  //                              | '-exact'
  //                              | '-casesensitive'
  //                              | '-parallel'
  static boolean switch_parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_parameter")) return false;
    boolean r;
    r = consumeToken(b, "-regex");
    if (!r) r = consumeToken(b, "-wildcard");
    if (!r) r = consumeToken(b, "-exact");
    if (!r) r = consumeToken(b, "-casesensitive");
    if (!r) r = consumeToken(b, "-parallel");
    return r;
  }

  /* ********************************************************** */
  // 'switch' nls? switch_parameter* switch_condition switch_block_body
  public static boolean switch_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_statement")) return false;
    if (!nextTokenIs(b, SWITCH)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SWITCH_STATEMENT, null);
    r = consumeToken(b, SWITCH);
    p = r; // pin = 1
    r = r && report_error_(b, switch_statement_1(b, l + 1));
    r = p && report_error_(b, switch_statement_2(b, l + 1)) && r;
    r = p && report_error_(b, switch_condition(b, l + 1)) && r;
    r = p && switch_block_body(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean switch_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // switch_parameter*
  private static boolean switch_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_statement_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!switch_parameter(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "switch_statement_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // variable
  public static boolean target_variable_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "target_variable_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TARGET_VARIABLE_EXPRESSION, "<target variable expression>");
    r = variable(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '-throttlelimit' primary_expression
  static boolean throttlelimit_parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throttlelimit_parameter")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "-throttlelimit");
    r = r && primary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ('throw' | 'return' | 'exit') pipeline?
  static boolean throw_return_exit_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throw_return_exit_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = throw_return_exit_statement_0(b, l + 1);
    p = r; // pin = 1
    r = r && throw_return_exit_statement_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // 'throw' | 'return' | 'exit'
  private static boolean throw_return_exit_statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throw_return_exit_statement_0")) return false;
    boolean r;
    r = consumeToken(b, THROW);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, EXIT);
    return r;
  }

  // pipeline?
  private static boolean throw_return_exit_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "throw_return_exit_statement_1")) return false;
    pipeline(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // sep* ( top_script_block | statement_list | configuration_block_list )?
  static boolean top_level_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = top_level_element_0(b, l + 1);
    r = r && top_level_element_1(b, l + 1);
    exit_section_(b, l, m, r, false, PowerShellParser::top_level_recover);
    return r;
  }

  // sep*
  private static boolean top_level_element_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_element_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!sep(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "top_level_element_0", c)) break;
    }
    return true;
  }

  // ( top_script_block | statement_list | configuration_block_list )?
  private static boolean top_level_element_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_element_1")) return false;
    top_level_element_1_0(b, l + 1);
    return true;
  }

  // top_script_block | statement_list | configuration_block_list
  private static boolean top_level_element_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_element_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = top_script_block(b, l + 1);
    if (!r) r = statement_list(b, l + 1);
    if (!r) r = configuration_block_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(reserved_statement_keywords | sep | nls | RCURLY | LCURLY | RP | SQBR_L)
  static boolean top_level_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !top_level_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // reserved_statement_keywords | sep | nls | RCURLY | LCURLY | RP | SQBR_L
  private static boolean top_level_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_recover_0")) return false;
    boolean r;
    r = reserved_statement_keywords(b, l + 1);
    if (!r) r = sep(b, l + 1);
    if (!r) r = nls(b, l + 1);
    if (!r) r = consumeTokenFast(b, RCURLY);
    if (!r) r = consumeTokenFast(b, LCURLY);
    if (!r) r = consumeTokenFast(b, RP);
    if (!r) r = consumeTokenFast(b, SQBR_L);
    return r;
  }

  /* ********************************************************** */
  // nls? top_script_block_content nls? | nls
  static boolean top_script_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_script_block")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = top_script_block_0(b, l + 1);
    if (!r) r = nls(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls? top_script_block_content nls?
  private static boolean top_script_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_script_block_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = top_script_block_0_0(b, l + 1);
    r = r && top_script_block_content(b, l + 1);
    r = r && top_script_block_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean top_script_block_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_script_block_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean top_script_block_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_script_block_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // block_parameter_clause? statement_terminators? script_block_body | block_parameter_clause statement_terminators?
  static boolean top_script_block_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_script_block_content")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = top_script_block_content_0(b, l + 1);
    if (!r) r = top_script_block_content_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // block_parameter_clause? statement_terminators? script_block_body
  private static boolean top_script_block_content_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_script_block_content_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = top_script_block_content_0_0(b, l + 1);
    r = r && top_script_block_content_0_1(b, l + 1);
    r = r && script_block_body(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // block_parameter_clause?
  private static boolean top_script_block_content_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_script_block_content_0_0")) return false;
    block_parameter_clause(b, l + 1);
    return true;
  }

  // statement_terminators?
  private static boolean top_script_block_content_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_script_block_content_0_1")) return false;
    statement_terminators(b, l + 1);
    return true;
  }

  // block_parameter_clause statement_terminators?
  private static boolean top_script_block_content_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_script_block_content_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block_parameter_clause(b, l + 1);
    r = r && top_script_block_content_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_terminators?
  private static boolean top_script_block_content_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_script_block_content_1_1")) return false;
    statement_terminators(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // trap_statement statement?
  static boolean trap_pipeline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trap_pipeline")) return false;
    if (!nextTokenIs(b, TRAP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = trap_statement(b, l + 1);
    r = r && trap_pipeline_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement?
  private static boolean trap_pipeline_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trap_pipeline_1")) return false;
    statement(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'trap' nls? type_literal_expression? nls? statement_block
  public static boolean trap_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trap_statement")) return false;
    if (!nextTokenIs(b, TRAP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TRAP_STATEMENT, null);
    r = consumeToken(b, TRAP);
    p = r; // pin = 1
    r = r && report_error_(b, trap_statement_1(b, l + 1));
    r = p && report_error_(b, trap_statement_2(b, l + 1)) && r;
    r = p && report_error_(b, trap_statement_3(b, l + 1)) && r;
    r = p && statement_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean trap_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trap_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // type_literal_expression?
  private static boolean trap_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trap_statement_2")) return false;
    type_literal_expression(b, l + 1);
    return true;
  }

  // nls?
  private static boolean trap_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trap_statement_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'try' nls? statement_block (nls? catch_clauses)? (nls? finally_clause)?
  public static boolean try_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement")) return false;
    if (!nextTokenIs(b, TRY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TRY_STATEMENT, null);
    r = consumeToken(b, TRY);
    p = r; // pin = 1
    r = r && report_error_(b, try_statement_1(b, l + 1));
    r = p && report_error_(b, statement_block(b, l + 1)) && r;
    r = p && report_error_(b, try_statement_3(b, l + 1)) && r;
    r = p && try_statement_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean try_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // (nls? catch_clauses)?
  private static boolean try_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement_3")) return false;
    try_statement_3_0(b, l + 1);
    return true;
  }

  // nls? catch_clauses
  private static boolean try_statement_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = try_statement_3_0_0(b, l + 1);
    r = r && catch_clauses(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean try_statement_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement_3_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // (nls? finally_clause)?
  private static boolean try_statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement_4")) return false;
    try_statement_4_0(b, l + 1);
    return true;
  }

  // nls? finally_clause
  private static boolean try_statement_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = try_statement_4_0_0(b, l + 1);
    r = r && finally_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean try_statement_4_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement_4_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // array_type_element | generic_type_element | reference_type_element
  public static boolean type_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, TYPE_ELEMENT, "<type element>");
    r = array_type_element(b, l + 1);
    if (!r) r = generic_type_element(b, l + 1);
    if (!r) r = reference_type_element(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SQBR_L type_element SQBR_R
  public static boolean type_literal_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_literal_expression")) return false;
    if (!nextTokenIsFast(b, SQBR_L)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, SQBR_L);
    r = r && type_element(b, l + 1);
    r = r && consumeToken(b, SQBR_R);
    exit_section_(b, m, TYPE_LITERAL_EXPRESSION, r);
    return r;
  }

  /* ********************************************************** */
  // 'using' nls? pipeline
  public static boolean using_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "using_statement")) return false;
    if (!nextTokenIs(b, USING)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, USING_STATEMENT, null);
    r = consumeToken(b, USING);
    p = r; // pin = 1
    r = r && report_error_(b, using_statement_1(b, l + 1));
    r = p && pipeline(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // nls?
  private static boolean using_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "using_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // parenthesized_expression
  //                   | sub_expression
  //                   | array_expression
  //                   | script_block_expression
  //                   | hash_literal_expression
  //                   | literal
  //                   | cast_expression
  // //                  | prefix_op lvalue
  //                   | type_literal_expression
  //                   | target_variable_expression
  static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    r = parenthesized_expression(b, l + 1);
    if (!r) r = sub_expression(b, l + 1);
    if (!r) r = array_expression(b, l + 1);
    if (!r) r = script_block_expression(b, l + 1);
    if (!r) r = hash_literal_expression(b, l + 1);
    if (!r) r = literal(b, l + 1);
    if (!r) r = cast_expression(b, l + 1);
    if (!r) r = type_literal_expression(b, l + 1);
    if (!r) r = target_variable_expression(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // DS nws predefined_var_id | simple_variable | braced_variable
  static boolean variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_0(b, l + 1);
    if (!r) r = simple_variable(b, l + 1);
    if (!r) r = braced_variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // DS nws predefined_var_id
  private static boolean variable_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DS);
    r = r && nws(b, l + 1);
    r = r && predefined_var_id(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // BRACED_ID
  public static boolean variable_name_braced(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_name_braced")) return false;
    if (!nextTokenIs(b, BRACED_ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACED_ID);
    exit_section_(b, m, IDENTIFIER, r);
    return r;
  }

  /* ********************************************************** */
  // SIMPLE_ID | VAR_ID | 'this'
  public static boolean variable_name_simple(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_name_simple")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IDENTIFIER, "<variable name simple>");
    r = consumeToken(b, SIMPLE_ID);
    if (!r) r = consumeToken(b, VAR_ID);
    if (!r) r = consumeToken(b, THIS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SIMPLE_ID ':'
  static boolean variable_namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_namespace")) return false;
    if (!nextTokenIs(b, SIMPLE_ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SIMPLE_ID, COLON);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // VERBATIM_ARG_START VERBATIM_ARG_INPUT
  public static boolean verbatim_command_argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "verbatim_command_argument")) return false;
    if (!nextTokenIs(b, VERBATIM_ARG_START)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VERBATIM_ARG_START, VERBATIM_ARG_INPUT);
    exit_section_(b, m, VERBATIM_COMMAND_ARGUMENT, r);
    return r;
  }

  /* ********************************************************** */
  // VERBATIM_HERE_STRING
  static boolean verbatim_here_string_literal(PsiBuilder b, int l) {
    return consumeToken(b, VERBATIM_HERE_STRING);
  }

  /* ********************************************************** */
  // VERBATIM_STRING
  static boolean verbatim_string_literal(PsiBuilder b, int l) {
    return consumeToken(b, VERBATIM_STRING);
  }

  /* ********************************************************** */
  // pipeline
  static boolean while_condition(PsiBuilder b, int l) {
    return pipeline(b, l + 1);
  }

  /* ********************************************************** */
  // 'while' nls? LP nls? while_condition nls? RP nls? statement_block
  public static boolean while_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WHILE_STATEMENT, "<while statement>");
    r = consumeToken(b, WHILE);
    p = r; // pin = 1
    r = r && report_error_(b, while_statement_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LP)) && r;
    r = p && report_error_(b, while_statement_3(b, l + 1)) && r;
    r = p && report_error_(b, while_condition(b, l + 1)) && r;
    r = p && report_error_(b, while_statement_5(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, RP)) && r;
    r = p && report_error_(b, while_statement_7(b, l + 1)) && r;
    r = p && statement_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, PowerShellParser::statement_recover);
    return r || p;
  }

  // nls?
  private static boolean while_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean while_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement_3")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean while_statement_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement_5")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean while_statement_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement_7")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'workflow' nls? function_statement_tail
  public static boolean workflow_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "workflow_statement")) return false;
    if (!nextTokenIs(b, WORKFLOW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WORKFLOW);
    r = r && workflow_statement_1(b, l + 1);
    r = r && function_statement_tail(b, l + 1);
    exit_section_(b, m, FUNCTION_STATEMENT, r);
    return r;
  }

  // nls?
  private static boolean workflow_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "workflow_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // <<isWhiteSpace>>
  static boolean ws(PsiBuilder b, int l) {
    return isWhiteSpace(b, l + 1);
  }

  /* ********************************************************** */
  // Expression root: expression
  // Operator priority table:
  // 0: POSTFIX(assignment_expression)
  // 1: BINARY(logical_expression)
  // 2: BINARY(bitwise_expression)
  // 3: BINARY(comparison_expression)
  // 4: N_ARY(additive_expression)
  // 5: BINARY(multiplicative_expression)
  // 6: BINARY(format_expression)
  // 7: BINARY(range_expression)
  // 8: N_ARY(array_literal_expression)
  // 9: ATOM(unary_expression)
  public static boolean expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expression")) return false;
    addVariant(b, "<expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expression>");
    r = unary_expression(b, l + 1);
    p = r;
    r = r && expression_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean expression_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expression_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 0 && assignment_expression_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, ASSIGNMENT_EXPRESSION, r, true, null);
      }
      else if (g < 1 && logical_expression_0(b, l + 1)) {
        r = expression(b, l, 1);
        exit_section_(b, l, m, LOGICAL_EXPRESSION, r, true, null);
      }
      else if (g < 2 && bitwise_expression_0(b, l + 1)) {
        r = expression(b, l, 2);
        exit_section_(b, l, m, BITWISE_EXPRESSION, r, true, null);
      }
      else if (g < 3 && comparison_expression_0(b, l + 1)) {
        r = expression(b, l, 3);
        exit_section_(b, l, m, COMPARISON_EXPRESSION, r, true, null);
      }
      else if (g < 4 && additive_expression_0(b, l + 1)) {
        while (true) {
          r = report_error_(b, expression(b, l, 4));
          if (!additive_expression_0(b, l + 1)) break;
        }
        exit_section_(b, l, m, ADDITIVE_EXPRESSION, r, true, null);
      }
      else if (g < 5 && multiplicative_expression_0(b, l + 1)) {
        r = expression(b, l, 5);
        exit_section_(b, l, m, MULTIPLICATIVE_EXPRESSION, r, true, null);
      }
      else if (g < 6 && format_expression_0(b, l + 1)) {
        r = expression(b, l, 6);
        exit_section_(b, l, m, FORMAT_EXPRESSION, r, true, null);
      }
      else if (g < 7 && range_expression_0(b, l + 1)) {
        r = expression(b, l, 7);
        exit_section_(b, l, m, RANGE_EXPRESSION, r, true, null);
      }
      else if (g < 8 && array_literal_expression_0(b, l + 1)) {
        while (true) {
          r = report_error_(b, expression(b, l, 8));
          if (!array_literal_expression_0(b, l + 1)) break;
        }
        exit_section_(b, l, m, ARRAY_LITERAL_EXPRESSION, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // (!is_in_call_arguments assignment_operator) nls? statement
  private static boolean assignment_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = assignment_expression_0_0(b, l + 1);
    r = r && assignment_expression_0_1(b, l + 1);
    r = r && statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !is_in_call_arguments assignment_operator
  private static boolean assignment_expression_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_expression_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = assignment_expression_0_0_0(b, l + 1);
    r = r && assignment_operator(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !is_in_call_arguments
  private static boolean assignment_expression_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_expression_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !is_in_call_arguments(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls?
  private static boolean assignment_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_expression_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // ( OP_AND | OP_OR | OP_XOR ) nls?
  private static boolean logical_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = logical_expression_0_0(b, l + 1);
    r = r && logical_expression_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_AND | OP_OR | OP_XOR
  private static boolean logical_expression_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_expression_0_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, OP_AND);
    if (!r) r = consumeTokenSmart(b, OP_OR);
    if (!r) r = consumeTokenSmart(b, OP_XOR);
    return r;
  }

  // nls?
  private static boolean logical_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_expression_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // (OP_BAND | OP_BOR | OP_BXOR ) nls?
  private static boolean bitwise_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = bitwise_expression_0_0(b, l + 1);
    r = r && bitwise_expression_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // OP_BAND | OP_BOR | OP_BXOR
  private static boolean bitwise_expression_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_expression_0_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, OP_BAND);
    if (!r) r = consumeTokenSmart(b, OP_BOR);
    if (!r) r = consumeTokenSmart(b, OP_BXOR);
    return r;
  }

  // nls?
  private static boolean bitwise_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_expression_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // comparison_operator nls?
  private static boolean comparison_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = comparison_operator(b, l + 1);
    r = r && comparison_expression_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean comparison_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_expression_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // ( PLUS | DASH ) nls?
  private static boolean additive_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = additive_expression_0_0(b, l + 1);
    r = r && additive_expression_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PLUS | DASH
  private static boolean additive_expression_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_0_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, PLUS);
    if (!r) r = consumeTokenSmart(b, DASH);
    return r;
  }

  // nls?
  private static boolean additive_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // ( STAR | DIV | PERS) nls?
  private static boolean multiplicative_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = multiplicative_expression_0_0(b, l + 1);
    r = r && multiplicative_expression_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // STAR | DIV | PERS
  private static boolean multiplicative_expression_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_0_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, STAR);
    if (!r) r = consumeTokenSmart(b, DIV);
    if (!r) r = consumeTokenSmart(b, PERS);
    return r;
  }

  // nls?
  private static boolean multiplicative_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // format_operator nls?
  private static boolean format_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "format_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = format_operator(b, l + 1);
    r = r && format_expression_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean format_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "format_expression_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // DOT_DOT nls?
  private static boolean range_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, DOT_DOT);
    r = r && range_expression_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean range_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // !is_in_call_arguments COMMA nls?
  private static boolean array_literal_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_literal_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array_literal_expression_0_0(b, l + 1);
    r = r && consumeTokenSmart(b, COMMA);
    r = r && array_literal_expression_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !is_in_call_arguments
  private static boolean array_literal_expression_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_literal_expression_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !is_in_call_arguments(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls?
  private static boolean array_literal_expression_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_literal_expression_0_2")) return false;
    nls(b, l + 1);
    return true;
  }

  // expression_with_unary_operator | primary_expression
  public static boolean unary_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, UNARY_EXPRESSION, "<unary expression>");
    r = expression_with_unary_operator(b, l + 1);
    if (!r) r = primary_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
