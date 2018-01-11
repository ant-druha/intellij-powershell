package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PowerShellConfigurable implements SearchableConfigurable {

    private PowerShellJPanelComponent myPowerShellSettingsComponent;
    private String myOldValue;


    @NotNull
    @Override
    public String getId() {
        return "Setting.PowerShell";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "PowerShell";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return getSettingsComponent();
    }

    @Override
    public boolean isModified() {
        return myOldValue != null && !getScriptPathFromForm().equals(myOldValue);
    }

    @Override
    public void reset() {
        LSPInitMain lspInitMain = ApplicationManager.getApplication().getComponent(LSPInitMain.class);
        String pathFromSettings = lspInitMain.getState().getEditorServicesStartupScript();
        getPSJpanel().getEditorServicesStartupScriptTextField().setText(pathFromSettings);
        myOldValue = pathFromSettings;
    }

    @Override
    public void apply() {
        String pathFromForm = getScriptPathFromForm();
        myOldValue = pathFromForm;
        LSPInitMain lspInitMain = ApplicationManager.getApplication().getComponent(LSPInitMain.class);
        lspInitMain.setPSEditorServicesPath(StringUtil.notNullize(pathFromForm));
    }

    @NotNull
    private String getScriptPathFromForm() {
        return getPSJpanel().getEditorServicesStartupScriptTextField().getText();
    }

    private PowerShellJPanelComponent getPSJpanel() {
        if (myPowerShellSettingsComponent == null) {
            myPowerShellSettingsComponent = new PowerShellJPanelComponent();
        }
        return myPowerShellSettingsComponent;
    }

    private JComponent getSettingsComponent() {
        return getPSJpanel().getMyPanel();
    }

}
