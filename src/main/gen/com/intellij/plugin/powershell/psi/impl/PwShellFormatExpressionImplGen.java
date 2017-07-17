// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellExpression;
import com.intellij.plugin.powershell.psi.PwShellFormatExpression;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PwShellFormatExpressionImplGen extends PwShellExpressionImplGen implements PwShellFormatExpression {

  public PwShellFormatExpressionImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitFormatExpression(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PwShellExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellExpression.class);
  }

}
