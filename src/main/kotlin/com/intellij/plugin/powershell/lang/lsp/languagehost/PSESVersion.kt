package com.intellij.plugin.powershell.lang.lsp.languagehost

import java.lang.NumberFormatException

class PSESVersion(private val major: Int, private val minor: Int, private val bugFix: Int) : Comparable<PSESVersion> {

  override fun compareTo(other: PSESVersion): Int {
    if (major > other.major) return 1
    if (major < other.major) return -1

    if (minor > other.minor) return 1
    if (minor < other.minor) return -1

    if (bugFix > other.bugFix) return 1
    if (bugFix < other.bugFix) return -1

    return 0
  }

  public fun isGreaterOrEqual(other: PSESVersion): Boolean = compareTo(other) >= 0
  public fun isLessOrEqual(other: PSESVersion): Boolean = compareTo(other) <= 0

  override fun toString(): String {
    return "$major.$minor.$bugFix"
  }

  companion object {

    val v1_7_0 = PSESVersion(1, 7, 0)
    val v1_8_0 = PSESVersion(1, 8, 0)

    @Throws(NumberFormatException::class)
    fun create(string: String): PSESVersion? {
      val versions = string.split('.')
      val major = if (versions.isNotEmpty()) versions[0].toInt() else return null
      val minor = if (versions.size > 1) versions[1].toInt() else 0
      val bugFix = if (versions.size > 2) versions[2].toInt() else 0
      return PSESVersion(major, minor, bugFix)
    }
  }
}