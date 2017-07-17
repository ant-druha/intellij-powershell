// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PwShellExpressionWithUnaryOperator extends PwShellPsiElement {

  @NotNull
  PwShellExpression getExpression();

  @Nullable
  PwShellTypeLiteral getTypeLiteral();

  @Nullable
  PsiElement getNls();

}
