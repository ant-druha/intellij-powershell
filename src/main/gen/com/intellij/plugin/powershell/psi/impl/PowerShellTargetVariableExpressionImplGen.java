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

public class PowerShellTargetVariableExpressionImplGen extends PowerShellTargetVariableImpl implements PowerShellTargetVariableExpression {

  public PowerShellTargetVariableExpressionImplGen(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitTargetVariableExpression(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PowerShellIdentifier getIdentifier() {
    return findNotNullChildByClass(PowerShellIdentifier.class);
  }

  @Override
  @Nullable
  public PsiElement getSimpleId() {
    return findChildByType(SIMPLE_ID);
  }

}
