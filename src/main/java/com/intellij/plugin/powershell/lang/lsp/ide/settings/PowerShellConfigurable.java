package com.intellij.plugin.powershell.lang.lsp.ide.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.plugin.powershell.lang.lsp.LSPInitMain;
import com.intellij.plugin.powershell.lang.lsp.languagehost.PowerShellExtensionError;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.intellij.plugin.powershell.lang.lsp.languagehost.PSLanguageHostUtils.INSTANCE;

public class PowerShellConfigurable implements SearchableConfigurable {
  private PowerShellJPanelComponent myPowerShellSettingsComponent;
  private String myOldValue;
  private boolean myOldIsEnabled;
  private static Logger LOG = Logger.getInstance("com.intellij.plugin.powershell.lang.lsp.ide.settings.PowerShellConfigurable");
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
    return !getScriptPathFromForm().equals(StringUtil.notNullize(myOldValue)) || myOldIsEnabled != getPSJpanel().getIsUseLanguageServer();
  }

  @Override
  public void reset() {
    LSPInitMain lspInitMain = ApplicationManager.getApplication().getComponent(LSPInitMain.class);
    String pathFromSettings = lspInitMain.getPowerShellInfo().getPowerShellExtensionPath();
    boolean isEnabledInSettings = lspInitMain.getPowerShellInfo().isUseLanguageServer();
    getPSJpanel().isUseLanguageServerSetSelected(isEnabledInSettings);
    getPSJpanel().setPowerShellExtensionPath(pathFromSettings);
    getPSJpanel().powerShellPathTextFieldSetEnabled(isEnabledInSettings);
    getPSJpanel().setVersionLabelValue(lspInitMain.getPowerShellInfo().getEditorServicesModuleVersion());
    myOldValue = pathFromSettings;
    myOldIsEnabled = isEnabledInSettings;
  }

  @Override
  public void apply() throws ConfigurationException {
    String pathFromForm = getScriptPathFromForm();
    boolean isEnabled = getPSJpanel().getIsUseLanguageServer();
    LSPInitMain lspInitMain = ApplicationManager.getApplication().getComponent(LSPInitMain.class);
    LSPInitMain.PowerShellExtensionInfo powerShellInfo = createPowerShellInfo(pathFromForm, isEnabled);
    if (powerShellInfo == null) {
      getPSJpanel().setVersionLabelValue(null);
      throw new ConfigurationException("Can not validate PowerShell extension");
    }
    myOldValue = pathFromForm;
    myOldIsEnabled = isEnabled;
    lspInitMain.setPSExtensionInfo(powerShellInfo);
    getPSJpanel().setVersionLabelValue(powerShellInfo.getEditorServicesModuleVersion());
    getPSJpanel().powerShellPathTextFieldSetEnabled(isEnabled);
  }


  @Nullable
  static LSPInitMain.PowerShellExtensionInfo createPowerShellInfo(@NotNull final String psExtDir, boolean isEnabled) throws ConfigurationException {
    try {
      if (StringUtil.isEmpty(psExtDir)) return new LSPInitMain.PowerShellExtensionInfo(isEnabled);

      if (!FileUtil.exists(psExtDir)) throw new ConfigurationException("Path " + psExtDir + "does not exist.");

      String editorServicesVersion = INSTANCE
          .getEditorServicesModuleVersion(INSTANCE.getPSExtensionModulesDir(psExtDir));
      if (StringUtil.isEmpty(editorServicesVersion))
        throw new ConfigurationException("Can not detect Editor Services module version");

      String startupScript = INSTANCE.getEditorServicesStartupScript(psExtDir);
      if (StringUtil.isEmpty(startupScript))
        throw new ConfigurationException("Can not find Editor Services startup script");

      return new LSPInitMain.PowerShellExtensionInfo(startupScript, psExtDir, editorServicesVersion, isEnabled);
    } catch (PowerShellExtensionError e) {
      LOG.warn("Configuration error: " + e.getMessage());
      return null;
    }
  }

  @NotNull
  private String getScriptPathFromForm() {
    return getPSJpanel().getPowerShellExtensionPath();
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
