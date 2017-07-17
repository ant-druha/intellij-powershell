// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PwShellDataStatementImplGen extends PwShellPsiElementImpl implements PwShellDataStatement {

  public PwShellDataStatementImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitDataStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PwShellCommandName> getCommandNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellCommandName.class);
  }

  @Override
  @NotNull
  public List<PwShellExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellExpression.class);
  }

  @Override
  @NotNull
  public PwShellSimpleName getSimpleName() {
    return findNotNullChildByClass(PwShellSimpleName.class);
  }

  @Override
  @NotNull
  public PwShellStatementBlock getStatementBlock() {
    return findNotNullChildByClass(PwShellStatementBlock.class);
  }

}
