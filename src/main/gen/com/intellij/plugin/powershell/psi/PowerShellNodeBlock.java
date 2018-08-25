// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PowerShellNodeBlock extends PowerShellPsiElement {

  @NotNull
  List<PowerShellArrayLiteralExpression> getArrayLiteralExpressionList();

  @NotNull
  List<PowerShellCommandArgument> getCommandArgumentList();

  @NotNull
  List<PowerShellComment> getCommentList();

  @NotNull
  PsiElement getSimpleId();

}
