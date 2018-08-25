package com.intellij.plugin.powershell.lang.lsp.languagehost.terminal

import com.google.common.base.Predicate
import com.intellij.execution.ExecutionManager
import com.intellij.execution.Executor
import com.intellij.execution.TaskExecutor
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessWaitFor
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.execution.ui.actions.CloseAction
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.CharsetToolkit
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.plugin.powershell.lang.lsp.languagehost.EditorServicesLanguageHostStarter
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageHostConnectionManager
import com.intellij.plugin.powershell.lang.lsp.languagehost.LanguageServerEndpoint
import com.intellij.ui.GuiUtils
import com.intellij.util.EnvironmentUtil
import com.intellij.util.concurrency.AppExecutorUtil
import com.jediterm.pty.PtyMain
import com.jediterm.terminal.TtyConnector
import com.jediterm.terminal.ui.TerminalWidget
import com.pty4j.PtyProcess
import org.jetbrains.plugins.terminal.JBTabbedTerminalWidget
import org.jetbrains.plugins.terminal.JBTerminalSystemSettingsProvider
import org.jetbrains.plugins.terminal.TerminalProjectOptionsProvider
import org.jetbrains.plugins.terminal.TerminalView
import java.awt.BorderLayout
import java.awt.Component
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Future
import javax.swing.JPanel

class PowerShellConsoleTerminalRunner(project: Project) : EditorServicesLanguageHostStarter(project), LanguageHostConnectionManager {
  private val myDefaultCharset = CharsetToolkit.UTF8_CHARSET
  private val LOG = Logger.getInstance(javaClass)

  private var myServer: LanguageServerEndpoint? = null

  override fun useConsoleRepl(): Boolean = true
  internal fun getServer(): LanguageServerEndpoint? = myServer

  override fun connectServer(server: LanguageServerEndpoint) {
    myServer = server
  }

  private fun createTtyConnector(process: PtyProcess): TtyConnector {
    return PSPtyProcessTtyConnector(process, myDefaultCharset)
  }

  companion object {
    @Volatile
    private var sessionCount = 0

    private fun createPtyProcess(project: Project, command: Array<out String>, directory: String?): PtyProcess {
      val envs = HashMap(EnvironmentUtil.getEnvironmentMap())
      if (!SystemInfo.isWindows) {
        envs["TERM"] = "xterm-256color"
      }
      if (SystemInfo.isMac) {
        EnvironmentUtil.setLocaleEnv(envs, charset("UTF-8"))
      }
      try {
        val logFile = File(PathManager.getLogPath(), "pty-ps.log")
        logFile.createNewFile()
        return PtyProcess.exec(command, envs,
                               directory ?: TerminalProjectOptionsProvider.getInstance(project).startingDirectory,
                               false, false, logFile)
      } catch (e: IOException) {
        throw ExecutionException(e)
      }
    }
  }

  private fun getSessionCount(): Int {
    return ++sessionCount - 1
  }

  override fun getLogFileName(): String = "EditorServices-IJ-Console-${getSessionCount()}"

  override fun createProcess(project: Project, command: List<String>, directory: String?): PtyProcess {
    LOG.info("Language server starting... exe: '$command'")
    val process = createPtyProcess(myProject, command.toTypedArray(), directory)
    try {
      initConsoleUI(process)
    } catch (e: Exception) {
      GuiUtils.invokeLaterIfNeeded({ Messages.showErrorDialog(this@PowerShellConsoleTerminalRunner.myProject, e.message, "Launching PowerShell terminal console") },
                                   ModalityState.NON_MODAL)
    }
    return process
  }

  private fun initConsoleUI(process: PtyProcess) {
    try {
      GuiUtils.invokeLaterIfNeeded({ doInitConsoleUI(process) }, ModalityState.NON_MODAL)
    } catch (e: Exception) {
      throw RuntimeException(e.message, e)
    }
  }

