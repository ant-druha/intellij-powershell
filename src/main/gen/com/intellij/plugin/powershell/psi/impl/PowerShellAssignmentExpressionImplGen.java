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

public class PowerShellAssignmentExpressionImplGen extends PowerShellExpressionImplGen implements PowerShellAssignmentExpression {

  public PowerShellAssignmentExpressionImplGen(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitAssignmentExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PowerShellClassDeclarationStatement getClassDeclarationStatement() {
    return findChildByClass(PowerShellClassDeclarationStatement.class);
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
  @Nullable
  public PowerShellDoStatement getDoStatement() {
    return findChildByClass(PowerShellDoStatement.class);
  }

  @Override
  @Nullable
  public PowerShellEnumDeclarationStatement getEnumDeclarationStatement() {
    return findChildByClass(PowerShellEnumDeclarationStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellExpression.class);
  }

  @Override
  @Nullable
  public PowerShellFlowControlStatement getFlowControlStatement() {
    return findChildByClass(PowerShellFlowControlStatement.class);
  }

  @Override
  @Nullable
  public PowerShellForStatement getForStatement() {
    return findChildByClass(PowerShellForStatement.class);
  }

  @Override
  @Nullable
  public PowerShellForeachStatement getForeachStatement() {
    return findChildByClass(PowerShellForeachStatement.class);
  }

  @Override
  @Nullable
  public PowerShellFunctionStatement getFunctionStatement() {
    return findChildByClass(PowerShellFunctionStatement.class);
  }

  @Override
  @Nullable
  public PowerShellIfStatement getIfStatement() {
    return findChildByClass(PowerShellIfStatement.class);
  }

  @Override
  @NotNull
  public List<PowerShellInlinescriptStatement> getInlinescriptStatementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellInlinescriptStatement.class);
  }

  @Override
  @Nullable
  public PowerShellLabel getLabel() {
    return findChildByClass(PowerShellLabel.class);
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
  @Nullable
  public PowerShellSwitchStatement getSwitchStatement() {
    return findChildByClass(PowerShellSwitchStatement.class);
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
  @Nullable
  public PowerShellWhileStatement getWhileStatement() {
    return findChildByClass(PowerShellWhileStatement.class);
  }

  @Override
  public List<PowerShellTargetVariableExpression> getTargetVariables() {
    return PowerShellPsiImplUtil.getTargetVariables(this);
  }

  @Override
  public @Nullable PowerShellPsiElement getRHSElement() {
    return PowerShellPsiImplUtil.getRHSElement(this);
  }

}
