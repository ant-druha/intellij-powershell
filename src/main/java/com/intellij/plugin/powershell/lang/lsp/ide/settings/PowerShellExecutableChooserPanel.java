package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.plugin.powershell.ide.MessagesBundle;
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain;
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.CancellablePromise;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
        JBTextField textField = new JBTextField(0);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String powerShellExePath = textField.getText();
                updatePowerShellVersionLabel(powerShellExePath);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String powerShellExePath = textField.getText();
                updatePowerShellVersionLabel(powerShellExePath);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        psExecutablePathTextFieldChooser = FormUIUtil.createTextFieldWithBrowseButton(
                MessagesBundle.INSTANCE.message("powershell.executable.path.dialog.text"),
                textField, fileChooserDescriptor);
    }

    private void updatePowerShellVersionLabel(@NotNull String powerShellExePath) {
        CancellablePromise<String> versionPromise = PSLanguageHostUtils.INSTANCE.getPowerShellVersion(powerShellExePath);
        versionPromise.onError(throwable -> {
            LOG.debug("Exception when getting PowerShell version: ", throwable);
            if (getExecutablePath().equals(powerShellExePath)) {
                setPowerShellVersionLabelValue(null);
            }
        }).onSuccess(version -> {
            if (getExecutablePath().equals(powerShellExePath)) {
                setPowerShellVersionLabelValue(version);
            }
        });
    }

    public @NotNull String getExecutablePath() {
        return psExecutablePathTextFieldChooser.getText().trim();
    }

    public void updateExecutablePath(@Nullable String path) {
        if (StringUtil.isEmpty(path) || path.equals(getExecutablePath())) return;
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
