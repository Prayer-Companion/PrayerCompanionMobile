package com.prayercompanion.prayercompanionandroid

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.navigation.NavController
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import java.io.Serializable

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

fun <T : Serializable?> Intent.getSerializable(key: String, m_class: Class<T>): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        this.getSerializableExtra(key, m_class)!!
    else
        this.getSerializableExtra(key) as T
}