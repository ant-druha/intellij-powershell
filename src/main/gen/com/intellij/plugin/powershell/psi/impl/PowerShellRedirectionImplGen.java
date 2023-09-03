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

public class PowerShellRedirectionImplGen extends PowerShellPsiElementImpl implements PowerShellRedirection {

  public PowerShellRedirectionImplGen(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitRedirection(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PowerShellCommandArgument> getCommandArgumentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellCommandArgument.class);
  }

  @Override
  @Nullable
  public PowerShellExpression getExpression() {
    return findChildByClass(PowerShellExpression.class);
  }

  @Override
  @Nullable
  public PsiElement getOpFr() {
    return findChildByType(OP_FR);
  }

  @Override
  @Nullable
  public PsiElement getOpMr() {
    return findChildByType(OP_MR);
  }

}
