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

public class PowerShellStringLiteralExpressionImplGen extends PowerShellAbstractStringLiteralExpressionImpl implements PowerShellStringLiteralExpression {

  public PowerShellStringLiteralExpressionImplGen(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitStringLiteralExpression(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PowerShellExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellExpression.class);
  }

  @Override
  @Nullable
  public PsiElement getVerbatimHereString() {
    return findChildByType(VERBATIM_HERE_STRING);
  }

  @Override
  @Nullable
  public PsiElement getVerbatimString() {
    return findChildByType(VERBATIM_STRING);
  }

}
