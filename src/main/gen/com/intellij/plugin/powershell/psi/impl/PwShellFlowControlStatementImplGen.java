// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.*;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PwShellFlowControlStatementImplGen extends PwShellPsiElementImpl implements PwShellFlowControlStatement {

  public PwShellFlowControlStatementImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitFlowControlStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PwShellExpression getExpression() {
    return findChildByClass(PwShellExpression.class);
  }

  @Override
  @Nullable
  public PwShellPipeline getPipeline() {
    return findChildByClass(PwShellPipeline.class);
  }

  @Override
  @Nullable
  public PwShellSimpleName getSimpleName() {
    return findChildByClass(PwShellSimpleName.class);
  }

}
