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

public class PowerShellCommandCallExpressionImplGen extends PowerShellCommandCallExpressionImpl implements PowerShellCommandCallExpression {

  public PowerShellCommandCallExpressionImplGen(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitCommandCallExpression(this);
  }

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
  public PowerShellCommandName getCommandName() {
    return findChildByClass(PowerShellCommandName.class);
  }

  @Override
  @NotNull
  public List<PowerShellCommandParameter> getCommandParameterList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellCommandParameter.class);
  }

  @Override
  @NotNull
  public List<PowerShellExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellExpression.class);
  }

  @Override
  @NotNull
  public List<PowerShellRedirection> getRedirectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellRedirection.class);
  }

}
