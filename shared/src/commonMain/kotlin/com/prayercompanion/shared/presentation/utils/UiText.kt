package com.prayercompanion.shared.presentation.utils

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
sealed class UiText {
    data class DynamicString(val text: String) : UiText()
    data class StringResource(val stringRes: StringRes) : UiText()
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> stringResource(stringRes.id)
        }
    }
}

fun String?.toUiText(): UiText.DynamicString {
    return UiText.DynamicString(this ?: "")
}

fun StringRes.toUiText(): UiText.StringResource {
    return UiText.StringResource(this)
}