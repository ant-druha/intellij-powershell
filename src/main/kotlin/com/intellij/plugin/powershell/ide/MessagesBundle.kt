package com.intellij.plugin.powershell.ide

import com.intellij.CommonBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.util.*


object MessagesBundle {
  fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
    return CommonBundle.message(getBundle(), key, *params)
  }

  private var ourBundle: Reference<ResourceBundle>? = null
  @NonNls private const val BUNDLE = "messages.MessagesBundle"


  private fun getBundle(): ResourceBundle {
    var bundle = com.intellij.reference.SoftReference.dereference<ResourceBundle>(ourBundle)
    if (bundle == null) {
      bundle = ResourceBundle.getBundle(BUNDLE)
      ourBundle = SoftReference(bundle)
      return bundle
    }
    return bundle
  }
}