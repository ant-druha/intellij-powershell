// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.lang.lexer.PowerShellTokenType;
import com.intellij.plugin.powershell.lang.parser.PwShellElementType;
import com.intellij.plugin.powershell.psi.impl.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public interface PowerShellTypes {

  IElementType ADDITIVE_EXPRESSION = new PwShellElementType("ADDITIVE_EXPRESSION");
  IElementType ASSIGNMENT_EXPRESSION = new PwShellElementType("ASSIGNMENT_EXPRESSION");
  IElementType BITWISE_EXPRESSION = new PwShellElementType("BITWISE_EXPRESSION");
  IElementType COMMAND = new PwShellElementType("COMMAND");
  IElementType COMMAND_ARGUMENT = new PwShellElementType("COMMAND_ARGUMENT");
  IElementType COMMAND_NAME = new PwShellElementType("COMMAND_NAME");
  IElementType COMMAND_PARAMETER = new PwShellElementType("COMMAND_PARAMETER");
  IElementType COMPARISON_EXPRESSION = new PwShellElementType("COMPARISON_EXPRESSION");
  IElementType DATA_STATEMENT = new PwShellElementType("DATA_STATEMENT");
  IElementType EXPRESSION = new PwShellElementType("EXPRESSION");
  IElementType EXPRESSION_WITH_UNARY_OPERATOR = new PwShellElementType("EXPRESSION_WITH_UNARY_OPERATOR");
  IElementType FLOW_CONTROL_STATEMENT = new PwShellElementType("FLOW_CONTROL_STATEMENT");
  IElementType FORMAT_EXPRESSION = new PwShellElementType("FORMAT_EXPRESSION");
  IElementType FUNCTION_STATEMENT = new PwShellElementType("FUNCTION_STATEMENT");
  IElementType INLINESCRIPT_STATEMENT = new PwShellElementType("INLINESCRIPT_STATEMENT");
  IElementType INTEGER_LITERAL = new PwShellElementType("INTEGER_LITERAL");
  IElementType LOGICAL_EXPRESSION = new PwShellElementType("LOGICAL_EXPRESSION");
  IElementType MULTIPLICATIVE_EXPRESSION = new PwShellElementType("MULTIPLICATIVE_EXPRESSION");
  IElementType PARALLEL_STATEMENT = new PwShellElementType("PARALLEL_STATEMENT");
  IElementType PARENTHESIZED_EXPRESSION = new PwShellElementType("PARENTHESIZED_EXPRESSION");
  IElementType PIPELINE = new PwShellElementType("PIPELINE");
  IElementType PRIMARY_EXPRESSION = new PwShellElementType("PRIMARY_EXPRESSION");
  IElementType RANGE_EXPRESSION = new PwShellElementType("RANGE_EXPRESSION");
  IElementType REAL_LITERAL = new PwShellElementType("REAL_LITERAL");
  IElementType SCRIPT_BLOCK = new PwShellElementType("SCRIPT_BLOCK");
  IElementType SEP = new PwShellElementType("SEP");
  IElementType SEQUENCE_STATEMENT = new PwShellElementType("SEQUENCE_STATEMENT");
  IElementType SIMPLE_NAME = new PwShellElementType("SIMPLE_NAME");
  IElementType STATEMENT_BLOCK = new PwShellElementType("STATEMENT_BLOCK");
  IElementType STRING_LITERAL = new PwShellElementType("STRING_LITERAL");
  IElementType TRAP_STATEMENT = new PwShellElementType("TRAP_STATEMENT");
  IElementType TRY_STATEMENT = new PwShellElementType("TRY_STATEMENT");
  IElementType TYPE_LITERAL = new PwShellElementType("TYPE_LITERAL");
  IElementType TYPE_NAME = new PwShellElementType("TYPE_NAME");
  IElementType UNARY_EXPRESSION = new PwShellElementType("UNARY_EXPRESSION");
  IElementType VARIABLE = new PwShellElementType("VARIABLE");
  IElementType VERBATIM_COMMAND_ARGUMENT = new PwShellElementType("VERBATIM_COMMAND_ARGUMENT");
  IElementType WHILE_STATEMENT = new PwShellElementType("WHILE_STATEMENT");

  IElementType ALNUM = new PowerShellTokenType("ALNUM");
  IElementType AMP = new PowerShellTokenType("&");
  IElementType AMP_ARG = new PowerShellTokenType("AMP_ARG");
  IElementType AT = new PowerShellTokenType("@");
  IElementType BEGIN = new PowerShellTokenType("begin");
  IElementType BREAK = new PowerShellTokenType("break");
  IElementType CATCH = new PowerShellTokenType("catch");
  IElementType CLASS = new PowerShellTokenType("class");
  IElementType COLON = new PowerShellTokenType(":");
  IElementType COMMA = new PowerShellTokenType(",");
  IElementType COMMENT = new PowerShellTokenType("COMMENT");
  IElementType CONTINUE = new PowerShellTokenType("continue");
  IElementType DATA = new PowerShellTokenType("data");
  IElementType DEC_EXPONENT = new PowerShellTokenType("DEC_EXPONENT");
  IElementType DEFINE = new PowerShellTokenType("define");
  IElementType DIGITS = new PowerShellTokenType("DIGITS");
  IElementType DO = new PowerShellTokenType("do");
  IElementType DS = new PowerShellTokenType("$");
  IElementType DYNAMICPARAM = new PowerShellTokenType("dynamicparam");
  IElementType ELSE = new PowerShellTokenType("else");
  IElementType ELSEIF = new PowerShellTokenType("elseif");
  IElementType END = new PowerShellTokenType("end");
  IElementType EQ = new PowerShellTokenType("=");
  IElementType EXIT = new PowerShellTokenType("exit");
  IElementType EXPANDABLE_HERE_STRING = new PowerShellTokenType("EXPANDABLE_HERE_STRING");
  IElementType FILTER = new PowerShellTokenType("filter");
  IElementType FINALLY = new PowerShellTokenType("finally");
  IElementType FOR = new PowerShellTokenType("for");
  IElementType FOREACH = new PowerShellTokenType("foreach");
  IElementType FROM = new PowerShellTokenType("from");
  IElementType FUNCTION = new PowerShellTokenType("function");
  IElementType IF = new PowerShellTokenType("if");
  IElementType IN = new PowerShellTokenType("in");
  IElementType INLINESCRIPT = new PowerShellTokenType("inlinescript");
  IElementType LCURLY = new PowerShellTokenType("{");
  IElementType LETTERS = new PowerShellTokenType("LETTERS");
  IElementType LF = new PowerShellTokenType("LF");
  IElementType LP = new PowerShellTokenType("(");
  IElementType MM = new PowerShellTokenType("--");
  IElementType MULTI_LINE_COMMENT_END = new PowerShellTokenType("*)");
  IElementType MULTI_LINE_COMMENT_START = new PowerShellTokenType("(*");
  IElementType NLS = new PowerShellTokenType("NLS");
  IElementType OP_C = new PowerShellTokenType("OP_C");
  IElementType OP_FR = new PowerShellTokenType("OP_FR");
  IElementType OP_MR = new PowerShellTokenType("OP_MR");
  IElementType PARALLEL = new PowerShellTokenType("parallel");
  IElementType PARAM = new PowerShellTokenType("param");
  IElementType PARAM_ARGUMENT = new PowerShellTokenType("PARAM_ARGUMENT");
  IElementType PARAM_TOKEN = new PowerShellTokenType("PARAM_TOKEN");
  IElementType PIPE = new PowerShellTokenType("|");
  IElementType PP = new PowerShellTokenType("++");
  IElementType PROCESS = new PowerShellTokenType("process");
  IElementType RAW_LBR = new PowerShellTokenType("«");
  IElementType RAW_RBR = new PowerShellTokenType("»");
  IElementType RCURLY = new PowerShellTokenType("}");
  IElementType RETURN = new PowerShellTokenType("return");
  IElementType RP = new PowerShellTokenType(")");
  IElementType STRING_DQ = new PowerShellTokenType("STRING_DQ");
  IElementType STRING_SQ = new PowerShellTokenType("STRING_SQ");
  IElementType SWITCH = new PowerShellTokenType("switch");
  IElementType THROW = new PowerShellTokenType("throw");
  IElementType TRAP = new PowerShellTokenType("trap");
  IElementType TRY = new PowerShellTokenType("try");
  IElementType UNTIL = new PowerShellTokenType("until");
  IElementType USING = new PowerShellTokenType("using");
  IElementType VAR = new PowerShellTokenType("var");
  IElementType VAR_SIMPLE = new PowerShellTokenType("VAR_SIMPLE");
  IElementType VERBATIM_HERE_STRING = new PowerShellTokenType("VERBATIM_HERE_STRING");
  IElementType WHILE = new PowerShellTokenType("while");
  IElementType WORKFLOW = new PowerShellTokenType("workflow");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADDITIVE_EXPRESSION) {
        return new PwShellAdditiveExpressionImplGen(node);
      } else if (type == ASSIGNMENT_EXPRESSION) {
        return new PwShellAssignmentExpressionImplGen(node);
      } else if (type == BITWISE_EXPRESSION) {
        return new PwShellBitwiseExpressionImplGen(node);
      } else if (type == COMMAND) {
        return new PwShellCommandImplGen(node);
      } else if (type == COMMAND_ARGUMENT) {
        return new PwShellCommandArgumentImplGen(node);
      } else if (type == COMMAND_NAME) {
        return new PwShellCommandNameImplGen(node);
      } else if (type == COMMAND_PARAMETER) {
        return new PwShellCommandParameterImplGen(node);
      } else if (type == COMPARISON_EXPRESSION) {
        return new PwShellComparisonExpressionImplGen(node);
      } else if (type == DATA_STATEMENT) {
        return new PwShellDataStatementImplGen(node);
      } else if (type == EXPRESSION_WITH_UNARY_OPERATOR) {
        return new PwShellExpressionWithUnaryOperatorImplGen(node);
      } else if (type == FLOW_CONTROL_STATEMENT) {
        return new PwShellFlowControlStatementImplGen(node);
      } else if (type == FORMAT_EXPRESSION) {
        return new PwShellFormatExpressionImplGen(node);
      } else if (type == FUNCTION_STATEMENT) {
        return new PwShellFunctionStatementImplGen(node);
      } else if (type == INLINESCRIPT_STATEMENT) {
        return new PwShellInlinescriptStatementImplGen(node);
      } else if (type == INTEGER_LITERAL) {
        return new PwShellIntegerLiteralImplGen(node);
      } else if (type == LOGICAL_EXPRESSION) {
        return new PwShellLogicalExpressionImplGen(node);
      } else if (type == MULTIPLICATIVE_EXPRESSION) {
        return new PwShellMultiplicativeExpressionImplGen(node);
      } else if (type == PARALLEL_STATEMENT) {
        return new PwShellParallelStatementImplGen(node);
      } else if (type == PARENTHESIZED_EXPRESSION) {
        return new PwShellParenthesizedExpressionImplGen(node);
      } else if (type == PIPELINE) {
        return new PwShellPipelineImplGen(node);
      } else if (type == PRIMARY_EXPRESSION) {
        return new PwShellPrimaryExpressionImplGen(node);
      } else if (type == RANGE_EXPRESSION) {
        return new PwShellRangeExpressionImplGen(node);
      } else if (type == REAL_LITERAL) {
        return new PwShellRealLiteralImplGen(node);
      } else if (type == SCRIPT_BLOCK) {
        return new PwShellScriptBlockImplGen(node);
      } else if (type == SEP) {
        return new PwShellSepImplGen(node);
      } else if (type == SEQUENCE_STATEMENT) {
        return new PwShellSequenceStatementImplGen(node);
      } else if (type == SIMPLE_NAME) {
        return new PwShellSimpleNameImplGen(node);
      } else if (type == STATEMENT_BLOCK) {
        return new PwShellStatementBlockImplGen(node);
      } else if (type == STRING_LITERAL) {
        return new PwShellStringLiteralImplGen(node);
      } else if (type == TRAP_STATEMENT) {
        return new PwShellTrapStatementImplGen(node);
      } else if (type == TRY_STATEMENT) {
        return new PwShellTryStatementImplGen(node);
      } else if (type == TYPE_LITERAL) {
        return new PwShellTypeLiteralImplGen(node);
      } else if (type == TYPE_NAME) {
        return new PwShellTypeNameImplGen(node);
      } else if (type == UNARY_EXPRESSION) {
        return new PwShellUnaryExpressionImplGen(node);
      } else if (type == VARIABLE) {
        return new PwShellVariableImplGen(node);
      } else if (type == VERBATIM_COMMAND_ARGUMENT) {
        return new PwShellVerbatimCommandArgumentImplGen(node);
      } else if (type == WHILE_STATEMENT) {
        return new PwShellWhileStatementImplGen(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
