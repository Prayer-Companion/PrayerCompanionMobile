package com.prayercompanion.shared.presentation.utils

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc

actual class StringResourceReader {

    actual fun read(stringRes: StringResource, args: List<Any>): String {
        return StringDesc.ResourceFormatted(stringRes, args).localized()
    }
}

@Composable
actual fun stringResource(id: StringResource, args: List<Any>): String {
    return StringDesc.ResourceFormatted(id, args).localized()
}