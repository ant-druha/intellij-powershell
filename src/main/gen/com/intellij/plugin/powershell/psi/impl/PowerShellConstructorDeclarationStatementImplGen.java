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

public class PowerShellConstructorDeclarationStatementImplGen extends PowerShellMethodDeclarationImpl implements PowerShellConstructorDeclarationStatement {

  public PowerShellConstructorDeclarationStatementImplGen(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitConstructorDeclarationStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PowerShellBlockBody getBlockBody() {
    return findChildByClass(PowerShellBlockBody.class);
  }

  @Override
  @NotNull
  public List<PowerShellComment> getCommentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellComment.class);
  }

  @Override
  @NotNull
  public PowerShellIdentifier getIdentifier() {
    return findNotNullChildByClass(PowerShellIdentifier.class);
  }

  @Override
  @NotNull
  public List<PowerShellParameterClause> getParameterClauseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PowerShellParameterClause.class);
  }

}
