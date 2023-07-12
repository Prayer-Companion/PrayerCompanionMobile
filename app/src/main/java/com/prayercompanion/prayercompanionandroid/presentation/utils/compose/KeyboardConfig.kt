package com.prayercompanion.prayercompanionandroid.presentation.utils.compose

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

enum class KeyboardConfig {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<KeyboardConfig> {
    val keyboardConfigState = remember { mutableStateOf(KeyboardConfig.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardConfigState.value = if (keypadHeight > screenHeight * 0.15) {
                KeyboardConfig.Opened
            } else {
                KeyboardConfig.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardConfigState
}