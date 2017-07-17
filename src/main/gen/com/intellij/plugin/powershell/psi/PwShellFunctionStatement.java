// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PwShellFunctionStatement extends PwShellPsiElement {

  @NotNull
  PwShellCommandArgument getCommandArgument();

  @NotNull
  List<PwShellExpression> getExpressionList();

  @NotNull
  PwShellScriptBlock getScriptBlock();

  @NotNull
  List<PwShellSimpleName> getSimpleNameList();

  @NotNull
  List<PwShellTypeLiteral> getTypeLiteralList();

  @NotNull
  List<PwShellTypeName> getTypeNameList();

  @NotNull
  List<PwShellVariable> getVariableList();

}
