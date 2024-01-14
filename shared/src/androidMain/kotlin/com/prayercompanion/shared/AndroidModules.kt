package com.prayercompanion.shared

import com.prayercompanion.shared.data.di.androidDataModule
import com.prayercompanion.shared.domain.di.androidDomainModule
import com.prayercompanion.shared.presentation.di.androidPresentationModule

fun androidMainModules() = listOf(androidPresentationModule, androidDomainModule, androidDataModule)