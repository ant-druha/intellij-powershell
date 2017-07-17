// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PwShellDataStatement extends PwShellPsiElement {

  @NotNull
  List<PwShellCommandName> getCommandNameList();

  @NotNull
  List<PwShellExpression> getExpressionList();

  @NotNull
  PwShellSimpleName getSimpleName();

  @NotNull
  PwShellStatementBlock getStatementBlock();

}
