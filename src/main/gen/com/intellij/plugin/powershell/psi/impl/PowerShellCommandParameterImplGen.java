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

public class PowerShellCommandParameterImplGen extends PowerShellPsiElementImpl implements PowerShellCommandParameter {

  public PowerShellCommandParameterImplGen(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PowerShellVisitor visitor) {
    visitor.visitCommandParameter(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PowerShellVisitor) accept((PowerShellVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getCmdParameter() {
    return findChildByType(CMD_PARAMETER);
  }

  @Override
  @Nullable
  public PsiElement getOpC() {
    return findChildByType(OP_C);
  }

}
