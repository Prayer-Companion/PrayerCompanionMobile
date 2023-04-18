package com.prayercompanion.prayercompanionandroid

import android.util.Log

fun Throwable.printStackTraceInDebug() {
    if (BuildConfig.DEBUG) {
        printStackTrace()
    }
}

inline fun Any.log(message: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message())
    }
}

inline fun Any.logE(message: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.e(this::class.java.simpleName, message())
    }
}