// This is a generated file. Not intended for manual editing.
package com.intellij.plugin.powershell.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.plugin.powershell.psi.types.PowerShellTypedElement;

public class PowerShellVisitor extends PsiElementVisitor {

  public void visitAdditiveExpression(@NotNull PowerShellAdditiveExpression o) {
    visitExpression(o);
  }

  public void visitArrayExpression(@NotNull PowerShellArrayExpression o) {
    visitExpression(o);
  }

  public void visitArrayLiteralExpression(@NotNull PowerShellArrayLiteralExpression o) {
    visitExpression(o);
  }

  public void visitArrayTypeElement(@NotNull PowerShellArrayTypeElement o) {
    visitTypeElement(o);
  }

  public void visitAssignmentExpression(@NotNull PowerShellAssignmentExpression o) {
    visitExpression(o);
  }

  public void visitAttribute(@NotNull PowerShellAttribute o) {
    visitPsiElement(o);
  }

  public void visitAttributeArgument(@NotNull PowerShellAttributeArgument o) {
    visitPsiElement(o);
  }

  public void visitBitwiseExpression(@NotNull PowerShellBitwiseExpression o) {
    visitExpression(o);
  }

  public void visitBlockBody(@NotNull PowerShellBlockBody o) {
    visitPsiElement(o);
  }

  public void visitBlockParameterClause(@NotNull PowerShellBlockParameterClause o) {
    visitPsiElement(o);
  }

  public void visitCastExpression(@NotNull PowerShellCastExpression o) {
    visitExpression(o);
  }

  public void visitCatchClause(@NotNull PowerShellCatchClause o) {
    visitPsiElement(o);
  }

  public void visitClassDeclarationStatement(@NotNull PowerShellClassDeclarationStatement o) {
    visitClassDeclaration(o);
  }

  public void visitCommandArgument(@NotNull PowerShellCommandArgument o) {
    visitPsiElement(o);
  }

  public void visitCommandCallExpression(@NotNull PowerShellCommandCallExpression o) {
    visitExpression(o);
    // visitReferencePsiElement(o);
  }

  public void visitCommandName(@NotNull PowerShellCommandName o) {
    visitPsiElement(o);
  }

  public void visitCommandParameter(@NotNull PowerShellCommandParameter o) {
    visitPsiElement(o);
  }

  public void visitComment(@NotNull PowerShellComment o) {
    visitPsiElement(o);
  }

  public void visitComparisonExpression(@NotNull PowerShellComparisonExpression o) {
    visitExpression(o);
  }

  public void visitConfigurationBlock(@NotNull PowerShellConfigurationBlock o) {
    visitComponent(o);
  }

  public void visitConstructorDeclarationStatement(@NotNull PowerShellConstructorDeclarationStatement o) {
    visitMemberDeclaration(o);
    // visitCallableDeclaration(o);
  }

  public void visitDataStatement(@NotNull PowerShellDataStatement o) {
    visitPsiElement(o);
  }

  public void visitDoStatement(@NotNull PowerShellDoStatement o) {
    visitPsiElement(o);
  }

  public void visitElementAccessExpression(@NotNull PowerShellElementAccessExpression o) {
    visitExpression(o);
  }

  public void visitElseClause(@NotNull PowerShellElseClause o) {
    visitPsiElement(o);
  }

  public void visitElseifClause(@NotNull PowerShellElseifClause o) {
    visitPsiElement(o);
  }

  public void visitEnumDeclarationStatement(@NotNull PowerShellEnumDeclarationStatement o) {
    visitEnumDeclaration(o);
  }

  public void visitEnumLabelDeclaration(@NotNull PowerShellEnumLabelDeclaration o) {
    visitMemberDeclaration(o);
  }

  public void visitExpression(@NotNull PowerShellExpression o) {
    visitTypedElement(o);
  }

