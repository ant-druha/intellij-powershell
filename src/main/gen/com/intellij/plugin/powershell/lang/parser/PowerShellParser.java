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

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, EXTENDS_SETS_);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return top_level_element(builder_, level_ + 1);
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
  static boolean allowed_identifier_keywords(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "allowed_identifier_keywords")) return false;
    boolean result_;
    result_ = consumeToken(builder_, BEGIN);
    if (!result_) result_ = consumeToken(builder_, BREAK);
    if (!result_) result_ = consumeToken(builder_, CATCH);
    if (!result_) result_ = consumeToken(builder_, CONTINUE);
    if (!result_) result_ = consumeToken(builder_, DATA);
    if (!result_) result_ = consumeToken(builder_, EXIT);
    if (!result_) result_ = consumeToken(builder_, DEFINE);
    if (!result_) result_ = consumeToken(builder_, DYNAMICPARAM);
    if (!result_) result_ = consumeToken(builder_, ELSE);
    if (!result_) result_ = consumeToken(builder_, END);
    if (!result_) result_ = consumeToken(builder_, FINALLY);
    if (!result_) result_ = consumeToken(builder_, FOREACH);
    if (!result_) result_ = consumeToken(builder_, HIDDEN);
    if (!result_) result_ = consumeToken(builder_, IN);
    if (!result_) result_ = consumeToken(builder_, INLINESCRIPT);
    if (!result_) result_ = consumeToken(builder_, PARALLEL);
    if (!result_) result_ = consumeToken(builder_, PARAM);
    if (!result_) result_ = consumeToken(builder_, PROCESS);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    if (!result_) result_ = consumeToken(builder_, THIS);
    if (!result_) result_ = consumeToken(builder_, UNTIL);
    if (!result_) result_ = consumeToken(builder_, WORKFLOW);
    return result_;
  }

  /* ********************************************************** */
  // <<parse_argument_list expression_list_rule>>
  static boolean argument_expression_list(PsiBuilder builder_, int level_) {
    return parse_argument_list(builder_, level_ + 1, PowerShellParser::expression_list_rule);
  }

  /* ********************************************************** */
  // COMMA | LP | RP | LCURLY | RCURLY | sep | DS | PIPE | AMP | BACKTICK | DQ_OPEN | DQ_CLOSE | nls | <<eof>>
  static boolean argument_separation_token_no_ws(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_separation_token_no_ws")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, LP);
    if (!result_) result_ = consumeToken(builder_, RP);
    if (!result_) result_ = consumeToken(builder_, LCURLY);
    if (!result_) result_ = consumeToken(builder_, RCURLY);
    if (!result_) result_ = sep(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, DS);
    if (!result_) result_ = consumeToken(builder_, PIPE);
    if (!result_) result_ = consumeToken(builder_, AMP);
    if (!result_) result_ = consumeToken(builder_, BACKTICK);
    if (!result_) result_ = consumeToken(builder_, DQ_OPEN);
    if (!result_) result_ = consumeToken(builder_, DQ_CLOSE);
    if (!result_) result_ = nls(builder_, level_ + 1);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // &argument_separator_token
  static boolean argument_separator_condition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_separator_condition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = argument_separator_token(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // &argument_separation_token_no_ws
  static boolean argument_separator_condition_no_ws(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_separator_condition_no_ws")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = argument_separation_token_no_ws(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // argument_separation_token_no_ws | ws
  static boolean argument_separator_token(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argument_separator_token")) return false;
    boolean result_;
    result_ = argument_separation_token_no_ws(builder_, level_ + 1);
    if (!result_) result_ = ws(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // AT nws LP nls? statement_list_rule? nls? RP
  public static boolean array_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expression")) return false;
    if (!nextTokenIsFast(builder_, AT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, AT);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LP);
    result_ = result_ && array_expression_3(builder_, level_ + 1);
    result_ = result_ && array_expression_4(builder_, level_ + 1);
    result_ = result_ && array_expression_5(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    exit_section_(builder_, marker_, ARRAY_EXPRESSION, result_);
    return result_;
  }

  // nls?
  private static boolean array_expression_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expression_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // statement_list_rule?
  private static boolean array_expression_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expression_4")) return false;
    statement_list_rule(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean array_expression_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expression_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // array_type_name nls? dimension? SQBR_R
  public static boolean array_type_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_type_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_TYPE_ELEMENT, "<array type element>");
    result_ = array_type_name(builder_, level_ + 1);
    result_ = result_ && array_type_element_1(builder_, level_ + 1);
    result_ = result_ && array_type_element_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SQBR_R);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nls?
  private static boolean array_type_element_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_type_element_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // dimension?
  private static boolean array_type_element_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_type_element_2")) return false;
    dimension(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // reference_type_element SQBR_L
  static boolean array_type_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_type_name")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = reference_type_element(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SQBR_L);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // EQ | EQ_DASH | EQ_PLUS | EQ_STAR | EQ_DIV | EQ_PERS
  static boolean assignment_operator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assignment_operator")) return false;
    boolean result_;
    result_ = consumeToken(builder_, EQ);
    if (!result_) result_ = consumeToken(builder_, EQ_DASH);
    if (!result_) result_ = consumeToken(builder_, EQ_PLUS);
    if (!result_) result_ = consumeToken(builder_, EQ_STAR);
    if (!result_) result_ = consumeToken(builder_, EQ_DIV);
    if (!result_) result_ = consumeToken(builder_, EQ_PERS);
    return result_;
  }

  /* ********************************************************** */
  // SQBR_L nls? attribute_name LP nls? attribute_arguments? nls? RP nls? SQBR_R | type_literal_expression
  public static boolean attribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute")) return false;
    if (!nextTokenIs(builder_, SQBR_L)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = attribute_0(builder_, level_ + 1);
    if (!result_) result_ = type_literal_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, ATTRIBUTE, result_);
    return result_;
  }

  // SQBR_L nls? attribute_name LP nls? attribute_arguments? nls? RP nls? SQBR_R
  private static boolean attribute_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SQBR_L);
    result_ = result_ && attribute_0_1(builder_, level_ + 1);
    result_ = result_ && attribute_name(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LP);
    result_ = result_ && attribute_0_4(builder_, level_ + 1);
    result_ = result_ && attribute_0_5(builder_, level_ + 1);
    result_ = result_ && attribute_0_6(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    result_ = result_ && attribute_0_8(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SQBR_R);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean attribute_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean attribute_0_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_0_4")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // attribute_arguments?
  private static boolean attribute_0_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_0_5")) return false;
    attribute_arguments(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean attribute_0_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_0_6")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean attribute_0_8(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_0_8")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // simple_name_identifier '=' nls? expression | simple_name_identifier | expression
  public static boolean attribute_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_argument")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATTRIBUTE_ARGUMENT, "<attribute argument>");
    result_ = attribute_argument_0(builder_, level_ + 1);
    if (!result_) result_ = simple_name_identifier(builder_, level_ + 1);
    if (!result_) result_ = expression(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // simple_name_identifier '=' nls? expression
  private static boolean attribute_argument_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_argument_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = simple_name_identifier(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EQ);
    result_ = result_ && attribute_argument_0_2(builder_, level_ + 1);
    result_ = result_ && expression(builder_, level_ + 1, -1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean attribute_argument_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_argument_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parse_argument_list attribute_arguments_rule>>
  static boolean attribute_arguments(PsiBuilder builder_, int level_) {
    return parse_argument_list(builder_, level_ + 1, PowerShellParser::attribute_arguments_rule);
  }

  /* ********************************************************** */
  // attribute_argument (nls? COMMA nls? attribute_argument)*
  static boolean attribute_arguments_rule(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_arguments_rule")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = attribute_argument(builder_, level_ + 1);
    result_ = result_ && attribute_arguments_rule_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (nls? COMMA nls? attribute_argument)*
  private static boolean attribute_arguments_rule_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_arguments_rule_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!attribute_arguments_rule_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "attribute_arguments_rule_1", pos_)) break;
    }
    return true;
  }

  // nls? COMMA nls? attribute_argument
  private static boolean attribute_arguments_rule_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_arguments_rule_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = attribute_arguments_rule_1_0_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COMMA);
    result_ = result_ && attribute_arguments_rule_1_0_2(builder_, level_ + 1);
    result_ = result_ && attribute_argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean attribute_arguments_rule_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_arguments_rule_1_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean attribute_arguments_rule_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_arguments_rule_1_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // (attribute nls?)+ !(nws member_access_operator)
  static boolean attribute_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_list")) return false;
    if (!nextTokenIs(builder_, SQBR_L)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = attribute_list_0(builder_, level_ + 1);
    result_ = result_ && attribute_list_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (attribute nls?)+
  private static boolean attribute_list_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_list_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = attribute_list_0_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!attribute_list_0_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "attribute_list_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // attribute nls?
  private static boolean attribute_list_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_list_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = attribute(builder_, level_ + 1);
    result_ = result_ && attribute_list_0_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean attribute_list_0_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_list_0_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // !(nws member_access_operator)
  private static boolean attribute_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_list_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !attribute_list_1_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nws member_access_operator
  private static boolean attribute_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && member_access_operator(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // type_element
  static boolean attribute_name(PsiBuilder builder_, int level_) {
    return type_element(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // COLON nls? base_class_name
  static boolean base_class_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "base_class_clause")) return false;
    if (!nextTokenIs(builder_, COLON)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COLON);
    result_ = result_ && base_class_clause_1(builder_, level_ + 1);
    result_ = result_ && base_class_name(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean base_class_clause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "base_class_clause_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // type_element
  static boolean base_class_name(PsiBuilder builder_, int level_) {
    return type_element(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // COLON 'base' method_argument_expression_list
  static boolean base_constructor_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "base_constructor_call")) return false;
    if (!nextTokenIs(builder_, COLON)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COLON);
    result_ = result_ && consumeToken(builder_, "base");
    result_ = result_ && method_argument_expression_list(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // OP_C | OP_NOT | OP_BNOT | OP_BAND | OP_BOR | OP_BXOR | OP_AND | OP_OR | OP_XOR
  static boolean binary_operator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "binary_operator")) return false;
    boolean result_;
    result_ = consumeToken(builder_, OP_C);
    if (!result_) result_ = consumeToken(builder_, OP_NOT);
    if (!result_) result_ = consumeToken(builder_, OP_BNOT);
    if (!result_) result_ = consumeToken(builder_, OP_BAND);
    if (!result_) result_ = consumeToken(builder_, OP_BOR);
    if (!result_) result_ = consumeToken(builder_, OP_BXOR);
    if (!result_) result_ = consumeToken(builder_, OP_AND);
    if (!result_) result_ = consumeToken(builder_, OP_OR);
    if (!result_) result_ = consumeToken(builder_, OP_XOR);
    return result_;
  }

  /* ********************************************************** */
  // nls? block_body_content nls? | nls
  public static boolean block_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, BLOCK_BODY, "<block body>");
    result_ = block_body_0(builder_, level_ + 1);
    if (!result_) result_ = nls(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PowerShellParser::block_body_recover);
    return result_;
  }

  // nls? block_body_content nls?
  private static boolean block_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_body_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = block_body_0_0(builder_, level_ + 1);
    result_ = result_ && block_body_content(builder_, level_ + 1);
    result_ = result_ && block_body_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean block_body_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_body_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean block_body_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_body_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // (statement_list | statement_terminators)+
  static boolean block_body_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_body_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = block_body_content_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!block_body_content_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "block_body_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, PowerShellParser::statement_block_recover);
    return result_;
  }

  // statement_list | statement_terminators
  private static boolean block_body_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_body_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_list(builder_, level_ + 1);
    if (!result_) result_ = statement_terminators(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // !RCURLY
  static boolean block_body_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_body_recover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeTokenFast(builder_, RCURLY);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // reserved_statement_keywords | sep | nls | RCURLY
  static boolean block_body_stop_tokens(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_body_stop_tokens")) return false;
    boolean result_;
    result_ = reserved_statement_keywords(builder_, level_ + 1);
    if (!result_) result_ = sep(builder_, level_ + 1);
    if (!result_) result_ = nls(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, RCURLY);
    return result_;
  }

  /* ********************************************************** */
  // 'dynamicparam' | 'begin' | 'process' | 'end'
  static boolean block_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_name")) return false;
    boolean result_;
    result_ = consumeToken(builder_, DYNAMICPARAM);
    if (!result_) result_ = consumeToken(builder_, BEGIN);
    if (!result_) result_ = consumeToken(builder_, PROCESS);
    if (!result_) result_ = consumeToken(builder_, END);
    return result_;
  }

  /* ********************************************************** */
  // attribute_list? nls? 'param' nls? LP nls? script_parameter_list? nls? RP
  public static boolean block_parameter_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_parameter_clause")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_PARAMETER_CLAUSE, "<block parameter clause>");
    result_ = block_parameter_clause_0(builder_, level_ + 1);
    result_ = result_ && block_parameter_clause_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PARAM);
    pinned_ = result_; // pin = 3
    result_ = result_ && report_error_(builder_, block_parameter_clause_3(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, LP)) && result_;
    result_ = pinned_ && report_error_(builder_, block_parameter_clause_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, block_parameter_clause_6(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, block_parameter_clause_7(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, RP) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // attribute_list?
  private static boolean block_parameter_clause_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_parameter_clause_0")) return false;
    attribute_list(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean block_parameter_clause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_parameter_clause_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean block_parameter_clause_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_parameter_clause_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean block_parameter_clause_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_parameter_clause_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // script_parameter_list?
  private static boolean block_parameter_clause_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_parameter_clause_6")) return false;
    script_parameter_list(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean block_parameter_clause_7(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_parameter_clause_7")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // BRACED_VAR_START (nws declaration_scope)? variable_name_braced RCURLY
  static boolean braced_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "braced_variable")) return false;
    if (!nextTokenIs(builder_, BRACED_VAR_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BRACED_VAR_START);
    result_ = result_ && braced_variable_1(builder_, level_ + 1);
    result_ = result_ && variable_name_braced(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RCURLY);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (nws declaration_scope)?
  private static boolean braced_variable_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "braced_variable_1")) return false;
    braced_variable_1_0(builder_, level_ + 1);
    return true;
  }

  // nws declaration_scope
  private static boolean braced_variable_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "braced_variable_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && declaration_scope(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // ('break' | 'continue') label_reference_expression?
  static boolean break_continue_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "break_continue_statement")) return false;
    if (!nextTokenIs(builder_, "", BREAK, CONTINUE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = break_continue_statement_0(builder_, level_ + 1);
    result_ = result_ && break_continue_statement_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // 'break' | 'continue'
  private static boolean break_continue_statement_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "break_continue_statement_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, BREAK);
    if (!result_) result_ = consumeToken(builder_, CONTINUE);
    return result_;
  }

  // label_reference_expression?
  private static boolean break_continue_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "break_continue_statement_1")) return false;
    label_reference_expression(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // ncm nls? parenthesized_argument_list | primary_expression
  static boolean call_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = call_arguments_0(builder_, level_ + 1);
    if (!result_) result_ = primary_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ncm nls? parenthesized_argument_list
  private static boolean call_arguments_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = ncm(builder_, level_ + 1);
    result_ = result_ && call_arguments_0_1(builder_, level_ + 1);
    result_ = result_ && parenthesized_argument_list(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean call_arguments_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // type_literal_expression unary_expression
  public static boolean cast_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "cast_expression")) return false;
    if (!nextTokenIsFast(builder_, SQBR_L)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, CAST_EXPRESSION, null);
    result_ = type_literal_expression(builder_, level_ + 1);
    result_ = result_ && unary_expression(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // 'catch' nls? catch_type_list? nls? statement_block
  public static boolean catch_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_clause")) return false;
    if (!nextTokenIs(builder_, CATCH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CATCH_CLAUSE, null);
    result_ = consumeToken(builder_, CATCH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, catch_clause_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, catch_clause_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, catch_clause_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && statement_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean catch_clause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_clause_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // catch_type_list?
  private static boolean catch_clause_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_clause_2")) return false;
    catch_type_list(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean catch_clause_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_clause_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // catch_clause (nls? catch_clause)*
  static boolean catch_clauses(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_clauses")) return false;
    if (!nextTokenIs(builder_, CATCH)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = catch_clause(builder_, level_ + 1);
    result_ = result_ && catch_clauses_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (nls? catch_clause)*
  private static boolean catch_clauses_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_clauses_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!catch_clauses_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "catch_clauses_1", pos_)) break;
    }
    return true;
  }

  // nls? catch_clause
  private static boolean catch_clauses_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_clauses_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = catch_clauses_1_0_0(builder_, level_ + 1);
    result_ = result_ && catch_clause(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean catch_clauses_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_clauses_1_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // type_literal_expression nls? ( ',' nls? type_literal_expression nls? )*
  static boolean catch_type_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_type_list")) return false;
    if (!nextTokenIsFast(builder_, SQBR_L)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = type_literal_expression(builder_, level_ + 1);
    result_ = result_ && catch_type_list_1(builder_, level_ + 1);
    result_ = result_ && catch_type_list_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean catch_type_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_type_list_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // ( ',' nls? type_literal_expression nls? )*
  private static boolean catch_type_list_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_type_list_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!catch_type_list_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "catch_type_list_2", pos_)) break;
    }
    return true;
  }

  // ',' nls? type_literal_expression nls?
  private static boolean catch_type_list_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_type_list_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && catch_type_list_2_0_1(builder_, level_ + 1);
    result_ = result_ && type_literal_expression(builder_, level_ + 1);
    result_ = result_ && catch_type_list_2_0_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean catch_type_list_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_type_list_2_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean catch_type_list_2_0_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_type_list_2_0_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // LCURLY class_block_body? RCURLY
  static boolean class_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_block")) return false;
    if (!nextTokenIs(builder_, LCURLY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LCURLY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, class_block_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RCURLY) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // class_block_body?
  private static boolean class_block_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_block_1")) return false;
    class_block_body(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? (class_member_declaration_list | statement_terminators)+ nls? | nls
  public static boolean class_block_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_block_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_BODY, "<class block body>");
    result_ = class_block_body_0(builder_, level_ + 1);
    if (!result_) result_ = nls(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PowerShellParser::block_body_recover);
    return result_;
  }

  // nls? (class_member_declaration_list | statement_terminators)+ nls?
  private static boolean class_block_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_block_body_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = class_block_body_0_0(builder_, level_ + 1);
    result_ = result_ && class_block_body_0_1(builder_, level_ + 1);
    result_ = result_ && class_block_body_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean class_block_body_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_block_body_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // (class_member_declaration_list | statement_terminators)+
  private static boolean class_block_body_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_block_body_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = class_block_body_0_1_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!class_block_body_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "class_block_body_0_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // class_member_declaration_list | statement_terminators
  private static boolean class_block_body_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_block_body_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = class_member_declaration_list(builder_, level_ + 1);
    if (!result_) result_ = statement_terminators(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean class_block_body_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_block_body_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // attribute_list? 'class' class_name (nls? base_class_clause)? (nls? interface_list)? nls? class_block
  public static boolean class_declaration_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_declaration_statement")) return false;
    if (!nextTokenIs(builder_, "<class declaration statement>", CLASS, SQBR_L)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CLASS_DECLARATION_STATEMENT, "<class declaration statement>");
    result_ = class_declaration_statement_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, CLASS);
    result_ = result_ && class_name(builder_, level_ + 1);
    result_ = result_ && class_declaration_statement_3(builder_, level_ + 1);
    result_ = result_ && class_declaration_statement_4(builder_, level_ + 1);
    result_ = result_ && class_declaration_statement_5(builder_, level_ + 1);
    result_ = result_ && class_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // attribute_list?
  private static boolean class_declaration_statement_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_declaration_statement_0")) return false;
    attribute_list(builder_, level_ + 1);
    return true;
  }

  // (nls? base_class_clause)?
  private static boolean class_declaration_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_declaration_statement_3")) return false;
    class_declaration_statement_3_0(builder_, level_ + 1);
    return true;
  }

  // nls? base_class_clause
  private static boolean class_declaration_statement_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_declaration_statement_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = class_declaration_statement_3_0_0(builder_, level_ + 1);
    result_ = result_ && base_class_clause(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean class_declaration_statement_3_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_declaration_statement_3_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // (nls? interface_list)?
  private static boolean class_declaration_statement_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_declaration_statement_4")) return false;
    class_declaration_statement_4_0(builder_, level_ + 1);
    return true;
  }

  // nls? interface_list
  private static boolean class_declaration_statement_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_declaration_statement_4_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = class_declaration_statement_4_0_0(builder_, level_ + 1);
    result_ = result_ && interface_list(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean class_declaration_statement_4_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_declaration_statement_4_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean class_declaration_statement_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_declaration_statement_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // property_declaration_statement | constructor_declaration_statement | method_declaration_statement | incomplete_declaration
  static boolean class_member_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_member_declaration")) return false;
    boolean result_;
    result_ = property_declaration_statement(builder_, level_ + 1);
    if (!result_) result_ = constructor_declaration_statement(builder_, level_ + 1);
    if (!result_) result_ = method_declaration_statement(builder_, level_ + 1);
    if (!result_) result_ = incomplete_declaration(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // class_member_declaration (statement_terminators class_member_declaration)*
  static boolean class_member_declaration_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_member_declaration_list")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = class_member_declaration(builder_, level_ + 1);
    result_ = result_ && class_member_declaration_list_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PowerShellParser::top_level_recover);
    return result_;
  }

  // (statement_terminators class_member_declaration)*
  private static boolean class_member_declaration_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_member_declaration_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!class_member_declaration_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "class_member_declaration_list_1", pos_)) break;
    }
    return true;
  }

  // statement_terminators class_member_declaration
  private static boolean class_member_declaration_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_member_declaration_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_terminators(builder_, level_ + 1);
    result_ = result_ && class_member_declaration(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // simple_name_identifier
  static boolean class_name(PsiBuilder builder_, int level_) {
    return simple_name_identifier(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // parse_command_argument
  public static boolean command_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_argument")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMMAND_ARGUMENT, "<command argument>");
    result_ = parse_command_argument(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // command_argument ( (COMMA | EQ) command_argument)* | array_literal_expression
  static boolean command_argument_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_argument_list")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_argument_list_0(builder_, level_ + 1);
    if (!result_) result_ = expression(builder_, level_ + 1, 7);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // command_argument ( (COMMA | EQ) command_argument)*
  private static boolean command_argument_list_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_argument_list_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_argument(builder_, level_ + 1);
    result_ = result_ && command_argument_list_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ( (COMMA | EQ) command_argument)*
  private static boolean command_argument_list_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_argument_list_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!command_argument_list_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "command_argument_list_0_1", pos_)) break;
    }
    return true;
  }

  // (COMMA | EQ) command_argument
  private static boolean command_argument_list_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_argument_list_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_argument_list_0_1_0_0(builder_, level_ + 1);
    result_ = result_ && command_argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // COMMA | EQ
  private static boolean command_argument_list_0_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_argument_list_0_1_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, EQ);
    return result_;
  }

  /* ********************************************************** */
  // command_invocation_operator ( (command_module command_name_expr command_element* ) | command_name_expr command_element* ) | command_name_expression command_element*
  public static boolean command_call_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_call_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, COMMAND_CALL_EXPRESSION, "<command call expression>");
    result_ = command_call_expression_0(builder_, level_ + 1);
    if (!result_) result_ = command_call_expression_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // command_invocation_operator ( (command_module command_name_expr command_element* ) | command_name_expr command_element* )
  private static boolean command_call_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_call_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_invocation_operator(builder_, level_ + 1);
    result_ = result_ && command_call_expression_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (command_module command_name_expr command_element* ) | command_name_expr command_element*
  private static boolean command_call_expression_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_call_expression_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_call_expression_0_1_0(builder_, level_ + 1);
    if (!result_) result_ = command_call_expression_0_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // command_module command_name_expr command_element*
  private static boolean command_call_expression_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_call_expression_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_module(builder_, level_ + 1);
    result_ = result_ && command_name_expr(builder_, level_ + 1);
    result_ = result_ && command_call_expression_0_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // command_element*
  private static boolean command_call_expression_0_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_call_expression_0_1_0_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!command_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "command_call_expression_0_1_0_2", pos_)) break;
    }
    return true;
  }

  // command_name_expr command_element*
  private static boolean command_call_expression_0_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_call_expression_0_1_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_name_expr(builder_, level_ + 1);
    result_ = result_ && command_call_expression_0_1_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // command_element*
  private static boolean command_call_expression_0_1_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_call_expression_0_1_1_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!command_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "command_call_expression_0_1_1_1", pos_)) break;
    }
    return true;
  }

  // command_name_expression command_element*
  private static boolean command_call_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_call_expression_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_name_expression(builder_, level_ + 1);
    result_ = result_ && command_call_expression_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // command_element*
  private static boolean command_call_expression_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_call_expression_1_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!command_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "command_call_expression_1_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // command_parameter | redirection | command_argument_list
  static boolean command_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_element")) return false;
    boolean result_;
    result_ = command_parameter(builder_, level_ + 1);
    if (!result_) result_ = redirection(builder_, level_ + 1);
    if (!result_) result_ = command_argument_list(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '&' | DOT
  static boolean command_invocation_operator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_invocation_operator")) return false;
    if (!nextTokenIs(builder_, "", AMP, DOT)) return false;
    boolean result_;
    result_ = consumeToken(builder_, AMP);
    if (!result_) result_ = consumeToken(builder_, DOT);
    return result_;
  }

  /* ********************************************************** */
  // primary_expression
  static boolean command_module(PsiBuilder builder_, int level_) {
    return primary_expression(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // command_name_identifier
  public static boolean command_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_name")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMMAND_NAME, "<command name>");
    result_ = command_name_identifier(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // generic_token_chars | allowed_identifier_keywords
  public static boolean command_name_char_tokens(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_name_char_tokens")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, REFERENCE_IDENTIFIER, "<command name char tokens>");
    result_ = generic_token_chars(builder_, level_ + 1);
    if (!result_) result_ = allowed_identifier_keywords(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // primary_expression | command_name_expression
  static boolean command_name_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_name_expr")) return false;
    boolean result_;
    result_ = primary_expression(builder_, level_ + 1);
    if (!result_) result_ = command_name_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // path_expression | command_name
  static boolean command_name_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_name_expression")) return false;
    boolean result_;
    result_ = path_expression(builder_, level_ + 1);
    if (!result_) result_ = command_name(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // generic_token_with_sub_expr | generic_token_part
  public static boolean command_name_identifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_name_identifier")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, IDENTIFIER, "<command name identifier>");
    result_ = generic_token_with_sub_expr(builder_, level_ + 1);
    if (!result_) result_ = generic_token_part(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // CMD_PARAMETER | binary_operator
  public static boolean command_parameter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "command_parameter")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMMAND_PARAMETER, "<command parameter>");
    result_ = consumeToken(builder_, CMD_PARAMETER);
    if (!result_) result_ = binary_operator(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !is_id requires_comment | !is_id SINGLE_LINE_COMMENT | DELIMITED_COMMENT
  public static boolean comment(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comment")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMMENT, "<comment>");
    result_ = comment_0(builder_, level_ + 1);
    if (!result_) result_ = comment_1(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, DELIMITED_COMMENT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // !is_id requires_comment
  private static boolean comment_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comment_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = comment_0_0(builder_, level_ + 1);
    result_ = result_ && requires_comment(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !is_id
  private static boolean comment_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comment_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !is_id(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // !is_id SINGLE_LINE_COMMENT
  private static boolean comment_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comment_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = comment_1_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SINGLE_LINE_COMMENT);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !is_id
  private static boolean comment_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comment_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !is_id(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // OP_C
  static boolean comparison_operator(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, OP_C);
  }

  /* ********************************************************** */
  // DOT (SIMPLE_ID | allowed_identifier_keywords)
  public static boolean compound_type_identifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compound_type_identifier")) return false;
    if (!nextTokenIs(builder_, DOT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, REFERENCE_TYPE_ELEMENT, null);
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && compound_type_identifier_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // SIMPLE_ID | allowed_identifier_keywords
  private static boolean compound_type_identifier_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compound_type_identifier_1")) return false;
    boolean result_;
    result_ = consumeToken(builder_, SIMPLE_ID);
    if (!result_) result_ = allowed_identifier_keywords(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // attribute_list? 'configuration' nls? configuration_name nls? configuration_block_block
  public static boolean configuration_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONFIGURATION_BLOCK, "<configuration block>");
    result_ = configuration_block_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, CONFIGURATION);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, configuration_block_2(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, configuration_name(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, configuration_block_4(builder_, level_ + 1)) && result_;
    result_ = pinned_ && configuration_block_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, PowerShellParser::configuration_block_recover);
    return result_ || pinned_;
  }

  // attribute_list?
  private static boolean configuration_block_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_0")) return false;
    attribute_list(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean configuration_block_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean configuration_block_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_4")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // LCURLY configuration_block_body? RCURLY
  static boolean configuration_block_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_block")) return false;
    if (!nextTokenIs(builder_, LCURLY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LCURLY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, configuration_block_block_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RCURLY) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // configuration_block_body?
  private static boolean configuration_block_block_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_block_1")) return false;
    configuration_block_body(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // top_script_block
  public static boolean configuration_block_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, BLOCK_BODY, "<configuration block body>");
    result_ = top_script_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PowerShellParser::configuration_block_body_recover);
    return result_;
  }

  /* ********************************************************** */
  // !(nls? RCURLY statement_terminators? ('configuration' | <<eof>> | SQBR_L | statement_stop_tokens))
  static boolean configuration_block_body_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_body_recover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !configuration_block_body_recover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nls? RCURLY statement_terminators? ('configuration' | <<eof>> | SQBR_L | statement_stop_tokens)
  private static boolean configuration_block_body_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_body_recover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = configuration_block_body_recover_0_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RCURLY);
    result_ = result_ && configuration_block_body_recover_0_2(builder_, level_ + 1);
    result_ = result_ && configuration_block_body_recover_0_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean configuration_block_body_recover_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_body_recover_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // statement_terminators?
  private static boolean configuration_block_body_recover_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_body_recover_0_2")) return false;
    statement_terminators(builder_, level_ + 1);
    return true;
  }

  // 'configuration' | <<eof>> | SQBR_L | statement_stop_tokens
  private static boolean configuration_block_body_recover_0_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_body_recover_0_3")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, CONFIGURATION);
    if (!result_) result_ = eof(builder_, level_ + 1);
    if (!result_) result_ = consumeTokenFast(builder_, SQBR_L);
    if (!result_) result_ = statement_stop_tokens(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // configuration_block (statement_terminators configuration_block)* statement_terminators?
  static boolean configuration_block_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_list")) return false;
    if (!nextTokenIs(builder_, "", CONFIGURATION, SQBR_L)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = configuration_block(builder_, level_ + 1);
    result_ = result_ && configuration_block_list_1(builder_, level_ + 1);
    result_ = result_ && configuration_block_list_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (statement_terminators configuration_block)*
  private static boolean configuration_block_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!configuration_block_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "configuration_block_list_1", pos_)) break;
    }
    return true;
  }

  // statement_terminators configuration_block
  private static boolean configuration_block_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_terminators(builder_, level_ + 1);
    result_ = result_ && configuration_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // statement_terminators?
  private static boolean configuration_block_list_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_list_2")) return false;
    statement_terminators(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // !(statement_terminators? ('configuration' | <<eof>> | SQBR_L| statement_stop_tokens))
  static boolean configuration_block_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_recover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !configuration_block_recover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // statement_terminators? ('configuration' | <<eof>> | SQBR_L| statement_stop_tokens)
  private static boolean configuration_block_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_recover_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = configuration_block_recover_0_0(builder_, level_ + 1);
    result_ = result_ && configuration_block_recover_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // statement_terminators?
  private static boolean configuration_block_recover_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_recover_0_0")) return false;
    statement_terminators(builder_, level_ + 1);
    return true;
  }

  // 'configuration' | <<eof>> | SQBR_L| statement_stop_tokens
  private static boolean configuration_block_recover_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "configuration_block_recover_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, CONFIGURATION);
    if (!result_) result_ = eof(builder_, level_ + 1);
    if (!result_) result_ = consumeTokenFast(builder_, SQBR_L);
    if (!result_) result_ = statement_stop_tokens(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // function_name
  static boolean configuration_name(PsiBuilder builder_, int level_) {
    return function_name(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // class_name method_argument_expression_list nls? base_constructor_call? nls? method_block
  public static boolean constructor_declaration_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constructor_declaration_statement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONSTRUCTOR_DECLARATION_STATEMENT, "<constructor declaration statement>");
    result_ = class_name(builder_, level_ + 1);
    result_ = result_ && method_argument_expression_list(builder_, level_ + 1);
    result_ = result_ && constructor_declaration_statement_2(builder_, level_ + 1);
    result_ = result_ && constructor_declaration_statement_3(builder_, level_ + 1);
    result_ = result_ && constructor_declaration_statement_4(builder_, level_ + 1);
    result_ = result_ && method_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nls?
  private static boolean constructor_declaration_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constructor_declaration_statement_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // base_constructor_call?
  private static boolean constructor_declaration_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constructor_declaration_statement_3")) return false;
    base_constructor_call(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean constructor_declaration_statement_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "constructor_declaration_statement_4")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // command_name_expr
  static boolean data_command(PsiBuilder builder_, int level_) {
    return command_name_expr(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // '-supportedcommand' nls? data_commands_list
  static boolean data_commands_allowed(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_commands_allowed")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, "-supportedcommand");
    result_ = result_ && data_commands_allowed_1(builder_, level_ + 1);
    result_ = result_ && data_commands_list(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean data_commands_allowed_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_commands_allowed_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // data_command ( nls? ',' nls? data_command )*
  static boolean data_commands_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_commands_list")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = data_command(builder_, level_ + 1);
    result_ = result_ && data_commands_list_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ( nls? ',' nls? data_command )*
  private static boolean data_commands_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_commands_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!data_commands_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "data_commands_list_1", pos_)) break;
    }
    return true;
  }

  // nls? ',' nls? data_command
  private static boolean data_commands_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_commands_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = data_commands_list_1_0_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COMMA);
    result_ = result_ && data_commands_list_1_0_2(builder_, level_ + 1);
    result_ = result_ && data_command(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean data_commands_list_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_commands_list_1_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean data_commands_list_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_commands_list_1_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // simple_name_identifier
  static boolean data_name(PsiBuilder builder_, int level_) {
    return simple_name_identifier(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // 'data' nls? data_name? nls? data_commands_allowed? nls? statement_block
  public static boolean data_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_statement")) return false;
    if (!nextTokenIs(builder_, DATA)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DATA_STATEMENT, null);
    result_ = consumeToken(builder_, DATA);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, data_statement_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, data_statement_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, data_statement_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, data_statement_4(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, data_statement_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && statement_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean data_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // data_name?
  private static boolean data_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_statement_2")) return false;
    data_name(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean data_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_statement_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // data_commands_allowed?
  private static boolean data_statement_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_statement_4")) return false;
    data_commands_allowed(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean data_statement_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "data_statement_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // DEC_INTEGER
  static boolean decimal_integer_literal(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, DEC_INTEGER);
  }

  /* ********************************************************** */
  // 'hidden' | 'static'
  static boolean declaration_modifier_attribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declaration_modifier_attribute")) return false;
    if (!nextTokenIs(builder_, "", HIDDEN, STATIC)) return false;
    boolean result_;
    result_ = consumeToken(builder_, HIDDEN);
    if (!result_) result_ = consumeToken(builder_, STATIC);
    return result_;
  }

  /* ********************************************************** */
  // 'global:' | 'local:' | 'private:' | 'script:' | 'using:' | 'workflow:' | variable_namespace
  static boolean declaration_scope(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "declaration_scope")) return false;
    boolean result_;
    result_ = consumeToken(builder_, "global:");
    if (!result_) result_ = consumeToken(builder_, "local:");
    if (!result_) result_ = consumeToken(builder_, "private:");
    if (!result_) result_ = consumeToken(builder_, "script:");
    if (!result_) result_ = consumeToken(builder_, "using:");
    if (!result_) result_ = consumeToken(builder_, "workflow:");
    if (!result_) result_ = variable_namespace(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // COMMA+
  static boolean dimension(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dimension")) return false;
    if (!nextTokenIs(builder_, COMMA)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, COMMA)) break;
      if (!empty_element_parsed_guard_(builder_, "dimension", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'do' nls? statement_block nls? ( 'while' | 'until' ) nls? LP nls? while_condition nls? RP
  public static boolean do_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "do_statement")) return false;
    if (!nextTokenIs(builder_, DO)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DO_STATEMENT, null);
    result_ = consumeToken(builder_, DO);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, do_statement_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, statement_block(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, do_statement_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, do_statement_4(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, do_statement_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, LP)) && result_;
    result_ = pinned_ && report_error_(builder_, do_statement_7(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, while_condition(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, do_statement_9(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, RP) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean do_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "do_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean do_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "do_statement_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // 'while' | 'until'
  private static boolean do_statement_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "do_statement_4")) return false;
    boolean result_;
    result_ = consumeToken(builder_, WHILE);
    if (!result_) result_ = consumeToken(builder_, UNTIL);
    return result_;
  }

  // nls?
  private static boolean do_statement_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "do_statement_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean do_statement_7(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "do_statement_7")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean do_statement_9(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "do_statement_9")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // DQ_CLOSE
  static boolean dq_close(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, DQ_CLOSE);
  }

  /* ********************************************************** */
  // DQ_OPEN
  static boolean dq_open(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, DQ_OPEN);
  }

  /* ********************************************************** */
  // nws SQBR_L nls? expression nls? SQBR_R
  public static boolean element_access_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "element_access_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, ELEMENT_ACCESS_EXPRESSION, "<element access expression>");
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SQBR_L);
    result_ = result_ && element_access_expression_2(builder_, level_ + 1);
    result_ = result_ && expression(builder_, level_ + 1, -1);
    result_ = result_ && element_access_expression_4(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SQBR_R);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nls?
  private static boolean element_access_expression_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "element_access_expression_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean element_access_expression_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "element_access_expression_4")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'else' nls? statement_block
  public static boolean else_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "else_clause")) return false;
    if (!nextTokenIs(builder_, ELSE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ELSE_CLAUSE, null);
    result_ = consumeToken(builder_, ELSE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, else_clause_1(builder_, level_ + 1));
    result_ = pinned_ && statement_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean else_clause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "else_clause_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'elseif' nls? LP nls? pipeline nls? RP nls? statement_block
  public static boolean elseif_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "elseif_clause")) return false;
    if (!nextTokenIs(builder_, ELSEIF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ELSEIF_CLAUSE, null);
    result_ = consumeToken(builder_, ELSEIF);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, elseif_clause_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, LP)) && result_;
    result_ = pinned_ && report_error_(builder_, elseif_clause_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, pipeline(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, elseif_clause_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RP)) && result_;
    result_ = pinned_ && report_error_(builder_, elseif_clause_7(builder_, level_ + 1)) && result_;
    result_ = pinned_ && statement_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean elseif_clause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "elseif_clause_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean elseif_clause_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "elseif_clause_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean elseif_clause_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "elseif_clause_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean elseif_clause_7(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "elseif_clause_7")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // LCURLY enum_block_body? RCURLY
  static boolean enum_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_block")) return false;
    if (!nextTokenIs(builder_, LCURLY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LCURLY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, enum_block_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RCURLY) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // enum_block_body?
  private static boolean enum_block_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_block_1")) return false;
    enum_block_body(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? (label_list | statement_terminators)+ nls? | nls
  public static boolean enum_block_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_block_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_BODY, "<enum block body>");
    result_ = enum_block_body_0(builder_, level_ + 1);
    if (!result_) result_ = nls(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PowerShellParser::block_body_recover);
    return result_;
  }

  // nls? (label_list | statement_terminators)+ nls?
  private static boolean enum_block_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_block_body_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = enum_block_body_0_0(builder_, level_ + 1);
    result_ = result_ && enum_block_body_0_1(builder_, level_ + 1);
    result_ = result_ && enum_block_body_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean enum_block_body_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_block_body_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // (label_list | statement_terminators)+
  private static boolean enum_block_body_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_block_body_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = enum_block_body_0_1_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!enum_block_body_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "enum_block_body_0_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // label_list | statement_terminators
  private static boolean enum_block_body_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_block_body_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = label_list(builder_, level_ + 1);
    if (!result_) result_ = statement_terminators(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean enum_block_body_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_block_body_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'enum' enum_name nls? enum_block
  static boolean enum_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_declaration")) return false;
    if (!nextTokenIs(builder_, ENUM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ENUM);
    result_ = result_ && enum_name(builder_, level_ + 1);
    result_ = result_ && enum_declaration_2(builder_, level_ + 1);
    result_ = result_ && enum_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean enum_declaration_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_declaration_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // enum_declaration | enum_with_flags_declaration
  public static boolean enum_declaration_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_declaration_statement")) return false;
    if (!nextTokenIs(builder_, "<enum declaration statement>", ENUM, SQBR_L)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ENUM_DECLARATION_STATEMENT, "<enum declaration statement>");
    result_ = enum_declaration(builder_, level_ + 1);
    if (!result_) result_ = enum_with_flags_declaration(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // label_identifier_expression (EQ expression)?
  public static boolean enum_label_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_label_declaration")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ENUM_LABEL_DECLARATION, "<enum label declaration>");
    result_ = label_identifier_expression(builder_, level_ + 1);
    result_ = result_ && enum_label_declaration_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (EQ expression)?
  private static boolean enum_label_declaration_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_label_declaration_1")) return false;
    enum_label_declaration_1_0(builder_, level_ + 1);
    return true;
  }

  // EQ expression
  private static boolean enum_label_declaration_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_label_declaration_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, EQ);
    result_ = result_ && expression(builder_, level_ + 1, -1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // simple_name_identifier
  static boolean enum_name(PsiBuilder builder_, int level_) {
    return simple_name_identifier(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // attribute /*SQBR_L SIMPLE_ID LP RP SQBR_R */nls? enum_declaration
  static boolean enum_with_flags_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_with_flags_declaration")) return false;
    if (!nextTokenIs(builder_, SQBR_L)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = attribute(builder_, level_ + 1);
    result_ = result_ && enum_with_flags_declaration_1(builder_, level_ + 1);
    result_ = result_ && enum_declaration(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean enum_with_flags_declaration_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "enum_with_flags_declaration_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // ( expandable_string_content | EXPANDABLE_HERE_STRING_PART )+
  static boolean expandable_here_string_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expandable_here_string_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = expandable_here_string_content_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!expandable_here_string_content_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "expandable_here_string_content", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expandable_string_content | EXPANDABLE_HERE_STRING_PART
  private static boolean expandable_here_string_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expandable_here_string_content_0")) return false;
    boolean result_;
    result_ = expandable_string_content(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, EXPANDABLE_HERE_STRING_PART);
    return result_;
  }

  /* ********************************************************** */
  // EXPANDABLE_HERE_STRING_START expandable_here_string_content? EXPANDABLE_HERE_STRING_END
  static boolean expandable_here_string_literal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expandable_here_string_literal")) return false;
    if (!nextTokenIs(builder_, EXPANDABLE_HERE_STRING_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, EXPANDABLE_HERE_STRING_START);
    result_ = result_ && expandable_here_string_literal_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EXPANDABLE_HERE_STRING_END);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expandable_here_string_content?
  private static boolean expandable_here_string_literal_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expandable_here_string_literal_1")) return false;
    expandable_here_string_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // ( string_literal_entry | target_variable_expression | sub_expression )+
  static boolean expandable_string_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expandable_string_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = expandable_string_content_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!expandable_string_content_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "expandable_string_content", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // string_literal_entry | target_variable_expression | sub_expression
  private static boolean expandable_string_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expandable_string_content_0")) return false;
    boolean result_;
    result_ = string_literal_entry(builder_, level_ + 1);
    if (!result_) result_ = target_variable_expression(builder_, level_ + 1);
    if (!result_) result_ = sub_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // dq_open expandable_string_content? dq_close
  static boolean expandable_string_literal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expandable_string_literal")) return false;
    if (!nextTokenIs(builder_, DQ_OPEN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = dq_open(builder_, level_ + 1);
    result_ = result_ && expandable_string_literal_1(builder_, level_ + 1);
    result_ = result_ && dq_close(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expandable_string_content?
  private static boolean expandable_string_literal_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expandable_string_literal_1")) return false;
    expandable_string_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // expression ( nls? COMMA nls? expression)*
  static boolean expression_list_rule(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_list_rule")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = expression(builder_, level_ + 1, -1);
    result_ = result_ && expression_list_rule_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ( nls? COMMA nls? expression)*
  private static boolean expression_list_rule_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_list_rule_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!expression_list_rule_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "expression_list_rule_1", pos_)) break;
    }
    return true;
  }

  // nls? COMMA nls? expression
  private static boolean expression_list_rule_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_list_rule_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = expression_list_rule_1_0_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COMMA);
    result_ = result_ && expression_list_rule_1_0_2(builder_, level_ + 1);
    result_ = result_ && expression(builder_, level_ + 1, -1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean expression_list_rule_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_list_rule_1_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean expression_list_rule_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_list_rule_1_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // prefix_op nls? unary_expression | cast_expression
  static boolean expression_with_unary_operator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_with_unary_operator")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = expression_with_unary_operator_0(builder_, level_ + 1);
    if (!result_) result_ = cast_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // prefix_op nls? unary_expression
  private static boolean expression_with_unary_operator_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_with_unary_operator_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = prefix_op(builder_, level_ + 1);
    result_ = result_ && expression_with_unary_operator_0_1(builder_, level_ + 1);
    result_ = result_ && unary_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean expression_with_unary_operator_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "expression_with_unary_operator_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // OP_FR
  static boolean file_redirection_operator(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, OP_FR);
  }

  /* ********************************************************** */
  // 'finally' nls? statement_block
  public static boolean finally_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "finally_clause")) return false;
    if (!nextTokenIs(builder_, FINALLY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FINALLY_CLAUSE, null);
    result_ = consumeToken(builder_, FINALLY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, finally_clause_1(builder_, level_ + 1));
    result_ = pinned_ && statement_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean finally_clause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "finally_clause_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // break_continue_statement | throw_return_exit_statement
  public static boolean flow_control_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "flow_control_statement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FLOW_CONTROL_STATEMENT, "<flow control statement>");
    result_ = break_continue_statement(builder_, level_ + 1);
    if (!result_) result_ = throw_return_exit_statement(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // LP nls? (for_initializer sep | sep?)
  //                   nls? (for_condition sep | sep?)
  //                   nls? for_iterator? nls? RP
  public static boolean for_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause")) return false;
    if (!nextTokenIs(builder_, LP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LP);
    result_ = result_ && for_clause_1(builder_, level_ + 1);
    result_ = result_ && for_clause_2(builder_, level_ + 1);
    result_ = result_ && for_clause_3(builder_, level_ + 1);
    result_ = result_ && for_clause_4(builder_, level_ + 1);
    result_ = result_ && for_clause_5(builder_, level_ + 1);
    result_ = result_ && for_clause_6(builder_, level_ + 1);
    result_ = result_ && for_clause_7(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    exit_section_(builder_, marker_, FOR_CLAUSE, result_);
    return result_;
  }

  // nls?
  private static boolean for_clause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // for_initializer sep | sep?
  private static boolean for_clause_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = for_clause_2_0(builder_, level_ + 1);
    if (!result_) result_ = for_clause_2_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // for_initializer sep
  private static boolean for_clause_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = for_initializer(builder_, level_ + 1);
    result_ = result_ && sep(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // sep?
  private static boolean for_clause_2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_2_1")) return false;
    sep(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean for_clause_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // for_condition sep | sep?
  private static boolean for_clause_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_4")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = for_clause_4_0(builder_, level_ + 1);
    if (!result_) result_ = for_clause_4_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // for_condition sep
  private static boolean for_clause_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_4_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = for_condition(builder_, level_ + 1);
    result_ = result_ && sep(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // sep?
  private static boolean for_clause_4_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_4_1")) return false;
    sep(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean for_clause_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // for_iterator?
  private static boolean for_clause_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_6")) return false;
    for_iterator(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean for_clause_7(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_clause_7")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // pipeline
  static boolean for_condition(PsiBuilder builder_, int level_) {
    return pipeline(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // pipeline
  static boolean for_initializer(PsiBuilder builder_, int level_) {
    return pipeline(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // pipeline
  static boolean for_iterator(PsiBuilder builder_, int level_) {
    return pipeline(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // 'for' nls? for_clause nls? statement_block
  public static boolean for_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_statement")) return false;
    if (!nextTokenIs(builder_, FOR)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOR_STATEMENT, null);
    result_ = consumeToken(builder_, FOR);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, for_statement_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, for_clause(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, for_statement_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && statement_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean for_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean for_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_statement_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // LP nls? target_variable_expression nls? 'in' nls? pipeline nls? RP
  static boolean foreach_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_clause")) return false;
    if (!nextTokenIs(builder_, LP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LP);
    result_ = result_ && foreach_clause_1(builder_, level_ + 1);
    result_ = result_ && target_variable_expression(builder_, level_ + 1);
    result_ = result_ && foreach_clause_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, IN);
    result_ = result_ && foreach_clause_5(builder_, level_ + 1);
    result_ = result_ && pipeline(builder_, level_ + 1);
    result_ = result_ && foreach_clause_7(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean foreach_clause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_clause_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean foreach_clause_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_clause_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean foreach_clause_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_clause_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean foreach_clause_7(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_clause_7")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '-parallel' throttlelimit_parameter | throttlelimit_parameter '-parallel' | '-parallel' | throttlelimit_parameter
  static boolean foreach_parameters(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_parameters")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = foreach_parameters_0(builder_, level_ + 1);
    if (!result_) result_ = foreach_parameters_1(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, "-parallel");
    if (!result_) result_ = throttlelimit_parameter(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // '-parallel' throttlelimit_parameter
  private static boolean foreach_parameters_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_parameters_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, "-parallel");
    result_ = result_ && throttlelimit_parameter(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // throttlelimit_parameter '-parallel'
  private static boolean foreach_parameters_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_parameters_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = throttlelimit_parameter(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, "-parallel");
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'foreach' nls? foreach_parameters? nls? foreach_clause nls?
  //                                 nls? statement_block
  public static boolean foreach_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement")) return false;
    if (!nextTokenIs(builder_, FOREACH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOREACH_STATEMENT, null);
    result_ = consumeToken(builder_, FOREACH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, foreach_statement_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, foreach_statement_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, foreach_statement_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, foreach_clause(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, foreach_statement_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, foreach_statement_6(builder_, level_ + 1)) && result_;
    result_ = pinned_ && statement_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean foreach_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // foreach_parameters?
  private static boolean foreach_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement_2")) return false;
    foreach_parameters(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean foreach_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean foreach_statement_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean foreach_statement_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_statement_6")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '-f'
  static boolean format_operator(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, "-f");
  }

  /* ********************************************************** */
  // LCURLY function_block_body? RCURLY
  static boolean function_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_block")) return false;
    if (!nextTokenIs(builder_, LCURLY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LCURLY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, function_block_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RCURLY) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // function_block_body?
  private static boolean function_block_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_block_1")) return false;
    function_block_body(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? attribute_list nls? top_script_block | top_script_block
  public static boolean function_block_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_block_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, BLOCK_BODY, "<function block body>");
    result_ = function_block_body_0(builder_, level_ + 1);
    if (!result_) result_ = top_script_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PowerShellParser::block_body_recover);
    return result_;
  }

  // nls? attribute_list nls? top_script_block
  private static boolean function_block_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_block_body_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = function_block_body_0_0(builder_, level_ + 1);
    result_ = result_ && attribute_list(builder_, level_ + 1);
    result_ = result_ && function_block_body_0_2(builder_, level_ + 1);
    result_ = result_ && top_script_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean function_block_body_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_block_body_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean function_block_body_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_block_body_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // primary_expression | identifier
  static boolean function_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_name")) return false;
    boolean result_;
    result_ = primary_expression(builder_, level_ + 1);
    if (!result_) result_ = identifier(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // parameter_clause
  static boolean function_parameter_declaration(PsiBuilder builder_, int level_) {
    return parameter_clause(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // declaration_scope nws
  static boolean function_scope(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_scope")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = declaration_scope(builder_, level_ + 1);
    result_ = result_ && nws(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // ('function' | 'filter') nls? function_statement_tail
  public static boolean function_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement")) return false;
    if (!nextTokenIs(builder_, "<function statement>", FILTER, FUNCTION)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, FUNCTION_STATEMENT, "<function statement>");
    result_ = function_statement_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, function_statement_1(builder_, level_ + 1));
    result_ = pinned_ && function_statement_tail(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'function' | 'filter'
  private static boolean function_statement_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, FUNCTION);
    if (!result_) result_ = consumeToken(builder_, FILTER);
    return result_;
  }

  // nls?
  private static boolean function_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // function_scope? function_name nls? function_parameter_declaration? nls? function_block
  static boolean function_statement_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement_tail")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = function_statement_tail_0(builder_, level_ + 1);
    result_ = result_ && function_name(builder_, level_ + 1);
    result_ = result_ && function_statement_tail_2(builder_, level_ + 1);
    result_ = result_ && function_statement_tail_3(builder_, level_ + 1);
    result_ = result_ && function_statement_tail_4(builder_, level_ + 1);
    result_ = result_ && function_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // function_scope?
  private static boolean function_statement_tail_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement_tail_0")) return false;
    function_scope(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean function_statement_tail_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement_tail_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // function_parameter_declaration?
  private static boolean function_statement_tail_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement_tail_3")) return false;
    function_parameter_declaration(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean function_statement_tail_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "function_statement_tail_4")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // path_expression | primary_expression | command_name_char_tokens
  static boolean generic_chars_or_path_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_chars_or_path_expression")) return false;
    boolean result_;
    result_ = path_expression(builder_, level_ + 1);
    if (!result_) result_ = primary_expression(builder_, level_ + 1);
    if (!result_) result_ = command_name_char_tokens(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // generic_id_part_tokens_start | DOT | PATH_SEP | DIV | CMD_PARAMETER | PLUS | DASH | STAR | PERS
  static boolean generic_id_part_tokens(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_id_part_tokens")) return false;
    boolean result_;
    result_ = generic_id_part_tokens_start(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, PATH_SEP);
    if (!result_) result_ = consumeToken(builder_, DIV);
    if (!result_) result_ = consumeToken(builder_, CMD_PARAMETER);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, DASH);
    if (!result_) result_ = consumeToken(builder_, STAR);
    if (!result_) result_ = consumeToken(builder_, PERS);
    return result_;
  }

  /* ********************************************************** */
  // SIMPLE_ID | GENERIC_ID_PART | STAR | PERS | allowed_identifier_keywords | DEC_INTEGER | REAL_NUM
  static boolean generic_id_part_tokens_start(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_id_part_tokens_start")) return false;
    boolean result_;
    result_ = consumeToken(builder_, SIMPLE_ID);
    if (!result_) result_ = consumeToken(builder_, GENERIC_ID_PART);
    if (!result_) result_ = consumeToken(builder_, STAR);
    if (!result_) result_ = consumeToken(builder_, PERS);
    if (!result_) result_ = allowed_identifier_keywords(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, DEC_INTEGER);
    if (!result_) result_ = consumeToken(builder_, REAL_NUM);
    return result_;
  }

  /* ********************************************************** */
  // generic_token_with_sub_expr | generic_token_part | allowed_identifier_keywords
  static boolean generic_identifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_identifier")) return false;
    boolean result_;
    result_ = generic_token_with_sub_expr(builder_, level_ + 1);
    if (!result_) result_ = generic_token_part(builder_, level_ + 1);
    if (!result_) result_ = allowed_identifier_keywords(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // generic_id_part_tokens_start generic_token_chars_tail?
  static boolean generic_token_chars(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_token_chars")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = generic_id_part_tokens_start(builder_, level_ + 1);
    result_ = result_ && generic_token_chars_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // generic_token_chars_tail?
  private static boolean generic_token_chars_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_token_chars_1")) return false;
    generic_token_chars_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // ( nws generic_id_part_tokens )+
  static boolean generic_token_chars_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_token_chars_tail")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = generic_token_chars_tail_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!generic_token_chars_tail_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "generic_token_chars_tail", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nws generic_id_part_tokens
  private static boolean generic_token_chars_tail_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_token_chars_tail_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && generic_id_part_tokens(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // (expandable_string_literal | verbatim_here_string_literal | target_variable_expression | generic_token_chars) generic_token_chars_tail?
  static boolean generic_token_part(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_token_part")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = generic_token_part_0(builder_, level_ + 1);
    result_ = result_ && generic_token_part_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expandable_string_literal | verbatim_here_string_literal | target_variable_expression | generic_token_chars
  private static boolean generic_token_part_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_token_part_0")) return false;
    boolean result_;
    result_ = expandable_string_literal(builder_, level_ + 1);
    if (!result_) result_ = verbatim_here_string_literal(builder_, level_ + 1);
    if (!result_) result_ = target_variable_expression(builder_, level_ + 1);
    if (!result_) result_ = generic_token_chars(builder_, level_ + 1);
    return result_;
  }

  // generic_token_chars_tail?
  private static boolean generic_token_part_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_token_part_1")) return false;
    generic_token_chars_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // generic_token_with_subexpr_start statement_list? RP nws command_name
  static boolean generic_token_with_sub_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_token_with_sub_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = generic_token_with_subexpr_start(builder_, level_ + 1);
    result_ = result_ && generic_token_with_sub_expr_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && command_name(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // statement_list?
  private static boolean generic_token_with_sub_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_token_with_sub_expr_1")) return false;
    statement_list(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // generic_token_part nws DS nws LP
  static boolean generic_token_with_subexpr_start(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_token_with_subexpr_start")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = generic_token_part(builder_, level_ + 1);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, DS);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LP);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // type_element nls? (',' nls? type_element)*
  static boolean generic_type_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_type_arguments")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = type_element(builder_, level_ + 1);
    result_ = result_ && generic_type_arguments_1(builder_, level_ + 1);
    result_ = result_ && generic_type_arguments_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean generic_type_arguments_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_type_arguments_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // (',' nls? type_element)*
  private static boolean generic_type_arguments_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_type_arguments_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!generic_type_arguments_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "generic_type_arguments_2", pos_)) break;
    }
    return true;
  }

  // ',' nls? type_element
  private static boolean generic_type_arguments_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_type_arguments_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && generic_type_arguments_2_0_1(builder_, level_ + 1);
    result_ = result_ && type_element(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean generic_type_arguments_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_type_arguments_2_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // generic_type_name nls? generic_type_arguments SQBR_R
  public static boolean generic_type_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_type_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GENERIC_TYPE_ELEMENT, "<generic type element>");
    result_ = generic_type_name(builder_, level_ + 1);
    result_ = result_ && generic_type_element_1(builder_, level_ + 1);
    result_ = result_ && generic_type_arguments(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SQBR_R);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nls?
  private static boolean generic_type_element_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_type_element_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // reference_type_element SQBR_L
  static boolean generic_type_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "generic_type_name")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = reference_type_element(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SQBR_L);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // key_expression '=' nls? statement
  static boolean hash_entry(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_entry")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = key_expression(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EQ);
    result_ = result_ && hash_entry_2(builder_, level_ + 1);
    result_ = result_ && statement(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean hash_entry_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_entry_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // hash_entry ( statement_terminators hash_entry )*
  static boolean hash_entry_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_entry_list")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = hash_entry(builder_, level_ + 1);
    result_ = result_ && hash_entry_list_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ( statement_terminators hash_entry )*
  private static boolean hash_entry_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_entry_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!hash_entry_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "hash_entry_list_1", pos_)) break;
    }
    return true;
  }

  // statement_terminators hash_entry
  private static boolean hash_entry_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_entry_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_terminators(builder_, level_ + 1);
    result_ = result_ && hash_entry(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // (hash_entry_list | statement_terminators )+
  public static boolean hash_literal_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_literal_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, BLOCK_BODY, "<hash literal body>");
    result_ = hash_literal_body_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!hash_literal_body_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "hash_literal_body", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // hash_entry_list | statement_terminators
  private static boolean hash_literal_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_literal_body_0")) return false;
    boolean result_;
    result_ = hash_entry_list(builder_, level_ + 1);
    if (!result_) result_ = statement_terminators(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // <<allow_any_expression hash_literal_body>>
  static boolean hash_literal_body_rule(PsiBuilder builder_, int level_) {
    return allow_any_expression(builder_, level_ + 1, PowerShellParser::hash_literal_body);
  }

  /* ********************************************************** */
  // AT nws LCURLY nls? hash_literal_body_rule? nls? RCURLY
  public static boolean hash_literal_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_literal_expression")) return false;
    if (!nextTokenIsFast(builder_, AT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, AT);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LCURLY);
    result_ = result_ && hash_literal_expression_3(builder_, level_ + 1);
    result_ = result_ && hash_literal_expression_4(builder_, level_ + 1);
    result_ = result_ && hash_literal_expression_5(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RCURLY);
    exit_section_(builder_, marker_, HASH_LITERAL_EXPRESSION, result_);
    return result_;
  }

  // nls?
  private static boolean hash_literal_expression_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_literal_expression_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // hash_literal_body_rule?
  private static boolean hash_literal_expression_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_literal_expression_4")) return false;
    hash_literal_body_rule(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean hash_literal_expression_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_literal_expression_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // HEX_INTEGER
  static boolean hexadecimal_integer_literal(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, HEX_INTEGER);
  }

  /* ********************************************************** */
  // generic_identifier
  public static boolean identifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "identifier")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, IDENTIFIER, "<identifier>");
    result_ = generic_identifier(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // 'if' nls? LP nls? pipeline nls? RP nls? statement_block (nls? elseif_clause)* (nls? else_clause)?
  public static boolean if_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement")) return false;
    if (!nextTokenIs(builder_, IF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IF_STATEMENT, null);
    result_ = consumeToken(builder_, IF);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, if_statement_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, LP)) && result_;
    result_ = pinned_ && report_error_(builder_, if_statement_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, pipeline(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, if_statement_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RP)) && result_;
    result_ = pinned_ && report_error_(builder_, if_statement_7(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, statement_block(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, if_statement_9(builder_, level_ + 1)) && result_;
    result_ = pinned_ && if_statement_10(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean if_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean if_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean if_statement_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean if_statement_7(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_7")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // (nls? elseif_clause)*
  private static boolean if_statement_9(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_9")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!if_statement_9_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "if_statement_9", pos_)) break;
    }
    return true;
  }

  // nls? elseif_clause
  private static boolean if_statement_9_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_9_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = if_statement_9_0_0(builder_, level_ + 1);
    result_ = result_ && elseif_clause(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean if_statement_9_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_9_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // (nls? else_clause)?
  private static boolean if_statement_10(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_10")) return false;
    if_statement_10_0(builder_, level_ + 1);
    return true;
  }

  // nls? else_clause
  private static boolean if_statement_10_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_10_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = if_statement_10_0_0(builder_, level_ + 1);
    result_ = result_ && else_clause(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean if_statement_10_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_10_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // attribute
  public static boolean incomplete_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "incomplete_declaration")) return false;
    if (!nextTokenIs(builder_, SQBR_L)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = attribute(builder_, level_ + 1);
    exit_section_(builder_, marker_, INCOMPLETE_DECLARATION, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'inlinescript' statement_block
  public static boolean inlinescript_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "inlinescript_statement")) return false;
    if (!nextTokenIs(builder_, INLINESCRIPT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, INLINESCRIPT);
    result_ = result_ && statement_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, INLINESCRIPT_STATEMENT, result_);
    return result_;
  }

  /* ********************************************************** */
  // decimal_integer_literal | hexadecimal_integer_literal
  public static boolean integer_literal_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "integer_literal_expression")) return false;
    if (!nextTokenIsFast(builder_, DEC_INTEGER, HEX_INTEGER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, INTEGER_LITERAL_EXPRESSION, "<integer literal expression>");
    result_ = decimal_integer_literal(builder_, level_ + 1);
    if (!result_) result_ = hexadecimal_integer_literal(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // COMMA nls? interface_name (nls? COMMA nls? interface_name)*
  static boolean interface_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interface_list")) return false;
    if (!nextTokenIs(builder_, COMMA)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && interface_list_1(builder_, level_ + 1);
    result_ = result_ && interface_name(builder_, level_ + 1);
    result_ = result_ && interface_list_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean interface_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interface_list_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // (nls? COMMA nls? interface_name)*
  private static boolean interface_list_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interface_list_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!interface_list_3_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "interface_list_3", pos_)) break;
    }
    return true;
  }

  // nls? COMMA nls? interface_name
  private static boolean interface_list_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interface_list_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = interface_list_3_0_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COMMA);
    result_ = result_ && interface_list_3_0_2(builder_, level_ + 1);
    result_ = result_ && interface_name(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean interface_list_3_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interface_list_3_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean interface_list_3_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "interface_list_3_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // type_element
  static boolean interface_name(PsiBuilder builder_, int level_) {
    return type_element(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // nws member_access_operator nls? member_name call_arguments
  public static boolean invocation_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "invocation_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, INVOCATION_EXPRESSION, "<invocation expression>");
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && member_access_operator(builder_, level_ + 1);
    result_ = result_ && invocation_expression_2(builder_, level_ + 1);
    result_ = result_ && member_name(builder_, level_ + 1);
    result_ = result_ && call_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nls?
  private static boolean invocation_expression_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "invocation_expression_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<is_parsing_configuration_block>>
  static boolean is_dsc(PsiBuilder builder_, int level_) {
    return is_parsing_configuration_block(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // <<isIdentifierBefore>>
  static boolean is_id(PsiBuilder builder_, int level_) {
    return isIdentifierBefore(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // <<is_parsing_call_arguments>>
  static boolean is_in_call_arguments(PsiBuilder builder_, int level_) {
    return is_parsing_call_arguments(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // simple_name_identifier | unary_expression
  public static boolean key_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "key_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, KEY_EXPRESSION, "<key expression>");
    result_ = simple_name_identifier(builder_, level_ + 1);
    if (!result_) result_ = unary_expression(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // ':' nws label_name
  public static boolean label(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label")) return false;
    if (!nextTokenIs(builder_, COLON)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COLON);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && label_name(builder_, level_ + 1);
    exit_section_(builder_, marker_, LABEL, result_);
    return result_;
  }

  /* ********************************************************** */
  // simple_name_identifier | unary_expression
  static boolean label_identifier_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_identifier_expression")) return false;
    boolean result_;
    result_ = simple_name_identifier(builder_, level_ + 1);
    if (!result_) result_ = unary_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // enum_label_declaration (statement_terminators enum_label_declaration)*
  static boolean label_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_list")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = enum_label_declaration(builder_, level_ + 1);
    result_ = result_ && label_list_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PowerShellParser::top_level_recover);
    return result_;
  }

  // (statement_terminators enum_label_declaration)*
  private static boolean label_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!label_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "label_list_1", pos_)) break;
    }
    return true;
  }

  // statement_terminators enum_label_declaration
  private static boolean label_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_terminators(builder_, level_ + 1);
    result_ = result_ && enum_label_declaration(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // SIMPLE_ID
  public static boolean label_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_name")) return false;
    if (!nextTokenIs(builder_, SIMPLE_ID)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SIMPLE_ID);
    exit_section_(builder_, marker_, IDENTIFIER, result_);
    return result_;
  }

  /* ********************************************************** */
  // label_identifier_expression
  public static boolean label_reference_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_reference_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, LABEL_REFERENCE_EXPRESSION, "<label reference expression>");
    result_ = label_identifier_expression(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // switch_statement
  //                               | foreach_statement
  //                               | for_statement
  //                               | while_statement
  //                               | do_statement
  static boolean labeled_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "labeled_statement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = switch_statement(builder_, level_ + 1);
    if (!result_) result_ = foreach_statement(builder_, level_ + 1);
    if (!result_) result_ = for_statement(builder_, level_ + 1);
    if (!result_) result_ = while_statement(builder_, level_ + 1);
    if (!result_) result_ = do_statement(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // real_literal_expression | integer_literal_expression | string_literal_expression
  static boolean literal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "literal")) return false;
    boolean result_;
    result_ = real_literal_expression(builder_, level_ + 1);
    if (!result_) result_ = integer_literal_expression(builder_, level_ + 1);
    if (!result_) result_ = string_literal_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // nws member_access_operator nls? member_name
  public static boolean member_access_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "member_access_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, MEMBER_ACCESS_EXPRESSION, "<member access expression>");
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && member_access_operator(builder_, level_ + 1);
    result_ = result_ && member_access_expression_2(builder_, level_ + 1);
    result_ = result_ && member_name(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nls?
  private static boolean member_access_expression_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "member_access_expression_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // DOT | COLON2
  static boolean member_access_operator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "member_access_operator")) return false;
    if (!nextTokenIs(builder_, "", COLON2, DOT)) return false;
    boolean result_;
    result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, COLON2);
    return result_;
  }

  /* ********************************************************** */
  // declaration_modifier_attribute | attribute
  static boolean member_declaration_attribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "member_declaration_attribute")) return false;
    boolean result_;
    result_ = declaration_modifier_attribute(builder_, level_ + 1);
    if (!result_) result_ = attribute(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // simple_name_reference
  //                         | string_literal_expression
  // //->                        | string_literal_with_subexpression
  //                         | expression_with_unary_operator //prefix_op unary_expression //
  //                         | value
  static boolean member_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "member_name")) return false;
    boolean result_;
    result_ = simple_name_reference(builder_, level_ + 1);
    if (!result_) result_ = string_literal_expression(builder_, level_ + 1);
    if (!result_) result_ = expression_with_unary_operator(builder_, level_ + 1);
    if (!result_) result_ = value(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // OP_MR
  static boolean merging_redirection_operator(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, OP_MR);
  }

  /* ********************************************************** */
  // LP nls? parameter_list? nls? RP
  public static boolean method_argument_expression_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_argument_expression_list")) return false;
    if (!nextTokenIs(builder_, LP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LP);
    result_ = result_ && method_argument_expression_list_1(builder_, level_ + 1);
    result_ = result_ && method_argument_expression_list_2(builder_, level_ + 1);
    result_ = result_ && method_argument_expression_list_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    exit_section_(builder_, marker_, PARAMETER_CLAUSE, result_);
    return result_;
  }

  // nls?
  private static boolean method_argument_expression_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_argument_expression_list_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // parameter_list?
  private static boolean method_argument_expression_list_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_argument_expression_list_2")) return false;
    parameter_list(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean method_argument_expression_list_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_argument_expression_list_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // statement_block
  static boolean method_block(PsiBuilder builder_, int level_) {
    return statement_block(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // (member_declaration_attribute nls?)* method_name method_argument_expression_list nls? method_block
  public static boolean method_declaration_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_declaration_statement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, METHOD_DECLARATION_STATEMENT, "<method declaration statement>");
    result_ = method_declaration_statement_0(builder_, level_ + 1);
    result_ = result_ && method_name(builder_, level_ + 1);
    result_ = result_ && method_argument_expression_list(builder_, level_ + 1);
    result_ = result_ && method_declaration_statement_3(builder_, level_ + 1);
    result_ = result_ && method_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (member_declaration_attribute nls?)*
  private static boolean method_declaration_statement_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_declaration_statement_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!method_declaration_statement_0_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "method_declaration_statement_0", pos_)) break;
    }
    return true;
  }

  // member_declaration_attribute nls?
  private static boolean method_declaration_statement_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_declaration_statement_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = member_declaration_attribute(builder_, level_ + 1);
    result_ = result_ && method_declaration_statement_0_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean method_declaration_statement_0_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_declaration_statement_0_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean method_declaration_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_declaration_statement_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // simple_name_identifier
  static boolean method_name(PsiBuilder builder_, int level_) {
    return simple_name_identifier(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // block_name nls? statement_block
  static boolean named_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_block")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = block_name(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, named_block_1(builder_, level_ + 1));
    result_ = pinned_ && statement_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean named_block_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_block_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // named_block (statement_terminators named_block )*
  static boolean named_block_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_block_list")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = named_block(builder_, level_ + 1);
    result_ = result_ && named_block_list_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (statement_terminators named_block )*
  private static boolean named_block_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_block_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!named_block_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "named_block_list_1", pos_)) break;
    }
    return true;
  }

  // statement_terminators named_block
  private static boolean named_block_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_block_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_terminators(builder_, level_ + 1);
    result_ = result_ && named_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // <<isNotComment>>
  static boolean ncm(PsiBuilder builder_, int level_) {
    return isNotComment(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // LF
  static boolean new_line_char(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, LF);
  }

  /* ********************************************************** */
  // (comment? NLS comment?)+
  static boolean nls(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nls")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nls_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!nls_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "nls", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // comment? NLS comment?
  private static boolean nls_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nls_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nls_0_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, NLS);
    result_ = result_ && nls_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // comment?
  private static boolean nls_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nls_0_0")) return false;
    comment(builder_, level_ + 1);
    return true;
  }

  // comment?
  private static boolean nls_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nls_0_2")) return false;
    comment(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // node_token nls? command_argument_list+
  public static boolean node_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "node_block")) return false;
    if (!nextTokenIs(builder_, SIMPLE_ID)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NODE_BLOCK, null);
    result_ = node_token(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, node_block_1(builder_, level_ + 1));
    result_ = pinned_ && node_block_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean node_block_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "node_block_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // command_argument_list+
  private static boolean node_block_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "node_block_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_argument_list(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!command_argument_list(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "node_block_2", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // SIMPLE_ID
  static boolean node_token(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, SIMPLE_ID);
  }

  /* ********************************************************** */
  // <<isNotWhiteSpace>>
  static boolean nws(PsiBuilder builder_, int level_) {
    return isNotWhiteSpace(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // '-join' | '–join' | '—join' | '―join'
  static boolean op_join_text(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "op_join_text")) return false;
    boolean result_;
    result_ = consumeToken(builder_, "-join");
    if (!result_) result_ = consumeToken(builder_, "–join");
    if (!result_) result_ = consumeToken(builder_, "—join");
    if (!result_) result_ = consumeToken(builder_, "―join");
    return result_;
  }

  /* ********************************************************** */
  // '-split' | '–split' | '—split' | '―split'
  static boolean op_split_text(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "op_split_text")) return false;
    boolean result_;
    result_ = consumeToken(builder_, "-split");
    if (!result_) result_ = consumeToken(builder_, "–split");
    if (!result_) result_ = consumeToken(builder_, "—split");
    if (!result_) result_ = consumeToken(builder_, "―split");
    return result_;
  }

  /* ********************************************************** */
  // 'parallel' statement_block
  public static boolean parallel_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parallel_statement")) return false;
    if (!nextTokenIs(builder_, PARALLEL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PARALLEL);
    result_ = result_ && statement_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, PARALLEL_STATEMENT, result_);
    return result_;
  }

  /* ********************************************************** */
  // LP nls? parameter_list? nls? RP
  public static boolean parameter_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameter_clause")) return false;
    if (!nextTokenIs(builder_, LP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LP);
    result_ = result_ && parameter_clause_1(builder_, level_ + 1);
    result_ = result_ && parameter_clause_2(builder_, level_ + 1);
    result_ = result_ && parameter_clause_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    exit_section_(builder_, marker_, PARAMETER_CLAUSE, result_);
    return result_;
  }

  // nls?
  private static boolean parameter_clause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameter_clause_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // parameter_list?
  private static boolean parameter_clause_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameter_clause_2")) return false;
    parameter_list(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean parameter_clause_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parameter_clause_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // script_parameter_list
  static boolean parameter_list(PsiBuilder builder_, int level_) {
    return script_parameter_list(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // LP nls? argument_expression_list? nls? RP
  public static boolean parenthesized_argument_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesized_argument_list")) return false;
    if (!nextTokenIs(builder_, LP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LP);
    result_ = result_ && parenthesized_argument_list_1(builder_, level_ + 1);
    result_ = result_ && parenthesized_argument_list_2(builder_, level_ + 1);
    result_ = result_ && parenthesized_argument_list_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    exit_section_(builder_, marker_, PARENTHESIZED_ARGUMENT_LIST, result_);
    return result_;
  }

  // nls?
  private static boolean parenthesized_argument_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesized_argument_list_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // argument_expression_list?
  private static boolean parenthesized_argument_list_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesized_argument_list_2")) return false;
    argument_expression_list(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean parenthesized_argument_list_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesized_argument_list_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // LP nls? <<allow_any_expression pipeline>> nls? RP
  public static boolean parenthesized_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesized_expression")) return false;
    if (!nextTokenIsFast(builder_, LP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, LP);
    result_ = result_ && parenthesized_expression_1(builder_, level_ + 1);
    result_ = result_ && allow_any_expression(builder_, level_ + 1, PowerShellParser::pipeline);
    result_ = result_ && parenthesized_expression_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    exit_section_(builder_, marker_, PARENTHESIZED_EXPRESSION, result_);
    return result_;
  }

  // nls?
  private static boolean parenthesized_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesized_expression_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean parenthesized_expression_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesized_expression_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parseCommandArgumentInner generic_chars_or_path_expression>>
  static boolean parse_command_argument(PsiBuilder builder_, int level_) {
    return parseCommandArgumentInner(builder_, level_ + 1, PowerShellParser::generic_chars_or_path_expression);
  }

  /* ********************************************************** */
  // is_dsc node_block
  static boolean parse_node_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_node_block")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = is_dsc(builder_, level_ + 1);
    result_ = result_ && node_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // is_dsc resource_block
  static boolean parse_resource_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_resource_block")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = is_dsc(builder_, level_ + 1);
    result_ = result_ && resource_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
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
  public static boolean path_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PATH_EXPRESSION, "<path expression>");
    result_ = path_expression_0(builder_, level_ + 1);
    if (!result_) result_ = path_expression_1(builder_, level_ + 1);
    if (!result_) result_ = relative_path_item(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
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
  private static boolean path_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = path_item(builder_, level_ + 1);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && path_expression_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
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
  private static boolean path_expression_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = path_expression_0_2_0(builder_, level_ + 1);
    if (!result_) result_ = path_expression_0_2_1(builder_, level_ + 1);
    if (!result_) result_ = path_expression_0_2_2(builder_, level_ + 1);
    if (!result_) result_ = consumeTokenFast(builder_, PATH_SEP);
    exit_section_(builder_, marker_, null, result_);
    return result_;
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
  private static boolean path_expression_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = path_expression_0_2_0_0(builder_, level_ + 1);
    result_ = result_ && path_expression_0_2_0_1(builder_, level_ + 1);
    result_ = result_ && path_expression_0_2_0_2(builder_, level_ + 1);
    result_ = result_ && path_expression_0_2_0_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (   (PATH_SEP nws COLON2 nws) (path_item nws PATH_SEP nws COLON2 nws)*  //provider+ drive?
  //                                   (nws path_item nws (COLON|DS) nws PATH_SEP)? //drive
  //                                )
  //                               |
  //                                (
  //                                   ((COLON|DS) nws PATH_SEP)     // or drive
  //                                )
  private static boolean path_expression_0_2_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = path_expression_0_2_0_0_0(builder_, level_ + 1);
    if (!result_) result_ = path_expression_0_2_0_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (PATH_SEP nws COLON2 nws) (path_item nws PATH_SEP nws COLON2 nws)*  //provider+ drive?
  //                                   (nws path_item nws (COLON|DS) nws PATH_SEP)?
  private static boolean path_expression_0_2_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = path_expression_0_2_0_0_0_0(builder_, level_ + 1);
    result_ = result_ && path_expression_0_2_0_0_0_1(builder_, level_ + 1);
    result_ = result_ && path_expression_0_2_0_0_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // PATH_SEP nws COLON2 nws
  private static boolean path_expression_0_2_0_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_0_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, PATH_SEP);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON2);
    result_ = result_ && nws(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (path_item nws PATH_SEP nws COLON2 nws)*
  private static boolean path_expression_0_2_0_0_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_0_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!path_expression_0_2_0_0_0_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "path_expression_0_2_0_0_0_1", pos_)) break;
    }
    return true;
  }

  // path_item nws PATH_SEP nws COLON2 nws
  private static boolean path_expression_0_2_0_0_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_0_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = path_item(builder_, level_ + 1);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PATH_SEP);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON2);
    result_ = result_ && nws(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (nws path_item nws (COLON|DS) nws PATH_SEP)?
  private static boolean path_expression_0_2_0_0_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_0_0_2")) return false;
    path_expression_0_2_0_0_0_2_0(builder_, level_ + 1);
    return true;
  }

  // nws path_item nws (COLON|DS) nws PATH_SEP
  private static boolean path_expression_0_2_0_0_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_0_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && path_item(builder_, level_ + 1);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && path_expression_0_2_0_0_0_2_0_3(builder_, level_ + 1);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PATH_SEP);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // COLON|DS
  private static boolean path_expression_0_2_0_0_0_2_0_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_0_0_2_0_3")) return false;
    boolean result_;
    result_ = consumeTokenFast(builder_, COLON);
    if (!result_) result_ = consumeTokenFast(builder_, DS);
    return result_;
  }

  // (COLON|DS) nws PATH_SEP
  private static boolean path_expression_0_2_0_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = path_expression_0_2_0_0_1_0(builder_, level_ + 1);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PATH_SEP);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // COLON|DS
  private static boolean path_expression_0_2_0_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_0_1_0")) return false;
    boolean result_;
    result_ = consumeTokenFast(builder_, COLON);
    if (!result_) result_ = consumeTokenFast(builder_, DS);
    return result_;
  }

  // nws path_item
  private static boolean path_expression_0_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && path_item(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (nws PATH_SEP nws path_item)*
  private static boolean path_expression_0_2_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!path_expression_0_2_0_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "path_expression_0_2_0_2", pos_)) break;
    }
    return true;
  }

  // nws PATH_SEP nws path_item
  private static boolean path_expression_0_2_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PATH_SEP);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && path_item(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (nws PATH_SEP)?
  private static boolean path_expression_0_2_0_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_3")) return false;
    path_expression_0_2_0_3_0(builder_, level_ + 1);
    return true;
  }

  // nws PATH_SEP
  private static boolean path_expression_0_2_0_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_0_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PATH_SEP);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (PATH_SEP nws path_item)+
  private static boolean path_expression_0_2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = path_expression_0_2_1_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!path_expression_0_2_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "path_expression_0_2_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // PATH_SEP nws path_item
  private static boolean path_expression_0_2_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, PATH_SEP);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && path_item(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (COLON|DS) (nws PATH_SEP)?
  private static boolean path_expression_0_2_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = path_expression_0_2_2_0(builder_, level_ + 1);
    result_ = result_ && path_expression_0_2_2_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // COLON|DS
  private static boolean path_expression_0_2_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_2_0")) return false;
    boolean result_;
    result_ = consumeTokenFast(builder_, COLON);
    if (!result_) result_ = consumeTokenFast(builder_, DS);
    return result_;
  }

  // (nws PATH_SEP)?
  private static boolean path_expression_0_2_2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_2_1")) return false;
    path_expression_0_2_2_1_0(builder_, level_ + 1);
    return true;
  }

  // nws PATH_SEP
  private static boolean path_expression_0_2_2_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_0_2_2_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, PATH_SEP);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // PATH_SEP (nws (path_expression | path_item))?
  private static boolean path_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, PATH_SEP);
    result_ = result_ && path_expression_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (nws (path_expression | path_item))?
  private static boolean path_expression_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_1_1")) return false;
    path_expression_1_1_0(builder_, level_ + 1);
    return true;
  }

  // nws (path_expression | path_item)
  private static boolean path_expression_1_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_1_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && path_expression_1_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // path_expression | path_item
  private static boolean path_expression_1_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_expression_1_1_0_1")) return false;
    boolean result_;
    result_ = path_expression(builder_, level_ + 1);
    if (!result_) result_ = path_item(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // path_item_name
  public static boolean path_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, PATH_ITEM, "<path item>");
    result_ = path_item_name(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // relative_path | path_name_tokens | allowed_identifier_keywords | target_variable_expression
  static boolean path_item_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_item_name")) return false;
    boolean result_;
    result_ = relative_path(builder_, level_ + 1);
    if (!result_) result_ = path_name_tokens(builder_, level_ + 1);
    if (!result_) result_ = allowed_identifier_keywords(builder_, level_ + 1);
    if (!result_) result_ = target_variable_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // generic_id_part_tokens_start | CMD_PARAMETER | PLUS | DASH
  static boolean path_name_part_tokens_start(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_name_part_tokens_start")) return false;
    boolean result_;
    result_ = generic_id_part_tokens_start(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, CMD_PARAMETER);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, DASH);
    return result_;
  }

  /* ********************************************************** */
  // path_name_part_tokens_start (nws (path_name_part_tokens_start | DOT))*
  static boolean path_name_tokens(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_name_tokens")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = path_name_part_tokens_start(builder_, level_ + 1);
    result_ = result_ && path_name_tokens_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (nws (path_name_part_tokens_start | DOT))*
  private static boolean path_name_tokens_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_name_tokens_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!path_name_tokens_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "path_name_tokens_1", pos_)) break;
    }
    return true;
  }

  // nws (path_name_part_tokens_start | DOT)
  private static boolean path_name_tokens_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_name_tokens_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = nws(builder_, level_ + 1);
    result_ = result_ && path_name_tokens_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // path_name_part_tokens_start | DOT
  private static boolean path_name_tokens_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "path_name_tokens_1_0_1")) return false;
    boolean result_;
    result_ = path_name_part_tokens_start(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, DOT);
    return result_;
  }

  /* ********************************************************** */
  // expression redirection* pipeline_tail? &statement_end | command_call_expression verbatim_command_argument? pipeline_tail?
  static boolean pipeline(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pipeline_0(builder_, level_ + 1);
    if (!result_) result_ = pipeline_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // expression redirection* pipeline_tail? &statement_end
  private static boolean pipeline_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = expression(builder_, level_ + 1, -1);
    result_ = result_ && pipeline_0_1(builder_, level_ + 1);
    result_ = result_ && pipeline_0_2(builder_, level_ + 1);
    result_ = result_ && pipeline_0_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // redirection*
  private static boolean pipeline_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!redirection(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "pipeline_0_1", pos_)) break;
    }
    return true;
  }

  // pipeline_tail?
  private static boolean pipeline_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline_0_2")) return false;
    pipeline_tail(builder_, level_ + 1);
    return true;
  }

  // &statement_end
  private static boolean pipeline_0_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline_0_3")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = statement_end(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // command_call_expression verbatim_command_argument? pipeline_tail?
  private static boolean pipeline_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_call_expression(builder_, level_ + 1);
    result_ = result_ && pipeline_1_1(builder_, level_ + 1);
    result_ = result_ && pipeline_1_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // verbatim_command_argument?
  private static boolean pipeline_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline_1_1")) return false;
    verbatim_command_argument(builder_, level_ + 1);
    return true;
  }

  // pipeline_tail?
  private static boolean pipeline_1_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline_1_2")) return false;
    pipeline_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // ( PIPE nls? command_call_expression )+
  public static boolean pipeline_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline_tail")) return false;
    if (!nextTokenIs(builder_, PIPE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, PIPELINE, null);
    result_ = pipeline_tail_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!pipeline_tail_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "pipeline_tail", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // PIPE nls? command_call_expression
  private static boolean pipeline_tail_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline_tail_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, PIPE);
    result_ = result_ && pipeline_tail_0_1(builder_, level_ + 1);
    result_ = result_ && command_call_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean pipeline_tail_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pipeline_tail_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // MM
  public static boolean post_decrement_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "post_decrement_expression")) return false;
    if (!nextTokenIsFast(builder_, MM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, POST_DECREMENT_EXPRESSION, null);
    result_ = consumeTokenFast(builder_, MM);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // PP
  public static boolean post_increment_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "post_increment_expression")) return false;
    if (!nextTokenIsFast(builder_, PP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, POST_INCREMENT_EXPRESSION, null);
    result_ = consumeTokenFast(builder_, PP);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // DS | QMARK | HAT
  public static boolean predefined_var_id(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "predefined_var_id")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IDENTIFIER, "<predefined var id>");
    result_ = consumeToken(builder_, DS);
    if (!result_) result_ = consumeToken(builder_, QMARK);
    if (!result_) result_ = consumeToken(builder_, HAT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // OP_BNOT | EXCL_MARK | OP_NOT | MM | PP | op_split_text | op_join_text | COMMA | PLUS | DASH
  static boolean prefix_op(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "prefix_op")) return false;
    boolean result_;
    result_ = consumeToken(builder_, OP_BNOT);
    if (!result_) result_ = consumeToken(builder_, EXCL_MARK);
    if (!result_) result_ = consumeToken(builder_, OP_NOT);
    if (!result_) result_ = consumeToken(builder_, MM);
    if (!result_) result_ = consumeToken(builder_, PP);
    if (!result_) result_ = op_split_text(builder_, level_ + 1);
    if (!result_) result_ = op_join_text(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, PLUS);
    if (!result_) result_ = consumeToken(builder_, DASH);
    return result_;
  }

  /* ********************************************************** */
  // value (invocation_expression | member_access_expression | element_access_expression | post_increment_expression | post_decrement_expression)*
  static boolean primary_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "primary_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = value(builder_, level_ + 1);
    result_ = result_ && primary_expression_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (invocation_expression | member_access_expression | element_access_expression | post_increment_expression | post_decrement_expression)*
  private static boolean primary_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "primary_expression_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!primary_expression_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "primary_expression_1", pos_)) break;
    }
    return true;
  }

  // invocation_expression | member_access_expression | element_access_expression | post_increment_expression | post_decrement_expression
  private static boolean primary_expression_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "primary_expression_1_0")) return false;
    boolean result_;
    result_ = invocation_expression(builder_, level_ + 1);
    if (!result_) result_ = member_access_expression(builder_, level_ + 1);
    if (!result_) result_ = element_access_expression(builder_, level_ + 1);
    if (!result_) result_ = post_increment_expression(builder_, level_ + 1);
    if (!result_) result_ = post_decrement_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // (member_declaration_attribute nls?)* property_definition_expression
  public static boolean property_declaration_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_declaration_statement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY_DECLARATION_STATEMENT, "<property declaration statement>");
    result_ = property_declaration_statement_0(builder_, level_ + 1);
    result_ = result_ && property_definition_expression(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (member_declaration_attribute nls?)*
  private static boolean property_declaration_statement_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_declaration_statement_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property_declaration_statement_0_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "property_declaration_statement_0", pos_)) break;
    }
    return true;
  }

  // member_declaration_attribute nls?
  private static boolean property_declaration_statement_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_declaration_statement_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = member_declaration_attribute(builder_, level_ + 1);
    result_ = result_ && property_declaration_statement_0_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean property_declaration_statement_0_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_declaration_statement_0_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // expression
  static boolean property_definition_expression(PsiBuilder builder_, int level_) {
    return expression(builder_, level_ + 1, -1);
  }

  /* ********************************************************** */
  // REAL_NUM
  public static boolean real_literal_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "real_literal_expression")) return false;
    if (!nextTokenIsFast(builder_, REAL_NUM)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, REAL_NUM);
    exit_section_(builder_, marker_, REAL_LITERAL_EXPRESSION, result_);
    return result_;
  }

  /* ********************************************************** */
  // command_argument_list | primary_expression
  static boolean redirected_file_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "redirected_file_name")) return false;
    boolean result_;
    result_ = command_argument_list(builder_, level_ + 1);
    if (!result_) result_ = primary_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // merging_redirection_operator | file_redirection_operator  redirected_file_name
  public static boolean redirection(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "redirection")) return false;
    if (!nextTokenIs(builder_, "<redirection>", OP_FR, OP_MR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, REDIRECTION, "<redirection>");
    result_ = merging_redirection_operator(builder_, level_ + 1);
    if (!result_) result_ = redirection_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // file_redirection_operator  redirected_file_name
  private static boolean redirection_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "redirection_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = file_redirection_operator(builder_, level_ + 1);
    result_ = result_ && redirected_file_name(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // simple_type_identifier compound_type_identifier*
  public static boolean reference_type_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "reference_type_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, REFERENCE_TYPE_ELEMENT, "<reference type element>");
    result_ = simple_type_identifier(builder_, level_ + 1);
    result_ = result_ && reference_type_element_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // compound_type_identifier*
  private static boolean reference_type_element_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "reference_type_element_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!compound_type_identifier(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "reference_type_element_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // DOT | DOT_DOT | PATH_SEP
  static boolean relative_path(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "relative_path")) return false;
    boolean result_;
    result_ = consumeToken(builder_, DOT);
    if (!result_) result_ = consumeToken(builder_, DOT_DOT);
    if (!result_) result_ = consumeToken(builder_, PATH_SEP);
    return result_;
  }

  /* ********************************************************** */
  // relative_path
  public static boolean relative_path_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "relative_path_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PATH_ITEM, "<relative path item>");
    result_ = relative_path(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // REQUIRES_COMMENT_START ws command_argument_list+
  static boolean requires_comment(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "requires_comment")) return false;
    if (!nextTokenIs(builder_, REQUIRES_COMMENT_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, REQUIRES_COMMENT_START);
    result_ = result_ && ws(builder_, level_ + 1);
    result_ = result_ && requires_comment_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // command_argument_list+
  private static boolean requires_comment_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "requires_comment_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = command_argument_list(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!command_argument_list(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "requires_comment_2", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'class'|'data'|'do'|'elseif'|'exit'|'filter'|'for'|'foreach'|'from'|'function'|'if'|'return'|'switch'|'throw'
  // |'trap'|'try'|'using'|'var'|'while'|'configuration'|'enum'
  static boolean reserved_statement_keywords(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "reserved_statement_keywords")) return false;
    boolean result_;
    result_ = consumeToken(builder_, CLASS);
    if (!result_) result_ = consumeToken(builder_, DATA);
    if (!result_) result_ = consumeToken(builder_, DO);
    if (!result_) result_ = consumeToken(builder_, ELSEIF);
    if (!result_) result_ = consumeToken(builder_, EXIT);
    if (!result_) result_ = consumeToken(builder_, FILTER);
    if (!result_) result_ = consumeToken(builder_, FOR);
    if (!result_) result_ = consumeToken(builder_, FOREACH);
    if (!result_) result_ = consumeToken(builder_, FROM);
    if (!result_) result_ = consumeToken(builder_, FUNCTION);
    if (!result_) result_ = consumeToken(builder_, IF);
    if (!result_) result_ = consumeToken(builder_, RETURN);
    if (!result_) result_ = consumeToken(builder_, SWITCH);
    if (!result_) result_ = consumeToken(builder_, THROW);
    if (!result_) result_ = consumeToken(builder_, TRAP);
    if (!result_) result_ = consumeToken(builder_, TRY);
    if (!result_) result_ = consumeToken(builder_, USING);
    if (!result_) result_ = consumeToken(builder_, VAR);
    if (!result_) result_ = consumeToken(builder_, WHILE);
    if (!result_) result_ = consumeToken(builder_, CONFIGURATION);
    if (!result_) result_ = consumeToken(builder_, ENUM);
    return result_;
  }

  /* ********************************************************** */
  // resource_type nls? ( resource_name nls? resource_block_tail | resource_block_tail )
  public static boolean resource_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_block")) return false;
    if (!nextTokenIs(builder_, SIMPLE_ID)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = resource_type(builder_, level_ + 1);
    result_ = result_ && resource_block_1(builder_, level_ + 1);
    result_ = result_ && resource_block_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, RESOURCE_BLOCK, result_);
    return result_;
  }

  // nls?
  private static boolean resource_block_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_block_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // resource_name nls? resource_block_tail | resource_block_tail
  private static boolean resource_block_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_block_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = resource_block_2_0(builder_, level_ + 1);
    if (!result_) result_ = resource_block_tail(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // resource_name nls? resource_block_tail
  private static boolean resource_block_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_block_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = resource_name(builder_, level_ + 1);
    result_ = result_ && resource_block_2_0_1(builder_, level_ + 1);
    result_ = result_ && resource_block_tail(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean resource_block_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_block_2_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // hash_literal_body
  public static boolean resource_block_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_block_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, BLOCK_BODY, "<resource block body>");
    result_ = hash_literal_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // LCURLY nls? resource_block_body? nls? RCURLY
  static boolean resource_block_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_block_tail")) return false;
    if (!nextTokenIs(builder_, LCURLY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LCURLY);
    result_ = result_ && resource_block_tail_1(builder_, level_ + 1);
    result_ = result_ && resource_block_tail_2(builder_, level_ + 1);
    result_ = result_ && resource_block_tail_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RCURLY);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean resource_block_tail_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_block_tail_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // resource_block_body?
  private static boolean resource_block_tail_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_block_tail_2")) return false;
    resource_block_body(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean resource_block_tail_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_block_tail_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // function_name
  static boolean resource_name(PsiBuilder builder_, int level_) {
    return function_name(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // SIMPLE_ID
  static boolean resource_type(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, SIMPLE_ID);
  }

  /* ********************************************************** */
  // top_script_block
  public static boolean script_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_block")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, BLOCK_BODY, "<script block>");
    result_ = top_script_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // named_block_list | (statement_list | statement_terminators)+
  static boolean script_block_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_block_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = named_block_list(builder_, level_ + 1);
    if (!result_) result_ = script_block_body_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (statement_list | statement_terminators)+
  private static boolean script_block_body_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_block_body_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = script_block_body_1_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!script_block_body_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "script_block_body_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // statement_list | statement_terminators
  private static boolean script_block_body_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_block_body_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_list(builder_, level_ + 1);
    if (!result_) result_ = statement_terminators(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // LCURLY script_block_rule? RCURLY
  public static boolean script_block_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_block_expression")) return false;
    if (!nextTokenIsFast(builder_, LCURLY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, LCURLY);
    result_ = result_ && script_block_expression_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RCURLY);
    exit_section_(builder_, marker_, SCRIPT_BLOCK_EXPRESSION, result_);
    return result_;
  }

  // script_block_rule?
  private static boolean script_block_expression_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_block_expression_1")) return false;
    script_block_rule(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<allow_any_expression script_block>>
  static boolean script_block_rule(PsiBuilder builder_, int level_) {
    return allow_any_expression(builder_, level_ + 1, PowerShellParser::script_block);
  }

  /* ********************************************************** */
  // attribute_list? nls? target_variable_expression nls? script_parameter_default?
  static boolean script_parameter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = script_parameter_0(builder_, level_ + 1);
    result_ = result_ && script_parameter_1(builder_, level_ + 1);
    result_ = result_ && target_variable_expression(builder_, level_ + 1);
    result_ = result_ && script_parameter_3(builder_, level_ + 1);
    result_ = result_ && script_parameter_4(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // attribute_list?
  private static boolean script_parameter_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_0")) return false;
    attribute_list(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean script_parameter_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean script_parameter_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // script_parameter_default?
  private static boolean script_parameter_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_4")) return false;
    script_parameter_default(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '=' nls? expression
  static boolean script_parameter_default(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_default")) return false;
    if (!nextTokenIs(builder_, EQ)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, EQ);
    result_ = result_ && script_parameter_default_1(builder_, level_ + 1);
    result_ = result_ && expression(builder_, level_ + 1, -1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean script_parameter_default_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_default_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parse_argument_list script_parameter_list_rule>>
  static boolean script_parameter_list(PsiBuilder builder_, int level_) {
    return parse_argument_list(builder_, level_ + 1, PowerShellParser::script_parameter_list_rule);
  }

  /* ********************************************************** */
  // script_parameter (nls? COMMA nls? script_parameter)*
  static boolean script_parameter_list_rule(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_list_rule")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = script_parameter(builder_, level_ + 1);
    result_ = result_ && script_parameter_list_rule_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (nls? COMMA nls? script_parameter)*
  private static boolean script_parameter_list_rule_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_list_rule_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!script_parameter_list_rule_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "script_parameter_list_rule_1", pos_)) break;
    }
    return true;
  }

  // nls? COMMA nls? script_parameter
  private static boolean script_parameter_list_rule_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_list_rule_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = script_parameter_list_rule_1_0_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COMMA);
    result_ = result_ && script_parameter_list_rule_1_0_2(builder_, level_ + 1);
    result_ = result_ && script_parameter(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean script_parameter_list_rule_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_list_rule_1_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean script_parameter_list_rule_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "script_parameter_list_rule_1_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // SEMI |  NLS | new_line_char | comment
  static boolean sep(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sep")) return false;
    boolean result_;
    result_ = consumeToken(builder_, SEMI);
    if (!result_) result_ = consumeToken(builder_, NLS);
    if (!result_) result_ = new_line_char(builder_, level_ + 1);
    if (!result_) result_ = comment(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // 'sequence' statement_block
  public static boolean sequence_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sequence_statement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SEQUENCE_STATEMENT, "<sequence statement>");
    result_ = consumeToken(builder_, "sequence");
    result_ = result_ && statement_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // SIMPLE_ID | allowed_identifier_keywords
  public static boolean simple_name_identifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "simple_name_identifier")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IDENTIFIER, "<simple name identifier>");
    result_ = consumeToken(builder_, SIMPLE_ID);
    if (!result_) result_ = allowed_identifier_keywords(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // SIMPLE_ID | allowed_identifier_keywords
  public static boolean simple_name_reference(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "simple_name_reference")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, REFERENCE_IDENTIFIER, "<simple name reference>");
    result_ = consumeToken(builder_, SIMPLE_ID);
    if (!result_) result_ = allowed_identifier_keywords(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // SIMPLE_ID | allowed_identifier_keywords
  public static boolean simple_type_identifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "simple_type_identifier")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, REFERENCE_TYPE_ELEMENT, "<reference type element>");
    result_ = consumeToken(builder_, SIMPLE_ID);
    if (!result_) result_ = allowed_identifier_keywords(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // (DS | AT) nws (declaration_scope nws)? variable_name_simple
  static boolean simple_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "simple_variable")) return false;
    if (!nextTokenIs(builder_, "", AT, DS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = simple_variable_0(builder_, level_ + 1);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && simple_variable_2(builder_, level_ + 1);
    result_ = result_ && variable_name_simple(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // DS | AT
  private static boolean simple_variable_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "simple_variable_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, DS);
    if (!result_) result_ = consumeToken(builder_, AT);
    return result_;
  }

  // (declaration_scope nws)?
  private static boolean simple_variable_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "simple_variable_2")) return false;
    simple_variable_2_0(builder_, level_ + 1);
    return true;
  }

  // declaration_scope nws
  private static boolean simple_variable_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "simple_variable_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = declaration_scope(builder_, level_ + 1);
    result_ = result_ && nws(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
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
  static boolean statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = if_statement(builder_, level_ + 1);
    if (!result_) result_ = statement_1(builder_, level_ + 1);
    if (!result_) result_ = class_declaration_statement(builder_, level_ + 1);
    if (!result_) result_ = enum_declaration_statement(builder_, level_ + 1);
    if (!result_) result_ = function_statement(builder_, level_ + 1);
    if (!result_) result_ = workflow_statement(builder_, level_ + 1);
    if (!result_) result_ = flow_control_statement(builder_, level_ + 1);
    if (!result_) result_ = trap_pipeline(builder_, level_ + 1);
    if (!result_) result_ = try_statement(builder_, level_ + 1);
    if (!result_) result_ = data_statement(builder_, level_ + 1);
    if (!result_) result_ = using_statement(builder_, level_ + 1);
    if (!result_) result_ = inlinescript_statement(builder_, level_ + 1);
    if (!result_) result_ = parallel_statement(builder_, level_ + 1);
    if (!result_) result_ = sequence_statement(builder_, level_ + 1);
    if (!result_) result_ = parse_resource_block(builder_, level_ + 1);
    if (!result_) result_ = parse_node_block(builder_, level_ + 1);
    if (!result_) result_ = pipeline(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PowerShellParser::statement_recover);
    return result_;
  }

  // (label nls?)? labeled_statement
  private static boolean statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_1_0(builder_, level_ + 1);
    result_ = result_ && labeled_statement(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (label nls?)?
  private static boolean statement_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_1_0")) return false;
    statement_1_0_0(builder_, level_ + 1);
    return true;
  }

  // label nls?
  private static boolean statement_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_1_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = label(builder_, level_ + 1);
    result_ = result_ && statement_1_0_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean statement_1_0_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_1_0_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // LCURLY block_body? RCURLY
  static boolean statement_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_block")) return false;
    if (!nextTokenIs(builder_, LCURLY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LCURLY);
    result_ = result_ && statement_block_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RCURLY);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // block_body?
  private static boolean statement_block_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_block_1")) return false;
    block_body(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // !block_body_stop_tokens
  static boolean statement_block_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_block_recover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !block_body_stop_tokens(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // sep | RCURLY | RP | <<eof>>
  static boolean statement_end(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_end")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = sep(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, RCURLY);
    if (!result_) result_ = consumeToken(builder_, RP);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // statement ( statement_terminators statement )*
  static boolean statement_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_list")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = statement(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && statement_list_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, PowerShellParser::top_level_recover);
    return result_ || pinned_;
  }

  // ( statement_terminators statement )*
  private static boolean statement_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!statement_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "statement_list_1", pos_)) break;
    }
    return true;
  }

  // statement_terminators statement
  private static boolean statement_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_terminators(builder_, level_ + 1);
    result_ = result_ && statement(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // <<allow_any_expression statement_list>>
  static boolean statement_list_rule(PsiBuilder builder_, int level_) {
    return allow_any_expression(builder_, level_ + 1, PowerShellParser::statement_list);
  }

  /* ********************************************************** */
  // !statement_stop_tokens
  static boolean statement_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_recover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !statement_stop_tokens(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // reserved_statement_keywords | sep | nls | RCURLY | LCURLY | RP | SQBR_L
  static boolean statement_stop_tokens(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_stop_tokens")) return false;
    boolean result_;
    result_ = reserved_statement_keywords(builder_, level_ + 1);
    if (!result_) result_ = sep(builder_, level_ + 1);
    if (!result_) result_ = nls(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, RCURLY);
    if (!result_) result_ = consumeToken(builder_, LCURLY);
    if (!result_) result_ = consumeToken(builder_, RP);
    if (!result_) result_ = consumeToken(builder_, SQBR_L);
    return result_;
  }

  /* ********************************************************** */
  // sep+
  static boolean statement_terminators(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_terminators")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = sep(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!sep(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "statement_terminators", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // EXPANDABLE_STRING_PART
  static boolean string_literal_entry(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, EXPANDABLE_STRING_PART);
  }

  /* ********************************************************** */
  // expandable_string_literal | verbatim_string_literal | expandable_here_string_literal | verbatim_here_string_literal
  public static boolean string_literal_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_literal_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STRING_LITERAL_EXPRESSION, "<string literal expression>");
    result_ = expandable_string_literal(builder_, level_ + 1);
    if (!result_) result_ = verbatim_string_literal(builder_, level_ + 1);
    if (!result_) result_ = expandable_here_string_literal(builder_, level_ + 1);
    if (!result_) result_ = verbatim_here_string_literal(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // DS nws LP nls? statement_list_rule? nls? RP
  public static boolean sub_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_expression")) return false;
    if (!nextTokenIsFast(builder_, DS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, DS);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LP);
    result_ = result_ && sub_expression_3(builder_, level_ + 1);
    result_ = result_ && sub_expression_4(builder_, level_ + 1);
    result_ = result_ && sub_expression_5(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    exit_section_(builder_, marker_, SUB_EXPRESSION, result_);
    return result_;
  }

  // nls?
  private static boolean sub_expression_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_expression_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // statement_list_rule?
  private static boolean sub_expression_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_expression_4")) return false;
    statement_list_rule(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean sub_expression_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_expression_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? '{' switch_body? '}'
  static boolean switch_block_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_block_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = switch_block_body_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, LCURLY);
    result_ = result_ && switch_block_body_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RCURLY);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean switch_block_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_block_body_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // switch_body?
  private static boolean switch_block_body_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_block_body_2")) return false;
    switch_body(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? switch_clause+ nls? | nls
  public static boolean switch_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_BODY, "<switch body>");
    result_ = switch_body_0(builder_, level_ + 1);
    if (!result_) result_ = nls(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nls? switch_clause+ nls?
  private static boolean switch_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_body_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = switch_body_0_0(builder_, level_ + 1);
    result_ = result_ && switch_body_0_1(builder_, level_ + 1);
    result_ = result_ && switch_body_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean switch_body_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_body_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // switch_clause+
  private static boolean switch_body_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_body_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = switch_clause(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!switch_clause(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "switch_body_0_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean switch_body_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_body_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // switch_clause_condition nls? switch_clause_block statement_terminators?
  static boolean switch_clause(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_clause")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = switch_clause_condition(builder_, level_ + 1);
    result_ = result_ && switch_clause_1(builder_, level_ + 1);
    result_ = result_ && switch_clause_block(builder_, level_ + 1);
    result_ = result_ && switch_clause_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean switch_clause_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_clause_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // statement_terminators?
  private static boolean switch_clause_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_clause_3")) return false;
    statement_terminators(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // statement_block
  public static boolean switch_clause_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_clause_block")) return false;
    if (!nextTokenIs(builder_, LCURLY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, SWITCH_CLAUSE_BLOCK, result_);
    return result_;
  }

  /* ********************************************************** */
  // parse_command_argument | primary_expression
  static boolean switch_clause_condition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_clause_condition")) return false;
    boolean result_;
    result_ = parse_command_argument(builder_, level_ + 1);
    if (!result_) result_ = primary_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // LP nls? pipeline nls? RP | '-file' nls? switch_filename
  static boolean switch_condition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_condition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = switch_condition_0(builder_, level_ + 1);
    if (!result_) result_ = switch_condition_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LP nls? pipeline nls? RP
  private static boolean switch_condition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_condition_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, LP);
    result_ = result_ && switch_condition_0_1(builder_, level_ + 1);
    result_ = result_ && pipeline(builder_, level_ + 1);
    result_ = result_ && switch_condition_0_3(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RP);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean switch_condition_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_condition_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean switch_condition_0_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_condition_0_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // '-file' nls? switch_filename
  private static boolean switch_condition_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_condition_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, "-file");
    result_ = result_ && switch_condition_1_1(builder_, level_ + 1);
    result_ = result_ && switch_filename(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean switch_condition_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_condition_1_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // parse_command_argument | primary_expression
  static boolean switch_filename(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_filename")) return false;
    boolean result_;
    result_ = parse_command_argument(builder_, level_ + 1);
    if (!result_) result_ = primary_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '-regex'
  //                              | '-wildcard'
  //                              | '-exact'
  //                              | '-casesensitive'
  //                              | '-parallel'
  static boolean switch_parameter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_parameter")) return false;
    boolean result_;
    result_ = consumeToken(builder_, "-regex");
    if (!result_) result_ = consumeToken(builder_, "-wildcard");
    if (!result_) result_ = consumeToken(builder_, "-exact");
    if (!result_) result_ = consumeToken(builder_, "-casesensitive");
    if (!result_) result_ = consumeToken(builder_, "-parallel");
    return result_;
  }

  /* ********************************************************** */
  // 'switch' nls? switch_parameter* switch_condition switch_block_body
  public static boolean switch_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_statement")) return false;
    if (!nextTokenIs(builder_, SWITCH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SWITCH_STATEMENT, null);
    result_ = consumeToken(builder_, SWITCH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, switch_statement_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, switch_statement_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, switch_condition(builder_, level_ + 1)) && result_;
    result_ = pinned_ && switch_block_body(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean switch_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // switch_parameter*
  private static boolean switch_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_statement_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!switch_parameter(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "switch_statement_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // variable
  public static boolean target_variable_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "target_variable_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TARGET_VARIABLE_EXPRESSION, "<target variable expression>");
    result_ = variable(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '-throttlelimit' primary_expression
  static boolean throttlelimit_parameter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "throttlelimit_parameter")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, "-throttlelimit");
    result_ = result_ && primary_expression(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // ('throw' | 'return' | 'exit') pipeline?
  static boolean throw_return_exit_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "throw_return_exit_statement")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = throw_return_exit_statement_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && throw_return_exit_statement_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'throw' | 'return' | 'exit'
  private static boolean throw_return_exit_statement_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "throw_return_exit_statement_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, THROW);
    if (!result_) result_ = consumeToken(builder_, RETURN);
    if (!result_) result_ = consumeToken(builder_, EXIT);
    return result_;
  }

  // pipeline?
  private static boolean throw_return_exit_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "throw_return_exit_statement_1")) return false;
    pipeline(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // sep* ( top_script_block | statement_list | configuration_block_list )?
  static boolean top_level_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_level_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = top_level_element_0(builder_, level_ + 1);
    result_ = result_ && top_level_element_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PowerShellParser::top_level_recover);
    return result_;
  }

  // sep*
  private static boolean top_level_element_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_level_element_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!sep(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "top_level_element_0", pos_)) break;
    }
    return true;
  }

  // ( top_script_block | statement_list | configuration_block_list )?
  private static boolean top_level_element_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_level_element_1")) return false;
    top_level_element_1_0(builder_, level_ + 1);
    return true;
  }

  // top_script_block | statement_list | configuration_block_list
  private static boolean top_level_element_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_level_element_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = top_script_block(builder_, level_ + 1);
    if (!result_) result_ = statement_list(builder_, level_ + 1);
    if (!result_) result_ = configuration_block_list(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // !(reserved_statement_keywords | sep | nls | RCURLY | LCURLY | RP | SQBR_L)
  static boolean top_level_recover(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_level_recover")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !top_level_recover_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // reserved_statement_keywords | sep | nls | RCURLY | LCURLY | RP | SQBR_L
  private static boolean top_level_recover_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_level_recover_0")) return false;
    boolean result_;
    result_ = reserved_statement_keywords(builder_, level_ + 1);
    if (!result_) result_ = sep(builder_, level_ + 1);
    if (!result_) result_ = nls(builder_, level_ + 1);
    if (!result_) result_ = consumeTokenFast(builder_, RCURLY);
    if (!result_) result_ = consumeTokenFast(builder_, LCURLY);
    if (!result_) result_ = consumeTokenFast(builder_, RP);
    if (!result_) result_ = consumeTokenFast(builder_, SQBR_L);
    return result_;
  }

  /* ********************************************************** */
  // nls? top_script_block_content nls? | nls
  static boolean top_script_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_script_block")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = top_script_block_0(builder_, level_ + 1);
    if (!result_) result_ = nls(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls? top_script_block_content nls?
  private static boolean top_script_block_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_script_block_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = top_script_block_0_0(builder_, level_ + 1);
    result_ = result_ && top_script_block_content(builder_, level_ + 1);
    result_ = result_ && top_script_block_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean top_script_block_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_script_block_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean top_script_block_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_script_block_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // block_parameter_clause? statement_terminators? script_block_body | block_parameter_clause statement_terminators?
  static boolean top_script_block_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_script_block_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = top_script_block_content_0(builder_, level_ + 1);
    if (!result_) result_ = top_script_block_content_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // block_parameter_clause? statement_terminators? script_block_body
  private static boolean top_script_block_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_script_block_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = top_script_block_content_0_0(builder_, level_ + 1);
    result_ = result_ && top_script_block_content_0_1(builder_, level_ + 1);
    result_ = result_ && script_block_body(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // block_parameter_clause?
  private static boolean top_script_block_content_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_script_block_content_0_0")) return false;
    block_parameter_clause(builder_, level_ + 1);
    return true;
  }

  // statement_terminators?
  private static boolean top_script_block_content_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_script_block_content_0_1")) return false;
    statement_terminators(builder_, level_ + 1);
    return true;
  }

  // block_parameter_clause statement_terminators?
  private static boolean top_script_block_content_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_script_block_content_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = block_parameter_clause(builder_, level_ + 1);
    result_ = result_ && top_script_block_content_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // statement_terminators?
  private static boolean top_script_block_content_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "top_script_block_content_1_1")) return false;
    statement_terminators(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // trap_statement statement?
  static boolean trap_pipeline(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trap_pipeline")) return false;
    if (!nextTokenIs(builder_, TRAP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = trap_statement(builder_, level_ + 1);
    result_ = result_ && trap_pipeline_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // statement?
  private static boolean trap_pipeline_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trap_pipeline_1")) return false;
    statement(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'trap' nls? type_literal_expression? nls? statement_block
  public static boolean trap_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trap_statement")) return false;
    if (!nextTokenIs(builder_, TRAP)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TRAP_STATEMENT, null);
    result_ = consumeToken(builder_, TRAP);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, trap_statement_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, trap_statement_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, trap_statement_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && statement_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean trap_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trap_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // type_literal_expression?
  private static boolean trap_statement_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trap_statement_2")) return false;
    type_literal_expression(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean trap_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trap_statement_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'try' nls? statement_block (nls? catch_clauses)? (nls? finally_clause)?
  public static boolean try_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_statement")) return false;
    if (!nextTokenIs(builder_, TRY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TRY_STATEMENT, null);
    result_ = consumeToken(builder_, TRY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, try_statement_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, statement_block(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, try_statement_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && try_statement_4(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean try_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // (nls? catch_clauses)?
  private static boolean try_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_statement_3")) return false;
    try_statement_3_0(builder_, level_ + 1);
    return true;
  }

  // nls? catch_clauses
  private static boolean try_statement_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_statement_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = try_statement_3_0_0(builder_, level_ + 1);
    result_ = result_ && catch_clauses(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean try_statement_3_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_statement_3_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // (nls? finally_clause)?
  private static boolean try_statement_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_statement_4")) return false;
    try_statement_4_0(builder_, level_ + 1);
    return true;
  }

  // nls? finally_clause
  private static boolean try_statement_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_statement_4_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = try_statement_4_0_0(builder_, level_ + 1);
    result_ = result_ && finally_clause(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean try_statement_4_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_statement_4_0_0")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // array_type_element | generic_type_element | reference_type_element
  public static boolean type_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, TYPE_ELEMENT, "<type element>");
    result_ = array_type_element(builder_, level_ + 1);
    if (!result_) result_ = generic_type_element(builder_, level_ + 1);
    if (!result_) result_ = reference_type_element(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // SQBR_L type_element SQBR_R
  public static boolean type_literal_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_literal_expression")) return false;
    if (!nextTokenIsFast(builder_, SQBR_L)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenFast(builder_, SQBR_L);
    result_ = result_ && type_element(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SQBR_R);
    exit_section_(builder_, marker_, TYPE_LITERAL_EXPRESSION, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'using' nls? pipeline
  public static boolean using_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "using_statement")) return false;
    if (!nextTokenIs(builder_, USING)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, USING_STATEMENT, null);
    result_ = consumeToken(builder_, USING);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, using_statement_1(builder_, level_ + 1));
    result_ = pinned_ && pipeline(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // nls?
  private static boolean using_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "using_statement_1")) return false;
    nls(builder_, level_ + 1);
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
  static boolean value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value")) return false;
    boolean result_;
    result_ = parenthesized_expression(builder_, level_ + 1);
    if (!result_) result_ = sub_expression(builder_, level_ + 1);
    if (!result_) result_ = array_expression(builder_, level_ + 1);
    if (!result_) result_ = script_block_expression(builder_, level_ + 1);
    if (!result_) result_ = hash_literal_expression(builder_, level_ + 1);
    if (!result_) result_ = literal(builder_, level_ + 1);
    if (!result_) result_ = cast_expression(builder_, level_ + 1);
    if (!result_) result_ = type_literal_expression(builder_, level_ + 1);
    if (!result_) result_ = target_variable_expression(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // DS nws predefined_var_id | simple_variable | braced_variable
  static boolean variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = variable_0(builder_, level_ + 1);
    if (!result_) result_ = simple_variable(builder_, level_ + 1);
    if (!result_) result_ = braced_variable(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // DS nws predefined_var_id
  private static boolean variable_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, DS);
    result_ = result_ && nws(builder_, level_ + 1);
    result_ = result_ && predefined_var_id(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // BRACED_ID
  public static boolean variable_name_braced(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_name_braced")) return false;
    if (!nextTokenIs(builder_, BRACED_ID)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BRACED_ID);
    exit_section_(builder_, marker_, IDENTIFIER, result_);
    return result_;
  }

  /* ********************************************************** */
  // SIMPLE_ID | VAR_ID | 'this'
  public static boolean variable_name_simple(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_name_simple")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IDENTIFIER, "<variable name simple>");
    result_ = consumeToken(builder_, SIMPLE_ID);
    if (!result_) result_ = consumeToken(builder_, VAR_ID);
    if (!result_) result_ = consumeToken(builder_, THIS);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // SIMPLE_ID ':'
  static boolean variable_namespace(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_namespace")) return false;
    if (!nextTokenIs(builder_, SIMPLE_ID)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, SIMPLE_ID, COLON);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // VERBATIM_ARG_START VERBATIM_ARG_INPUT
  public static boolean verbatim_command_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "verbatim_command_argument")) return false;
    if (!nextTokenIs(builder_, VERBATIM_ARG_START)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, VERBATIM_ARG_START, VERBATIM_ARG_INPUT);
    exit_section_(builder_, marker_, VERBATIM_COMMAND_ARGUMENT, result_);
    return result_;
  }

  /* ********************************************************** */
  // VERBATIM_HERE_STRING
  static boolean verbatim_here_string_literal(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, VERBATIM_HERE_STRING);
  }

  /* ********************************************************** */
  // VERBATIM_STRING
  static boolean verbatim_string_literal(PsiBuilder builder_, int level_) {
    return consumeToken(builder_, VERBATIM_STRING);
  }

  /* ********************************************************** */
  // pipeline
  static boolean while_condition(PsiBuilder builder_, int level_) {
    return pipeline(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // 'while' nls? LP nls? while_condition nls? RP nls? statement_block
  public static boolean while_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_statement")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WHILE_STATEMENT, "<while statement>");
    result_ = consumeToken(builder_, WHILE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, while_statement_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, LP)) && result_;
    result_ = pinned_ && report_error_(builder_, while_statement_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, while_condition(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, while_statement_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, RP)) && result_;
    result_ = pinned_ && report_error_(builder_, while_statement_7(builder_, level_ + 1)) && result_;
    result_ = pinned_ && statement_block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, PowerShellParser::statement_recover);
    return result_ || pinned_;
  }

  // nls?
  private static boolean while_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean while_statement_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_statement_3")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean while_statement_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_statement_5")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // nls?
  private static boolean while_statement_7(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_statement_7")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'workflow' nls? function_statement_tail
  public static boolean workflow_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "workflow_statement")) return false;
    if (!nextTokenIs(builder_, WORKFLOW)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, WORKFLOW);
    result_ = result_ && workflow_statement_1(builder_, level_ + 1);
    result_ = result_ && function_statement_tail(builder_, level_ + 1);
    exit_section_(builder_, marker_, FUNCTION_STATEMENT, result_);
    return result_;
  }

  // nls?
  private static boolean workflow_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "workflow_statement_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<isWhiteSpace>>
  static boolean ws(PsiBuilder builder_, int level_) {
    return isWhiteSpace(builder_, level_ + 1);
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
  public static boolean expression(PsiBuilder builder_, int level_, int priority_) {
    if (!recursion_guard_(builder_, level_, "expression")) return false;
    addVariant(builder_, "<expression>");
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, "<expression>");
    result_ = unary_expression(builder_, level_ + 1);
    pinned_ = result_;
    result_ = result_ && expression_0(builder_, level_ + 1, priority_);
    exit_section_(builder_, level_, marker_, null, result_, pinned_, null);
    return result_ || pinned_;
  }

  public static boolean expression_0(PsiBuilder builder_, int level_, int priority_) {
    if (!recursion_guard_(builder_, level_, "expression_0")) return false;
    boolean result_ = true;
    while (true) {
      Marker marker_ = enter_section_(builder_, level_, _LEFT_, null);
      if (priority_ < 0 && assignment_expression_0(builder_, level_ + 1)) {
        result_ = true;
        exit_section_(builder_, level_, marker_, ASSIGNMENT_EXPRESSION, result_, true, null);
      }
      else if (priority_ < 1 && logical_expression_0(builder_, level_ + 1)) {
        result_ = expression(builder_, level_, 1);
        exit_section_(builder_, level_, marker_, LOGICAL_EXPRESSION, result_, true, null);
      }
      else if (priority_ < 2 && bitwise_expression_0(builder_, level_ + 1)) {
        result_ = expression(builder_, level_, 2);
        exit_section_(builder_, level_, marker_, BITWISE_EXPRESSION, result_, true, null);
      }
      else if (priority_ < 3 && comparison_expression_0(builder_, level_ + 1)) {
        result_ = expression(builder_, level_, 3);
        exit_section_(builder_, level_, marker_, COMPARISON_EXPRESSION, result_, true, null);
      }
      else if (priority_ < 4 && additive_expression_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expression(builder_, level_, 4));
          if (!additive_expression_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, ADDITIVE_EXPRESSION, result_, true, null);
      }
      else if (priority_ < 5 && multiplicative_expression_0(builder_, level_ + 1)) {
        result_ = expression(builder_, level_, 5);
        exit_section_(builder_, level_, marker_, MULTIPLICATIVE_EXPRESSION, result_, true, null);
      }
      else if (priority_ < 6 && format_expression_0(builder_, level_ + 1)) {
        result_ = expression(builder_, level_, 6);
        exit_section_(builder_, level_, marker_, FORMAT_EXPRESSION, result_, true, null);
      }
      else if (priority_ < 7 && range_expression_0(builder_, level_ + 1)) {
        result_ = expression(builder_, level_, 7);
        exit_section_(builder_, level_, marker_, RANGE_EXPRESSION, result_, true, null);
      }
      else if (priority_ < 8 && array_literal_expression_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expression(builder_, level_, 8));
          if (!array_literal_expression_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, ARRAY_LITERAL_EXPRESSION, result_, true, null);
      }
      else {
        exit_section_(builder_, level_, marker_, null, false, false, null);
        break;
      }
    }
    return result_;
  }

  // (!is_in_call_arguments assignment_operator) nls? statement
  private static boolean assignment_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assignment_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = assignment_expression_0_0(builder_, level_ + 1);
    result_ = result_ && assignment_expression_0_1(builder_, level_ + 1);
    result_ = result_ && statement(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !is_in_call_arguments assignment_operator
  private static boolean assignment_expression_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assignment_expression_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = assignment_expression_0_0_0(builder_, level_ + 1);
    result_ = result_ && assignment_operator(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !is_in_call_arguments
  private static boolean assignment_expression_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assignment_expression_0_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !is_in_call_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nls?
  private static boolean assignment_expression_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assignment_expression_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // ( OP_AND | OP_OR | OP_XOR ) nls?
  private static boolean logical_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = logical_expression_0_0(builder_, level_ + 1);
    result_ = result_ && logical_expression_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // OP_AND | OP_OR | OP_XOR
  private static boolean logical_expression_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_expression_0_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OP_AND);
    if (!result_) result_ = consumeTokenSmart(builder_, OP_OR);
    if (!result_) result_ = consumeTokenSmart(builder_, OP_XOR);
    return result_;
  }

  // nls?
  private static boolean logical_expression_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "logical_expression_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // (OP_BAND | OP_BOR | OP_BXOR ) nls?
  private static boolean bitwise_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitwise_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = bitwise_expression_0_0(builder_, level_ + 1);
    result_ = result_ && bitwise_expression_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // OP_BAND | OP_BOR | OP_BXOR
  private static boolean bitwise_expression_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitwise_expression_0_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OP_BAND);
    if (!result_) result_ = consumeTokenSmart(builder_, OP_BOR);
    if (!result_) result_ = consumeTokenSmart(builder_, OP_BXOR);
    return result_;
  }

  // nls?
  private static boolean bitwise_expression_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitwise_expression_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // comparison_operator nls?
  private static boolean comparison_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comparison_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = comparison_operator(builder_, level_ + 1);
    result_ = result_ && comparison_expression_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean comparison_expression_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comparison_expression_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // ( PLUS | DASH ) nls?
  private static boolean additive_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = additive_expression_0_0(builder_, level_ + 1);
    result_ = result_ && additive_expression_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // PLUS | DASH
  private static boolean additive_expression_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expression_0_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, PLUS);
    if (!result_) result_ = consumeTokenSmart(builder_, DASH);
    return result_;
  }

  // nls?
  private static boolean additive_expression_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "additive_expression_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // ( STAR | DIV | PERS) nls?
  private static boolean multiplicative_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = multiplicative_expression_0_0(builder_, level_ + 1);
    result_ = result_ && multiplicative_expression_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // STAR | DIV | PERS
  private static boolean multiplicative_expression_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expression_0_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, STAR);
    if (!result_) result_ = consumeTokenSmart(builder_, DIV);
    if (!result_) result_ = consumeTokenSmart(builder_, PERS);
    return result_;
  }

  // nls?
  private static boolean multiplicative_expression_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "multiplicative_expression_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // format_operator nls?
  private static boolean format_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "format_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = format_operator(builder_, level_ + 1);
    result_ = result_ && format_expression_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean format_expression_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "format_expression_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // DOT_DOT nls?
  private static boolean range_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenSmart(builder_, DOT_DOT);
    result_ = result_ && range_expression_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // nls?
  private static boolean range_expression_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "range_expression_0_1")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // !is_in_call_arguments COMMA nls?
  private static boolean array_literal_expression_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_literal_expression_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = array_literal_expression_0_0(builder_, level_ + 1);
    result_ = result_ && consumeTokenSmart(builder_, COMMA);
    result_ = result_ && array_literal_expression_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !is_in_call_arguments
  private static boolean array_literal_expression_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_literal_expression_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !is_in_call_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // nls?
  private static boolean array_literal_expression_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_literal_expression_0_2")) return false;
    nls(builder_, level_ + 1);
    return true;
  }

  // expression_with_unary_operator | primary_expression
  public static boolean unary_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, UNARY_EXPRESSION, "<unary expression>");
    result_ = expression_with_unary_operator(builder_, level_ + 1);
    if (!result_) result_ = primary_expression(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

}
