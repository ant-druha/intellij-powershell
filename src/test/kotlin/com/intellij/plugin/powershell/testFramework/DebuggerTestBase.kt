package com.intellij.plugin.powershell.testFramework

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.testFramework.HeavyPlatformTestCase
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

abstract class DebuggerTestBase : HeavyPlatformTestCase() {

  private val testData = "testData"

  protected fun copyAndOpenFile(nameRelativeToTestData: String): PsiFile {
    val fullName = "$testData/$nameRelativeToTestData"
    val original = DebuggerTestBase::class.java.classLoader.getResource(fullName)
      ?: error("Cannot find the original file \"$fullName\".")
    val originalVirtual = VfsUtil.findFileByURL(original)
      ?: error("Cannot find the original file \"$original\" on disk.")
    val basePath = Path(project.basePath ?: error("No base path defined for project $project."))
    basePath.createDirectories()

    val baseDir = VfsUtil.findFile(basePath, /* refreshIfNeeded = */ true)
      ?: error("Cannot find the base directory for project $project.")
    val result = copy(originalVirtual, baseDir, originalVirtual.name)

    return PsiManager.getInstance(project).findFile(result) ?: error("Cannot find the PSI file for \"$result\".")
  }
}
