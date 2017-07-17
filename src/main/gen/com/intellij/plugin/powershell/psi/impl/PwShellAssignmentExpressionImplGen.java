// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.ALNUM;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.LETTERS;

public class PwShellAssignmentExpressionImplGen extends PwShellExpressionImplGen implements PwShellAssignmentExpression {

  public PwShellAssignmentExpressionImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitAssignmentExpression(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PwShellCommandArgument> getCommandArgumentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellCommandArgument.class);
  }

  @Override
  @Nullable
  public PwShellDataStatement getDataStatement() {
    return findChildByClass(PwShellDataStatement.class);
  }

  @Override
  @NotNull
  public List<PwShellExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellExpression.class);
  }

  @Override
  @Nullable
  public PwShellFlowControlStatement getFlowControlStatement() {
    return findChildByClass(PwShellFlowControlStatement.class);
  }

  @Override
  @Nullable
  public PwShellFunctionStatement getFunctionStatement() {
    return findChildByClass(PwShellFunctionStatement.class);
  }

  @Override
  @Nullable
  public PwShellInlinescriptStatement getInlinescriptStatement() {
    return findChildByClass(PwShellInlinescriptStatement.class);
  }

  @Override
  @Nullable
  public PwShellParallelStatement getParallelStatement() {
    return findChildByClass(PwShellParallelStatement.class);
  }

  @Override
  @NotNull
  public List<PwShellPipeline> getPipelineList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellPipeline.class);
  }

  @Override
  @NotNull
  public List<PwShellSep> getSepList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellSep.class);
  }

  @Override
  @Nullable
  public PwShellSequenceStatement getSequenceStatement() {
    return findChildByClass(PwShellSequenceStatement.class);
  }

  @Override
  @NotNull
  public List<PwShellStatementBlock> getStatementBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellStatementBlock.class);
  }

  @Override
  @Nullable
  public PwShellTrapStatement getTrapStatement() {
    return findChildByClass(PwShellTrapStatement.class);
  }

  @Override
  @Nullable
  public PwShellTryStatement getTryStatement() {
    return findChildByClass(PwShellTryStatement.class);
  }

  @Override
  @Nullable
  public PwShellVariable getVariable() {
    return findChildByClass(PwShellVariable.class);
  }

  @Override
  @Nullable
  public PwShellWhileStatement getWhileStatement() {
    return findChildByClass(PwShellWhileStatement.class);
  }

  @Override
  @Nullable
  public PsiElement getAlnum() {
    return findChildByType(ALNUM);
  }

  @Override
  @Nullable
  public PsiElement getLetters() {
    return findChildByType(LETTERS);
  }

}
