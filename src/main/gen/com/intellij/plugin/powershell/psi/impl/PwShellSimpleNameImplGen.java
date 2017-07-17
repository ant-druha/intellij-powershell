// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellSimpleName;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.SIMPLE_NAME;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.VAR_SIMPLE;

public class PwShellSimpleNameImplGen extends PwShellPsiElementImpl implements PwShellSimpleName {

  public PwShellSimpleNameImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitSimpleName(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getSimpleName() {
    return findChildByType(SIMPLE_NAME);
  }

  @Override
  @Nullable
  public PsiElement getVarSimple() {
    return findChildByType(VAR_SIMPLE);
  }

}
