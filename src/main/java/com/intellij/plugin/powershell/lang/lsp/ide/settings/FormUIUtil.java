package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.plugin.powershell.lang.lsp.languagehost.PowerShellExtensionError;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.INSTANCE;

public class FormUIUtil {

    public static String getEditorServicesVersion(@NotNull final String editorServicesDir) throws ConfigurationException {
        if (!FileUtil.exists(editorServicesDir))
            throw new ConfigurationException("Editor Services directory '" + editorServicesDir + "' does not exist.");
        String editorServicesVersion;
        try {
            editorServicesVersion = INSTANCE.getEditorServicesModuleVersion(INSTANCE.getPSExtensionModulesDir(editorServicesDir));
        } catch (PowerShellExtensionError e) {
            PowerShellConfigurable.LOG.warn("Error detecting PowerShell Editor Services module version: " + e.getMessage(), e);
            throw new ConfigurationException("Can not detect Editor Services module version" + e.getMessage());
        }
        if (StringUtil.isEmpty(editorServicesVersion))
            throw new ConfigurationException("Can not detect Editor Services module version");

        String startupScript = INSTANCE.getEditorServicesStartupScript(editorServicesDir);
        if (StringUtil.isEmpty(startupScript))
            throw new ConfigurationException("Can not find Editor Services startup script");
        return editorServicesVersion;
    }

    @Contract("null -> fail")
    public static void validatePowerShellExecutablePath(@Nullable String powerShellExePath) throws ConfigurationException {
        if (!FileUtil.exists(powerShellExePath))
            throw new ConfigurationException("Path to PowerShell executable does not exist: '" + powerShellExePath + "'.");
    }
}
