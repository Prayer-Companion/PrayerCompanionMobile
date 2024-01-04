package com.prayercompanion.shared.presentation.di

import com.prayercompanion.shared.presentation.utils.StringResourceReader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val iosPresentationModule = module {
    singleOf(::StringResourceReader)
}