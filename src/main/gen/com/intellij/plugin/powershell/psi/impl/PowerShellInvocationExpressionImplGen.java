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

public class PowerShellInvocationExpressionImplGen extends PowerShellInvocationExpressionImpl implements PowerShellInvocationExpression {

  public PowerShellInvocationExpressionImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitInvocationExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PowerShellComment> getCommentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellComment.class);
  }

  @Override
  @NotNull
  public List<PowerShellExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellExpression.class);
  }

  @Override
  @Nullable
  public PowerShellParenthesizedArgumentList getParenthesizedArgumentList() {
    return findChildByClass(PowerShellParenthesizedArgumentList.class);
  }

  @Override
  @Nullable
  public PowerShellReferenceIdentifier getReferenceIdentifier() {
    return findChildByClass(PowerShellReferenceIdentifier.class);
  }

  @Override
  @Nullable
  public PsiElement getDash() {
    return findChildByType(DASH);
  }

}
