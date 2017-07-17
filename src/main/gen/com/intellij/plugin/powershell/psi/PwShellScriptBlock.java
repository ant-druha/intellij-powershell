// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PwShellScriptBlock extends PwShellPsiElement {

  @NotNull
  List<PwShellCommandArgument> getCommandArgumentList();

  @NotNull
  List<PwShellDataStatement> getDataStatementList();

  @NotNull
  List<PwShellExpression> getExpressionList();

  @NotNull
  List<PwShellFlowControlStatement> getFlowControlStatementList();

  @NotNull
  List<PwShellFunctionStatement> getFunctionStatementList();

  @NotNull
  List<PwShellInlinescriptStatement> getInlinescriptStatementList();

  @NotNull
  List<PwShellParallelStatement> getParallelStatementList();

  @NotNull
  List<PwShellPipeline> getPipelineList();

  @NotNull
  List<PwShellSep> getSepList();

  @NotNull
  List<PwShellSequenceStatement> getSequenceStatementList();

  @NotNull
  List<PwShellSimpleName> getSimpleNameList();

  @NotNull
  List<PwShellStatementBlock> getStatementBlockList();

  @NotNull
  List<PwShellTrapStatement> getTrapStatementList();

  @NotNull
  List<PwShellTryStatement> getTryStatementList();

  @NotNull
  List<PwShellTypeLiteral> getTypeLiteralList();

  @NotNull
  List<PwShellTypeName> getTypeNameList();

  @NotNull
  List<PwShellVariable> getVariableList();

  @NotNull
  List<PwShellWhileStatement> getWhileStatementList();

}
