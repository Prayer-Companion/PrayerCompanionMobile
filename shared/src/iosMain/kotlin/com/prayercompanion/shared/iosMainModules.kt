package com.prayercompanion.shared

import com.prayercompanion.shared.data.di.iosDataModule
import com.prayercompanion.shared.domain.di.iosDomainModule

fun iosMainModules() = listOf(iosDataModule, iosDomainModule)