// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PowerShellPropertyDeclarationStatement extends PowerShellMemberDeclaration, PowerShellAttributesHolder {

  @NotNull
  List<PowerShellAttribute> getAttributeList();

  @NotNull
  List<PowerShellComment> getCommentList();

  @NotNull
  PowerShellExpression getExpression();

}
