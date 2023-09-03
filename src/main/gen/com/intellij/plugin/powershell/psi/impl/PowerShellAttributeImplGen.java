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

public class PowerShellAttributeImplGen extends PowerShellPsiElementImpl implements PowerShellAttribute {

  public PowerShellAttributeImplGen(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitAttribute(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PowerShellComment> getCommentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellComment.class);
  }

  @Override
  @Nullable
  public PowerShellTypeElement getTypeElement() {
    return findChildByClass(PowerShellTypeElement.class);
  }

  @Override
  @Nullable
  public PowerShellTypeLiteralExpression getTypeLiteralExpression() {
    return findChildByClass(PowerShellTypeLiteralExpression.class);
  }

}
