package com.prayercompanion.shared

import com.prayercompanion.shared.domain.extensions.now
import com.prayercompanion.shared.presentation.utils.printStackTraceInDebug
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

fun Long.toBoolean() = this != 0L
fun Boolean.toLong() = if (this) 1L else 0L

fun currentTimeMillis(): Long {
    return LocalDateTime.now().toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}