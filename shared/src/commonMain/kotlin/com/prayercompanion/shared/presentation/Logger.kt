package com.prayercompanion.shared.presentation

expect fun Any.log(msg: () -> String)

expect fun Throwable.printStackTraceInDebug()