package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.plugin.powershell.ide.MessagesBundle;
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain;
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageHostStarter;
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils;
import com.intellij.ui.HyperlinkAdapter;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.UIUtil;
import com.intellij.xml.util.XmlStringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

public class PowerShellJPanelComponent {
  private static final String PS_VSCODE_LINK = MessagesBundle.INSTANCE.message("powershell.vs.code.extension.install.link");
  private static final String PS_ES_LINK = MessagesBundle.INSTANCE.message("powershell.editor.services.download.link");
  private JPanel myPanel;
  private TextFieldWithBrowseButton myPSExtensionPathTextField;
  private JLabel myDetectedESVersionLabel;
  private JCheckBox myIsUseLanguageServerCheckBox;
  private JLabel myPathToPSExtensionLabel;
  private JTextPane myExplanationTextPane;
  @Nullable
  private LSPInitMain.PowerShellExtensionInfo myDetectedInfo;
  private JBTextField myPathToPSExtDirjTextField;
  private Logger LOG = Logger.getInstance(getClass());

  JPanel getMyPanel() {
    return myPanel;
  }

  PowerShellJPanelComponent() {
    setVersionLabelVisible(false);
    String pathDescription = MessagesBundle.INSTANCE.message("powershell.extension.path.form.description", PS_ES_LINK);
    myExplanationTextPane.setEditorKit(UIUtil.getHTMLEditorKit());
    myExplanationTextPane.setEditable(false);
    myExplanationTextPane.setBackground(UIUtil.getWindowColor());
    Font defFont = myExplanationTextPane.getFont();
    myExplanationTextPane.setFont(defFont.deriveFont(Font.PLAIN, defFont.getSize() - 1));
    myExplanationTextPane.setForeground(UIUtil.getLabelFontColor(UIUtil.FontColor.BRIGHTER));

    myExplanationTextPane.setText(XmlStringUtil.wrapInHtml(pathDescription));

    myExplanationTextPane.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      public void hyperlinkActivated(HyperlinkEvent e) {
        final String desc = e.getDescription();
        if (PS_VSCODE_LINK.equals(desc) || PS_ES_LINK.equals(desc)) {
          BrowserUtil.browse(desc);
        }
      }
    });
  }

  void setVersionLabelValue(@Nullable String version) {
    myDetectedESVersionLabel.setText(getEditorServicesVersionText(version));
    boolean notEmpty = StringUtil.isNotEmpty(version);
    setVersionLabelVisible(notEmpty);
    myDetectedESVersionLabel.setEnabled(notEmpty);
  }

  @NotNull
  private String getEditorServicesVersionText(@Nullable String version) {
    return MessagesBundle.INSTANCE.message("ps.editor.services.detected.version.label") + " " + StringUtil.notNullize(version);
  }

  String getPowerShellExtensionPath() {
    return myPSExtensionPathTextField.getText().trim();
  }

  private void setVersionLabelVisible(boolean aFlag) {
    myDetectedESVersionLabel.setVisible(aFlag);
  }

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
    myPSExtensionPathTextField = createTextFieldWithBrowseButton(MessagesBundle.INSTANCE.message("powershell.editor.services.path.dialog.text"));
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
    myDetectedESVersionLabel.setEnabled(value && !LanguageHostStarter.Companion.isUseBundledPowerShellExtension());
    myExplanationTextPane.setEnabled(value);
  }

  void powerShellPathTextFieldSetEnabled(boolean isEnabled) {
    myPSExtensionPathTextField.setEnabled(isEnabled);
  }

  private void setPowerShellExtensionPath(@Nullable String path) {
    if (path == null) {
      try {
        setBundledPowerShellExtensionPath();
      } catch (ConfigurationException e) {
        LOG.warn("Can not detect bundled PowerShell Language Host: ", e);
      }
    } else {
      myPSExtensionPathTextField.setText(path);
      LanguageHostStarter.Companion.setUseBundledPowerShellExtension(false);
    }
  }

  void fillPowerShellInfo(@NotNull LSPInitMain.PowerShellExtensionInfo powerShellInfo) {
    String path = powerShellInfo.getPowerShellExtensionPath();
    String esVersion = powerShellInfo.getEditorServicesModuleVersion();
    setVersionLabelValue(esVersion);//todo make consistent with 'setPowerShellExtensionPath'
    setPowerShellExtensionPath(path);
  }

  private void setBundledPowerShellExtensionPath() throws ConfigurationException {
    LSPInitMain.PowerShellExtensionInfo psInfo = PowerShellConfigurable.createPowerShellInfo(PSLanguageHostUtils.INSTANCE.getBUNDLED_PSES_PATH(), myIsUseLanguageServerCheckBox.isSelected());
    String version = psInfo != null ? psInfo.getEditorServicesModuleVersion() : null;
    myPathToPSExtDirjTextField.getEmptyText().setText("Bundled");
    setVersionLabelValue(version);
    myDetectedESVersionLabel.setEnabled(false);
    LanguageHostStarter.Companion.setUseBundledPowerShellExtension(true);
  }
}
