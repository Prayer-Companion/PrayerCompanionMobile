package com.prayercompanion.prayercompanionandroid

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

fun <T> Result.Companion.failure(message: String): Result<T> {
    return failure(Exception(message))
}

inline fun <reified T> fromJson(str: String): T? {
    return runCatching<T> {
        Json.decodeFromString(str)
    }.getOrElse {
        it.printStackTraceInDebug()
        null
    }
}

inline fun <reified T> T.toJson(): String {
    return Json.encodeToString(this)
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