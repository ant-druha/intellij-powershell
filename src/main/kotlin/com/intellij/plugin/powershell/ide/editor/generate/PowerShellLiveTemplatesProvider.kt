package com.intellij.plugin.powershell.ide.editor.generate

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider

class PowerShellLiveTemplatesProvider: DefaultLiveTemplatesProvider {
  private val TEMPLATE_FILES = arrayOf("liveTemplates/PowerShellLiveTemplates")

  override fun getDefaultLiveTemplateFiles(): Array<String> = TEMPLATE_FILES

  override fun getHiddenLiveTemplateFiles(): Array<String>? = null
}