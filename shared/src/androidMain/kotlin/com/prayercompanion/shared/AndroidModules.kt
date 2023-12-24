package com.prayercompanion.shared

import com.prayercompanion.shared.data.di.androidDataModule
import com.prayercompanion.shared.domain.di.androidDomainModule

fun androidMainModules() = listOf(androidDataModule, androidDomainModule)