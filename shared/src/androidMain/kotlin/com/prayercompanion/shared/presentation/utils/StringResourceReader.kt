package com.prayercompanion.shared.presentation.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@SuppressLint("DiscouragedApi")
@Composable
actual fun stringResource(id: String): String {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(id, "string", context.packageName)
    return context.getString(resId)
}