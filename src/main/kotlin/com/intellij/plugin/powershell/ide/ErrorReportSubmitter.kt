package com.intellij.plugin.powershell.ide

import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.extensions.ExtensionNotApplicableException

class ErrorReportSubmitter : ErrorReportSubmitter() {

  private val delegate: ErrorReportSubmitter
  init {
    try {
      delegate = Class.forName("com.intellij.diagnostic.JetBrainsMarketplaceErrorReportSubmitter")
        .declaredConstructors.first().newInstance() as ErrorReportSubmitter
    } catch (e: ClassNotFoundException) {
      throw ExtensionNotApplicableException.create()
    }
  }

    override fun changeReporterAccount(parentComponent: java.awt.Component): Unit = delegate.changeReporterAccount(parentComponent)

    override fun getPrivacyNoticeText(): String? = delegate.privacyNoticeText

    override fun getReportActionText(): String = delegate.reportActionText

    override fun getReporterAccount(): String? = delegate.reporterAccount

    override fun submit(events: Array<IdeaLoggingEvent>, additionalInfo: String?, parentComponent: java.awt.Component, consumer: com.intellij.util.Consumer<in com.intellij.openapi.diagnostic.SubmittedReportInfo>): Boolean =
            delegate.submit(events, additionalInfo, parentComponent, consumer)
}
