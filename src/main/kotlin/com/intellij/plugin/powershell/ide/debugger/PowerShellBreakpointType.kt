package com.intellij.plugin.powershell.ide.debugger

import com.intellij.idea.ActionsBundle
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.plugin.powershell.PowerShellFileType
import com.intellij.plugin.powershell.ide.MessagesBundle
import com.intellij.psi.PsiDocumentManager
import com.intellij.ui.components.AnActionLink
import com.intellij.ui.components.JBTextField
import com.intellij.ui.components.Label
import com.intellij.util.ui.JBUI
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.intellij.xdebugger.breakpoints.XLineBreakpoint
import com.intellij.xdebugger.breakpoints.XLineBreakpointType
import com.intellij.xdebugger.breakpoints.ui.XBreakpointCustomPropertiesPanel
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider
import com.intellij.xdebugger.impl.breakpoints.XBreakpointBase
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.*

class PowerShellBreakpointType : XLineBreakpointType<XBreakpointProperties<*>>("powershell", MessagesBundle.message("powershell.debugger.breakpoints.title")) {
  override fun canPutAt(file: VirtualFile, line: Int, project: Project): Boolean {
    val document = FileDocumentManager.getInstance().getDocument(file) ?: return false

    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: return false
    val fileType = psiFile.fileType
    return fileType is PowerShellFileType
  }

  override fun createBreakpointProperties(file: VirtualFile, line: Int): XBreakpointProperties<*>? {
    return null
  }

  override fun getEditorsProvider(
    breakpoint: XLineBreakpoint<XBreakpointProperties<*>>,
    project: Project
  ): XDebuggerEditorsProvider {
    return PowerShellDebuggerEditorsProvider(XDebuggerManager.getInstance(project).currentSession, "BPTYPE")
  }


  /*override fun createCustomConditionsPanel(): XBreakpointCustomPropertiesPanel<XLineBreakpoint<XBreakpointProperties<*>>> {
    return CollectionBreakpointPropertiesPanel()
  }*/
}

class CollectionBreakpointPropertiesPanel : XBreakpointCustomPropertiesPanel<XLineBreakpoint<XBreakpointProperties<*>>>() {
  private var myClsName: String? = null
  private var myFieldName: String? = "Condition:"
  private var mySaveCollectionHistoryCheckBox: JCheckBox? = null

  override fun getComponent(): JComponent {

    val box = Box.createVerticalBox()

    var panel: JPanel = JBUI.Panels.simplePanel()
    //panel.add(JTextField())
    val textField = JBTextField()
    val label = Label("Condition:").apply {
      labelFor = textField
      alignmentX = Component.LEFT_ALIGNMENT
    }
    box.add(label)
    box.add(textField)
    panel.add(box)

    return panel
  }

  override fun saveTo(breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
   /* val changed =
      breakpoint.getProperties().SHOULD_SAVE_COLLECTION_HISTORY !== mySaveCollectionHistoryCheckBox!!.isSelected
    breakpoint.getProperties().SHOULD_SAVE_COLLECTION_HISTORY = mySaveCollectionHistoryCheckBox!!.isSelected*/
    /*if (true) {
      (breakpoint as XBreakpointBase<*, *, *>).fireBreakpointChanged()
    }*/
  }

  override fun loadFrom(breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
    /*val properties = breakpoint.getProperties()
    myClsName = properties.myClassName
    myFieldName = properties.myFieldName
    mySaveCollectionHistoryCheckBox!!.isSelected = properties.SHOULD_SAVE_COLLECTION_HISTORY*/
  }

  private inner class MyShowCollectionHistoryAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
      val clsName = myClsName
      val fieldName = myFieldName
      if (clsName == null || fieldName == null) {
        return
      }
      val project = getEventProject(e) ?: return
      val session = XDebuggerManager.getInstance(project).currentSession ?: return
      //DebuggerUtilsEx.addCollectionHistoryTab(session, clsName, fieldName, null)
    }
  }

  companion object {
    private const val PREFERRED_PANEL_HEIGHT = 40
  }
}
