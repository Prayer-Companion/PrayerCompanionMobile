package com.prayercompanion.shared.presentation.utils

expect fun Any.log(msg: () -> String)

expect fun Throwable.printStackTraceInDebug()