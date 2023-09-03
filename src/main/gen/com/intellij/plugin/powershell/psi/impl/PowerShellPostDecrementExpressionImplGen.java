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

public class PowerShellPostDecrementExpressionImplGen extends PowerShellExpressionImplGen implements PowerShellPostDecrementExpression {

  public PowerShellPostDecrementExpressionImplGen(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitPostDecrementExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PowerShellExpression getExpression() {
    return findNotNullChildByClass(PowerShellExpression.class);
  }

}
