package com.prayercompanion.prayercompanionandroid

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent

fun NavController.navigate(event: UiEvent.Navigate) {
    this.navigate(event.route.name)
}

fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Throwable.printStackTraceInDebug() {
    if (BuildConfig.DEBUG) {
        printStackTrace()
    }
}