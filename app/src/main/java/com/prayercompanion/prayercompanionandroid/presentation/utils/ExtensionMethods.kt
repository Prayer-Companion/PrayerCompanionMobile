package com.prayercompanion.prayercompanionandroid.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.prayercompanion.shared.presentation.utils.StringRes
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.UiText

fun NavController.navigate(event: UiEvent.Navigate, builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(route = event.route.name + event.args.joinToString { "/$it" }, builder)
}

fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun UiText.asString(context: Context): String {
    return when (this) {
        is UiText.DynamicString -> text
        is UiText.StringResource -> {
            context.getString(stringRes.resId(context))
        }
    }
}

@SuppressLint("DiscouragedApi")
@androidx.annotation.StringRes
fun StringRes.resId(context: Context): Int {
    return context.resources.getIdentifier(id, "string", context.packageName)
}