// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PowerShellCommandCallExpression extends PowerShellExpression, PowerShellReferencePsiElement {

  @NotNull
  List<PowerShellCommandArgument> getCommandArgumentList();

  @Nullable
  PowerShellCommandName getCommandName();

  @NotNull
  List<PowerShellCommandParameter> getCommandParameterList();

  @NotNull
  List<PowerShellExpression> getExpressionList();

  @NotNull
  List<PowerShellRedirection> getRedirectionList();

}
