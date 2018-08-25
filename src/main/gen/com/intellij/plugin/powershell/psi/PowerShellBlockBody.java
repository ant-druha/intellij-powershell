// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PowerShellBlockBody extends PowerShellPsiElement {

  @NotNull
  List<PowerShellClassDeclarationStatement> getClassDeclarationStatementList();

  @NotNull
  List<PowerShellComment> getCommentList();

  @NotNull
  List<PowerShellDataStatement> getDataStatementList();

  @NotNull
  List<PowerShellDoStatement> getDoStatementList();

  @NotNull
  List<PowerShellEnumDeclarationStatement> getEnumDeclarationStatementList();

  @NotNull
  List<PowerShellExpression> getExpressionList();

  @NotNull
  List<PowerShellFlowControlStatement> getFlowControlStatementList();

  @NotNull
  List<PowerShellForStatement> getForStatementList();

  @NotNull
  List<PowerShellForeachStatement> getForeachStatementList();

  @NotNull
  List<PowerShellFunctionStatement> getFunctionStatementList();

  @NotNull
  List<PowerShellIfStatement> getIfStatementList();

  @NotNull
  List<PowerShellInlinescriptStatement> getInlinescriptStatementList();

  @NotNull
  List<PowerShellLabel> getLabelList();

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

  @NotNull
  List<PowerShellSwitchStatement> getSwitchStatementList();

  @NotNull
  List<PowerShellTrapStatement> getTrapStatementList();

  @NotNull
  List<PowerShellTryStatement> getTryStatementList();

  @NotNull
  List<PowerShellUsingStatement> getUsingStatementList();

  @NotNull
  List<PowerShellVerbatimCommandArgument> getVerbatimCommandArgumentList();

  @NotNull
  List<PowerShellWhileStatement> getWhileStatementList();

}
