package com.intellij.plugin.powershell

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory
import com.intellij.openapi.util.text.StringUtil

/**
 * Andrey 17/07/17.
 */
class PowerShellFileTypeFactory : FileTypeFactory() {
  override fun createFileTypes(consumer: FileTypeConsumer) {
    consumer.consume(PowerShellFileType(), StringUtil.join(PowerShellFileType.INSTANCE.getExtensions(), FileTypeConsumer.EXTENSION_DELIMITER))
  }
}