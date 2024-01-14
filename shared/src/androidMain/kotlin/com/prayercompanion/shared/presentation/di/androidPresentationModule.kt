package com.prayercompanion.shared.presentation.di

import com.prayercompanion.shared.MainActivityViewModel
import com.prayercompanion.shared.data.local.system.ScheduleDailyPrayersNotificationsWorker
import com.prayercompanion.shared.presentation.utils.StringResourceReader
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidPresentationModule = module {
    singleOf(::StringResourceReader)
    viewModelOf(::MainActivityViewModel)
    singleOf(::ScheduleDailyPrayersNotificationsWorker)
}