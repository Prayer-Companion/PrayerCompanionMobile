package com.prayercompanion.shared.presentation.utils

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource

expect class StringResourceReader {

    fun read(stringRes: StringResource, args: List<Any> = emptyList()): String
}

@Composable
expect fun stringResource(id: StringResource, args: List<Any> = emptyList()): String