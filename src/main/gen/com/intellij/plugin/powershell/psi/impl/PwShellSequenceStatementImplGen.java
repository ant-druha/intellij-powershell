// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellSequenceStatement;
import com.intellij.plugin.powershell.psi.PwShellStatementBlock;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class PwShellSequenceStatementImplGen extends PwShellPsiElementImpl implements PwShellSequenceStatement {

  public PwShellSequenceStatementImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitSequenceStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PwShellStatementBlock getStatementBlock() {
    return findNotNullChildByClass(PwShellStatementBlock.class);
  }

}
