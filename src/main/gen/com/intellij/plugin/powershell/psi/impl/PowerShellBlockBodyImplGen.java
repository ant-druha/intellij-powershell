// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.*;
import com.intellij.plugin.powershell.psi.*;

public class PowerShellBlockBodyImplGen extends PowerShellPsiElementImpl implements PowerShellBlockBody {

  public PowerShellBlockBodyImplGen(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitBlockBody(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PowerShellClassDeclarationStatement> getClassDeclarationStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellClassDeclarationStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellComment> getCommentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellComment.class);
  }

  @Override
  @NotNull
  public List<PowerShellDataStatement> getDataStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellDataStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellDoStatement> getDoStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellDoStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellEnumDeclarationStatement> getEnumDeclarationStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellEnumDeclarationStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellExpression.class);
  }

  @Override
  @NotNull
  public List<PowerShellFlowControlStatement> getFlowControlStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellFlowControlStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellForStatement> getForStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellForStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellForeachStatement> getForeachStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellForeachStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellFunctionStatement> getFunctionStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellFunctionStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellIfStatement> getIfStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellIfStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellInlinescriptStatement> getInlinescriptStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellInlinescriptStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellLabel> getLabelList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellLabel.class);
  }

  @Override
  @NotNull
  public List<PowerShellNodeBlock> getNodeBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellNodeBlock.class);
  }

  @Override
  @NotNull
  public List<PowerShellParallelStatement> getParallelStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellParallelStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellPipelineTail> getPipelineTailList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellPipelineTail.class);
  }

  @Override
  @NotNull
  public List<PowerShellRedirection> getRedirectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellRedirection.class);
  }

  @Override
  @NotNull
  public List<PowerShellResourceBlock> getResourceBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellResourceBlock.class);
  }

  @Override
  @NotNull
  public List<PowerShellSequenceStatement> getSequenceStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellSequenceStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellSwitchStatement> getSwitchStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellSwitchStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellTrapStatement> getTrapStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellTrapStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellTryStatement> getTryStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellTryStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellUsingStatement> getUsingStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellUsingStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellVerbatimCommandArgument> getVerbatimCommandArgumentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellVerbatimCommandArgument.class);
  }

  @Override
  @NotNull
  public List<PowerShellWhileStatement> getWhileStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellWhileStatement.class);
  }

}
