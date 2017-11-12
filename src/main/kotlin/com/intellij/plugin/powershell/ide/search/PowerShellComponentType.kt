package com.intellij.plugin.powershell.ide.search

import com.intellij.icons.AllIcons
import com.intellij.plugin.powershell.PowerShellIcons
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.impl.PowerShellFile
import com.intellij.psi.PsiElement
import com.intellij.util.PlatformIcons
import javax.swing.Icon

/**
 * Andrey 24/08/17.
 */
enum class PowerShellComponentType(icon: Icon) {

  CLASS(PlatformIcons.CLASS_ICON),
  ENUM(PlatformIcons.ENUM_ICON),
  ENUM_LABEL(PlatformIcons.FIELD_ICON),
  METHOD(PlatformIcons.METHOD_ICON),
  FIELD(PlatformIcons.FIELD_ICON),
  FUNCTION(PlatformIcons.FUNCTION_ICON),
  VARIABLE(PlatformIcons.VARIABLE_ICON),
  FILE(PowerShellIcons.FILE),
  DSC_CONFIGURATION(AllIcons.General.ExternalToolsSmall);

  companion object {
    fun typeOf(element: PsiElement): PowerShellComponentType? {
      return when (element) {
        is PowerShellFunctionStatement -> FUNCTION
        is PowerShellPropertyDeclarationStatement -> FIELD
        is PowerShellVariable -> VARIABLE
        is PowerShellClassDeclarationStatement -> CLASS
        is PowerShellEnumDeclarationStatement -> ENUM
        is PowerShellEnumLabelDeclaration -> ENUM_LABEL
        is PowerShellMethodDeclarationStatement -> METHOD
        is PowerShellConstructorDeclarationStatement -> METHOD
        is PowerShellConfigurationBlock -> DSC_CONFIGURATION
        is PowerShellFile -> FILE
        else -> null
      }
    }
  }

  private val myIcon = icon

  fun getIcon(): Icon {
    return myIcon
  }


}