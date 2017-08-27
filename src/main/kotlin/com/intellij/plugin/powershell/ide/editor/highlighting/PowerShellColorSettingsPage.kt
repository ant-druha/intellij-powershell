package com.intellij.plugin.powershell.ide.editor.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.plugin.powershell.PowerShellIcons
import java.util.*
import javax.swing.Icon

/**
 * Andrey 05/08/17.
 */
class PowerShellColorSettingsPage : ColorSettingsPage {

  @JvmField
  val additionalTags = HashMap<String, TextAttributesKey>()
  @JvmField
  val DESCRIPTORS: Array<AttributesDescriptor> = arrayOf(
      AttributesDescriptor("Keyword", PowerShellSyntaxHighlighter.KEYWORD),
      AttributesDescriptor("Comment", PowerShellSyntaxHighlighter.COMMENT),
      AttributesDescriptor("Number", PowerShellSyntaxHighlighter.NUMBER),
      AttributesDescriptor("String", PowerShellSyntaxHighlighter.STRING),
      AttributesDescriptor("Command name", PowerShellSyntaxHighlighter.COMMAND_NAME)
  )

  init {
    additionalTags.put("keyword", PowerShellSyntaxHighlighter.KEYWORD)
    additionalTags.put("comment", PowerShellSyntaxHighlighter.COMMENT)
    additionalTags.put("number", PowerShellSyntaxHighlighter.NUMBER)
    additionalTags.put("string", PowerShellSyntaxHighlighter.STRING)
    additionalTags.put("command name", PowerShellSyntaxHighlighter.COMMAND_NAME)
  }

  override fun getHighlighter(): SyntaxHighlighter {
    return PowerShellSyntaxHighlighter()
  }

  override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> {
    return additionalTags
  }

  override fun getIcon(): Icon? {
    return PowerShellIcons.FILE
  }

  override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
    return DESCRIPTORS
  }

  override fun getColorDescriptors(): Array<ColorDescriptor> {
    return ColorDescriptor.EMPTY_ARRAY
  }

  override fun getDisplayName(): String {
    return "PowerShell"
  }

  override fun getDemoText(): String {
    return "foreach (\$i in <command name>get-childitem</command name> | <command name>sort-object</command name> length)\n" +
        " {\n" +
        " \$i  #highlight\n" +
        " \$sum += \$i.length\n" +
        " }\n" +
        "\n" +
        "# 3) variable name token: '\$_.length ' and 'get-childitem'\n" +
        "switch -regex -casesensitive (<command name>get-childitem</command name> | sort length)\n" +
        "{\n" +
        " \"^5\" {\"length for \$_ started with 5\" ; continue}\n" +
        " { \$_.length > 20000 } {\"length of \$_ is greater than 20000\"}\n" +
        " default {\"Didn't match anything else...\"}\n" +
        "}"
  }
}