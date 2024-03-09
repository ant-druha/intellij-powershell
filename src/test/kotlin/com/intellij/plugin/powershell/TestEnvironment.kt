package com.intellij.plugin.powershell

val isOnCiServer =
  System.getenv("CI").equals("true", ignoreCase = true)
