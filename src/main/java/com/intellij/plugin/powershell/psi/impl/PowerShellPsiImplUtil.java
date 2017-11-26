package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Andrey 26/06/17.
 */
public class PowerShellPsiImplUtil {
  private static boolean isWhiteSpace(@NotNull ASTNode node) {
    return node.getElementType() == TokenType.WHITE_SPACE;
  }

  public static boolean isWhiteSpaceOrNls(@NotNull ASTNode node) {
    return isWhiteSpace(node) || node.getElementType() == PowerShellTypes.NLS || node.getElementType() == PowerShellTypes.LF;
  }

  public static List<PowerShellTargetVariableExpression> getTargetVariables(@NotNull PowerShellAssignmentExpression assignment) {
    List<PowerShellTargetVariableExpression> result = new ArrayList<>();
    Collection<PowerShellTargetVariableExpression> targets = PsiTreeUtil.findChildrenOfAnyType(assignment, PowerShellTargetVariableExpression.class);
    if (!targets.isEmpty()) result.addAll(targets); return result;
  }

  @Nullable
  public static PsiElement getMember(@NotNull PowerShellMemberAccessExpression expression) {
    return expression.getReferenceIdentifier();
  }

  @Nullable
  public static PsiElement getMember(@NotNull PowerShellInvocationExpression call) {
    return call.getReferenceIdentifier();
  }

  public static boolean isTypeDeclarationContext(@NotNull PsiElement element) {
    PsiElement context = element.getContext();
    return context instanceof PowerShellClassDeclarationStatement || context instanceof PowerShellEnumDeclarationStatement;
  }
}
