package com.prayercompanion.prayercompanionandroid

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent

fun NavController.navigate(event: UiEvent.Navigate, builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(route = event.route.name + event.args.joinToString { "/$it" }, builder)
}

fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

@Stable
fun Modifier.autoMirror(): Modifier = composed {
    if (LocalLayoutDirection.current == LayoutDirection.Rtl)
        this.scale(scaleX = -1f, scaleY = 1f)
    else
        this
}