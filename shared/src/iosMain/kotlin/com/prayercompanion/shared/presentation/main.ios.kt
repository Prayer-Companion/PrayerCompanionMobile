package com.prayercompanion.shared.presentation

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        printLogger()
        modules(appModule())
    }
}

val uiViewController = ComposeUIViewController { App() }

fun MainViewController() = uiViewController