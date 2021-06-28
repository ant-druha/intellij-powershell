package com.intellij.plugin.powershell.ide.run

import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.plugin.powershell.lang.lsp.languagehost.PowerShellExtensionError
import com.intellij.plugin.powershell.lang.lsp.languagehost.PowerShellNotInstalled
import com.intellij.util.EnvironmentUtil


@Throws(PowerShellNotInstalled::class)
fun findPsExecutable(): String {
  val osPath = EnvironmentUtil.getValue("PATH") ?: throw PowerShellNotInstalled("Can not get OS PATH")
  var paths = osPath.split(if (SystemInfo.isWindows) ";" else ":")
  if (SystemInfo.isMac) paths = paths.plusElement("/usr/local/bin")
  val suf = if (SystemInfo.isWindows) ".exe" else ""
  val exec = arrayListOf("powershell$suf", "pwsh$suf")
  paths.forEach { p ->
    exec.forEach { e ->
      val answer = join(p, e)
      if (checkExists(answer)) return answer
    }
  }
  throw PowerShellNotInstalled("Can not find PowerShell executable in PATH")
}

@Throws(PowerShellExtensionError::class)
fun getModuleVersion(moduleBase: String, moduleName: String): String {
  val moduleFile = join(moduleBase, "$moduleName/$moduleName.psd1")
  if (!checkExists(moduleFile)) throw PowerShellExtensionError("Module manifest file not found: $moduleFile")
  val lines = FileUtil.loadLines(moduleFile)
  for (l in lines) {
    if (l.contains("ModuleVersion", true)) {
      //todo can be not in one line
      return l.trimStart { c -> c != '=' }.substringAfter('=').trim().trim { c -> c == '\'' }
    }
  }
  throw PowerShellExtensionError("ModuleVersion info not found in: $moduleFile")
}

fun escapePath(path: String) = "&(\"$path\")"

fun checkExists(path: String?): Boolean {
  return FileUtil.exists(path)
}

fun join(vararg pathPart: String): String {
  return FileUtil.toCanonicalPath(FileUtil.join(*pathPart))
}

fun getDefaultWorkingDirectory(): String {
  return EnvironmentUtil.getValue("HOME") ?: System.getProperty("user.home") ?: System.getProperty("user.dir") ?: ""
}
