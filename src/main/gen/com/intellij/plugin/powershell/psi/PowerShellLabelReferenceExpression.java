// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.plugin.powershell.psi.types.PowerShellType;

public interface PowerShellLabelReferenceExpression extends PowerShellExpression, PowerShellReferencePsiElement {

  @Nullable
  PowerShellExpression getExpression();

  @Nullable
  PowerShellIdentifier getIdentifier();

  @NotNull
  PowerShellType getType();

}
