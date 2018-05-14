package com.intellij.plugin.powershell.ide.injection;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.plugin.powershell.psi.PowerShellStringLiteralExpression;

public class PSPatterns extends PlatformPatterns {
  public static PSElementPatterns.Capture<PowerShellStringLiteralExpression> sqlCapture() {
    return PSPatternUtil.INSTANCE.psCapture();
  }
}
