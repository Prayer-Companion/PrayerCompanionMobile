package com.prayercompanion.shared.presentation.utils

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource
import kotlinx.serialization.Serializable

@Serializable
sealed class UiText {
    data class DynamicString(val text: String) : UiText()
    data class StringResource(
        val stringRes: dev.icerock.moko.resources.StringResource,
        val args: List<Any> = emptyList()
    ) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> stringResource(stringRes)
        }
    }

}

fun StringResource.toUiText(args: List<Any> = emptyList()): UiText.StringResource {
    return UiText.StringResource(this, args)
}

fun UiText.asString(stringResourceReader: StringResourceReader): String {
    return when (this) {
        is UiText.DynamicString -> text
        is UiText.StringResource -> stringResourceReader.read(stringRes, args)
    }
}