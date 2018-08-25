// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PowerShellRedirection extends PowerShellPsiElement {

  @NotNull
  List<PowerShellCommandArgument> getCommandArgumentList();

  @Nullable
  PowerShellExpression getExpression();

  @Nullable
  PsiElement getOpFr();

  @Nullable
  PsiElement getOpMr();

}
