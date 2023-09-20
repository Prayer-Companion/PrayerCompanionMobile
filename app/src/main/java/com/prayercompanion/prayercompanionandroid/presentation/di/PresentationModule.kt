package com.prayercompanion.prayercompanionandroid.presentation.di

import android.app.Activity
import com.prayercompanion.prayercompanionandroid.BuildConfig
import com.prayercompanion.prayercompanionandroid.MainActivityViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.HomeScreenViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.permissions.PermissionsRequestViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.sign_in.SignInViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.splash_screen.SplashScreenViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.qibla.QiblaViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.quran.QuranViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.settings.SettingsScreenViewModel
import com.prayercompanion.prayercompanionandroid.presentation.utils.FeedbackUtilsImpl
import io.doorbell.android.Doorbell
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    single { FeedbackUtilsImpl(androidContext(), get()) }
    single {
        Doorbell(
            androidContext() as Activity,
            BuildConfig.DOORBELL_ID.toLong(),
            BuildConfig.DOORBELL_PRIVATE_KEY
        )
    }

    viewModelOf(::SettingsScreenViewModel)
    viewModelOf(::MainActivityViewModel)
    viewModelOf(::QuranViewModel)
    viewModelOf(::QiblaViewModel)
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::PermissionsRequestViewModel)
    viewModelOf(::HomeScreenViewModel)
}