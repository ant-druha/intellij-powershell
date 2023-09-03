package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.INSTANCE;

public class PowerShellConfigurable implements SearchableConfigurable {
  private PowerShellJPanelComponent myPowerShellSettingsComponent;
  public static final Logger LOG = Logger.getInstance("com.intellij.plugin.powershell.lang.lsp.ide.settings.PowerShellConfigurable");
  private String myOldPsesValue;
  private boolean myOldIsEnabled;
  private String myOldExeValue;
  public static String ID = "Setting.PowerShell";
  public static String NAME = "PowerShell";

  @NotNull
  @Override
  public String getId() {
    return ID;
  }

  @Nls
  @Override
  public String getDisplayName() {
    return NAME;
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    return getSettingsComponent();
  }

  @Override
  public boolean isModified() {
    return !getPSExtensionPathFromForm().equals(StringUtil.notNullize(myOldPsesValue)) ||
                   !getPowerShellExePathFromForm().equals(StringUtil.notNullize(myOldExeValue)) ||
                   myOldIsEnabled != getPSJpanel().getIsUseLanguageServer();
  }

  @Override
  public void reset() {
    LSPInitMain lspInitMain = LSPInitMain.getInstance();
    LSPInitMain.PowerShellInfo powerShellInfo = lspInitMain.getState();
    String psEsPathFromSettings = powerShellInfo.getPowerShellExtensionPath();
    String exePathFromSettings = powerShellInfo.getPowerShellExePath();
    boolean isEnabledInSettings = powerShellInfo.isUseLanguageServer();
    getPSJpanel().fillPowerShellInfo(powerShellInfo);
    myOldPsesValue = psEsPathFromSettings;
    myOldExeValue = exePathFromSettings;
    myOldIsEnabled = isEnabledInSettings;
  }

  @Override
  public void apply() throws ConfigurationException {
    String psExtensionPath = getPSExtensionPathFromForm();
    String powerShellExePath = getPowerShellExePathFromForm();
    boolean isEnabled = getPSJpanel().getIsUseLanguageServer();
    LSPInitMain lspInitMain = LSPInitMain.getInstance();
    LSPInitMain.PowerShellInfo powerShellInfo = lspInitMain.getState();
    FormUIUtil.validatePowerShellExecutablePath(powerShellExePath);
    String powerShellVersion = getPSJpanel().getPowerShellVersionValue();
    if (StringUtil.isEmpty(powerShellVersion)) throw new ConfigurationException("Can not detect PowerShell version");
    String editorServicesVersion;
    if (StringUtil.isNotEmpty(psExtensionPath)) {
      try {
        editorServicesVersion = FormUIUtil.getEditorServicesVersion(psExtensionPath);
      } catch (ConfigurationException e) {
        getPSJpanel().setEditorServicesVersionLabelValue(null);
        throw e;
      }
    } else {
      editorServicesVersion = null;
    }
    myOldPsesValue = psExtensionPath;
    myOldExeValue = powerShellExePath;
    myOldIsEnabled = isEnabled;
    powerShellInfo.setPowerShellExePath(powerShellExePath);
    powerShellInfo.setPowerShellVersion(powerShellVersion);
    powerShellInfo.setUseLanguageServer(isEnabled);
    powerShellInfo.setPowerShellExtensionPath(psExtensionPath);
    powerShellInfo.setEditorServicesModuleVersion(editorServicesVersion);
    String startupScript = INSTANCE.getEditorServicesStartupScript(psExtensionPath);
    powerShellInfo.setEditorServicesStartupScript(startupScript);

    getPSJpanel().fillPowerShellInfo(powerShellInfo);
  }

  @NotNull
  private String getPSExtensionPathFromForm() {
    return getPSJpanel().getPowerShellExtensionPath();
  }

  @NotNull
  private String getPowerShellExePathFromForm() {
    return getPSJpanel().getPowerShellExePath();
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
