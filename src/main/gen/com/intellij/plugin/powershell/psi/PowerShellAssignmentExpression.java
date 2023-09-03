// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PowerShellAssignmentExpression extends PowerShellExpression {

  @Nullable
  PowerShellClassDeclarationStatement getClassDeclarationStatement();

  @NotNull
  List<PowerShellComment> getCommentList();

  @NotNull
  List<PowerShellDataStatement> getDataStatementList();

  @Nullable
  PowerShellDoStatement getDoStatement();

  @Nullable
  PowerShellEnumDeclarationStatement getEnumDeclarationStatement();

  @NotNull
  List<PowerShellExpression> getExpressionList();

  @Nullable
  PowerShellFlowControlStatement getFlowControlStatement();

  @Nullable
  PowerShellForStatement getForStatement();

  @Nullable
  PowerShellForeachStatement getForeachStatement();

  @Nullable
  PowerShellFunctionStatement getFunctionStatement();

  @Nullable
  PowerShellIfStatement getIfStatement();

  @NotNull
  List<PowerShellInlinescriptStatement> getInlinescriptStatementList();

  @Nullable
  PowerShellLabel getLabel();

  @NotNull
  List<PowerShellNodeBlock> getNodeBlockList();

  @NotNull
  List<PowerShellParallelStatement> getParallelStatementList();

  @NotNull
  List<PowerShellPipelineTail> getPipelineTailList();

  @NotNull
  List<PowerShellRedirection> getRedirectionList();

  @NotNull
  List<PowerShellResourceBlock> getResourceBlockList();

  @NotNull
  List<PowerShellSequenceStatement> getSequenceStatementList();

  @Nullable
  PowerShellSwitchStatement getSwitchStatement();

  @NotNull
  List<PowerShellTrapStatement> getTrapStatementList();

  @NotNull
  List<PowerShellTryStatement> getTryStatementList();

  @NotNull
  List<PowerShellUsingStatement> getUsingStatementList();

  @NotNull
  List<PowerShellVerbatimCommandArgument> getVerbatimCommandArgumentList();

  @Nullable
  PowerShellWhileStatement getWhileStatement();

  List<PowerShellTargetVariableExpression> getTargetVariables();

  @Nullable PowerShellPsiElement getRHSElement();

}
