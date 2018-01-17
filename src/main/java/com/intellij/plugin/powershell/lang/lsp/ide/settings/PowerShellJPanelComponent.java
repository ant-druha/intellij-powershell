package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.plugin.powershell.ide.MessagesBundle;
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain;
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageHostStarter;
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PowerShellJPanelComponent {
  private JPanel myPanel;
  private TextFieldWithBrowseButton myPSExtensionPathTextField;
  private JLabel myDetectedESVersionLabel;
  private JCheckBox myIsUseLanguageServerCheckBox;
  private JLabel myPathToPSExtensionLabel;
  @Nullable
  private LSPInitMain.PowerShellExtensionInfo myDetectedInfo;
  private JBTextField myPathToPSExtDirjTextField;

  JPanel getMyPanel() {
    return myPanel;
  }

  PowerShellJPanelComponent() {
    setVersionLabelVisible(false);
  }

  void setVersionLabelValue(@Nullable String version) {
    myDetectedESVersionLabel.setText(MessagesBundle.INSTANCE.message("ps.editor.services.detected.version.label") + " " + StringUtil.notNullize(version));
    setVersionLabelVisible(StringUtil.isNotEmpty(version));
  }

  String getPowerShellExtensionPath() {
    return myPSExtensionPathTextField.getText().trim();
  }

  private void setVersionLabelVisible(boolean aFlag) {
    myDetectedESVersionLabel.setVisible(aFlag);
  }

//    @Nullable
//    LSPInitMain.PowerShellExtensionInfo getPowerShellInfo() { //how to reuse it?
//        return myDetectedInfo;
//    }

  private TextFieldWithBrowseButton createTextFieldWithBrowseButton(@NotNull String description) {
    myPathToPSExtDirjTextField = new JBTextField(0);
    TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton(myPathToPSExtDirjTextField);
    FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true, false, false, false, false) {
      @Override
      public void validateSelectedFiles(VirtualFile[] files) throws Exception {
        if (files == null || files.length <= 0) return;
        String path = files[0].getCanonicalPath();
        if (path == null) {
          setVersionLabelValue(null);
          return;
        }
        if (getPowerShellExtensionPath().equals(path)) return;

        myDetectedInfo = PowerShellConfigurable.createPowerShellInfo(path, myIsUseLanguageServerCheckBox.isSelected());
        setVersionLabelValue(myDetectedInfo != null ? myDetectedInfo.getEditorServicesModuleVersion() : null);
      }
    };
    fileChooserDescriptor.withShowHiddenFiles(true);
    JTextField textField = textFieldWithBrowseButton.getChildComponent();
    textField.setDisabledTextColor(UIUtil.getLabelDisabledForeground());
    textFieldWithBrowseButton.addBrowseFolderListener(description, null, null, fileChooserDescriptor, TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
    FileChooserFactory.getInstance().installFileCompletion(textField, fileChooserDescriptor, true, null);
    return textFieldWithBrowseButton;
  }

  private void createUIComponents() {
    myIsUseLanguageServerCheckBox = new JBCheckBox(MessagesBundle.INSTANCE.message("settings.powershell.lsp.is.enabled.box.text"));
    myPathToPSExtensionLabel = new JBLabel(MessagesBundle.INSTANCE.message("powershell.extension.path.form.label"));
    myPSExtensionPathTextField = createTextFieldWithBrowseButton(MessagesBundle.INSTANCE.message("powershell.editor.services.path.text"));
    myIsUseLanguageServerCheckBox.addChangeListener(e -> allControlsSetEnabled(myIsUseLanguageServerCheckBox.isSelected()));
  }

  boolean getIsUseLanguageServer() {
    return myIsUseLanguageServerCheckBox.isSelected();
  }

  void isUseLanguageServerSetSelected(boolean value) {
    myIsUseLanguageServerCheckBox.setSelected(value);
    allControlsSetEnabled(myIsUseLanguageServerCheckBox.isSelected());
  }

  private void allControlsSetEnabled(boolean value) {
    myPSExtensionPathTextField.setEnabled(value);
    myPathToPSExtensionLabel.setEnabled(value);
    myDetectedESVersionLabel.setEnabled(value);
  }

  void powerShellPathTextFieldSetEnabled(boolean isEnabled) {
    myPSExtensionPathTextField.setEnabled(isEnabled);
  }

  void setPowerShellExtensionPath(@Nullable String path) {
    if (path == null) {
      setAutoDetectedPowerShellExtensionPath();
    } else {
      myPSExtensionPathTextField.setText(path);
    }
  }

  private void setAutoDetectedPowerShellExtensionPath() {
    String discoveredPath = LanguageHostStarter.Companion.getDiscoveredPowerShellExtensionDir();
    if (StringUtil.isNotEmpty(discoveredPath) && PSLanguageHostUtils.INSTANCE.checkExists(discoveredPath)) {
      myPathToPSExtDirjTextField.getEmptyText().setText(discoveredPath + " (auto detected)");
    } else {
      myPathToPSExtDirjTextField.getEmptyText().setText("Not found", SimpleTextAttributes
          .merge(SimpleTextAttributes.GRAYED_ITALIC_ATTRIBUTES, SimpleTextAttributes.ERROR_ATTRIBUTES));
    }
  }
}