  private fun doInitConsoleUI(process: PtyProcess) {
    LOG.debug("Initializing PowerShell Console UI...")
    val toolbarActions = DefaultActionGroup()
    val actionToolbar = ActionManager.getInstance().createActionToolbar("PowerShellConsoleRunner", toolbarActions, false)

    val panel = JPanel(BorderLayout())
    panel.add(actionToolbar.component, BorderLayout.WEST)
    actionToolbar.setTargetComponent(panel)

    val processHandler = PSPtyProcessHandler(this, process, "PowerShell console process")
    val contentDescriptor = RunContentDescriptor(null, processHandler, panel, "PowerShell Terminal Console")
    contentDescriptor.isAutoFocusContent = true

    val runExecutor = DefaultRunExecutor.getRunExecutorInstance()
    toolbarActions.add(createCloseAction(runExecutor, contentDescriptor))

    val provider = JBTerminalSystemSettingsProvider()
    val widget = JBTabbedTerminalWidget(myProject, provider, Predicate<com.intellij.openapi.util.Pair<TerminalWidget, String>> { input ->
      if (input == null) return@Predicate false
      openSessionInDirectory(input.getFirst(), input.getSecond())
      true
    }, contentDescriptor)

    createAndStartSession(widget, createTtyConnector(process))

    panel.add(widget.component, BorderLayout.CENTER)

    showConsole(runExecutor, contentDescriptor, widget.component)

    processHandler.startNotify()
  }


  private fun openSessionInDirectory(terminalWidget: TerminalWidget, directory: String?) {
    val modalityState = ModalityState.stateForComponent(terminalWidget.component)

    ApplicationManager.getApplication().executeOnPooledThread {
      try {
        // Create Server process
        val process = createProcess(myProject, buildCommandLine(), directory)

        ApplicationManager.getApplication().invokeLater({
                                                          try {
                                                            createAndStartSession(terminalWidget, createTtyConnector(process))
                                                            terminalWidget.component.revalidate()
                                                          } catch (e: RuntimeException) {
                                                            showCannotOpenTerminalDialog(e)
                                                          }
                                                        }, modalityState)
      } catch (e: Exception) {
        ApplicationManager.getApplication().invokeLater({ showCannotOpenTerminalDialog(e) }, modalityState)
      }
    }
  }

  private fun showCannotOpenTerminalDialog(e: Throwable) {
    Messages.showErrorDialog(e.message, "Can not open PowerShell Console")
  }

  private fun createAndStartSession(terminal: TerminalWidget, ttyConnector: TtyConnector) {
    val session = terminal.createTerminalSession(ttyConnector)

    TerminalView.recordUsage(ttyConnector)

    session.start()
  }

  private fun createCloseAction(defaultExecutor: Executor, myDescriptor: RunContentDescriptor): AnAction {
    return CloseAction(defaultExecutor, myDescriptor, myProject)
  }

  private fun showConsole(defaultExecutor: Executor, myDescriptor: RunContentDescriptor, toFocus: Component) {
    // Show in run toolwindow
    ExecutionManager.getInstance(myProject).contentManager.showRunContent(defaultExecutor, myDescriptor)

    // Request focus
    val toolWindow = ToolWindowManager.getInstance(myProject).getToolWindow(defaultExecutor.id)
    toolWindow?.activate { IdeFocusManager.getInstance(myProject).requestFocus(toFocus, true) }
  }

}

class PSPtyProcessHandler(private val myPowerShellConsoleRunner: PowerShellConsoleTerminalRunner, private val myProcess: PtyProcess, presentableName: String) : ProcessHandler(), TaskExecutor {
  private val LOG = Logger.getInstance(PSPtyProcessHandler::class.java.name)
  private val myWaitFor: ProcessWaitFor = ProcessWaitFor(myProcess, this, presentableName)

  override fun startNotify() {
    addProcessListener(object : ProcessAdapter() {
      override fun startNotified(event: ProcessEvent) {
        try {
          myWaitFor.setTerminationCallback { integer -> notifyProcessTerminated(integer!!) }
        } finally {
          removeProcessListener(this)
        }
      }
    })
    super.startNotify()
  }

  override fun destroyProcessImpl() {
    LOG.info("Terminating PowerShell Console session...")
    //myProcess.destroy()//instead we send shutdown + close notification to the server so that it terminate process
    this@PSPtyProcessHandler.myPowerShellConsoleRunner.getServer()?.shutdown()
  }

  override fun detachProcessImpl() {
    destroyProcessImpl()
  }

  override fun detachIsDefault(): Boolean = false

  override fun isSilentlyDestroyOnClose(): Boolean = true

  override fun getProcessInput(): OutputStream? {
    return myProcess.outputStream
  }

  override fun executeTask(task: Runnable): Future<*> {
    return AppExecutorUtil.getAppExecutorService().submit(task)
  }
}

private class PSPtyProcessTtyConnector(process: PtyProcess, charset: Charset) : PtyMain.LoggingPtyProcessTtyConnector(process, charset) 