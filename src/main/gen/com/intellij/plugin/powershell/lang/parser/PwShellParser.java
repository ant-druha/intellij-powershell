// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.*;

;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PwShellParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == ADDITIVE_EXPRESSION) {
      r = additive_expression(b, 0);
    } else if (t == ASSIGNMENT_EXPRESSION) {
      r = assignment_expression(b, 0);
    } else if (t == BITWISE_EXPRESSION) {
      r = bitwise_expression(b, 0);
    } else if (t == COMMAND) {
      r = command(b, 0);
    } else if (t == COMMAND_ARGUMENT) {
      r = command_argument(b, 0);
    } else if (t == COMMAND_NAME) {
      r = command_name(b, 0);
    } else if (t == COMMAND_PARAMETER) {
      r = command_parameter(b, 0);
    } else if (t == COMPARISON_EXPRESSION) {
      r = comparison_expression(b, 0);
    } else if (t == DATA_STATEMENT) {
      r = data_statement(b, 0);
    } else if (t == EXPRESSION) {
      r = expression(b, 0);
    } else if (t == EXPRESSION_WITH_UNARY_OPERATOR) {
      r = expression_with_unary_operator(b, 0);
    } else if (t == FLOW_CONTROL_STATEMENT) {
      r = flow_control_statement(b, 0);
    } else if (t == FORMAT_EXPRESSION) {
      r = format_expression(b, 0);
    } else if (t == FUNCTION_STATEMENT) {
      r = function_statement(b, 0);
    } else if (t == INLINESCRIPT_STATEMENT) {
      r = inlinescript_statement(b, 0);
    } else if (t == INTEGER_LITERAL) {
      r = integer_literal(b, 0);
    } else if (t == LOGICAL_EXPRESSION) {
      r = logical_expression(b, 0);
    } else if (t == MULTIPLICATIVE_EXPRESSION) {
      r = multiplicative_expression(b, 0);
    } else if (t == PARALLEL_STATEMENT) {
      r = parallel_statement(b, 0);
    } else if (t == PARENTHESIZED_EXPRESSION) {
      r = parenthesized_expression(b, 0);
    } else if (t == PIPELINE) {
      r = pipeline(b, 0);
    } else if (t == PRIMARY_EXPRESSION) {
      r = primary_expression(b, 0);
    } else if (t == RANGE_EXPRESSION) {
      r = range_expression(b, 0);
    } else if (t == REAL_LITERAL) {
      r = real_literal(b, 0);
    } else if (t == SCRIPT_BLOCK) {
      r = script_block(b, 0);
    } else if (t == SEP) {
      r = sep(b, 0);
    } else if (t == SEQUENCE_STATEMENT) {
      r = sequence_statement(b, 0);
    } else if (t == SIMPLE_NAME) {
      r = simple_name(b, 0);
    } else if (t == STATEMENT_BLOCK) {
      r = statement_block(b, 0);
    } else if (t == STRING_LITERAL) {
      r = string_literal(b, 0);
    } else if (t == TRAP_STATEMENT) {
      r = trap_statement(b, 0);
    } else if (t == TRY_STATEMENT) {
      r = try_statement(b, 0);
    } else if (t == TYPE_LITERAL) {
      r = type_literal(b, 0);
    } else if (t == TYPE_NAME) {
      r = type_name(b, 0);
    } else if (t == UNARY_EXPRESSION) {
      r = unary_expression(b, 0);
    } else if (t == VARIABLE) {
      r = variable(b, 0);
    } else if (t == VERBATIM_COMMAND_ARGUMENT) {
      r = verbatim_command_argument(b, 0);
    } else if (t == WHILE_STATEMENT) {
      r = while_statement(b, 0);
    } else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return top_level_element(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[]{
      create_token_set_(ADDITIVE_EXPRESSION, ASSIGNMENT_EXPRESSION, BITWISE_EXPRESSION, COMPARISON_EXPRESSION,
          EXPRESSION, FORMAT_EXPRESSION, LOGICAL_EXPRESSION, MULTIPLICATIVE_EXPRESSION,
          PARENTHESIZED_EXPRESSION, PRIMARY_EXPRESSION, RANGE_EXPRESSION, UNARY_EXPRESSION),
  };

  /* ********************************************************** */
  // multiplicative_expression ( ( '+' | '-' ) nls? multiplicative_expression )*
  public static boolean additive_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, ADDITIVE_EXPRESSION, "<additive expression>");
    r = multiplicative_expression(b, l + 1);
    r = r && additive_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ( '+' | '-' ) nls? multiplicative_expression )*
  private static boolean additive_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!additive_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "additive_expression_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ( '+' | '-' ) nls? multiplicative_expression
  private static boolean additive_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = additive_expression_1_0_0(b, l + 1);
    r = r && additive_expression_1_0_1(b, l + 1);
    r = r && multiplicative_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '+' | '-'
  private static boolean additive_expression_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, "+");
    if (!r) r = consumeTokenFast(b, "-");
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean additive_expression_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "additive_expression_1_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // expression ( ',' expression)*
  static boolean argument_expression_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_expression_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_INNER_);
    r = expression(b, l + 1);
    r = r && argument_expression_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ',' expression)*
  private static boolean argument_expression_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_expression_list_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!argument_expression_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "argument_expression_list_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' expression
  private static boolean argument_expression_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_expression_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // AT LP statement_list? RP
  static boolean array_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expression")) return false;
    if (!nextTokenIsFast(b, AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, AT, LP);
    r = r && array_expression_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_list?
  private static boolean array_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_expression_2")) return false;
    statement_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // unary_expression ( ',' nls? unary_expression )*
  static boolean array_literal_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_literal_expression")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = unary_expression(b, l + 1);
    r = r && array_literal_expression_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' nls? unary_expression )*
  private static boolean array_literal_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_literal_expression_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!array_literal_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_literal_expression_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' nls? unary_expression
  private static boolean array_literal_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_literal_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, COMMA);
    r = r && array_literal_expression_1_0_1(b, l + 1);
    r = r && unary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean array_literal_expression_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_literal_expression_1_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // type_name '['
  static boolean array_type_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_name")) return false;
    if (!nextTokenIs(b, "", TYPE_NAME, VAR_SIMPLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_name(b, l + 1);
    r = r && consumeToken(b, "[");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expression assignment_operator statement
  public static boolean assignment_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, ASSIGNMENT_EXPRESSION, "<assignment expression>");
    r = expression(b, l + 1);
    r = r && assignment_operator(b, l + 1);
    r = r && statement(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '=' | '-=' | '+=' | '*=' | '/='| '%='
  static boolean assignment_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_operator")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    if (!r) r = consumeToken(b, "-=");
    if (!r) r = consumeToken(b, "+=");
    if (!r) r = consumeToken(b, "*=");
    if (!r) r = consumeToken(b, "/=");
    if (!r) r = consumeToken(b, "%=");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '[' nls? attribute_name LP attribute_arguments nls? RP nls? ']' | type_literal
  static boolean attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_0(b, l + 1);
    if (!r) r = type_literal(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '[' nls? attribute_name LP attribute_arguments nls? RP nls? ']'
  private static boolean attribute_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "[");
    r = r && attribute_0_1(b, l + 1);
    r = r && attribute_name(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && attribute_arguments(b, l + 1);
    r = r && attribute_0_5(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && attribute_0_7(b, l + 1);
    r = r && consumeToken(b, "]");
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
  private static boolean attribute_0_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0_5")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean attribute_0_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0_7")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? ( expression | simple_name | simple_name '=' nls? expression )
  static boolean attribute_argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_argument")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_argument_0(b, l + 1);
    r = r && attribute_argument_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean attribute_argument_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_argument_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // expression | simple_name | simple_name '=' nls? expression
  private static boolean attribute_argument_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_argument_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1);
    if (!r) r = simple_name(b, l + 1);
    if (!r) r = attribute_argument_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // simple_name '=' nls? expression
  private static boolean attribute_argument_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_argument_1_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = simple_name(b, l + 1);
    r = r && consumeToken(b, EQ);
    r = r && attribute_argument_1_2_2(b, l + 1);
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean attribute_argument_1_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_argument_1_2_2")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // attribute_argument (nls? attribute_argument)*
  static boolean attribute_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_arguments")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_argument(b, l + 1);
    r = r && attribute_arguments_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nls? attribute_argument)*
  private static boolean attribute_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_arguments_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!attribute_arguments_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "attribute_arguments_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // nls? attribute_argument
  private static boolean attribute_arguments_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_arguments_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_arguments_1_0_0(b, l + 1);
    r = r && attribute_argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean attribute_arguments_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_arguments_1_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (attribute nls?)+
  static boolean attribute_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_list_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!attribute_list_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "attribute_list", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // attribute nls?
  private static boolean attribute_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_list_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute(b, l + 1);
    r = r && attribute_list_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean attribute_list_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_list_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // type_spec
  static boolean attribute_name(PsiBuilder b, int l) {
    return type_spec(b, l + 1);
  }

  /* ********************************************************** */
  // comparison_expression ( ('-band' | '-bor' | '-bxor' )  nls? comparison_expression )*
  public static boolean bitwise_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, BITWISE_EXPRESSION, "<bitwise expression>");
    r = comparison_expression(b, l + 1);
    r = r && bitwise_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ('-band' | '-bor' | '-bxor' )  nls? comparison_expression )*
  private static boolean bitwise_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_expression_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!bitwise_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bitwise_expression_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ('-band' | '-bor' | '-bxor' )  nls? comparison_expression
  private static boolean bitwise_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = bitwise_expression_1_0_0(b, l + 1);
    r = r && bitwise_expression_1_0_1(b, l + 1);
    r = r && comparison_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '-band' | '-bor' | '-bxor'
  private static boolean bitwise_expression_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_expression_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, "-band");
    if (!r) r = consumeTokenFast(b, "-bor");
    if (!r) r = consumeTokenFast(b, "-bxor");
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean bitwise_expression_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitwise_expression_1_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'dynamicparam' | 'begin' | 'process' | 'end'
  static boolean block_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_name")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DYNAMICPARAM);
    if (!r) r = consumeToken(b, BEGIN);
    if (!r) r = consumeToken(b, PROCESS);
    if (!r) r = consumeToken(b, END);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DS LCURLY variable_name_braced RCURLY
  static boolean braced_variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "braced_variable")) return false;
    if (!nextTokenIs(b, DS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DS, LCURLY);
    r = r && variable_name_braced(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // type_literal unary_expression
  static boolean cast_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cast_expression")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_literal(b, l + 1);
    r = r && unary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // nls? 'catch' catch_type_list? statement_block
  static boolean catch_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clause")) return false;
    if (!nextTokenIs(b, "", CATCH, NLS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = catch_clause_0(b, l + 1);
    r = r && consumeToken(b, CATCH);
    r = r && catch_clause_2(b, l + 1);
    r = r && statement_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean catch_clause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clause_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // catch_type_list?
  private static boolean catch_clause_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_clause_2")) return false;
    catch_type_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? type_literal nls? ( ',' nls? type_literal nls? )*
  static boolean catch_type_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = catch_type_list_0(b, l + 1);
    r = r && type_literal(b, l + 1);
    r = r && catch_type_list_2(b, l + 1);
    r = r && catch_type_list_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean catch_type_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean catch_type_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_2")) return false;
    nls(b, l + 1);
    return true;
  }

  // ( ',' nls? type_literal nls? )*
  private static boolean catch_type_list_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_3")) return false;
    int c = current_position_(b);
    while (true) {
      if (!catch_type_list_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "catch_type_list_3", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' nls? type_literal nls?
  private static boolean catch_type_list_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && catch_type_list_3_0_1(b, l + 1);
    r = r && type_literal(b, l + 1);
    r = r && catch_type_list_3_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean catch_type_list_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_3_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean catch_type_list_3_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "catch_type_list_3_0_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // command_invocation_operator? command_module? command_name_expr command_element*
  public static boolean command(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMAND, "<command>");
    r = command_0(b, l + 1);
    r = r && command_1(b, l + 1);
    r = r && command_name_expr(b, l + 1);
    r = r && command_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // command_invocation_operator?
  private static boolean command_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_0")) return false;
    command_invocation_operator(b, l + 1);
    return true;
  }

  // command_module?
  private static boolean command_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_1")) return false;
    command_module(b, l + 1);
    return true;
  }

  // command_element*
  private static boolean command_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_3")) return false;
    int c = current_position_(b);
    while (true) {
      if (!command_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "command_3", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // command_name_expr
  public static boolean command_argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_argument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMAND_ARGUMENT, "<command argument>");
    r = command_name_expr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // command_parameter | command_argument | redirection
  static boolean command_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_element")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_parameter(b, l + 1);
    if (!r) r = command_argument(b, l + 1);
    if (!r) r = redirection(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '&' | '.'
  static boolean command_invocation_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_invocation_operator")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AMP);
    if (!r) r = consumeToken(b, ".");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // primary_expression
  static boolean command_module(PsiBuilder b, int l) {
    return primary_expression(b, l + 1);
  }

  /* ********************************************************** */
  // generic_token
  public static boolean command_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMAND_NAME, "<command name>");
    r = generic_token(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // primary_expression | command_name
  static boolean command_name_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_name_expr")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = primary_expression(b, l + 1);
    if (!r) r = command_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PARAM_TOKEN
  public static boolean command_parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "command_parameter")) return false;
    if (!nextTokenIs(b, PARAM_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PARAM_TOKEN);
    exit_section_(b, m, COMMAND_PARAMETER, r);
    return r;
  }

  /* ********************************************************** */
  // additive_expression ( comparison_operator nls? additive_expression )*
  public static boolean comparison_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, COMPARISON_EXPRESSION, "<comparison expression>");
    r = additive_expression(b, l + 1);
    r = r && comparison_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( comparison_operator nls? additive_expression )*
  private static boolean comparison_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_expression_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!comparison_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "comparison_expression_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // comparison_operator nls? additive_expression
  private static boolean comparison_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = comparison_operator(b, l + 1);
    r = r && comparison_expression_1_0_1(b, l + 1);
    r = r && additive_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean comparison_expression_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comparison_expression_1_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // OP_C
  static boolean comparison_operator(PsiBuilder b, int l) {
    return consumeToken(b, OP_C);
  }

  /* ********************************************************** */
  // command_name_expr
  static boolean data_command(PsiBuilder b, int l) {
    return command_name_expr(b, l + 1);
  }

  /* ********************************************************** */
  // nls? '-supportedcommand' data_commands_list
  static boolean data_commands_allowed(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_allowed")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = data_commands_allowed_0(b, l + 1);
    r = r && consumeToken(b, "-supportedcommand");
    r = r && data_commands_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean data_commands_allowed_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_allowed_0")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? data_command ( ',' nls? data_command nls? )*
  static boolean data_commands_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = data_commands_list_0(b, l + 1);
    r = r && data_command(b, l + 1);
    r = r && data_commands_list_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean data_commands_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // ( ',' nls? data_command nls? )*
  private static boolean data_commands_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!data_commands_list_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "data_commands_list_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' nls? data_command nls?
  private static boolean data_commands_list_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && data_commands_list_2_0_1(b, l + 1);
    r = r && data_command(b, l + 1);
    r = r && data_commands_list_2_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean data_commands_list_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list_2_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean data_commands_list_2_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_commands_list_2_0_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // simple_name
  static boolean data_name(PsiBuilder b, int l) {
    return simple_name(b, l + 1);
  }

  /* ********************************************************** */
  // 'data' nls? data_name data_commands_allowed? statement_block
  public static boolean data_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_statement")) return false;
    if (!nextTokenIs(b, DATA)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DATA);
    r = r && data_statement_1(b, l + 1);
    r = r && data_name(b, l + 1);
    r = r && data_statement_3(b, l + 1);
    r = r && statement_block(b, l + 1);
    exit_section_(b, m, DATA_STATEMENT, r);
    return r;
  }

  // nls?
  private static boolean data_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // data_commands_allowed?
  private static boolean data_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "data_statement_3")) return false;
    data_commands_allowed(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ','+
  static boolean dimension(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dimension")) return false;
    if (!nextTokenIs(b, COMMA)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    int c = current_position_(b);
    while (r) {
      if (!consumeToken(b, COMMA)) break;
      if (!empty_element_parsed_guard_(b, "dimension", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'do' statement_block nls? ( 'while' | 'until' ) nls? LP while_condition nls? RP
  static boolean do_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement")) return false;
    if (!nextTokenIs(b, DO)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DO);
    r = r && statement_block(b, l + 1);
    r = r && do_statement_2(b, l + 1);
    r = r && do_statement_3(b, l + 1);
    r = r && do_statement_4(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && while_condition(b, l + 1);
    r = r && do_statement_7(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean do_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement_2")) return false;
    nls(b, l + 1);
    return true;
  }

  // 'while' | 'until'
  private static boolean do_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHILE);
    if (!r) r = consumeToken(b, UNTIL);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean do_statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement_4")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean do_statement_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_statement_7")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '[' nls? expression nls? ']'
  static boolean element_access(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_access")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_INNER_);
    r = consumeToken(b, "[");
    r = r && element_access_1(b, l + 1);
    r = r && expression(b, l + 1);
    r = r && element_access_3(b, l + 1);
    r = r && consumeToken(b, "]");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // nls?
  private static boolean element_access_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_access_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean element_access_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "element_access_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? 'else' statement_block
  static boolean else_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "else_clause")) return false;
    if (!nextTokenIs(b, "", ELSE, NLS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = else_clause_0(b, l + 1);
    r = r && consumeToken(b, ELSE);
    r = r && statement_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean else_clause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "else_clause_0")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? 'elseif' nls? LP nls? pipeline nls? RP statement_block
  static boolean elseif_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause")) return false;
    if (!nextTokenIs(b, "", ELSEIF, NLS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = elseif_clause_0(b, l + 1);
    r = r && consumeToken(b, ELSEIF);
    r = r && elseif_clause_2(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && elseif_clause_4(b, l + 1);
    r = r && pipeline(b, l + 1);
    r = r && elseif_clause_6(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && statement_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean elseif_clause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean elseif_clause_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause_2")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean elseif_clause_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause_4")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean elseif_clause_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elseif_clause_6")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // EXPANDABLE_HERE_STRING
  static boolean expandable_here_string_literal(PsiBuilder b, int l) {
    return consumeToken(b, EXPANDABLE_HERE_STRING);
  }

  /* ********************************************************** */
  // STRING_DQ
  static boolean expandable_string_literal(PsiBuilder b, int l) {
    return consumeToken(b, STRING_DQ);
  }

  /* ********************************************************** */
  // logical_expression
  public static boolean expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, EXPRESSION, "<expression>");
    r = logical_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // prefix_op nls? primary_expression | cast_expression
  public static boolean expression_with_unary_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_with_unary_operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION_WITH_UNARY_OPERATOR, "<expression with unary operator>");
    r = expression_with_unary_operator_0(b, l + 1);
    if (!r) r = cast_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // prefix_op nls? primary_expression
  private static boolean expression_with_unary_operator_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_with_unary_operator_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = prefix_op(b, l + 1);
    r = r && expression_with_unary_operator_0_1(b, l + 1);
    r = r && primary_expression(b, l + 1);
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
  // nls? 'finally' statement_block
  static boolean finally_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "finally_clause")) return false;
    if (!nextTokenIs(b, "", FINALLY, NLS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = finally_clause_0(b, l + 1);
    r = r && consumeToken(b, FINALLY);
    r = r && statement_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean finally_clause_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "finally_clause_0")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ('break' | 'continue') label_expression? | ('throw' | 'return' | 'exit') pipeline?
  public static boolean flow_control_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "flow_control_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FLOW_CONTROL_STATEMENT, "<flow control statement>");
    r = flow_control_statement_0(b, l + 1);
    if (!r) r = flow_control_statement_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ('break' | 'continue') label_expression?
  private static boolean flow_control_statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "flow_control_statement_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = flow_control_statement_0_0(b, l + 1);
    r = r && flow_control_statement_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'break' | 'continue'
  private static boolean flow_control_statement_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "flow_control_statement_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BREAK);
    if (!r) r = consumeToken(b, CONTINUE);
    exit_section_(b, m, null, r);
    return r;
  }

  // label_expression?
  private static boolean flow_control_statement_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "flow_control_statement_0_1")) return false;
    label_expression(b, l + 1);
    return true;
  }

  // ('throw' | 'return' | 'exit') pipeline?
  private static boolean flow_control_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "flow_control_statement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = flow_control_statement_1_0(b, l + 1);
    r = r && flow_control_statement_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'throw' | 'return' | 'exit'
  private static boolean flow_control_statement_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "flow_control_statement_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, THROW);
    if (!r) r = consumeToken(b, RETURN);
    if (!r) r = consumeToken(b, EXIT);
    exit_section_(b, m, null, r);
    return r;
  }

  // pipeline?
  private static boolean flow_control_statement_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "flow_control_statement_1_1")) return false;
    pipeline(b, l + 1);
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
  // 'for' nls? LP
  //                                   nls? for_initializer? sep
  //                                   nls? for_condition? sep
  //                                   nls? for_iterator?
  //                                   nls? RP statement_block
  static boolean for_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement")) return false;
    if (!nextTokenIs(b, FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FOR);
    r = r && for_statement_1(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && for_statement_3(b, l + 1);
    r = r && for_statement_4(b, l + 1);
    r = r && sep(b, l + 1);
    r = r && for_statement_6(b, l + 1);
    r = r && for_statement_7(b, l + 1);
    r = r && sep(b, l + 1);
    r = r && for_statement_9(b, l + 1);
    r = r && for_statement_10(b, l + 1);
    r = r && for_statement_11(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && statement_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
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

  // for_initializer?
  private static boolean for_statement_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_4")) return false;
    for_initializer(b, l + 1);
    return true;
  }

  // nls?
  private static boolean for_statement_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_6")) return false;
    nls(b, l + 1);
    return true;
  }

  // for_condition?
  private static boolean for_statement_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_7")) return false;
    for_condition(b, l + 1);
    return true;
  }

  // nls?
  private static boolean for_statement_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_9")) return false;
    nls(b, l + 1);
    return true;
  }

  // for_iterator?
  private static boolean for_statement_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_10")) return false;
    for_iterator(b, l + 1);
    return true;
  }

  // nls?
  private static boolean for_statement_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_11")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'foreach' nls? '-parallel'? nls?
  //                                 LP nls?   variable   nls? 'in' nls? pipeline nls? RP nls? statement_block
  static boolean foreach_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement")) return false;
    if (!nextTokenIs(b, FOREACH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FOREACH);
    r = r && foreach_statement_1(b, l + 1);
    r = r && foreach_statement_2(b, l + 1);
    r = r && foreach_statement_3(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && foreach_statement_5(b, l + 1);
    r = r && variable(b, l + 1);
    r = r && foreach_statement_7(b, l + 1);
    r = r && consumeToken(b, IN);
    r = r && foreach_statement_9(b, l + 1);
    r = r && pipeline(b, l + 1);
    r = r && foreach_statement_11(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && foreach_statement_13(b, l + 1);
    r = r && statement_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean foreach_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // '-parallel'?
  private static boolean foreach_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_2")) return false;
    consumeToken(b, "-parallel");
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
  private static boolean foreach_statement_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_7")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean foreach_statement_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_9")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean foreach_statement_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_11")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean foreach_statement_13(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_13")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // range_expression ( format_operator nls? range_expression )*
  public static boolean format_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "format_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, FORMAT_EXPRESSION, "<format expression>");
    r = range_expression(b, l + 1);
    r = r && format_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( format_operator nls? range_expression )*
  private static boolean format_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "format_expression_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!format_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "format_expression_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // format_operator nls? range_expression
  private static boolean format_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "format_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = format_operator(b, l + 1);
    r = r && format_expression_1_0_1(b, l + 1);
    r = r && range_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean format_expression_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "format_expression_1_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '-f'
  static boolean format_operator(PsiBuilder b, int l) {
    return consumeToken(b, "-f");
  }

  /* ********************************************************** */
  // command_argument
  static boolean function_name(PsiBuilder b, int l) {
    return command_argument(b, l + 1);
  }

  /* ********************************************************** */
  // nls? LP parameter_list nls? RP
  static boolean function_parameter_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_parameter_declaration")) return false;
    if (!nextTokenIs(b, "", LP, NLS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_parameter_declaration_0(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && parameter_list(b, l + 1);
    r = r && function_parameter_declaration_3(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean function_parameter_declaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_parameter_declaration_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean function_parameter_declaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_parameter_declaration_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ('function' | 'filter' | 'workflow') nls? function_name function_parameter_declaration? '{' script_block '}'
  public static boolean function_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_STATEMENT, "<function statement>");
    r = function_statement_0(b, l + 1);
    r = r && function_statement_1(b, l + 1);
    r = r && function_name(b, l + 1);
    r = r && function_statement_3(b, l + 1);
    r = r && consumeToken(b, LCURLY);
    r = r && script_block(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'function' | 'filter' | 'workflow'
  private static boolean function_statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FUNCTION);
    if (!r) r = consumeToken(b, FILTER);
    if (!r) r = consumeToken(b, WORKFLOW);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean function_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // function_parameter_declaration?
  private static boolean function_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_statement_3")) return false;
    function_parameter_declaration(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // variable | generic_token_chars
  static boolean generic_token(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_token")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable(b, l + 1);
    if (!r) r = generic_token_chars(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // VAR_SIMPLE
  static boolean generic_token_chars(PsiBuilder b, int l) {
    return consumeToken(b, VAR_SIMPLE);
  }

  /* ********************************************************** */
  // type_spec nls? (',' nls? type_spec)*
  static boolean generic_type_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_arguments")) return false;
    if (!nextTokenIs(b, "", TYPE_NAME, VAR_SIMPLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_spec(b, l + 1);
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

  // (',' nls? type_spec)*
  private static boolean generic_type_arguments_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_arguments_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!generic_type_arguments_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "generic_type_arguments_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' nls? type_spec
  private static boolean generic_type_arguments_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_arguments_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && generic_type_arguments_2_0_1(b, l + 1);
    r = r && type_spec(b, l + 1);
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
  // type_name '['
  static boolean generic_type_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "generic_type_name")) return false;
    if (!nextTokenIs(b, "", TYPE_NAME, VAR_SIMPLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_name(b, l + 1);
    r = r && consumeToken(b, "[");
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
  static boolean hash_literal_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_body")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = hash_entry(b, l + 1);
    r = r && hash_literal_body_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( statement_terminators hash_entry )*
  private static boolean hash_literal_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_body_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!hash_literal_body_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "hash_literal_body_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // statement_terminators hash_entry
  private static boolean hash_literal_body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_body_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_terminators(b, l + 1);
    r = r && hash_entry(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // AT LCURLY hash_literal_body? RCURLY
  static boolean hash_literal_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_expression")) return false;
    if (!nextTokenIsFast(b, AT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, AT, LCURLY);
    r = r && hash_literal_expression_2(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // hash_literal_body?
  private static boolean hash_literal_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_literal_expression_2")) return false;
    hash_literal_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'if' nls? LP nls? pipeline nls? RP statement_block elseif_clause* else_clause?
  static boolean if_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IF);
    r = r && if_statement_1(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && if_statement_3(b, l + 1);
    r = r && pipeline(b, l + 1);
    r = r && if_statement_5(b, l + 1);
    r = r && consumeToken(b, RP);
    r = r && statement_block(b, l + 1);
    r = r && if_statement_8(b, l + 1);
    r = r && if_statement_9(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
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

  // elseif_clause*
  private static boolean if_statement_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_8")) return false;
    int c = current_position_(b);
    while (true) {
      if (!elseif_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "if_statement_8", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // else_clause?
  private static boolean if_statement_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_9")) return false;
    else_clause(b, l + 1);
    return true;
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
  // DIGITS
  public static boolean integer_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "integer_literal")) return false;
    if (!nextTokenIs(b, DIGITS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DIGITS);
    exit_section_(b, m, INTEGER_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // ('.' | '::') member_name LP argument_expression_list? nls? RP
  static boolean invocation_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invocation_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_INNER_);
    r = invocation_expression_0(b, l + 1);
    r = r && member_name(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && invocation_expression_3(b, l + 1);
    r = r && invocation_expression_4(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '.' | '::'
  private static boolean invocation_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invocation_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, ".");
    if (!r) r = consumeTokenFast(b, "::");
    exit_section_(b, m, null, r);
    return r;
  }

  // argument_expression_list?
  private static boolean invocation_expression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invocation_expression_3")) return false;
    argument_expression_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean invocation_expression_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "invocation_expression_4")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // simple_name | unary_expression
  static boolean key_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "key_expression")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = simple_name(b, l + 1);
    if (!r) r = unary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ':' LETTERS ALNUM?
  static boolean label(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COLON, LETTERS);
    r = r && label_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ALNUM?
  private static boolean label_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_2")) return false;
    consumeToken(b, ALNUM);
    return true;
  }

  /* ********************************************************** */
  // simple_name | unary_expression
  static boolean label_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_expression")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = simple_name(b, l + 1);
    if (!r) r = unary_expression(b, l + 1);
    exit_section_(b, m, null, r);
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
  // integer_literal | real_literal | string_literal
  static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = integer_literal(b, l + 1);
    if (!r) r = real_literal(b, l + 1);
    if (!r) r = string_literal(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // bitwise_expression ( ( '-and' | '-or' | '-xor' ) nls? bitwise_expression )*
  public static boolean logical_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, LOGICAL_EXPRESSION, "<logical expression>");
    r = bitwise_expression(b, l + 1);
    r = r && logical_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ( '-and' | '-or' | '-xor' ) nls? bitwise_expression )*
  private static boolean logical_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_expression_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!logical_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "logical_expression_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ( '-and' | '-or' | '-xor' ) nls? bitwise_expression
  private static boolean logical_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = logical_expression_1_0_0(b, l + 1);
    r = r && logical_expression_1_0_1(b, l + 1);
    r = r && bitwise_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '-and' | '-or' | '-xor'
  private static boolean logical_expression_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_expression_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, "-and");
    if (!r) r = consumeTokenFast(b, "-or");
    if (!r) r = consumeTokenFast(b, "-xor");
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean logical_expression_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_expression_1_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ( '.' | '::' ) member_name
  static boolean member_access(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_access")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_INNER_);
    r = member_access_0(b, l + 1);
    r = r && member_name(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '.' | '::'
  private static boolean member_access_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_access_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ".");
    if (!r) r = consumeToken(b, "::");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // simple_name
  //                         | string_literal
  // //->                        | string_literal_with_subexpression
  //                         | expression_with_unary_operator //prefix_op unary_expression //
  //                         | value
  static boolean member_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "member_name")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = simple_name(b, l + 1);
    if (!r) r = string_literal(b, l + 1);
    if (!r) r = expression_with_unary_operator(b, l + 1);
    if (!r) r = value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // OP_MR
  static boolean merging_redirection_operator(PsiBuilder b, int l) {
    return consumeToken(b, OP_MR);
  }

  /* ********************************************************** */
  // format_expression ( ( '*' | '/' | '%') nls? format_expression )*
  public static boolean multiplicative_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, MULTIPLICATIVE_EXPRESSION, "<multiplicative expression>");
    r = format_expression(b, l + 1);
    r = r && multiplicative_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ( '*' | '/' | '%') nls? format_expression )*
  private static boolean multiplicative_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!multiplicative_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "multiplicative_expression_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ( '*' | '/' | '%') nls? format_expression
  private static boolean multiplicative_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = multiplicative_expression_1_0_0(b, l + 1);
    r = r && multiplicative_expression_1_0_1(b, l + 1);
    r = r && format_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '*' | '/' | '%'
  private static boolean multiplicative_expression_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, "*");
    if (!r) r = consumeTokenFast(b, "/");
    if (!r) r = consumeTokenFast(b, "%");
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean multiplicative_expression_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplicative_expression_1_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // VAR_SIMPLE | SIMPLE_NAME
  static boolean name_identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "name_identifier")) return false;
    if (!nextTokenIs(b, "", SIMPLE_NAME, VAR_SIMPLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VAR_SIMPLE);
    if (!r) r = consumeToken(b, SIMPLE_NAME);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // block_name statement_block statement_terminators?
  static boolean named_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block_name(b, l + 1);
    r = r && statement_block(b, l + 1);
    r = r && named_block_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_terminators?
  private static boolean named_block_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_2")) return false;
    statement_terminators(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // named_block (statement_terminators named_block? )*
  static boolean named_block_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = named_block(b, l + 1);
    r = r && named_block_list_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (statement_terminators named_block? )*
  private static boolean named_block_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_list_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!named_block_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "named_block_list_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // statement_terminators named_block?
  private static boolean named_block_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_terminators(b, l + 1);
    r = r && named_block_list_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // named_block?
  private static boolean named_block_list_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_block_list_1_0_1")) return false;
    named_block(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LF
  static boolean new_line_char(PsiBuilder b, int l) {
    return consumeToken(b, LF);
  }

  /* ********************************************************** */
  // NLS
  static boolean nls(PsiBuilder b, int l) {
    return consumeToken(b, NLS);
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
  // nls? attribute_list? nls? 'param' nls? LP script_parameter_list? nls? RP
  static boolean param_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_block")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = param_block_0(b, l + 1);
    r = r && param_block_1(b, l + 1);
    r = r && param_block_2(b, l + 1);
    r = r && consumeToken(b, PARAM);
    r = r && param_block_4(b, l + 1);
    r = r && consumeToken(b, LP);
    r = r && param_block_6(b, l + 1);
    r = r && param_block_7(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean param_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_block_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // attribute_list?
  private static boolean param_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_block_1")) return false;
    attribute_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean param_block_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_block_2")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean param_block_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_block_4")) return false;
    nls(b, l + 1);
    return true;
  }

  // script_parameter_list?
  private static boolean param_block_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_block_6")) return false;
    script_parameter_list(b, l + 1);
    return true;
  }

  // nls?
  private static boolean param_block_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_block_7")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // script_parameter_list
  static boolean parameter_list(PsiBuilder b, int l) {
    return script_parameter_list(b, l + 1);
  }

  /* ********************************************************** */
  // LP nls? pipeline nls? RP
  public static boolean parenthesized_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parenthesized_expression")) return false;
    if (!nextTokenIsFast(b, LP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, LP);
    r = r && parenthesized_expression_1(b, l + 1);
    r = r && pipeline(b, l + 1);
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
  // assignment_expression | expression redirection* pipeline_tail? | command verbatim_command_argument? pipeline_tail?
  public static boolean pipeline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PIPELINE, "<pipeline>");
    r = assignment_expression(b, l + 1);
    if (!r) r = pipeline_1(b, l + 1);
    if (!r) r = pipeline_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // expression redirection* pipeline_tail?
  private static boolean pipeline_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1);
    r = r && pipeline_1_1(b, l + 1);
    r = r && pipeline_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // redirection*
  private static boolean pipeline_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_1_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!redirection(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "pipeline_1_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // pipeline_tail?
  private static boolean pipeline_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_1_2")) return false;
    pipeline_tail(b, l + 1);
    return true;
  }

  // command verbatim_command_argument? pipeline_tail?
  private static boolean pipeline_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command(b, l + 1);
    r = r && pipeline_2_1(b, l + 1);
    r = r && pipeline_2_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // verbatim_command_argument?
  private static boolean pipeline_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_2_1")) return false;
    verbatim_command_argument(b, l + 1);
    return true;
  }

  // pipeline_tail?
  private static boolean pipeline_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_2_2")) return false;
    pipeline_tail(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ( '|' nls? command )+
  static boolean pipeline_tail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_tail")) return false;
    if (!nextTokenIs(b, PIPE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pipeline_tail_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!pipeline_tail_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "pipeline_tail", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // '|' nls? command
  private static boolean pipeline_tail_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pipeline_tail_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && pipeline_tail_0_1(b, l + 1);
    r = r && command(b, l + 1);
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
  static boolean post_decrement_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "post_decrement_expression")) return false;
    if (!nextTokenIsFast(b, MM)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_INNER_);
    r = consumeTokenFast(b, MM);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PP
  static boolean post_increment_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "post_increment_expression")) return false;
    if (!nextTokenIsFast(b, PP)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_INNER_);
    r = consumeTokenFast(b, PP);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '-not' | '!' | '-bnot' | MM | PP | '-split' | '-join' | COMMA | '+' | '-'
  static boolean prefix_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prefix_op")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "-not");
    if (!r) r = consumeToken(b, "!");
    if (!r) r = consumeToken(b, "-bnot");
    if (!r) r = consumeToken(b, MM);
    if (!r) r = consumeToken(b, PP);
    if (!r) r = consumeToken(b, "-split");
    if (!r) r = consumeToken(b, "-join");
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, "+");
    if (!r) r = consumeToken(b, "-");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // value (invocation_expression | member_access | element_access | post_increment_expression | post_decrement_expression)?
  public static boolean primary_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, PRIMARY_EXPRESSION, "<primary expression>");
    r = value(b, l + 1);
    r = r && primary_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (invocation_expression | member_access | element_access | post_increment_expression | post_decrement_expression)?
  private static boolean primary_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expression_1")) return false;
    primary_expression_1_0(b, l + 1);
    return true;
  }

  // invocation_expression | member_access | element_access | post_increment_expression | post_decrement_expression
  private static boolean primary_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = invocation_expression(b, l + 1);
    if (!r) r = member_access(b, l + 1);
    if (!r) r = element_access(b, l + 1);
    if (!r) r = post_increment_expression(b, l + 1);
    if (!r) r = post_decrement_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // array_literal_expression ( '..' nls? array_literal_expression )*
  public static boolean range_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, RANGE_EXPRESSION, "<range expression>");
    r = array_literal_expression(b, l + 1);
    r = r && range_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( '..' nls? array_literal_expression )*
  private static boolean range_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!range_expression_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "range_expression_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // '..' nls? array_literal_expression
  private static boolean range_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, "..");
    r = r && range_expression_1_0_1(b, l + 1);
    r = r && array_literal_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean range_expression_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_expression_1_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // DIGITS
  public static boolean real_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "real_literal")) return false;
    if (!nextTokenIs(b, DIGITS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DIGITS);
    exit_section_(b, m, REAL_LITERAL, r);
    return r;
  }

  /* ********************************************************** */
  // command_argument | primary_expression
  static boolean redirected_file_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "redirected_file_name")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_argument(b, l + 1);
    if (!r) r = primary_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // merging_redirection_operator | file_redirection_operator  redirected_file_name
  static boolean redirection(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "redirection")) return false;
    if (!nextTokenIs(b, "", OP_FR, OP_MR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = merging_redirection_operator(b, l + 1);
    if (!r) r = redirection_1(b, l + 1);
    exit_section_(b, m, null, r);
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
  // param_block? statement_terminators? script_block_body
  public static boolean script_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCRIPT_BLOCK, "<script block>");
    r = script_block_0(b, l + 1);
    r = r && script_block_1(b, l + 1);
    r = r && script_block_body(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // param_block?
  private static boolean script_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block_0")) return false;
    param_block(b, l + 1);
    return true;
  }

  // statement_terminators?
  private static boolean script_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block_1")) return false;
    statement_terminators(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // named_block_list | statement_list
  static boolean script_block_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block_body")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = named_block_list(b, l + 1);
    if (!r) r = statement_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LCURLY statement_list? RCURLY
  static boolean script_block_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block_expression")) return false;
    if (!nextTokenIsFast(b, LCURLY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenFast(b, LCURLY);
    r = r && script_block_expression_1(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_list?
  private static boolean script_block_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_block_expression_1")) return false;
    statement_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // attribute_list? nls? variable nls? script_parameter_default?
  static boolean script_parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = script_parameter_0(b, l + 1);
    r = r && script_parameter_1(b, l + 1);
    r = r && variable(b, l + 1);
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
    r = r && expression(b, l + 1);
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
  // script_parameter (nls? ',' script_parameter)*
  static boolean script_parameter_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = script_parameter(b, l + 1);
    r = r && script_parameter_list_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (nls? ',' script_parameter)*
  private static boolean script_parameter_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_list_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!script_parameter_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "script_parameter_list_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // nls? ',' script_parameter
  private static boolean script_parameter_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = script_parameter_list_1_0_0(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && script_parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean script_parameter_list_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "script_parameter_list_1_0_0")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ';' |  NLS | new_line_char
  public static boolean sep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sep")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SEP, "<sep>");
    r = consumeToken(b, ";");
    if (!r) r = consumeToken(b, NLS);
    if (!r) r = new_line_char(b, l + 1);
    exit_section_(b, l, m, r, false, null);
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
  // name_identifier
  public static boolean simple_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_name")) return false;
    if (!nextTokenIs(b, "<simple name>", SIMPLE_NAME, VAR_SIMPLE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIMPLE_NAME, "<simple name>");
    r = name_identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (DS | AT) variable_scope? variable_name_simple
  static boolean simple_variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_variable")) return false;
    if (!nextTokenIs(b, "", AT, DS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = simple_variable_0(b, l + 1);
    r = r && simple_variable_1(b, l + 1);
    r = r && variable_name_simple(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // DS | AT
  private static boolean simple_variable_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_variable_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DS);
    if (!r) r = consumeToken(b, AT);
    exit_section_(b, m, null, r);
    return r;
  }

  // variable_scope?
  private static boolean simple_variable_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_variable_1")) return false;
    variable_scope(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // if_statement
  //                       | label? labeled_statement
  //                       | function_statement
  //                       | flow_control_statement //sep
  //                       | trap_statement
  //                       | try_statement
  //                       | data_statement
  //                       | inlinescript_statement
  //                       | parallel_statement
  //                       | sequence_statement
  //                       | pipeline
  static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = if_statement(b, l + 1);
    if (!r) r = statement_1(b, l + 1);
    if (!r) r = function_statement(b, l + 1);
    if (!r) r = flow_control_statement(b, l + 1);
    if (!r) r = trap_statement(b, l + 1);
    if (!r) r = try_statement(b, l + 1);
    if (!r) r = data_statement(b, l + 1);
    if (!r) r = inlinescript_statement(b, l + 1);
    if (!r) r = parallel_statement(b, l + 1);
    if (!r) r = sequence_statement(b, l + 1);
    if (!r) r = pipeline(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // label? labeled_statement
  private static boolean statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_1_0(b, l + 1);
    r = r && labeled_statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // label?
  private static boolean statement_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1_0")) return false;
    label(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LCURLY nls? statement_list nls? RCURLY
  public static boolean statement_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_block")) return false;
    if (!nextTokenIs(b, LCURLY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LCURLY);
    r = r && statement_block_1(b, l + 1);
    r = r && statement_list(b, l + 1);
    r = r && statement_block_3(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, STATEMENT_BLOCK, r);
    return r;
  }

  // nls?
  private static boolean statement_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_block_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean statement_block_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_block_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // statement ( statement_terminators statement? )*
  static boolean statement_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement(b, l + 1);
    r = r && statement_list_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( statement_terminators statement? )*
  private static boolean statement_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_list_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!statement_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "statement_list_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // statement_terminators statement?
  private static boolean statement_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_terminators(b, l + 1);
    r = r && statement_list_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement?
  private static boolean statement_list_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_list_1_0_1")) return false;
    statement(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // sep+
  static boolean statement_terminators(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_terminators")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sep(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!sep(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "statement_terminators", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expandable_string_literal | verbatim_string_literal | expandable_here_string_literal | verbatim_here_string_literal
  public static boolean string_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING_LITERAL, "<string literal>");
    r = expandable_string_literal(b, l + 1);
    if (!r) r = verbatim_string_literal(b, l + 1);
    if (!r) r = expandable_here_string_literal(b, l + 1);
    if (!r) r = verbatim_here_string_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // DS LP statement_list? RP
  static boolean sub_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_expression")) return false;
    if (!nextTokenIsFast(b, DS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DS, LP);
    r = r && sub_expression_2(b, l + 1);
    r = r && consumeToken(b, RP);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_list?
  private static boolean sub_expression_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_expression_2")) return false;
    statement_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // nls? '{' nls? switch_clause+ '}'
  static boolean switch_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_body")) return false;
    if (!nextTokenIs(b, "", LCURLY, NLS)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = switch_body_0(b, l + 1);
    r = r && consumeToken(b, LCURLY);
    r = r && switch_body_2(b, l + 1);
    r = r && switch_body_3(b, l + 1);
    r = r && consumeToken(b, RCURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean switch_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_body_0")) return false;
    nls(b, l + 1);
    return true;
  }

  // nls?
  private static boolean switch_body_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_body_2")) return false;
    nls(b, l + 1);
    return true;
  }

  // switch_clause+
  private static boolean switch_body_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_body_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = switch_clause(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!switch_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "switch_body_3", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // switch_clause_condition statement_block statement_terminators?
  static boolean switch_clause(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_clause")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = switch_clause_condition(b, l + 1);
    r = r && statement_block(b, l + 1);
    r = r && switch_clause_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_terminators?
  private static boolean switch_clause_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_clause_2")) return false;
    statement_terminators(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // command_argument | primary_expression
  static boolean switch_clause_condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_clause_condition")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_argument(b, l + 1);
    if (!r) r = primary_expression(b, l + 1);
    exit_section_(b, m, null, r);
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
  // command_argument | primary_expression
  static boolean switch_filename(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_filename")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = command_argument(b, l + 1);
    if (!r) r = primary_expression(b, l + 1);
    exit_section_(b, m, null, r);
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
    Marker m = enter_section_(b);
    r = consumeToken(b, "-regex");
    if (!r) r = consumeToken(b, "-wildcard");
    if (!r) r = consumeToken(b, "-exact");
    if (!r) r = consumeToken(b, "-casesensitive");
    if (!r) r = consumeToken(b, "-parallel");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'switch' nls? switch_parameter* switch_condition switch_body
  static boolean switch_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "switch_statement")) return false;
    if (!nextTokenIs(b, SWITCH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SWITCH);
    r = r && switch_statement_1(b, l + 1);
    r = r && switch_statement_2(b, l + 1);
    r = r && switch_condition(b, l + 1);
    r = r && switch_body(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
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
    int c = current_position_(b);
    while (true) {
      if (!switch_parameter(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "switch_statement_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // (script_block | statement_list )?
  static boolean top_level_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_element")) return false;
    top_level_element_0(b, l + 1);
    return true;
  }

  // script_block | statement_list
  private static boolean top_level_element_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_element_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = script_block(b, l + 1);
    if (!r) r = statement_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'trap' nls? type_literal? nls? statement_block
  public static boolean trap_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trap_statement")) return false;
    if (!nextTokenIs(b, TRAP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TRAP);
    r = r && trap_statement_1(b, l + 1);
    r = r && trap_statement_2(b, l + 1);
    r = r && trap_statement_3(b, l + 1);
    r = r && statement_block(b, l + 1);
    exit_section_(b, m, TRAP_STATEMENT, r);
    return r;
  }

  // nls?
  private static boolean trap_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trap_statement_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // type_literal?
  private static boolean trap_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trap_statement_2")) return false;
    type_literal(b, l + 1);
    return true;
  }

  // nls?
  private static boolean trap_statement_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trap_statement_3")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'try' statement_block (catch_clause+ | finally_clause | catch_clause finally_clause)
  public static boolean try_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement")) return false;
    if (!nextTokenIs(b, TRY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TRY);
    r = r && statement_block(b, l + 1);
    r = r && try_statement_2(b, l + 1);
    exit_section_(b, m, TRY_STATEMENT, r);
    return r;
  }

  // catch_clause+ | finally_clause | catch_clause finally_clause
  private static boolean try_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = try_statement_2_0(b, l + 1);
    if (!r) r = finally_clause(b, l + 1);
    if (!r) r = try_statement_2_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // catch_clause+
  private static boolean try_statement_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = catch_clause(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!catch_clause(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "try_statement_2_0", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // catch_clause finally_clause
  private static boolean try_statement_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "try_statement_2_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = catch_clause(b, l + 1);
    r = r && finally_clause(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TYPE_NAME | VAR_SIMPLE
  static boolean type_identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_identifier")) return false;
    if (!nextTokenIs(b, "", TYPE_NAME, VAR_SIMPLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TYPE_NAME);
    if (!r) r = consumeToken(b, VAR_SIMPLE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '[' type_spec ']'
  public static boolean type_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_LITERAL, "<type literal>");
    r = consumeToken(b, "[");
    r = r && type_spec(b, l + 1);
    r = r && consumeToken(b, "]");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // type_identifier ('.' type_identifier)*
  public static boolean type_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_name")) return false;
    if (!nextTokenIs(b, "<type name>", TYPE_NAME, VAR_SIMPLE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_NAME, "<type name>");
    r = type_identifier(b, l + 1);
    r = r && type_name_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ('.' type_identifier)*
  private static boolean type_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_name_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!type_name_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "type_name_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // '.' type_identifier
  private static boolean type_name_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_name_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ".");
    r = r && type_identifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // array_type_name nls? dimension? ']' | generic_type_name nls? generic_type_arguments ']' | type_name
  static boolean type_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec")) return false;
    if (!nextTokenIs(b, "", TYPE_NAME, VAR_SIMPLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_spec_0(b, l + 1);
    if (!r) r = type_spec_1(b, l + 1);
    if (!r) r = type_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // array_type_name nls? dimension? ']'
  private static boolean type_spec_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array_type_name(b, l + 1);
    r = r && type_spec_0_1(b, l + 1);
    r = r && type_spec_0_2(b, l + 1);
    r = r && consumeToken(b, "]");
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean type_spec_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec_0_1")) return false;
    nls(b, l + 1);
    return true;
  }

  // dimension?
  private static boolean type_spec_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec_0_2")) return false;
    dimension(b, l + 1);
    return true;
  }

  // generic_type_name nls? generic_type_arguments ']'
  private static boolean type_spec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = generic_type_name(b, l + 1);
    r = r && type_spec_1_1(b, l + 1);
    r = r && generic_type_arguments(b, l + 1);
    r = r && consumeToken(b, "]");
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean type_spec_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec_1_1")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
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

  /* ********************************************************** */
  // parenthesized_expression
  //                   | sub_expression
  //                   | array_expression
  //                   | script_block_expression
  //                   | hash_literal_expression
  //                   | literal
  //                   | type_literal
  // //                  | prefix_op lvalue
  //                   | cast_expression
  //                   | variable
  static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parenthesized_expression(b, l + 1);
    if (!r) r = sub_expression(b, l + 1);
    if (!r) r = array_expression(b, l + 1);
    if (!r) r = script_block_expression(b, l + 1);
    if (!r) r = hash_literal_expression(b, l + 1);
    if (!r) r = literal(b, l + 1);
    if (!r) r = type_literal(b, l + 1);
    if (!r) r = cast_expression(b, l + 1);
    if (!r) r = variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '$$' | '$?' | '$^' |  simple_variable | braced_variable
  public static boolean variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VARIABLE, "<variable>");
    r = consumeToken(b, "$$");
    if (!r) r = consumeToken(b, "$?");
    if (!r) r = consumeToken(b, "$^");
    if (!r) r = simple_variable(b, l + 1);
    if (!r) r = braced_variable(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // VAR_SIMPLE
  static boolean variable_name_braced(PsiBuilder b, int l) {
    return consumeToken(b, VAR_SIMPLE);
  }

  /* ********************************************************** */
  // VAR_SIMPLE
  static boolean variable_name_simple(PsiBuilder b, int l) {
    return consumeToken(b, VAR_SIMPLE);
  }

  /* ********************************************************** */
  // variable_name_simple ':'
  static boolean variable_namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_namespace")) return false;
    if (!nextTokenIs(b, VAR_SIMPLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_name_simple(b, l + 1);
    r = r && consumeToken(b, COLON);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'global:' | 'local:' | 'private:' | 'script:' | 'using:' | 'workflow:' | variable_namespace
  static boolean variable_scope(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_scope")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "global:");
    if (!r) r = consumeToken(b, "local:");
    if (!r) r = consumeToken(b, "private:");
    if (!r) r = consumeToken(b, "script:");
    if (!r) r = consumeToken(b, "using:");
    if (!r) r = consumeToken(b, "workflow:");
    if (!r) r = variable_namespace(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '--%' (verbatim_command_string | AMP_ARG /*|generic_token*/)+
  public static boolean verbatim_command_argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "verbatim_command_argument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VERBATIM_COMMAND_ARGUMENT, "<verbatim command argument>");
    r = consumeToken(b, "--%");
    r = r && verbatim_command_argument_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (verbatim_command_string | AMP_ARG /*|generic_token*/)+
  private static boolean verbatim_command_argument_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "verbatim_command_argument_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = verbatim_command_argument_1_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!verbatim_command_argument_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "verbatim_command_argument_1", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // verbatim_command_string | AMP_ARG
  private static boolean verbatim_command_argument_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "verbatim_command_argument_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = verbatim_command_string(b, l + 1);
    if (!r) r = consumeToken(b, AMP_ARG);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // STRING_DQ
  static boolean verbatim_command_string(PsiBuilder b, int l) {
    return consumeToken(b, STRING_DQ);
  }

  /* ********************************************************** */
  // VERBATIM_HERE_STRING
  static boolean verbatim_here_string_literal(PsiBuilder b, int l) {
    return consumeToken(b, VERBATIM_HERE_STRING);
  }

  /* ********************************************************** */
  // STRING_SQ
  static boolean verbatim_string_literal(PsiBuilder b, int l) {
    return consumeToken(b, STRING_SQ);
  }

  /* ********************************************************** */
  // nls? pipeline
  static boolean while_condition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_condition")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = while_condition_0(b, l + 1);
    r = r && pipeline(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // nls?
  private static boolean while_condition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_condition_0")) return false;
    nls(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'while' nls? LP nls? while_condition nls? RP nls? statement_block
  public static boolean while_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement")) return false;
    if (!nextTokenIs(b, WHILE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WHILE_STATEMENT, null);
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
    exit_section_(b, l, m, r, p, null);
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

}
