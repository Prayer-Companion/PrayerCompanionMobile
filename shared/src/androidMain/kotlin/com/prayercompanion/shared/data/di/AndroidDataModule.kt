package com.prayercompanion.shared.data.di

import app.cash.sqldelight.db.SqlDriver
import com.google.firebase.analytics.FirebaseAnalytics
import com.prayercompanion.prayercompanionandroid.PrayerCompanionDatabase
import com.prayercompanion.shared.SQLDelightDriverFactory
import com.prayercompanion.shared.data.local.assets.AssetsReader
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidDataModule = module {
    single {
        val driver: SqlDriver = SQLDelightDriverFactory(androidContext()).createDriver()
        PrayerCompanionDatabase(driver)
    }
    single { FirebaseAnalytics.getInstance(androidContext()) }
    singleOf(::AssetsReader)
}