package com.prayercompanion.shared.presentation

import androidx.compose.ui.window.ComposeUIViewController
import com.prayercompanion.shared.iosMainModules
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        printLogger()
        modules(
            *appModules().toTypedArray(),
            *iosMainModules().toTypedArray(),
        )
    }
}

val uiViewController = ComposeUIViewController { App() }

fun MainViewController() = uiViewController