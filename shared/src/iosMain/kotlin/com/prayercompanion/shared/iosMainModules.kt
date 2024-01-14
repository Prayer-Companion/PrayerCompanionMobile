package com.prayercompanion.shared

import com.prayercompanion.shared.data.di.iosDataModule
import com.prayercompanion.shared.domain.di.iosDomainModule
import com.prayercompanion.shared.presentation.di.iosPresentationModule

fun iosMainModules() = listOf(iosPresentationModule, iosDataModule, iosDomainModule)