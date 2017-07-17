// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PwShellFunctionStatementImplGen extends PwShellPsiElementImpl implements PwShellFunctionStatement {

  public PwShellFunctionStatementImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitFunctionStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PwShellCommandArgument getCommandArgument() {
    return findNotNullChildByClass(PwShellCommandArgument.class);
  }

  @Override
  @NotNull
  public List<PwShellExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellExpression.class);
  }

  @Override
  @NotNull
  public PwShellScriptBlock getScriptBlock() {
    return findNotNullChildByClass(PwShellScriptBlock.class);
  }

  @Override
  @NotNull
  public List<PwShellSimpleName> getSimpleNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellSimpleName.class);
  }

  @Override
  @NotNull
  public List<PwShellTypeLiteral> getTypeLiteralList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellTypeLiteral.class);
  }

  @Override
  @NotNull
  public List<PwShellTypeName> getTypeNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellTypeName.class);
  }

  @Override
  @NotNull
  public List<PwShellVariable> getVariableList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PwShellVariable.class);
  }

}
