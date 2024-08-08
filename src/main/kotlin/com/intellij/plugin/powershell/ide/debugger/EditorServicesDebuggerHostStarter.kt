package com.intellij.plugin.powershell.ide.debugger

import com.intellij.execution.configurations.PtyCommandLine
import com.intellij.openapi.project.Project
import com.intellij.plugin.powershell.lang.lsp.languagehost.EditorServicesLanguageHostStarter
import com.intellij.util.application
import org.jetbrains.plugins.terminal.TerminalProjectOptionsProvider

class EditorServicesDebuggerHostStarter(myProject: Project) : EditorServicesLanguageHostStarter(myProject) {
  override fun useConsoleRepl(): Boolean = true
  override fun createProcess(command: List<String>, environment: Map<String, String>?): Process {
    val workingDirectory = if (application.isUnitTestMode) {
      myProject.basePath ?: error("Project has no base path.")
    } else TerminalProjectOptionsProvider.getInstance(myProject).startingDirectory

    return PtyCommandLine(command)
      .withConsoleMode(false)
      .withWorkDirectory(workingDirectory)
      .withEnvironment(environment)
      .createProcess()
  }
}
