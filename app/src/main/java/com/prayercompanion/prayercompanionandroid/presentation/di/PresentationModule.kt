package com.prayercompanion.prayercompanionandroid.presentation.di

import com.prayercompanion.prayercompanionandroid.MainActivityViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.qibla.QiblaViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.full_sections.FullPrayerQuranSectionsViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.quran.QuranViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.settings.SettingsScreenViewModel
import com.prayercompanion.prayercompanionandroid.presentation.utils.AndroidPrayersAlarmScheduler
import com.prayercompanion.prayercompanionandroid.presentation.utils.MediaController
import com.prayercompanion.prayercompanionandroid.presentation.utils.OrientationSensor
import com.prayercompanion.prayercompanionandroid.presentation.utils.ScheduleDailyPrayersWorker
import com.prayercompanion.prayercompanionandroid.presentation.utils.notifications.PrayersNotificationsService
import com.prayercompanion.shared.presentation.features.home_screen.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidPresentationModule = module {
    viewModelOf(::SettingsScreenViewModel)
    viewModelOf(::MainActivityViewModel)
    viewModelOf(::QuranViewModel)
    viewModelOf(::QiblaViewModel)
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::FullPrayerQuranSectionsViewModel)
    singleOf(::PrayersNotificationsService)
    singleOf(::AndroidPrayersAlarmScheduler)
    singleOf(::OrientationSensor)
    singleOf(::MediaController)
    singleOf(::ScheduleDailyPrayersWorker)
}