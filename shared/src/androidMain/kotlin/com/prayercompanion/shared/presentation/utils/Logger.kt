package com.prayercompanion.shared.presentation.utils

import com.prayercompanion.shared.BuildConfig
import logcat.logcat

actual fun log(msg: () -> String) {
    logcat("DebugLog", message = { msg() })
}

actual fun Throwable.printStackTraceInDebug() {
    if (BuildConfig.DEBUG) {
        printStackTrace()
    }
}