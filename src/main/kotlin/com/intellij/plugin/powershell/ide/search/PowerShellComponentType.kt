package com.intellij.plugin.powershell.ide.search

import com.intellij.icons.AllIcons
import com.intellij.plugin.powershell.PowerShellIcons
import com.intellij.plugin.powershell.lang.lsp.psi.LSPWrapperPsiElement
import com.intellij.plugin.powershell.psi.*
import com.intellij.plugin.powershell.psi.impl.PowerShellFile
import com.intellij.psi.PsiElement
import com.intellij.util.PlatformIcons
import org.eclipse.lsp4j.CompletionItemKind
import javax.swing.Icon

/**
 * Andrey 24/08/17.
 */
enum class PowerShellComponentType(icon: Icon) {

  CLASS(PlatformIcons.CLASS_ICON),
  INTERFACE(PlatformIcons.INTERFACE_ICON),
  MODULE(AllIcons.Nodes.Module),
  TEXT(AllIcons.FileTypes.Text),

  ENUM(PlatformIcons.ENUM_ICON),
  ENUM_LABEL(PlatformIcons.FIELD_ICON),
  METHOD(PlatformIcons.METHOD_ICON),
  CONSTRUCTOR(PlatformIcons.METHOD_ICON),
  PROPERTY(PlatformIcons.FIELD_ICON),
  FUNCTION(PlatformIcons.FUNCTION_ICON),
  VARIABLE(PlatformIcons.VARIABLE_ICON),
  FILE(PowerShellIcons.FILE),
  GENERIC_FILE(PlatformIcons.CUSTOM_FILE_ICON),
  DSC_CONFIGURATION(AllIcons.General.ExternalToolsSmall);

  companion object {
    fun typeOf(element: PsiElement): PowerShellComponentType? {
      return when (element) {
        is PowerShellFunctionStatement -> FUNCTION
        is PowerShellPropertyDeclarationStatement -> PROPERTY
        is PowerShellVariable -> VARIABLE
        is PowerShellClassDeclarationStatement -> CLASS
        is PowerShellEnumDeclarationStatement -> ENUM
        is PowerShellEnumLabelDeclaration -> ENUM_LABEL
        is PowerShellMethodDeclarationStatement -> METHOD
        is PowerShellConstructorDeclarationStatement -> CONSTRUCTOR
        is PowerShellConfigurationBlock -> DSC_CONFIGURATION
        is PowerShellFile -> FILE
        is LSPWrapperPsiElement -> {
          mapLspToPowerShellKind(element.getCompletionKind())
        }
        else -> null
      }
    }

    private fun mapLspToPowerShellKind(kind: CompletionItemKind): PowerShellComponentType? {
      return when(kind) {
        CompletionItemKind.Method -> METHOD
        CompletionItemKind.Function -> FUNCTION
        CompletionItemKind.Constructor -> CONSTRUCTOR
        CompletionItemKind.Field -> PROPERTY
        CompletionItemKind.Variable -> VARIABLE
        CompletionItemKind.Class -> CLASS
        CompletionItemKind.Interface -> INTERFACE
        CompletionItemKind.Module -> MODULE
        CompletionItemKind.Property -> PROPERTY
        CompletionItemKind.Enum -> ENUM
        CompletionItemKind.File -> GENERIC_FILE
        CompletionItemKind.Text -> TEXT
//        CompletionItemKind.Color -> TODO()
//        CompletionItemKind.Snippet -> TODO()
//        CompletionItemKind.Keyword -> TODO()
//        CompletionItemKind.Reference -> TODO()
//        CompletionItemKind.Value -> TODO()
//        CompletionItemKind.Unit -> TODO()
        else -> null
      }
    }
  }

  private val myIcon = icon

  fun getIcon(): Icon {
    return myIcon
  }


}