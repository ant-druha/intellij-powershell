// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PowerShellDataStatement extends PowerShellPsiElement {

  @Nullable
  PowerShellBlockBody getBlockBody();

  @NotNull
  List<PowerShellCommandName> getCommandNameList();

  @NotNull
  List<PowerShellComment> getCommentList();

  @NotNull
  List<PowerShellExpression> getExpressionList();

  @Nullable
  PowerShellIdentifier getIdentifier();

}
