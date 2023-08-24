package com.intellij.plugin.powershell.lang.lsp.ide.settings

import com.intellij.execution.configurations.PathEnvironmentVariableUtil
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.TextComponentAccessor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getEditorServicesModuleVersion
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getEditorServicesStartupScript
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.getPSExtensionModulesDir
import com.intellij.plugin.powershell.lang.lsp.languagehost.PowerShellExtensionError
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.UIUtil
import org.jetbrains.annotations.Contract

object FormUIUtil {

    @JvmStatic
    @Throws(ConfigurationException::class)
    fun getEditorServicesVersion(editorServicesDir: String): String {
        if (!FileUtil.exists(editorServicesDir)) throw ConfigurationException(
            "Editor Services directory '$editorServicesDir' does not exist."
        )
        val editorServicesVersion: String
        try {
            editorServicesVersion = getEditorServicesModuleVersion(getPSExtensionModulesDir(editorServicesDir))
        } catch (e: PowerShellExtensionError) {
            PowerShellConfigurable.LOG.warn(
                "Error detecting PowerShell Editor Services module version: " + e.message,
                e
            )
            throw ConfigurationException("Can not detect Editor Services module version" + e.message)
        }
        if (StringUtil.isEmpty(editorServicesVersion)) throw ConfigurationException("Can not detect Editor Services module version")
        val startupScript = getEditorServicesStartupScript(editorServicesDir)
        if (StringUtil.isEmpty(startupScript)) throw ConfigurationException("Can not find Editor Services startup script")
        return editorServicesVersion
    }

    @Contract("null -> fail")
    @JvmStatic
    fun validatePowerShellExecutablePath(powerShellExePath: String) {
        val exists = if (FileUtil.isAbsolute(powerShellExePath)) {
            FileUtil.exists(powerShellExePath)
        } else {
            PathEnvironmentVariableUtil.findExecutableInPathOnAnyOS(powerShellExePath) != null
        }
        if (!exists) {
            throw ConfigurationException(
                MessagesBundle.message("settings.errors.executable-not-found", powerShellExePath)
            )
        }
    }

    @JvmStatic
    fun createTextFieldWithBrowseButton(
        description: String,
        field: JBTextField?,
        fileChooserDescriptor: FileChooserDescriptor
    ): TextFieldWithBrowseButton {
        val textFieldWithBrowseButton = TextFieldWithBrowseButton(field)
        fileChooserDescriptor.withShowHiddenFiles(true)
        val textField = textFieldWithBrowseButton.childComponent
        textField.setDisabledTextColor(UIUtil.getLabelDisabledForeground())
        textFieldWithBrowseButton.addBrowseFolderListener(
            description,
            null,
            null,
            fileChooserDescriptor,
            TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT
        )
        FileChooserFactory.getInstance().installFileCompletion(textField, fileChooserDescriptor, true, null)
        return textFieldWithBrowseButton
    }

    val globalSettingsExecutablePath: String?
        get() = ApplicationManager.getApplication().getComponent(LSPInitMain::class.java).state.powerShellExePath
}
