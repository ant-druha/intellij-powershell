// SPDX-FileCopyrightText: 2026 intellij-powershell contributors <https://github.com/intellij-powershell/intellij-powershell>
//
// SPDX-License-Identifier: Apache-2.0

package com.intellij.plugin.powershell.ide

import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.io.path.createTempDirectory
import kotlin.io.path.writeText
import kotlin.test.assertEquals

class EnvironmentUtilTests {

  @Test
  fun testResolveExecutablePathResolvesSymlink() {
    val directory = createTempDirectory("environment-util-test")
    try {
      val target = directory.resolve("target.exe").apply { writeText("") }
      val link = directory.resolve("pwsh.exe")
      try {
        Files.createSymbolicLink(link, target.fileName)
      } catch (_: UnsupportedOperationException) {
        assumeTrue(false, "Symbolic links are not supported")
      } catch (_: java.nio.file.FileSystemException) {
        assumeTrue(false, "Symbolic links cannot be created in this environment")
      }

      assertEquals(target.toRealPath().toString(), resolveExecutablePath(link.toString()))
    } finally {
      Files.walk(directory).sorted(Comparator.reverseOrder()).forEach(Files::deleteIfExists)
    }
  }
}
