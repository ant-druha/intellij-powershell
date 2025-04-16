/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp

import com.intellij.openapi.components.*
import com.intellij.plugin.powershell.ide.run.findPsExecutable

@Service
@State(name = "PowerShellSettings", storages = [Storage(value = "powerShellSettings.xml", roamingType = RoamingType.DISABLED)])
class PowerShellSettings : PersistentStateComponent<PowerShellSettings.PowerShellInfo> {

  companion object {
    @JvmStatic
    fun getInstance(): PowerShellSettings = service()
  }

  data class PowerShellInfo(
    var editorServicesStartupScript: String = "",
    var powerShellExePath: String? = null,
    var powerShellVersion: String? = null,
    var powerShellExtensionPath: String? = null,
    var editorServicesModuleVersion: String? = null,
    var isUseLanguageServer: Boolean = true
  )

  fun getPowerShellExecutable(): String {
    val psExecutable = myPowerShellInfo.powerShellExePath ?: findPsExecutable()
    myPowerShellInfo.powerShellExePath = psExecutable
    return psExecutable
  }

  private var myPowerShellInfo: PowerShellInfo = PowerShellInfo()

  override fun loadState(powerShellInfo: PowerShellInfo) {
    myPowerShellInfo = powerShellInfo
  }

  override fun getState(): PowerShellInfo {
    return myPowerShellInfo
  }
}
