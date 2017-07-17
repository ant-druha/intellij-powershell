// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellParenthesizedExpression;
import com.intellij.plugin.powershell.psi.PwShellPipeline;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class PwShellParenthesizedExpressionImplGen extends PwShellExpressionImplGen implements PwShellParenthesizedExpression {

  public PwShellParenthesizedExpressionImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitParenthesizedExpression(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PwShellPipeline getPipeline() {
    return findNotNullChildByClass(PwShellPipeline.class);
  }

}
