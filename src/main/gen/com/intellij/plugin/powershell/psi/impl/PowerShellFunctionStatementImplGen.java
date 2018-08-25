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

public class PowerShellFunctionStatementImplGen extends PowerShellCallableDeclarationImpl implements PowerShellFunctionStatement {

  public PowerShellFunctionStatementImplGen(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitFunctionStatement(this);
  }

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
  @Nullable
  public PowerShellExpression getExpression() {
    return findChildByClass(PowerShellExpression.class);
  }

  @Override
  @Nullable
  public PowerShellIdentifier getIdentifier() {
    return findChildByClass(PowerShellIdentifier.class);
  }

  @Override
  @Nullable
  public PowerShellParameterClause getParameterClause() {
    return findChildByClass(PowerShellParameterClause.class);
  }

  @Override
  @Nullable
  public PsiElement getSimpleId() {
    return findChildByType(SIMPLE_ID);
  }

}
