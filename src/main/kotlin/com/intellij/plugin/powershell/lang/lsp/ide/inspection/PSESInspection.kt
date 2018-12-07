package com.intellij.plugin.powershell.lang.lsp.ide.inspection

import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.TextRange
import com.intellij.plugin.powershell.lang.lsp.ide.EditorEventManager
import com.intellij.plugin.powershell.lang.lsp.psi.LSPInspectionPsiElementImpl
import com.intellij.plugin.powershell.lang.lsp.util.DocumentUtils.lspPosToOffset
import com.intellij.plugin.powershell.lang.lsp.util.DocumentUtils.rToString
import com.intellij.plugin.powershell.lang.lsp.util.getTextEditor
import com.intellij.psi.PsiFile
import org.eclipse.lsp4j.Diagnostic
import org.eclipse.lsp4j.DiagnosticSeverity

class PSESInspection : LocalInspectionTool() {

  private val LOG: Logger = Logger.getInstance(javaClass)

  override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
    val vFile = file.virtualFile ?: return null
    val editor = getTextEditor(vFile, manager.project) ?: return null
    val eventManager = EditorEventManager.forEditor(editor) ?: return null
    val diagnostics: List<Diagnostic> = eventManager.getDiagnostics()
    val result = mutableListOf<ProblemDescriptor>()
    for (d in diagnostics) {
      val range = d.range
      try {
        val message = d.message
        val highlightType: ProblemHighlightType = getHighlightType(d.severity)
        var rangeStart = lspPosToOffset(editor, range.start)
        var rangeEnd = lspPosToOffset(editor, range.end)
        if (rangeStart == rangeEnd) {
          if (rangeStart > 0) rangeStart -= 1 else if (rangeEnd < file.text.length) rangeEnd += 1
        }
        val element = if (rangeStart < rangeEnd) {
          LSPInspectionPsiElementImpl(file, TextRange.create(rangeStart, rangeEnd))
        } else {
          file
        }
        val problemDescriptor = manager.createProblemDescriptor(element, message, isOnTheFly, null, highlightType)
        result.add(problemDescriptor)
      } catch (e: StringIndexOutOfBoundsException) {
        LOG.warn("String out of bounds for server range ${rToString(range)}: ${e.message}")
      } catch (e: IndexOutOfBoundsException) {
        LOG.warn("Index out of bounds for server range ${rToString(range)}: ${e.message}")
      }
    }
    return result.toTypedArray()
  }

  private fun getHighlightType(severity: DiagnosticSeverity): ProblemHighlightType {
    return when (severity) {
      DiagnosticSeverity.Error -> ProblemHighlightType.GENERIC_ERROR
      DiagnosticSeverity.Warning -> ProblemHighlightType.GENERIC_ERROR_OR_WARNING
      DiagnosticSeverity.Information -> ProblemHighlightType.INFORMATION
      DiagnosticSeverity.Hint -> ProblemHighlightType.INFORMATION
    }
  }

  override fun isEnabledByDefault() = true

  override fun getStaticDescription(): String {
    return "Reports problems found by PowerShell ScriptAnalyzer tool."
  }
}