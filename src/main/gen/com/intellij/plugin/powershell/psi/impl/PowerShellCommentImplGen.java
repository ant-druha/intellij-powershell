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

public class PowerShellCommentImplGen extends PowerShellPsiElementImpl implements PowerShellComment {

  public PowerShellCommentImplGen(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitComment(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PowerShellArrayLiteralExpression> getArrayLiteralExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellArrayLiteralExpression.class);
  }

  @Override
  @NotNull
  public List<PowerShellCommandArgument> getCommandArgumentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellCommandArgument.class);
  }

  @Override
  @Nullable
  public PsiElement getDelimitedComment() {
    return findChildByType(DELIMITED_COMMENT);
  }

  @Override
  @Nullable
  public PsiElement getSingleLineComment() {
    return findChildByType(SINGLE_LINE_COMMENT);
  }

}
