/*
 * SPDX-FileCopyrightText: 2026 intellij-powershell contributors <https://github.com/intellij-powershell/intellij-powershell>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.intellij.plugin.powershell.ide

import com.intellij.execution.configurations.PathEnvironmentVariableUtil
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.platform.eel.EelOsFamily
import com.intellij.platform.eel.provider.getEelDescriptor
import java.nio.file.Path
import kotlin.io.path.exists

fun findExecutableInPath(pathOrFileNameOrBaseName: String): Path? {
  // Absolute path: just resolve and return if exists.
  val path = pathOrFileNameOrBaseName.toNioPathOrNull() ?: return null
  if (path.isAbsolute) {
    return if (path.exists()) path else null
  }

  // Executable name with extension (Windows): find in PATH directly.
  val exactFileNameInPath = PathEnvironmentVariableUtil.findInPath(pathOrFileNameOrBaseName)
  if (exactFileNameInPath != null) {
    return exactFileNameInPath.toPath()
  }

  // Windows, executable base name: find in PATH with executable extensions.
  // See IJPL-250645 for details why we can't universally use PathEnvironmentVariableUtil.findExecutableInPathOnAnyOS.
  @Suppress("UnstableApiUsage")
  if (path.getEelDescriptor().osFamily == EelOsFamily.Windows) {
    return PathEnvironmentVariableUtil.findExecutableInWindowsPath(pathOrFileNameOrBaseName, null)?.toNioPathOrNull()
  }

  // At this point, we didn't find any executable.
  return null
}

fun resolveExecutablePath(pathOrFileNameOrBaseName: String): String {
  val executable = findExecutableInPath(pathOrFileNameOrBaseName) ?: return pathOrFileNameOrBaseName
  return runCatching { executable.toRealPath().toString() }.getOrDefault(executable.toString())
}
