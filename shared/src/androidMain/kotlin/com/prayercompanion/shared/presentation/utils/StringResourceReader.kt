package com.prayercompanion.shared.presentation.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc

actual class StringResourceReader constructor(private val context: Context) {

    actual fun read(stringRes: StringResource, args: List<Any>): String {
        return StringDesc.ResourceFormatted(stringRes, args).toString(context)
    }
}

@Composable
actual fun stringResource(id: StringResource, args: List<Any>): String {
    val context = LocalContext.current
    return StringDesc.ResourceFormatted(id, args).toString(context)
}