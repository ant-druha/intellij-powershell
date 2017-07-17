// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellTypeLiteral;
import com.intellij.plugin.powershell.psi.PwShellTypeName;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.plugin.powershell.psi.PowerShellTypes.NLS;

public class PwShellTypeLiteralImplGen extends PwShellPsiElementImpl implements PwShellTypeLiteral {

  public PwShellTypeLiteralImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitTypeLiteral(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PwShellTypeName> getTypeNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellTypeName.class);
  }

  @Override
  @Nullable
  public PsiElement getNls() {
    return findChildByType(NLS);
  }

}
