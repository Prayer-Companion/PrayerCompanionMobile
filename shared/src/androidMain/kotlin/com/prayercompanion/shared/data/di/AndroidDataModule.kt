package com.prayercompanion.shared.data.di

import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidDataModule = module {
    single { FirebaseAnalytics.getInstance(androidContext()) }
}