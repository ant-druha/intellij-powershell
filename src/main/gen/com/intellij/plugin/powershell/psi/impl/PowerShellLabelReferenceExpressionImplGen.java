// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.*;
import com.intellij.plugin.powershell.psi.impl.mixin.PowerShellLabelReferenceExpressionMixin;
import com.intellij.plugin.powershell.psi.*;

public class PowerShellLabelReferenceExpressionImplGen extends PowerShellLabelReferenceExpressionMixin implements PowerShellLabelReferenceExpression {

  public PowerShellLabelReferenceExpressionImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitLabelReferenceExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PowerShellExpression getExpression() {
    return findChildByClass(PowerShellExpression.class);
  }

  @Override
  @Nullable
  public PowerShellIdentifier getIdentifier() {
    return findChildByClass(PowerShellIdentifier.class);
  }

}
