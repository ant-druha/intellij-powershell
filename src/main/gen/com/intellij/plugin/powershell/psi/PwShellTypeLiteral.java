// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PwShellTypeLiteral extends PwShellPsiElement {

  @NotNull
  List<PwShellTypeName> getTypeNameList();

  @Nullable
  PsiElement getNls();

}
