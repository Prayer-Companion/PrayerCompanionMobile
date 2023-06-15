package com.prayercompanion.prayercompanionandroid

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.google.gson.Gson
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun NavController.navigate(event: UiEvent.Navigate, builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(route = event.route.name + event.args.joinToString { "/$it" }, builder)
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

fun <T> Result.Companion.failure(message: String): Result<T> {
    return failure(Exception(message))
}

fun LocalDate.atEndOfDay(): LocalDateTime {
    return this.atTime(LocalTime.MAX)
}

inline fun <reified T> fromJson(str: String): T? {
    return Gson().fromJson(str, T::class.java)
}

inline fun <reified T> toJson(t: T): String {
    return Gson().toJson(t)
}
