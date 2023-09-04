package com.intellij.plugin.powershell.ide

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey
import java.util.function.Supplier


@Suppress("unused")
object MessagesBundle {
  @JvmStatic
  fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
    return delegate.getMessage(key, *params)
  }

  @JvmStatic
  fun messagePointer(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): Supplier<String> {
    return delegate.getLazyMessage(key, *params)
  }

  @NonNls private const val BUNDLE = "messages.MessagesBundle"
  private val delegate = DynamicBundle(MessagesBundle::class.java, BUNDLE)
}