// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellCommandArgument;
import com.intellij.plugin.powershell.psi.PwShellCommandName;
import com.intellij.plugin.powershell.psi.PwShellExpression;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PwShellCommandArgumentImplGen extends PwShellPsiElementImpl implements PwShellCommandArgument {

  public PwShellCommandArgumentImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitCommandArgument(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PwShellCommandName getCommandName() {
    return findChildByClass(PwShellCommandName.class);
  }

  @Override
  @Nullable
  public PwShellExpression getExpression() {
    return findChildByClass(PwShellExpression.class);
  }

}
