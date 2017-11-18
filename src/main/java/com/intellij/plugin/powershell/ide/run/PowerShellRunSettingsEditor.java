package com.intellij.plugin.powershell.ide.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PowerShellRunSettingsEditor extends SettingsEditor<PowerShellRunConfiguration> {

  private PowerShellRunConfiguration runConfiguration;
  private JPanel mainPanel;
  private TextFieldWithBrowseButton scriptTextField;
  private JTextField parametersTextField;
  private JTextField scriptOptionsTextField;

  public PowerShellRunSettingsEditor(Project project, PowerShellRunConfiguration runConfiguration) {
    this.runConfiguration = runConfiguration;

    FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
    scriptTextField.addBrowseFolderListener("Chose Script", "Please choose script to run", project, descriptor);
  }

  @Override
  protected void resetEditorFrom(@NotNull PowerShellRunConfiguration configuration) {
    String scriptPath = configuration.getScriptPath();
    String scriptParameters = configuration.getScriptParameters();
    String scriptOptions = configuration.getScriptOptions();
    if (!StringUtil.isEmpty(scriptPath)) {
      scriptTextField.setText(scriptPath);
      String[] parts = scriptPath.split("/");
      if (parts.length > 0) {
        runConfiguration.setName(parts[parts.length - 1]);
      }
    }
    if (!StringUtil.isEmpty(scriptParameters)) {
      parametersTextField.setText(scriptParameters);
    }
    if (!StringUtil.isEmpty(scriptOptions)) {
      scriptOptionsTextField.setText(scriptOptions);
    }
  }

  @Override
  protected void applyEditorTo(@NotNull PowerShellRunConfiguration configuration) {
    configuration.setScriptPath(scriptTextField.getText().trim());
    configuration.setScriptParameters(parametersTextField.getText().trim());
    configuration.setScriptOptions(scriptOptionsTextField.getText().trim());
  }

  @NotNull
  @Override
  protected JComponent createEditor() {
    return mainPanel;
  }

  @Nullable
  public String getScriptName() {
    return runConfiguration.getScriptPath();
  }
}
