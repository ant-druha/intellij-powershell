// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PwShellCommand extends PwShellPsiElement {

  @NotNull
  List<PwShellCommandArgument> getCommandArgumentList();

  @Nullable
  PwShellCommandName getCommandName();

  @NotNull
  List<PwShellCommandParameter> getCommandParameterList();

  @NotNull
  List<PwShellExpression> getExpressionList();

}
