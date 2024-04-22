package com.prayercompanion.shared.data.remote.logger

import com.prayercompanion.shared.domain.utils.ErrorLogger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

class ErrorLoggerImpl : ErrorLogger {

    private val crashlytics = Firebase.crashlytics

    override fun logException(e: Exception) {
        crashlytics.recordException(e)
    }

}