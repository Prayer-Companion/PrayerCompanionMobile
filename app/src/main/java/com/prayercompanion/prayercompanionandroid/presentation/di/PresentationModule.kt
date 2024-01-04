package com.prayercompanion.prayercompanionandroid.presentation.di

import com.prayercompanion.prayercompanionandroid.MainActivityViewModel
import com.prayercompanion.prayercompanionandroid.presentation.utils.AndroidPrayersAlarmScheduler
import com.prayercompanion.prayercompanionandroid.presentation.utils.ScheduleDailyPrayersWorker
import com.prayercompanion.prayercompanionandroid.presentation.utils.notifications.PrayersNotificationsService
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidPresentationModule = module {
    viewModelOf(::MainActivityViewModel)
    singleOf(::PrayersNotificationsService)
    singleOf(::AndroidPrayersAlarmScheduler)
    singleOf(::ScheduleDailyPrayersWorker)
}