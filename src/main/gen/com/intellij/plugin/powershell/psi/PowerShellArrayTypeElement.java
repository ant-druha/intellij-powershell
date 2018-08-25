// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

public interface PowerShellArrayTypeElement extends PowerShellTypeElement {

  @NotNull
  List<PowerShellComment> getCommentList();

  @NotNull
  PowerShellReferenceTypeElement getReferenceTypeElement();

  @NotNull
  PowerShellType getType();

}
