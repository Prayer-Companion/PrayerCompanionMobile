package com.prayercompanion.shared.data.di

import com.prayercompanion.shared.data.local.DatabaseDriverFactory
import com.prayercompanion.shared.data.local.assets.AssetsReader
import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.data.system.AppLocationManager
import com.prayercompanion.shared.data.system.AppLocationManagerImpl
import com.prayercompanion.shared.data.utils.TrackerImpl
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val iosDataModule = module {
    singleOf(::DataStoresRepo)
    singleOf(::DatabaseDriverFactory)
    singleOf(::AppLocationManagerImpl) { bind<AppLocationManager>() }
    singleOf(::TrackerImpl) { bind<Tracker>() }
    singleOf(::AssetsReader)
}