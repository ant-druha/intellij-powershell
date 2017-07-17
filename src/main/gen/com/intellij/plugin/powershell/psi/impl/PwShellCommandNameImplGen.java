// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellCommandName;
import com.intellij.plugin.powershell.psi.PwShellVariable;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.VAR_SIMPLE;

public class PwShellCommandNameImplGen extends PwShellPsiElementImpl implements PwShellCommandName {

  public PwShellCommandNameImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitCommandName(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PwShellVariable getVariable() {
    return findChildByClass(PwShellVariable.class);
  }

  @Override
  @Nullable
  public PsiElement getVarSimple() {
    return findChildByType(VAR_SIMPLE);
  }

}
