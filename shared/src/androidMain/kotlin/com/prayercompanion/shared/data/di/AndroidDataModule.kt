package com.prayercompanion.shared.data.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.prayercompanion.shared.data.local.assets.AssetsReader
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidDataModule = module {
    single { FirebaseAnalytics.getInstance(androidContext()) }
    singleOf(::AssetsReader)
}