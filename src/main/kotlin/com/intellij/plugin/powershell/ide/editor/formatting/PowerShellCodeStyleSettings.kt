package com.intellij.plugin.powershell.ide.editor.formatting

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class PowerShellCodeStyleSettings(settings: CodeStyleSettings?) : CustomCodeStyleSettings("PowerShellCodeStyleSettings", settings) {

  @JvmField
  var CATCH_TYPE_LIST_WRAP = CommonCodeStyleSettings.DO_NOT_WRAP
  @JvmField
  var ATTRIBUTE_ARGUMENT_WRAP = CommonCodeStyleSettings.DO_NOT_WRAP
  @JvmField
  var PIPELINE_TAIL_WRAP = CommonCodeStyleSettings.DO_NOT_WRAP
  @JvmField
  var ALIGN_MULTILINE_CATCH_TYPE_LIST = false
  @JvmField
  var ALIGN_MULTILINE_ATTRIBUTE_ARGUMENT = false
  @JvmField
  var ALIGN_MULTILINE_PIPELINE_STATEMENT = false
  @JvmField
  var KEEP_SIMPLE_HASH_LITERAL_IN_ONE_LINE = true
  @JvmField
  var SPACE_WITHIN_SUB_EXPRESSION_PARENTHESES = true
  @JvmField
  var SPACE_BEFORE_DATA_LBRACE = true
  @JvmField
  var SPACE_BEFORE_TRAP_LBRACE = true
  @JvmField
  var SPACE_BEFORE_COLON = false
  @JvmField
  var SPACE_AFTER_COLON = true
}