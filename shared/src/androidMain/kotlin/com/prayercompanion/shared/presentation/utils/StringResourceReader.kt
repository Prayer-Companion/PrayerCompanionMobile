package com.prayercompanion.shared.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual class StringResourceReader constructor(private val context: Context) {

    actual fun read(stringRes: StringRes): String {
        val resId = stringRes.resId(context)
        return context.getString(resId)
    }
}

@Composable
actual fun stringResource(stringRes: StringRes): String {
    val context = LocalContext.current
    val resId = stringRes.resId(context)
    return context.getString(resId)
}

@SuppressLint("DiscouragedApi")
@androidx.annotation.StringRes
fun StringRes.resId(context: Context): Int {
    return context.resources.getIdentifier(id, "string", context.packageName)
}