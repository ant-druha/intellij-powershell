// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellExpression;
import com.intellij.plugin.powershell.psi.PwShellExpressionWithUnaryOperator;
import com.intellij.plugin.powershell.psi.PwShellTypeLiteral;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.NLS;

public class PwShellExpressionWithUnaryOperatorImplGen extends PwShellPsiElementImpl implements PwShellExpressionWithUnaryOperator {

  public PwShellExpressionWithUnaryOperatorImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitExpressionWithUnaryOperator(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PwShellExpression getExpression() {
    return findNotNullChildByClass(PwShellExpression.class);
  }

  @Override
  @Nullable
  public PwShellTypeLiteral getTypeLiteral() {
    return findChildByClass(PwShellTypeLiteral.class);
  }

  @Override
  @Nullable
  public PsiElement getNls() {
    return findChildByType(NLS);
  }

}
