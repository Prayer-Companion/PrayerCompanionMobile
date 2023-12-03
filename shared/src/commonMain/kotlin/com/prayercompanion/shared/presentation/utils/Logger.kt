package com.prayercompanion.shared.presentation.utils

expect fun log(msg: () -> String)

expect fun Throwable.printStackTraceInDebug()