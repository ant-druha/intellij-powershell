/**
 * adopted from https://github.com/gtache/intellij-lsp
 */
package com.intellij.plugin.powershell.lang.lsp.util

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.DocumentUtil
import org.eclipse.lsp4j.Position
import org.eclipse.lsp4j.Range


object DocumentUtils {

  private val LOG: Logger = Logger.getInstance(DocumentUtils.toString())

  fun offsetToLSPPos(editor: Editor, offset: Int): Position {
    val doc = editor.document
    val line = doc.getLineNumber(offset)
    val lineStart = doc.getLineStartOffset(line)
    val lineTextBeforeOffset = doc.getText(TextRange.create(lineStart, offset))
    val column = lineTextBeforeOffset.length
    return Position(line, column)
  }

  fun logicalToLSPPos(position: LogicalPosition, editor: Editor): Position {
    return offsetToLSPPos(editor, editor.logicalPositionToOffset(position))
  }

  /**
   * ReadAccess must be allowed
   */
  @Throws(StringIndexOutOfBoundsException::class, IndexOutOfBoundsException::class)
  fun lspPosToOffset(editor: Editor, pos: Position): Int {
    val line = pos.line
    val doc = editor.document
    try {
      val lineTextForPosition = doc.getText(DocumentUtil.getLineTextRange(doc, line)).substring(0, pos.character)
      val tabs = StringUtil.countChars(lineTextForPosition, '\t')
      val tabSize = editor.settings.getTabSize(editor.project)
      val column = tabs * tabSize + lineTextForPosition.length - tabs
      val offset = editor.logicalPositionToOffset(LogicalPosition(line, column))
      val docLength = doc.textLength
      if (offset > docLength) {
        LOG.warn("Offset greater than text length : $offset > $docLength")
      }
      return Math.min(Math.max(offset, 0), docLength)
    } catch (e: StringIndexOutOfBoundsException) {
      throw StringIndexOutOfBoundsException("${e.message} for server Position: ${pToString(pos)}; Document line text: '${doc.getText(DocumentUtil.getLineTextRange(doc, line))}'")
    } catch (e: IndexOutOfBoundsException) {
      throw IndexOutOfBoundsException("${e.message} for server Position: ${pToString(pos)}; Document line text: '${doc.getText(DocumentUtil.getLineTextRange(doc, line))}'")
    }
  }

  fun rToString(range: Range): String = "(${pToString(range.start)}, ${pToString(range.start)})"

  private fun pToString(pos: Position): String = "[${pos.line}, ${pos.character}]"
}
