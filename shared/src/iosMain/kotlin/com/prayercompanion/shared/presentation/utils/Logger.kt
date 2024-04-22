package com.prayercompanion.shared.presentation.utils

import platform.Foundation.NSLog


actual fun log(msg: () -> String) {
    NSLog("DebugLog: ${msg()}")
}

actual fun Throwable.printStackTraceInDebug() {
    NSLog(this.stackTraceToString())
}