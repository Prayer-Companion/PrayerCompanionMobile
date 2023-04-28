package com.prayercompanion.prayercompanionandroid.presentation.utils

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {
    data class DynamicString(val text: String) : UiText()
    data class StringResource(@StringRes val resId: Int) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId)
        }
    }
}

fun String?.toUiText(): UiText.DynamicString {
    return UiText.DynamicString(this ?: "")
}

fun @receiver:StringRes Int.toUiText(): UiText.StringResource {
    return UiText.StringResource(this)
}