package com.prayercompanion.shared.presentation.di

import com.prayercompanion.shared.data.repositories.AuthenticationRepositoryImpl
import com.prayercompanion.shared.domain.repositories.AuthenticationRepository
import com.prayercompanion.shared.presentation.features.main.home_screen.HomeScreenViewModel
import com.prayercompanion.shared.presentation.features.main.quran.full_sections.FullPrayerQuranSectionsViewModel
import com.prayercompanion.shared.presentation.features.main.quran.quran.QuranViewModel
import com.prayercompanion.shared.presentation.features.main.settings.SettingsScreenViewModel
import com.prayercompanion.shared.presentation.features.onboarding.permissions.PermissionsRequestViewModel
import com.prayercompanion.shared.presentation.features.onboarding.sign_in.SignInViewModel
import com.prayercompanion.shared.presentation.features.onboarding.splash_screen.SplashScreenViewModel
import com.prayercompanion.shared.presentation.utils.StringResourceReader
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val presentationModule = module {
    singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
    singleOf(::StringResourceReader)

    // view models
    factoryOf(::SplashScreenViewModel)
    factoryOf(::SignInViewModel)
    factoryOf(::PermissionsRequestViewModel)
    factoryOf(::HomeScreenViewModel)
    factoryOf(::SettingsScreenViewModel)
    factoryOf(::QuranViewModel)
    factoryOf(::FullPrayerQuranSectionsViewModel)
}