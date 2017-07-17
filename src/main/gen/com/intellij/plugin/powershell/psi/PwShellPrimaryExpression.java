// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PwShellPrimaryExpression extends PwShellExpression {

  @NotNull
  List<PwShellCommandArgument> getCommandArgumentList();

  @NotNull
  List<PwShellDataStatement> getDataStatementList();

  @Nullable
  PwShellExpression getExpression();

  @NotNull
  List<PwShellFlowControlStatement> getFlowControlStatementList();

  @NotNull
  List<PwShellFunctionStatement> getFunctionStatementList();

  @NotNull
  List<PwShellInlinescriptStatement> getInlinescriptStatementList();

  @Nullable
  PwShellIntegerLiteral getIntegerLiteral();

  @NotNull
  List<PwShellParallelStatement> getParallelStatementList();

  @NotNull
  List<PwShellPipeline> getPipelineList();

  @Nullable
  PwShellRealLiteral getRealLiteral();

  @NotNull
  List<PwShellSep> getSepList();

  @NotNull
  List<PwShellSequenceStatement> getSequenceStatementList();

  @NotNull
  List<PwShellSimpleName> getSimpleNameList();

  @NotNull
  List<PwShellStatementBlock> getStatementBlockList();

  @Nullable
  PwShellStringLiteral getStringLiteral();

  @NotNull
  List<PwShellTrapStatement> getTrapStatementList();

  @NotNull
  List<PwShellTryStatement> getTryStatementList();

  @Nullable
  PwShellTypeLiteral getTypeLiteral();

  @NotNull
  List<PwShellVariable> getVariableList();

  @NotNull
  List<PwShellWhileStatement> getWhileStatementList();

}
