// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.PwShellPipeline;
import com.intellij.plugin.powershell.psi.PwShellStatementBlock;
import com.intellij.plugin.powershell.psi.PwShellVisitor;
import com.intellij.plugin.powershell.psi.PwShellWhileStatement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PwShellWhileStatementImplGen extends PwShellPsiElementImpl implements PwShellWhileStatement {

  public PwShellWhileStatementImplGen(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PwShellVisitor visitor) {
    visitor.visitWhileStatement(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PwShellVisitor) accept((PwShellVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PwShellPipeline getPipeline() {
    return findChildByClass(PwShellPipeline.class);
  }

  @Override
  @Nullable
  public PwShellStatementBlock getStatementBlock() {
    return findChildByClass(PwShellStatementBlock.class);
  }

}
