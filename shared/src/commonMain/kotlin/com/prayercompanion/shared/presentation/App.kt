package com.prayercompanion.shared.presentation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.prayercompanion.shared.data.di.dataModule
import com.prayercompanion.shared.domain.di.domainModule
import com.prayercompanion.shared.presentation.di.presentationModule
import com.prayercompanion.shared.presentation.features.onboarding.splash_screen.SplashScreen
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme

fun appModule() = listOf(presentationModule, domainModule, dataModule)

@Composable
fun App() {
    PrayerCompanionAndroidTheme {
        Navigator(SplashScreen)
    }
}