package com.intellij.plugin.powershell.lang.lsp.languagehost

enum class PowerShellEdition {

  /**
   * PowerShell 5.x mostly.
   */
  Desktop,

  /**
   * PowerShell Core (6+).
   */
  Core,

  Unknown;

  companion object {

    fun parse(name: String) = when(name) {
      "Desktop" -> Desktop
      "Core" -> Core
      else -> Unknown
    }
  }
}


data class PSVersionInfo(val versionString: String, val edition: PowerShellEdition?) {

  override fun toString(): String {
    if (edition == null) return versionString
    return "$versionString $edition"
  }

  companion object {

    val empty = PSVersionInfo("", null)

    fun parse(fullString: String): PSVersionInfo {
      val components = fullString.split(" ".toRegex(), 2)
      return when (components.size) {
        0 -> empty
        1 -> PSVersionInfo(components[0], null)
        2 -> PSVersionInfo(components[0], PowerShellEdition.parse(components[1]))
        else -> error("Impossible")
      }
    }
  }
}
