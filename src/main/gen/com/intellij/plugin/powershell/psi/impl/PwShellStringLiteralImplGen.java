// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellStringLiteral;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.*;

public class PwShellStringLiteralImplGen extends PwShellPsiElementImpl implements PwShellStringLiteral {

  public PwShellStringLiteralImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitStringLiteral(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getExpandableHereString() {
    return findChildByType(EXPANDABLE_HERE_STRING);
  }

  @Override
  @Nullable
  public PsiElement getStringDq() {
    return findChildByType(STRING_DQ);
  }

  @Override
  @Nullable
  public PsiElement getStringSq() {
    return findChildByType(STRING_SQ);
  }

  @Override
  @Nullable
  public PsiElement getVerbatimHereString() {
    return findChildByType(VERBATIM_HERE_STRING);
  }

}
