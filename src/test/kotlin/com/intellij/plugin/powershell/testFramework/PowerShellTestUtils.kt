package com.intellij.plugin.powershell.testFramework

import com.intellij.openapi.application.EDT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun runInEdt(test: suspend () -> Unit) {
  runBlocking {
    withContext(Dispatchers.EDT) {
      test()
    }
  }
}
