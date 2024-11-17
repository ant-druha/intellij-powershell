package com.intellij.plugin.powershell.ide

import com.intellij.openapi.diagnostic.Logger
import kotlin.coroutines.cancellation.CancellationException

/**
 * See [IJPL-171743](https://youtrack.jetbrains.com/issue/IJPL-171743) for details: IntelliJ has a similar function, but
 * it's marked as internal.
 */
inline fun <T> Logger.runAndLogException(action: () -> T): T? {
  try {
    return action()
  } catch (t: Throwable) {
    if (t is CancellationException) throw t
    error(t)
    return null
  }
}