  public void visitFinallyClause(@NotNull PowerShellFinallyClause o) {
    visitPsiElement(o);
  }

  public void visitFlowControlStatement(@NotNull PowerShellFlowControlStatement o) {
    visitPsiElement(o);
  }

  public void visitForClause(@NotNull PowerShellForClause o) {
    visitPsiElement(o);
  }

  public void visitForStatement(@NotNull PowerShellForStatement o) {
    visitPsiElement(o);
  }

  public void visitForeachStatement(@NotNull PowerShellForeachStatement o) {
    visitPsiElement(o);
  }

  public void visitFormatExpression(@NotNull PowerShellFormatExpression o) {
    visitExpression(o);
  }

  public void visitFunctionStatement(@NotNull PowerShellFunctionStatement o) {
    visitCallableDeclaration(o);
  }

  public void visitGenericTypeElement(@NotNull PowerShellGenericTypeElement o) {
    visitTypeElement(o);
  }

  public void visitHashLiteralExpression(@NotNull PowerShellHashLiteralExpression o) {
    visitExpression(o);
  }

  public void visitIdentifier(@NotNull PowerShellIdentifier o) {
    visitPsiElement(o);
  }

  public void visitIfStatement(@NotNull PowerShellIfStatement o) {
    visitPsiElement(o);
  }

  public void visitIncompleteDeclaration(@NotNull PowerShellIncompleteDeclaration o) {
    visitPsiElement(o);
  }

  public void visitInlinescriptStatement(@NotNull PowerShellInlinescriptStatement o) {
    visitPsiElement(o);
  }

  public void visitIntegerLiteralExpression(@NotNull PowerShellIntegerLiteralExpression o) {
    visitExpression(o);
  }

  public void visitInvocationExpression(@NotNull PowerShellInvocationExpression o) {
    visitExpression(o);
    // visitQualifiedReferenceElement(o);
  }

  public void visitKeyExpression(@NotNull PowerShellKeyExpression o) {
    visitExpression(o);
  }

  public void visitLabel(@NotNull PowerShellLabel o) {
    visitComponent(o);
  }

  public void visitLabelReferenceExpression(@NotNull PowerShellLabelReferenceExpression o) {
    visitExpression(o);
    // visitReferencePsiElement(o);
  }

  public void visitLogicalExpression(@NotNull PowerShellLogicalExpression o) {
    visitExpression(o);
  }

  public void visitMemberAccessExpression(@NotNull PowerShellMemberAccessExpression o) {
    visitExpression(o);
    // visitQualifiedReferenceElement(o);
  }

  public void visitMethodDeclarationStatement(@NotNull PowerShellMethodDeclarationStatement o) {
    visitMemberDeclaration(o);
    // visitAttributesHolder(o);
    // visitCallableDeclaration(o);
  }

  public void visitMultiplicativeExpression(@NotNull PowerShellMultiplicativeExpression o) {
    visitExpression(o);
  }

  public void visitNodeBlock(@NotNull PowerShellNodeBlock o) {
    visitPsiElement(o);
  }

  public void visitParallelStatement(@NotNull PowerShellParallelStatement o) {
    visitPsiElement(o);
  }

  public void visitParameterClause(@NotNull PowerShellParameterClause o) {
    visitPsiElement(o);
  }

  public void visitParenthesizedArgumentList(@NotNull PowerShellParenthesizedArgumentList o) {
    visitPsiElement(o);
  }

  public void visitParenthesizedExpression(@NotNull PowerShellParenthesizedExpression o) {
    visitExpression(o);
  }

  public void visitPathExpression(@NotNull PowerShellPathExpression o) {
    visitExpression(o);
  }

  public void visitPathItem(@NotNull PowerShellPathItem o) {
    visitPsiElement(o);
  }

  public void visitPipelineTail(@NotNull PowerShellPipelineTail o) {
    visitPsiElement(o);
  }

