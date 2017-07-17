// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellExpressionWithUnaryOperator;
import com.intellij.plugin.powershell.psi.PwShellUnaryExpression;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PwShellUnaryExpressionImplGen extends PwShellExpressionImplGen implements PwShellUnaryExpression {

  public PwShellUnaryExpressionImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitUnaryExpression(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PwShellExpressionWithUnaryOperator getExpressionWithUnaryOperator() {
    return findChildByClass(PwShellExpressionWithUnaryOperator.class);
  }

}
