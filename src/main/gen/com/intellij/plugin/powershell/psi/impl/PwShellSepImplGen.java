// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellSep;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.LF;
import static com.intellij.plugin.powershell.psi.PowerShellTypes.NLS;

public class PwShellSepImplGen extends PwShellPsiElementImpl implements PwShellSep {

  public PwShellSepImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitSep(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getLf() {
    return findChildByType(LF);
  }

  @Override
  @Nullable
  public PsiElement getNls() {
    return findChildByType(NLS);
  }

}
