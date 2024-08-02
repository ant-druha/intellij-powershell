package com.intellij.plugin.powershell.ide.debugger

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.breakpoints.XBreakpointHandler
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.intellij.xdebugger.breakpoints.XBreakpointType
import com.intellij.xdebugger.breakpoints.XLineBreakpoint
import com.jetbrains.rd.util.reactive.Signal

class PowerShellBreakpointHandler(powerShellDebugProcess: PowerShellDebugProcess, breakpointTypeClass: Class<out XBreakpointType<XLineBreakpoint<XBreakpointProperties<*>>, *>>): XBreakpointHandler<XLineBreakpoint<XBreakpointProperties<*>>>(
  breakpointTypeClass
) {
  val myPowerShellDebugProcess = powerShellDebugProcess
  val registerBreakpointEvent = Signal<Pair<String, XLineBreakpoint<XBreakpointProperties<*>>>>()
  val unregisterBreakpointEvent = Signal<Pair<String, XLineBreakpoint<XBreakpointProperties<*>>>>()

  override fun registerBreakpoint(breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
    val sourcePosition = breakpoint.sourcePosition
    if (sourcePosition == null || !sourcePosition.file.exists() || !sourcePosition.file.isValid) {
      return
    }
    val file = sourcePosition.file
    val project: Project = myPowerShellDebugProcess.session.project
    val fileURL: String = getFileURL(file)
    val lineNumber: Int = breakpoint.line
    if (lineNumber == -1) {
      //myXsltDebugProcess.getSession().setBreakpointInvalid(breakpoint, "Unsupported breakpoint position")
      return
    }
    registerBreakpointEvent.fire(Pair(fileURL, breakpoint))
    /*try {
      val manager: BreakpointManager = myPowerShellDebugProcess.getBreakpointManager()
      var bp: Breakpoint
      if ((manager.getBreakpoint(fileURL, lineNumber).also { bp = it }) != null) {
        bp.setEnabled(true)
      } else {
        manager.setBreakpoint(fileURL, lineNumber)
      }
    } catch (ignore: DebuggerStoppedException) {
    } catch (e: VMPausedException) {
      val session: XDebugSession = myXsltDebugProcess.getSession()
      session.reportMessage(
        XsltDebuggerBundle.message("notification.content.target.vm.not.responding.breakpoint.can.not.be.set"),
        MessageType.ERROR
      )
      session.setBreakpointInvalid(breakpoint, "Target VM is not responding. Breakpoint can not be set")
    }*/
  }

  override fun unregisterBreakpoint(breakpoint: XLineBreakpoint<XBreakpointProperties<*>>, temporary: Boolean) {
    val sourcePosition = breakpoint.sourcePosition
    if (sourcePosition == null || !sourcePosition.file.exists() || !sourcePosition.file.isValid) {
      return
    }
    val file = sourcePosition.file
    val fileURL: String = getFileURL(file)
    val lineNumber: Int = breakpoint.line
    if (lineNumber == -1) {
      //myXsltDebugProcess.getSession().setBreakpointInvalid(breakpoint, "Unsupported breakpoint position")
      return
    }
    unregisterBreakpointEvent.fire(Pair(fileURL, breakpoint))  }

  fun getFileURL(file: VirtualFile?): String {
    return VfsUtil.virtualToIoFile(file!!).toURI().toASCIIString()
  }

}
