package com.intellij.plugin.powershell.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.plugin.powershell.psi.*;
import com.intellij.plugin.powershell.psi.types.PowerShellType;
import com.intellij.plugin.powershell.psi.types.impl.PowerShellArrayClassTypeImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Contract;
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
    if (!targets.isEmpty()) result.addAll(targets);
    return result;
  }

  @Nullable
  public static PowerShellPsiElement getRHSElement(@NotNull PowerShellAssignmentExpression assignment) {
    PsiElement child = assignment.getFirstChild();
    if (child == null) return null;
    ASTNode rhsNode = TreeUtil.findSibling(child.getNode(), PowerShellTypes.EQ);
    if (rhsNode != null) {
      return PsiTreeUtil.getNextSiblingOfType(rhsNode.getPsi(), PowerShellPsiElement.class);
    }
    return null;
  }

  @Nullable
  public static PsiElement getIdentifier(@NotNull PowerShellMemberAccessExpression expression) {
    return expression.getReferenceIdentifier();
  }

  @Nullable
  public static PsiElement getIdentifier(@NotNull PowerShellInvocationExpression call) {
    return call.getReferenceIdentifier();
  }

  @NotNull
  public static PowerShellType getType(@NotNull PowerShellLabelReferenceExpression labelreference) {
    return PowerShellType.Companion.getUNKNOWN();
  }

  @NotNull
  public static PowerShellType getType(@NotNull PowerShellArrayTypeElement array) {
    return new PowerShellArrayClassTypeImpl(array);
  }

  @NotNull
  public static PowerShellType getType(@NotNull PowerShellTypeLiteralExpression typeLiteral) {
    return typeLiteral.getTypeElement().getType();
  }

  @NotNull
  public static PowerShellType getCastType(@NotNull PowerShellCastExpression castExpression) {
    PowerShellTypeLiteralExpression typeLiteral = PsiTreeUtil.getChildOfType(castExpression, PowerShellTypeLiteralExpression.class);
    assert typeLiteral != null;
    return typeLiteral.getTypeElement().getType();
  }

  @Contract(pure = true)
  public static boolean isTypeDeclarationContext(@NotNull PsiElement element) {
    PsiElement context = element.getContext();
    return context instanceof PowerShellClassDeclarationStatement || context instanceof PowerShellEnumDeclarationStatement;
  }
}