  public void visitPostDecrementExpression(@NotNull PowerShellPostDecrementExpression o) {
    visitExpression(o);
  }

  public void visitPostIncrementExpression(@NotNull PowerShellPostIncrementExpression o) {
    visitExpression(o);
  }

  public void visitPropertyDeclarationStatement(@NotNull PowerShellPropertyDeclarationStatement o) {
    visitMemberDeclaration(o);
    // visitAttributesHolder(o);
  }

  public void visitRangeExpression(@NotNull PowerShellRangeExpression o) {
    visitExpression(o);
  }

  public void visitRealLiteralExpression(@NotNull PowerShellRealLiteralExpression o) {
    visitExpression(o);
  }

  public void visitRedirection(@NotNull PowerShellRedirection o) {
    visitPsiElement(o);
  }

  public void visitReferenceIdentifier(@NotNull PowerShellReferenceIdentifier o) {
    visitPsiElement(o);
  }

  public void visitReferenceTypeElement(@NotNull PowerShellReferenceTypeElement o) {
    visitTypeElement(o);
    // visitQualifiedReferenceElement(o);
  }

  public void visitResourceBlock(@NotNull PowerShellResourceBlock o) {
    visitPsiElement(o);
  }

  public void visitScriptBlockExpression(@NotNull PowerShellScriptBlockExpression o) {
    visitExpression(o);
  }

  public void visitSequenceStatement(@NotNull PowerShellSequenceStatement o) {
    visitPsiElement(o);
  }

  public void visitStringLiteralExpression(@NotNull PowerShellStringLiteralExpression o) {
    visitExpression(o);
    // visitAbstractStringLiteralExpression(o);
  }

  public void visitSubExpression(@NotNull PowerShellSubExpression o) {
    visitExpression(o);
  }

  public void visitSwitchClauseBlock(@NotNull PowerShellSwitchClauseBlock o) {
    visitPsiElement(o);
  }

  public void visitSwitchStatement(@NotNull PowerShellSwitchStatement o) {
    visitPsiElement(o);
  }

  public void visitTargetVariableExpression(@NotNull PowerShellTargetVariableExpression o) {
    visitExpression(o);
    // visitVariable(o);
  }

  public void visitTrapStatement(@NotNull PowerShellTrapStatement o) {
    visitPsiElement(o);
  }

  public void visitTryStatement(@NotNull PowerShellTryStatement o) {
    visitPsiElement(o);
  }

  public void visitTypeElement(@NotNull PowerShellTypeElement o) {
    visitTypedElement(o);
  }

  public void visitTypeLiteralExpression(@NotNull PowerShellTypeLiteralExpression o) {
    visitExpression(o);
  }

  public void visitUnaryExpression(@NotNull PowerShellUnaryExpression o) {
    visitExpression(o);
  }

  public void visitUsingStatement(@NotNull PowerShellUsingStatement o) {
    visitPsiElement(o);
  }

  public void visitVerbatimCommandArgument(@NotNull PowerShellVerbatimCommandArgument o) {
    visitPsiElement(o);
  }

  public void visitWhileStatement(@NotNull PowerShellWhileStatement o) {
    visitPsiElement(o);
  }

  public void visitCallableDeclaration(@NotNull PowerShellCallableDeclaration o) {
    visitPsiElement(o);
  }

  public void visitClassDeclaration(@NotNull PowerShellClassDeclaration o) {
    visitPsiElement(o);
  }

  public void visitComponent(@NotNull PowerShellComponent o) {
    visitPsiElement(o);
  }

  public void visitEnumDeclaration(@NotNull PowerShellEnumDeclaration o) {
    visitPsiElement(o);
  }

  public void visitMemberDeclaration(@NotNull PowerShellMemberDeclaration o) {
    visitPsiElement(o);
  }

  public void visitTypedElement(@NotNull PowerShellTypedElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PowerShellPsiElement o) {
    visitElement(o);
  }

}
