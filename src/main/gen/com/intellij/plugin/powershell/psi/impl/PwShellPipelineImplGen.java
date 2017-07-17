// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PwShellPipelineImplGen extends PwShellPsiElementImpl implements PwShellPipeline {

  public PwShellPipelineImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitPipeline(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PwShellCommand> getCommandList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellCommand.class);
  }

  @Override
  @NotNull
  public List<PwShellCommandArgument> getCommandArgumentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellCommandArgument.class);
  }

  @Override
  @Nullable
  public PwShellExpression getExpression() {
    return findChildByClass(PwShellExpression.class);
  }

  @Override
  @Nullable
  public PwShellVerbatimCommandArgument getVerbatimCommandArgument() {
    return findChildByClass(PwShellVerbatimCommandArgument.class);
  }

}
