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
import com.intellij.plugin.powershell.psi.types.PowerShellType;

public class PowerShellLabelReferenceExpressionImplGen extends PowerShellReferencePsiElementImpl implements PowerShellLabelReferenceExpression {

  public PowerShellLabelReferenceExpressionImplGen(@NotNull ASTNode node) {
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

  @Override
  public @NotNull PowerShellType getType() {
    return PowerShellPsiImplUtil.getType(this);
  }

}
