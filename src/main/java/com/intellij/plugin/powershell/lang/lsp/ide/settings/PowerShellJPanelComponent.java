package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.plugin.powershell.ide.MessagesBundle;
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain;
import com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils;
import com.intellij.plugin.powershell.lang.lsp.languagehost.PowerShellNotInstalled;
import com.intellij.ui.HyperlinkAdapter;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.HTMLEditorKitBuilder;
import com.intellij.util.ui.UIUtil;
import com.intellij.xml.util.XmlStringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

import static com.intellij.plugin.powershell.ide.run.PSExecutionUtilKt.findPsExecutable;
import static com.intellij.plugin.powershell.lang.lsp.languagehost.EditorServicesLanguageHostStarter.Companion;

public class PowerShellJPanelComponent {
  private static final String PS_VSCODE_LINK = MessagesBundle.message("powershell.vs.code.extension.install.link");
  private static final String PS_ES_LINK = MessagesBundle.message("powershell.editor.services.download.link");
  private JPanel myPanel;
  private TextFieldWithBrowseButton myPSExtensionPathTextField;
  private final Logger LOG = Logger.getInstance(getClass());
  private JLabel myDetectedESVersionLabel;
  private JCheckBox myIsUseLanguageServerCheckBox;
  private JLabel myPathToPSExtensionLabel;
  private JTextPane myExplanationTextPane;
  private JBTextField myPathToPSExtDirjTextField;

  JPanel getMyPanel() {
    return myPanel;
  }

  PowerShellJPanelComponent() {
    setVersionLabelVisible(false);
    String pathDescription = MessagesBundle.message("powershell.extension.path.form.description", PS_ES_LINK);
    myExplanationTextPane.setEditorKit(HTMLEditorKitBuilder.simple());
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

  void setEditorServicesVersionLabelValue(@Nullable String version) {
    myDetectedESVersionLabel.setText(getLabeledText(version));
    boolean notEmpty = StringUtil.isNotEmpty(version);
    setVersionLabelVisible(notEmpty);
    myDetectedESVersionLabel.setEnabled(notEmpty);
  }

  private PowerShellExecutableChooserPanel psExecutableChooserPanel;

  @NotNull
  private String getLabeledText(@Nullable String version) {
    return MessagesBundle.message("ps.editor.services.detected.version.label") + " " + StringUtil.notNullize(version);
  }

  String getPowerShellExtensionPath() {
    return myPSExtensionPathTextField.getText().trim();
  }

  public @Nullable String getPowerShellVersionValue() {
    return psExecutableChooserPanel.getVersionValue();
  }

  private void setVersionLabelVisible(boolean aFlag) {
    myDetectedESVersionLabel.setVisible(aFlag);
  }

  String getPowerShellExePath() {
    return psExecutableChooserPanel.getExecutablePath();
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
    myDetectedESVersionLabel.setEnabled(value && !Companion.isUseBundledPowerShellExtension());
    myExplanationTextPane.setEnabled(value);
  }

  void powerShellPathTextFieldSetEnabled(boolean isEnabled) {
    myPSExtensionPathTextField.setEnabled(isEnabled);
  }

  private void setPowerShellExtensionPath(@Nullable String path) {
    if (StringUtil.isEmpty(path)) {
      try {
        setBundledPowerShellExtensionPath();
      } catch (ConfigurationException e) {
        LOG.warn("Can not detect bundled PowerShell Language Host: ", e);
      }
    } else {
      myPSExtensionPathTextField.setText(path);
      Companion.setUseBundledPowerShellExtension(false);
    }
  }

  private void setPowerShellExePath(@Nullable String path) {
    if (path == null) {
      try {
        psExecutableChooserPanel.updateExecutablePath(findPsExecutable());
      } catch (PowerShellNotInstalled e) {
        LOG.warn("Can not find PowerShell executable in PATH: ", e);
      }
    } else {
      psExecutableChooserPanel.updateExecutablePath(path);
    }
  }

  private void createUIComponents() {
    //noinspection DialogTitleCapitalization
    myIsUseLanguageServerCheckBox = new JBCheckBox(MessagesBundle.message("settings.powershell.lsp.is.enabled.box.text"));
    //noinspection DialogTitleCapitalization
    myPathToPSExtensionLabel = new JBLabel(MessagesBundle.message("powershell.extension.path.form.label"));
    myPathToPSExtDirjTextField = new JBTextField(0);
    myPSExtensionPathTextField = FormUIUtil.createTextFieldWithBrowseButton(
            MessagesBundle.message("powershell.editor.services.path.dialog.text"),
            myPathToPSExtDirjTextField,
            new FileChooserDescriptor(false, true, false, false, false, false) {
              @Override
              public void validateSelectedFiles(VirtualFile @NotNull [] files) throws Exception {
                if (files.length <= 0) return;
                String psEditorServicesPath = files[0].getCanonicalPath();
                if (psEditorServicesPath == null) {
                  setEditorServicesVersionLabelValue(null);//use bundled
                  return;
                }
                if (getPowerShellExtensionPath().equals(psEditorServicesPath)) return;

                String psLanguageServerVersion = FormUIUtil.getEditorServicesVersion(psEditorServicesPath);
                setEditorServicesVersionLabelValue(psLanguageServerVersion);
              }
            });
    psExecutableChooserPanel = new PowerShellExecutableChooserPanel(null);
    myIsUseLanguageServerCheckBox.addChangeListener(e -> allControlsSetEnabled(myIsUseLanguageServerCheckBox.isSelected()));
  }

  private void setBundledPowerShellExtensionPath() throws ConfigurationException {
    String version = FormUIUtil.getEditorServicesVersion(PSLanguageHostUtils.INSTANCE.getBUNDLED_PSES_PATH());
    myPathToPSExtDirjTextField.getEmptyText().setText("Bundled");
    setEditorServicesVersionLabelValue(version);
    myDetectedESVersionLabel.setEnabled(false);
    Companion.setUseBundledPowerShellExtension(true);
  }

  void fillPowerShellInfo(@NotNull LSPInitMain.PowerShellInfo powerShellInfo) {
    setEditorServicesVersionLabelValue(powerShellInfo.getEditorServicesModuleVersion());//todo make consistent with 'setPowerShellExtensionPath'
    setPowerShellExtensionPath(powerShellInfo.getPowerShellExtensionPath());
    setPowerShellExePath(powerShellInfo.getPowerShellExePath());
    psExecutableChooserPanel.setPowerShellVersionLabelValue(powerShellInfo.getPowerShellVersion());
    powerShellPathTextFieldSetEnabled(powerShellInfo.isUseLanguageServer());
    isUseLanguageServerSetSelected(powerShellInfo.isUseLanguageServer());

  }

}
