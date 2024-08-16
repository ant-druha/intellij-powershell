package com.intellij.plugin.powershell.ide.debugger

import com.intellij.openapi.diagnostic.logger
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.xdebugger.breakpoints.XBreakpointHandler
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.intellij.xdebugger.breakpoints.XBreakpointType
import com.intellij.xdebugger.breakpoints.XLineBreakpoint

class PowerShellBreakpointHandler(
  val powerShellDebugProcess: PowerShellDebugProcess,
  breakpointTypeClass: Class<out XBreakpointType<XLineBreakpoint<XBreakpointProperties<*>>, *>>
) : XBreakpointHandler<XLineBreakpoint<XBreakpointProperties<*>>>(breakpointTypeClass) {

  override fun registerBreakpoint(breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
    val sourcePosition = breakpoint.sourcePosition
    if (sourcePosition == null || !sourcePosition.file.exists() || !sourcePosition.file.isValid) {
      logger.warn("Invalid breakpoint $breakpoint")
      return
    }
    val file = sourcePosition.file
    val lineNumber: Int = breakpoint.line
    if (lineNumber == -1) {
      powerShellDebugProcess.xDebugSession.setBreakpointInvalid(breakpoint, MessagesBundle.message("powershell.debugger.breakpoints.invalidBreakPoint"))
      logger.warn("Invalid breakpoint $breakpoint - line $lineNumber")
      return
    }
    powerShellDebugProcess.powerShellDebugSession.setBreakpoint(file.path, breakpoint)
  }

  override fun unregisterBreakpoint(breakpoint: XLineBreakpoint<XBreakpointProperties<*>>, temporary: Boolean) {
    val sourcePosition = breakpoint.sourcePosition
    if (sourcePosition == null || !sourcePosition.file.exists() || !sourcePosition.file.isValid) {
      logger.warn("Invalid breakpoint $breakpoint")
      return
    }
    val file = sourcePosition.file
    val lineNumber: Int = breakpoint.line
    if (lineNumber == -1) {
      logger.warn("Invalid breakpoint $breakpoint - line $lineNumber")
      return
    }
    powerShellDebugProcess.powerShellDebugSession.removeBreakpoint(file.path, breakpoint)
  }
}

private val logger = logger<PowerShellBreakpointHandler>()
