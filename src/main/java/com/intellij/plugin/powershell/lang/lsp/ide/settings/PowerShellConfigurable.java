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
  private static Logger LOG = Logger.getInstance("com.intellij.plugin.powershell.lang.lsp.ide.settings.PowerShellConfigurable");


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
    return !getScriptPathFromForm().equals(StringUtil.notNullize(myOldValue));
  }

  @Override
  public void reset() {
    LSPInitMain lspInitMain = ApplicationManager.getApplication().getComponent(LSPInitMain.class);
    String pathFromSettings = lspInitMain.getPowerShellInfo().getPowerShellExtensionPath();
    getPSJpanel().getPowerShellExtensionPathTextField().setText(pathFromSettings);
    getPSJpanel().setVersionLabelValue(lspInitMain.getPowerShellInfo().getEditorServicesModuleVersion());
    myOldValue = pathFromSettings;
  }

  @Override
  public void apply() throws ConfigurationException {
    String pathFromForm = getScriptPathFromForm();
    String trimmed = pathFromForm.trim();
    LSPInitMain.PowerShellExtensionInfo powerShellInfo = null;
    LSPInitMain lspInitMain = ApplicationManager.getApplication().getComponent(LSPInitMain.class);
    if (StringUtil.isNotEmpty(trimmed)) {
      powerShellInfo = createPowerShellInfo(trimmed);
      if (powerShellInfo == null) {
        getPSJpanel().setVersionLabelValue(null);
        throw new ConfigurationException("Can not validate PowerShell extension");
      }
    }
    myOldValue = pathFromForm;
    lspInitMain.setPSExtensionInfo(powerShellInfo);
    getPSJpanel()
        .setVersionLabelValue(powerShellInfo != null ? powerShellInfo.getEditorServicesModuleVersion() : "");
  }


  @Nullable
  static LSPInitMain.PowerShellExtensionInfo createPowerShellInfo(@NotNull final String psExtDir) throws ConfigurationException {
    try {
      if (!FileUtil.exists(psExtDir)) throw new ConfigurationException("Path " + psExtDir + "does not exist.");

      String editorServicesVersion = INSTANCE
          .getEditorServicesModuleVersion(INSTANCE.getPSExtensionModulesDir(psExtDir));
      if (StringUtil.isEmpty(editorServicesVersion))
        throw new ConfigurationException("Can not detect Editor Services module version");

      String startupScript = INSTANCE.getEditorServicesStartupScript(psExtDir);
      if (StringUtil.isEmpty(startupScript))
        throw new ConfigurationException("Can not find Editor Services startup script");

      return new LSPInitMain.PowerShellExtensionInfo(startupScript, psExtDir, editorServicesVersion);
    } catch (PowerShellExtensionError e) {
      LOG.warn("Configuration error: " + e.getMessage());
      return null;
    }
  }

  @NotNull
  private String getScriptPathFromForm() {
    return getPSJpanel().getPowerShellExtensionPathTextField().getText();
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
