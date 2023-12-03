package com.prayercompanion.shared.presentation.utils

import platform.Foundation.NSLog


actual fun log(msg: () -> String) {
    NSLog(msg())
}

actual fun Throwable.printStackTraceInDebug() {
    NSLog(this.stackTraceToString())
}