package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.plugin.powershell.ide.MessagesBundle;
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain;
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.CancellablePromise;

import javax.swing.*;

public class PowerShellExecutableChooserPanel extends JComponent {
    private final Logger LOG = Logger.getInstance(getClass());
    private JLabel psDetectedVersion;
    private TextFieldWithBrowseButton psExecutablePathTextFieldChooser;
    @SuppressWarnings("unused")
    private JPanel myJpanel;

    public PowerShellExecutableChooserPanel(@Nullable String executablePath) {
        String globalSettingsPath = ApplicationManager.getApplication().getComponent(LSPInitMain.class).getState().getPowerShellExePath();
        updateExecutablePath(StringUtil.isEmpty(executablePath) ? globalSettingsPath : executablePath);
    }

    private void createUIComponents() {
        psExecutablePathTextFieldChooser = FormUIUtil.createTextFieldWithBrowseButton(
                MessagesBundle.INSTANCE.message("powershell.executable.path.dialog.text"),
                new JBTextField(0),
                new FileChooserDescriptor(true, false, false, false, false, false) {
                    @Override
                    public void validateSelectedFiles(VirtualFile @NotNull [] files) throws Exception {
                        if (files.length <= 0) return;
                        String powerShellExePath = files[0].getCanonicalPath();
                        FormUIUtil.validatePowerShellExecutablePath(powerShellExePath);
                        CancellablePromise<String> versionPromise = PSLanguageHostUtils.INSTANCE.getPowerShellVersion(powerShellExePath);
                        versionPromise.onError(throwable -> {
                            LOG.warn("Exception when getting PowerShell version: ", throwable);
                            setPowerShellVersionLabelValue(null);
                        }).onSuccess(version -> setPowerShellVersionLabelValue(version)
                        );
                    }
                });
    }

    private void updatePowerShellVersionLabel(String powerShellExePath) {
        CancellablePromise<String> versionPromise = PSLanguageHostUtils.INSTANCE.getPowerShellVersion(powerShellExePath);
        versionPromise.onError(throwable -> {
            LOG.warn("Exception when getting PowerShell version: ", throwable);
            setPowerShellVersionLabelValue(null);
        }).onSuccess(this::setPowerShellVersionLabelValue);
    }

    public String getExecutablePath() {
        return psExecutablePathTextFieldChooser.getText().trim();
    }

    public void updateExecutablePath(@NotNull String path) {
        if (path.equals(getExecutablePath())) return;
        updatePowerShellVersionLabel(path);
        psExecutablePathTextFieldChooser.setText(path);
    }

    public @Nullable String getVersionValue() {
        String version = StringUtil.substringAfterLast(
                psDetectedVersion.getText(),
                MessagesBundle.INSTANCE.message("ps.editor.services.detected.version.label"));
        return StringUtil.trim(version);
    }

    public void setPowerShellVersionLabelValue(@Nullable String version) {
        psDetectedVersion.setText(getLabeledText(version));
    }

    @NotNull
    private String getLabeledText(@Nullable String version) {
        return MessagesBundle.INSTANCE.message("ps.editor.services.detected.version.label") + " " + StringUtil.notNullize(version);
    }

}
