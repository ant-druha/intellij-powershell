package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.plugin.powershell.ide.MessagesBundle;
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PowerShellJPanelComponent {
  private JPanel myPanel;
  private TextFieldWithBrowseButton myPSExtensionPathTextField;
  private JLabel myDetectedESVersionLabel;
  @Nullable
  private LSPInitMain.PowerShellExtensionInfo myDetectedInfo;

  JPanel getMyPanel() {
    return myPanel;
  }

  PowerShellJPanelComponent() {
    setVersionLabelVisible(false);
  }

  void setVersionLabelValue(@Nullable String version) {
    myDetectedESVersionLabel
        .setText(MessagesBundle.INSTANCE.message("ps.editor.services.detected.version.label") + " " + StringUtil
            .notNullize(version));
    setVersionLabelVisible(StringUtil.isNotEmpty(version));
  }

  private void setVersionLabelVisible(boolean aFlag) {
    myDetectedESVersionLabel.setVisible(aFlag);
  }

//    @Nullable
//    LSPInitMain.PowerShellExtensionInfo getPowerShellInfo() { //how to reuse it?
//        return myDetectedInfo;
//    }

  private TextFieldWithBrowseButton createTextFieldWithBrowseButton(@NotNull String description) {
    TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton(new JTextField(0));
    FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false) {
      @Override
      public void validateSelectedFiles(VirtualFile[] files) throws Exception {
        if (files == null || files.length <= 0) return;
        String path = files[0].getCanonicalPath();
        if (path == null) {
          setVersionLabelValue(null);
          return;
        }
        if (myPSExtensionPathTextField.getText().trim().equals(path)) return;

        myDetectedInfo = PowerShellConfigurable.createPowerShellInfo(path);
        setVersionLabelValue(myDetectedInfo != null ? myDetectedInfo.getEditorServicesModuleVersion() : null);
      }
    };
    fileChooserDescriptor.withShowHiddenFiles(true);
    JTextField textField = textFieldWithBrowseButton.getChildComponent();
    textField.setDisabledTextColor(UIUtil.getLabelDisabledForeground());
    textFieldWithBrowseButton
        .addBrowseFolderListener(description, null, null, fileChooserDescriptor, TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
    FileChooserFactory.getInstance().installFileCompletion(textField, fileChooserDescriptor, true, null);
    return textFieldWithBrowseButton;
  }

  private void createUIComponents() {
    this.myPSExtensionPathTextField = createTextFieldWithBrowseButton(MessagesBundle.INSTANCE
        .message("powershell.editor.services.path.text"));
  }

  TextFieldWithBrowseButton getPowerShellExtensionPathTextField() {
    return myPSExtensionPathTextField;
  }
}
