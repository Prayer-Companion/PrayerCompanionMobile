package com.prayercompanion.prayercompanionandroid

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.google.gson.Gson
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import java.io.Serializable

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

@Suppress("DEPRECATION", "UNCHECKED_CAST")
fun <T : Serializable?> Intent.getSerializable(key: String, mClass: Class<T>): T? {
    return runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializableExtra(key, mClass)
        else
            this.getSerializableExtra(key) as T?
    }.getOrNull()
}

fun <T> Result.Companion.failure(message: String): Result<T> {
    return failure(Exception(message))
}

inline fun <reified T> fromJson(str: String): T? {
    return Gson().fromJson(str, T::class.java)
}

inline fun <reified T> toJson(t: T): String {
    return Gson().toJson(t)
}

@Stable
fun Modifier.autoMirror(): Modifier = composed {
    if (LocalLayoutDirection.current == LayoutDirection.Rtl)
        this.scale(scaleX = -1f, scaleY = 1f)
    else
        this
}

fun Long.toBoolean() = this != 0L
fun Boolean.toLong() = if (this) 1L else 0L