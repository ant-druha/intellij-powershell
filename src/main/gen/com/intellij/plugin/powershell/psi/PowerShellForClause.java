// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PowerShellForClause extends PowerShellPsiElement {

  @NotNull
  List<PowerShellComment> getCommentList();

  @NotNull
  List<PowerShellExpression> getExpressionList();

  @NotNull
  List<PowerShellPipelineTail> getPipelineTailList();

  @NotNull
  List<PowerShellRedirection> getRedirectionList();

  @NotNull
  List<PowerShellVerbatimCommandArgument> getVerbatimCommandArgumentList();

}
