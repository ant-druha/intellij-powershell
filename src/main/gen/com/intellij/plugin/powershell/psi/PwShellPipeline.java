// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PwShellPipeline extends PwShellPsiElement {

  @NotNull
  List<PwShellCommand> getCommandList();

  @NotNull
  List<PwShellCommandArgument> getCommandArgumentList();

  @Nullable
  PwShellExpression getExpression();

  @Nullable
  PwShellVerbatimCommandArgument getVerbatimCommandArgument();

}
