package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.plugin.powershell.ide.MessagesBundle;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PowerShellJPanelComponent {
    private JPanel myPanel;
    private TextFieldWithBrowseButton myEditorServicesStartupScriptTextField;

    JPanel getMyPanel() {
        return myPanel;
    }

    private static TextFieldWithBrowseButton createTextFieldWithBrowseButton(@NotNull String description) {
        TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton(new JTextField(0));
        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor();
        JTextField textField = textFieldWithBrowseButton.getChildComponent();
        textField.setDisabledTextColor(UIUtil.getLabelDisabledForeground());
        textFieldWithBrowseButton
                .addBrowseFolderListener(description, null, null, fileChooserDescriptor, TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
        FileChooserFactory.getInstance().installFileCompletion(textField, fileChooserDescriptor, true, null);
        return textFieldWithBrowseButton;
    }

    private void createUIComponents() {
        this.myEditorServicesStartupScriptTextField = createTextFieldWithBrowseButton(MessagesBundle.INSTANCE
                .message("powershell.editor.services.path.text"));
    }

    TextFieldWithBrowseButton getEditorServicesStartupScriptTextField() {
        return myEditorServicesStartupScriptTextField;
    }
}
