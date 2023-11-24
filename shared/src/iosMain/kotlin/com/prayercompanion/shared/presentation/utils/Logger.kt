package com.prayercompanion.shared.presentation.utils

import platform.Foundation.NSLog


actual fun Any.log(msg: () -> String) {
    NSLog(msg())
}

actual fun Throwable.printStackTraceInDebug() {
    NSLog(this.stackTraceToString())
}