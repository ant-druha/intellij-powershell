// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.lang.lexer.PowerShellTokenType;
import com.intellij.plugin.powershell.lang.parser.PowerShellElementType;
import com.intellij.plugin.powershell.psi.impl.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public interface PowerShellTypes {

  IElementType ADDITIVE_EXPRESSION = new PowerShellElementType("ADDITIVE_EXPRESSION");
  IElementType ARRAY_EXPRESSION = new PowerShellElementType("ARRAY_EXPRESSION");
  IElementType ARRAY_LITERAL_EXPRESSION = new PowerShellElementType("ARRAY_LITERAL_EXPRESSION");
  IElementType ASSIGNMENT_EXPRESSION = new PowerShellElementType("ASSIGNMENT_EXPRESSION");
  IElementType ATTRIBUTE = new PowerShellElementType("ATTRIBUTE");
  IElementType BITWISE_EXPRESSION = new PowerShellElementType("BITWISE_EXPRESSION");
  IElementType BLOCK_BODY = new PowerShellElementType("BLOCK_BODY");
  IElementType CAST_EXPRESSION = new PowerShellElementType("CAST_EXPRESSION");
  IElementType COMMAND_ARGUMENT = new PowerShellElementType("COMMAND_ARGUMENT");
  IElementType COMMAND_CALL_EXPRESSION = new PowerShellElementType("COMMAND_CALL_EXPRESSION");
  IElementType COMMAND_NAME = new PowerShellElementType("COMMAND_NAME");
  IElementType COMMAND_PARAMETER = new PowerShellElementType("COMMAND_PARAMETER");
  IElementType COMMENT = new PowerShellElementType("COMMENT");
  IElementType COMPARISON_EXPRESSION = new PowerShellElementType("COMPARISON_EXPRESSION");
  IElementType CONFIGURATION_BLOCK = new PowerShellElementType("CONFIGURATION_BLOCK");
  IElementType DATA_STATEMENT = new PowerShellElementType("DATA_STATEMENT");
  IElementType DO_STATEMENT = new PowerShellElementType("DO_STATEMENT");
  IElementType EXPRESSION = new PowerShellElementType("EXPRESSION");
  IElementType FLOW_CONTROL_STATEMENT = new PowerShellElementType("FLOW_CONTROL_STATEMENT");
  IElementType FOREACH_STATEMENT = new PowerShellElementType("FOREACH_STATEMENT");
  IElementType FORMAT_EXPRESSION = new PowerShellElementType("FORMAT_EXPRESSION");
  IElementType FOR_STATEMENT = new PowerShellElementType("FOR_STATEMENT");
  IElementType FUNCTION_STATEMENT = new PowerShellElementType("FUNCTION_STATEMENT");
  IElementType HASH_LITERAL_EXPRESSION = new PowerShellElementType("HASH_LITERAL_EXPRESSION");
  IElementType IDENTIFIER = new PowerShellElementType("IDENTIFIER");
  IElementType IF_STATEMENT = new PowerShellElementType("IF_STATEMENT");
  IElementType INLINESCRIPT_STATEMENT = new PowerShellElementType("INLINESCRIPT_STATEMENT");
  IElementType INTEGER_LITERAL_EXPRESSION = new PowerShellElementType("INTEGER_LITERAL_EXPRESSION");
  IElementType INVOCATION_EXPRESSION = new PowerShellElementType("INVOCATION_EXPRESSION");
  IElementType KEY_EXPRESSION = new PowerShellElementType("KEY_EXPRESSION");
  IElementType LOGICAL_EXPRESSION = new PowerShellElementType("LOGICAL_EXPRESSION");
  IElementType MULTIPLICATIVE_EXPRESSION = new PowerShellElementType("MULTIPLICATIVE_EXPRESSION");
  IElementType NODE_BLOCK = new PowerShellElementType("NODE_BLOCK");
  IElementType PARALLEL_STATEMENT = new PowerShellElementType("PARALLEL_STATEMENT");
  IElementType PARENTHESIZED_EXPRESSION = new PowerShellElementType("PARENTHESIZED_EXPRESSION");
  IElementType PATH_EXPRESSION = new PowerShellElementType("PATH_EXPRESSION");
  IElementType PATH_ITEM = new PowerShellElementType("PATH_ITEM");
  IElementType POST_DECREMENT_EXPRESSION = new PowerShellElementType("POST_DECREMENT_EXPRESSION");
  IElementType POST_INCREMENT_EXPRESSION = new PowerShellElementType("POST_INCREMENT_EXPRESSION");
  IElementType RANGE_EXPRESSION = new PowerShellElementType("RANGE_EXPRESSION");
  IElementType REAL_LITERAL_EXPRESSION = new PowerShellElementType("REAL_LITERAL_EXPRESSION");
  IElementType RESOURCE_BLOCK = new PowerShellElementType("RESOURCE_BLOCK");
  IElementType SCRIPT_BLOCK_EXPRESSION = new PowerShellElementType("SCRIPT_BLOCK_EXPRESSION");
  IElementType SEP = new PowerShellElementType("SEP");
  IElementType SEQUENCE_STATEMENT = new PowerShellElementType("SEQUENCE_STATEMENT");
  IElementType STRING_LITERAL_EXPRESSION = new PowerShellElementType("STRING_LITERAL_EXPRESSION");
  IElementType SUB_EXPRESSION = new PowerShellElementType("SUB_EXPRESSION");
  IElementType SWITCH_STATEMENT = new PowerShellElementType("SWITCH_STATEMENT");
  IElementType TARGET_VARIABLE_EXPRESSION = new PowerShellElementType("TARGET_VARIABLE_EXPRESSION");
  IElementType TRAP_STATEMENT = new PowerShellElementType("TRAP_STATEMENT");
  IElementType TRY_STATEMENT = new PowerShellElementType("TRY_STATEMENT");
  IElementType TYPE_LITERAL_EXPRESSION = new PowerShellElementType("TYPE_LITERAL_EXPRESSION");
  IElementType UNARY_EXPRESSION = new PowerShellElementType("UNARY_EXPRESSION");
  IElementType VERBATIM_COMMAND_ARGUMENT = new PowerShellElementType("VERBATIM_COMMAND_ARGUMENT");
  IElementType WHILE_STATEMENT = new PowerShellElementType("WHILE_STATEMENT");

  IElementType AMP = new PowerShellTokenType("&");
  IElementType AT = new PowerShellTokenType("@");
  IElementType BACKTICK = new PowerShellTokenType("`");
  IElementType BEGIN = new PowerShellTokenType("begin");
  IElementType BRACED_ID = new PowerShellTokenType("BRACED_ID");
  IElementType BRACED_VAR_START = new PowerShellTokenType("${");
  IElementType BREAK = new PowerShellTokenType("break");
  IElementType CATCH = new PowerShellTokenType("catch");
  IElementType CLASS = new PowerShellTokenType("class");
  IElementType COLON = new PowerShellTokenType(":");
  IElementType COLON2 = new PowerShellTokenType("::");
  IElementType COMMA = new PowerShellTokenType(",");
  IElementType CONFIGURATION = new PowerShellTokenType("configuration");
  IElementType CONTINUE = new PowerShellTokenType("continue");
  IElementType DASH = new PowerShellTokenType("DASH");
  IElementType DATA = new PowerShellTokenType("data");
  IElementType DEC_EXPONENT = new PowerShellTokenType("DEC_EXPONENT");
  IElementType DEC_INTEGER = new PowerShellTokenType("DEC_INTEGER");
  IElementType DEFINE = new PowerShellTokenType("define");
  IElementType DELIMITED_COMMENT = new PowerShellTokenType("DELIMITED_COMMENT");
  IElementType DEL_COMENT_END = new PowerShellTokenType("DEL_COMENT_END");
  IElementType DEL_COMENT_START = new PowerShellTokenType("DEL_COMENT_START");
  IElementType DIV = new PowerShellTokenType("/");
  IElementType DO = new PowerShellTokenType("do");
  IElementType DOT = new PowerShellTokenType(".");
  IElementType DOT_DOT = new PowerShellTokenType("..");
  IElementType DS = new PowerShellTokenType("$");
  IElementType DYNAMICPARAM = new PowerShellTokenType("dynamicparam");
  IElementType ELSE = new PowerShellTokenType("else");
  IElementType ELSEIF = new PowerShellTokenType("elseif");
  IElementType END = new PowerShellTokenType("end");
  IElementType EQ = new PowerShellTokenType("=");
  IElementType EXCL_MARK = new PowerShellTokenType("!");
  IElementType EXIT = new PowerShellTokenType("exit");
  IElementType EXPANDABLE_HERE_STRING = new PowerShellTokenType("EXPANDABLE_HERE_STRING");
  IElementType EXPANDABLE_STRING = new PowerShellTokenType("EXPANDABLE_STRING");
  IElementType FILTER = new PowerShellTokenType("filter");
  IElementType FINALLY = new PowerShellTokenType("finally");
  IElementType FOR = new PowerShellTokenType("for");
  IElementType FOREACH = new PowerShellTokenType("foreach");
  IElementType FROM = new PowerShellTokenType("from");
  IElementType FUNCTION = new PowerShellTokenType("function");
  IElementType GENERIC_ID_PART = new PowerShellTokenType("GENERIC_ID_PART");
  IElementType HASH = new PowerShellTokenType("#");
  IElementType HEX_INTEGER = new PowerShellTokenType("HEX_INTEGER");
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
  IElementType OP_BNOT = new PowerShellTokenType("-bnot");
  IElementType OP_C = new PowerShellTokenType("OP_C");
  IElementType OP_FR = new PowerShellTokenType("OP_FR");
  IElementType OP_MR = new PowerShellTokenType("OP_MR");
  IElementType OP_NOT = new PowerShellTokenType("-not");
  IElementType PARALLEL = new PowerShellTokenType("parallel");
  IElementType PARAM = new PowerShellTokenType("param");
  IElementType PARAM_TOKEN = new PowerShellTokenType("PARAM_TOKEN");
  IElementType PATH_SEP = new PowerShellTokenType("\\");
  IElementType PERS = new PowerShellTokenType("%");
  IElementType PIPE = new PowerShellTokenType("|");
  IElementType PLUS = new PowerShellTokenType("+");
  IElementType PP = new PowerShellTokenType("++");
  IElementType PROCESS = new PowerShellTokenType("process");
  IElementType RAW_LBR = new PowerShellTokenType("«");
  IElementType RAW_RBR = new PowerShellTokenType("»");
  IElementType RCURLY = new PowerShellTokenType("}");
  IElementType REAL_NUM = new PowerShellTokenType("REAL_NUM");
  IElementType RETURN = new PowerShellTokenType("return");
  IElementType RP = new PowerShellTokenType(")");
  IElementType SEMI = new PowerShellTokenType(";");
  IElementType SIMPLE_ID = new PowerShellTokenType("SIMPLE_ID");
  IElementType SINGLE_LINE_COMMENT = new PowerShellTokenType("SINGLE_LINE_COMMENT");
  IElementType SQBR_L = new PowerShellTokenType("[");
  IElementType SQBR_R = new PowerShellTokenType("]");
  IElementType STAR = new PowerShellTokenType("*");
  IElementType SWITCH = new PowerShellTokenType("switch");
  IElementType THROW = new PowerShellTokenType("throw");
  IElementType TRAP = new PowerShellTokenType("trap");
  IElementType TRY = new PowerShellTokenType("try");
  IElementType UNTIL = new PowerShellTokenType("until");
  IElementType USING = new PowerShellTokenType("using");
  IElementType VAR = new PowerShellTokenType("var");
  IElementType VAR_ID = new PowerShellTokenType("VAR_ID");
  IElementType VERBATIM_ARG_INPUT = new PowerShellTokenType("VERBATIM_ARG_INPUT");
  IElementType VERBATIM_ARG_START = new PowerShellTokenType("--%");
  IElementType VERBATIM_HERE_STRING = new PowerShellTokenType("VERBATIM_HERE_STRING");
  IElementType VERBATIM_STRING = new PowerShellTokenType("VERBATIM_STRING");
  IElementType WHILE = new PowerShellTokenType("while");
  IElementType WORKFLOW = new PowerShellTokenType("workflow");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == ADDITIVE_EXPRESSION) {
        return new PowerShellAdditiveExpressionImplGen(node);
      }
      else if (type == ARRAY_EXPRESSION) {
        return new PowerShellArrayExpressionImplGen(node);
      }
      else if (type == ARRAY_LITERAL_EXPRESSION) {
        return new PowerShellArrayLiteralExpressionImplGen(node);
      }
      else if (type == ASSIGNMENT_EXPRESSION) {
        return new PowerShellAssignmentExpressionImplGen(node);
      }
      else if (type == ATTRIBUTE) {
        return new PowerShellAttributeImplGen(node);
      }
      else if (type == BITWISE_EXPRESSION) {
        return new PowerShellBitwiseExpressionImplGen(node);
      }
      else if (type == BLOCK_BODY) {
        return new PowerShellBlockBodyImplGen(node);
      }
      else if (type == CAST_EXPRESSION) {
        return new PowerShellCastExpressionImplGen(node);
      }
      else if (type == COMMAND_ARGUMENT) {
        return new PowerShellCommandArgumentImplGen(node);
      }
      else if (type == COMMAND_CALL_EXPRESSION) {
        return new PowerShellCommandCallExpressionImplGen(node);
      }
      else if (type == COMMAND_NAME) {
        return new PowerShellCommandNameImplGen(node);
      }
      else if (type == COMMAND_PARAMETER) {
        return new PowerShellCommandParameterImplGen(node);
      }
      else if (type == COMMENT) {
        return new PowerShellCommentImplGen(node);
      }
      else if (type == COMPARISON_EXPRESSION) {
        return new PowerShellComparisonExpressionImplGen(node);
      }
      else if (type == CONFIGURATION_BLOCK) {
        return new PowerShellConfigurationBlockImplGen(node);
      }
      else if (type == DATA_STATEMENT) {
        return new PowerShellDataStatementImplGen(node);
      }
      else if (type == DO_STATEMENT) {
        return new PowerShellDoStatementImplGen(node);
      }
      else if (type == FLOW_CONTROL_STATEMENT) {
        return new PowerShellFlowControlStatementImplGen(node);
      }
      else if (type == FOREACH_STATEMENT) {
        return new PowerShellForeachStatementImplGen(node);
      }
      else if (type == FORMAT_EXPRESSION) {
        return new PowerShellFormatExpressionImplGen(node);
      }
      else if (type == FOR_STATEMENT) {
        return new PowerShellForStatementImplGen(node);
      }
      else if (type == FUNCTION_STATEMENT) {
        return new PowerShellFunctionStatementImplGen(node);
      }
      else if (type == HASH_LITERAL_EXPRESSION) {
        return new PowerShellHashLiteralExpressionImplGen(node);
      }
      else if (type == IDENTIFIER) {
        return new PowerShellIdentifierImplGen(node);
      }
      else if (type == IF_STATEMENT) {
        return new PowerShellIfStatementImplGen(node);
      }
      else if (type == INLINESCRIPT_STATEMENT) {
        return new PowerShellInlinescriptStatementImplGen(node);
      }
      else if (type == INTEGER_LITERAL_EXPRESSION) {
        return new PowerShellIntegerLiteralExpressionImplGen(node);
      }
      else if (type == INVOCATION_EXPRESSION) {
        return new PowerShellInvocationExpressionImplGen(node);
      }
      else if (type == KEY_EXPRESSION) {
        return new PowerShellKeyExpressionImplGen(node);
      }
      else if (type == LOGICAL_EXPRESSION) {
        return new PowerShellLogicalExpressionImplGen(node);
      }
      else if (type == MULTIPLICATIVE_EXPRESSION) {
        return new PowerShellMultiplicativeExpressionImplGen(node);
      }
      else if (type == NODE_BLOCK) {
        return new PowerShellNodeBlockImplGen(node);
      }
      else if (type == PARALLEL_STATEMENT) {
        return new PowerShellParallelStatementImplGen(node);
      }
      else if (type == PARENTHESIZED_EXPRESSION) {
        return new PowerShellParenthesizedExpressionImplGen(node);
      }
      else if (type == PATH_EXPRESSION) {
        return new PowerShellPathExpressionImplGen(node);
      }
      else if (type == PATH_ITEM) {
        return new PowerShellPathItemImplGen(node);
      }
      else if (type == POST_DECREMENT_EXPRESSION) {
        return new PowerShellPostDecrementExpressionImplGen(node);
      }
      else if (type == POST_INCREMENT_EXPRESSION) {
        return new PowerShellPostIncrementExpressionImplGen(node);
      }
      else if (type == RANGE_EXPRESSION) {
        return new PowerShellRangeExpressionImplGen(node);
      }
      else if (type == REAL_LITERAL_EXPRESSION) {
        return new PowerShellRealLiteralExpressionImplGen(node);
      }
      else if (type == RESOURCE_BLOCK) {
        return new PowerShellResourceBlockImplGen(node);
      }
      else if (type == SCRIPT_BLOCK_EXPRESSION) {
        return new PowerShellScriptBlockExpressionImplGen(node);
      }
      else if (type == SEP) {
        return new PowerShellSepImplGen(node);
      }
      else if (type == SEQUENCE_STATEMENT) {
        return new PowerShellSequenceStatementImplGen(node);
      }
      else if (type == STRING_LITERAL_EXPRESSION) {
        return new PowerShellStringLiteralExpressionImplGen(node);
      }
      else if (type == SUB_EXPRESSION) {
        return new PowerShellSubExpressionImplGen(node);
      }
      else if (type == SWITCH_STATEMENT) {
        return new PowerShellSwitchStatementImplGen(node);
      }
      else if (type == TARGET_VARIABLE_EXPRESSION) {
        return new PowerShellTargetVariableExpressionImplGen(node);
      }
      else if (type == TRAP_STATEMENT) {
        return new PowerShellTrapStatementImplGen(node);
      }
      else if (type == TRY_STATEMENT) {
        return new PowerShellTryStatementImplGen(node);
      }
      else if (type == TYPE_LITERAL_EXPRESSION) {
        return new PowerShellTypeLiteralExpressionImplGen(node);
      }
      else if (type == UNARY_EXPRESSION) {
        return new PowerShellUnaryExpressionImplGen(node);
      }
      else if (type == VERBATIM_COMMAND_ARGUMENT) {
        return new PowerShellVerbatimCommandArgumentImplGen(node);
      }
      else if (type == WHILE_STATEMENT) {
        return new PowerShellWhileStatementImplGen(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
