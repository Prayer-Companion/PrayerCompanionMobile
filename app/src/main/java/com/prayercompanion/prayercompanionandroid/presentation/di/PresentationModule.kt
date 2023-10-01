package com.prayercompanion.prayercompanionandroid.presentation.di

import com.prayercompanion.prayercompanionandroid.MainActivityViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.HomeScreenViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.permissions.PermissionsRequestViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.sign_in.SignInViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.splash_screen.SplashScreenViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.qibla.QiblaViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.full_sections.FullPrayerQuranSectionsViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.quran.QuranViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.settings.SettingsScreenViewModel
import com.prayercompanion.prayercompanionandroid.presentation.utils.AndroidPrayersAlarmScheduler
import com.prayercompanion.prayercompanionandroid.presentation.utils.MediaController
import com.prayercompanion.prayercompanionandroid.presentation.utils.OrientationSensor
import com.prayercompanion.prayercompanionandroid.presentation.utils.ScheduleDailyPrayersWorker
import com.prayercompanion.prayercompanionandroid.presentation.utils.notifications.PrayersNotificationsService
import com.prayercompanion.shared.data.repositories.AuthenticationRepositoryImpl
import com.prayercompanion.shared.domain.repositories.AuthenticationRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::SettingsScreenViewModel)
    viewModelOf(::MainActivityViewModel)
    viewModelOf(::QuranViewModel)
    viewModelOf(::QiblaViewModel)
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::PermissionsRequestViewModel)
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::FullPrayerQuranSectionsViewModel)
    singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
    singleOf(::PrayersNotificationsService)
    singleOf(::AndroidPrayersAlarmScheduler)
    singleOf(::OrientationSensor)
    singleOf(::MediaController)
    singleOf(::ScheduleDailyPrayersWorker)
}