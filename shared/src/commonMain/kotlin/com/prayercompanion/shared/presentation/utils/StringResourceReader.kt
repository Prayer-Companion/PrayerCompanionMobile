package com.prayercompanion.shared.presentation.utils

import androidx.compose.runtime.Composable


expect class StringResourceReader {

    fun read(stringRes: StringRes): String
}

@Composable
expect fun stringResource(stringRes: StringRes): String