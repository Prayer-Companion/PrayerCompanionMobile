package com.prayercompanion.shared.presentation

import com.prayercompanion.shared.BuildConfig
import logcat.logcat

actual fun Any.log(msg: () -> String) {
    logcat { msg() }
}

actual fun Throwable.printStackTraceInDebug() {
    if (BuildConfig.DEBUG) {
        printStackTrace()
    }
}