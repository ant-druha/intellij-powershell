// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PwShellAssignmentExpression extends PwShellExpression {

  @NotNull
  List<PwShellCommandArgument> getCommandArgumentList();

  @Nullable
  PwShellDataStatement getDataStatement();

  @NotNull
  List<PwShellExpression> getExpressionList();

  @Nullable
  PwShellFlowControlStatement getFlowControlStatement();

  @Nullable
  PwShellFunctionStatement getFunctionStatement();

  @Nullable
  PwShellInlinescriptStatement getInlinescriptStatement();

  @Nullable
  PwShellParallelStatement getParallelStatement();

  @NotNull
  List<PwShellPipeline> getPipelineList();

  @NotNull
  List<PwShellSep> getSepList();

  @Nullable
  PwShellSequenceStatement getSequenceStatement();

  @NotNull
  List<PwShellStatementBlock> getStatementBlockList();

  @Nullable
  PwShellTrapStatement getTrapStatement();

  @Nullable
  PwShellTryStatement getTryStatement();

  @Nullable
  PwShellVariable getVariable();

  @Nullable
  PwShellWhileStatement getWhileStatement();

  @Nullable
  PsiElement getAlnum();

  @Nullable
  PsiElement getLetters();

}
