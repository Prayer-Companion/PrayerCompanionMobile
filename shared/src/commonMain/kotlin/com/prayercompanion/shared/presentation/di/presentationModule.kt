package com.prayercompanion.shared.presentation.di

import com.prayercompanion.shared.data.repositories.AuthenticationRepositoryImpl
import com.prayercompanion.shared.domain.repositories.AuthenticationRepository
import com.prayercompanion.shared.presentation.features.home_screen.HomeScreenViewModel
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
    factoryOf(::SplashScreenViewModel)
    singleOf(::SignInViewModel)
    singleOf(::PermissionsRequestViewModel)
    singleOf(::HomeScreenViewModel)
    singleOf(::StringResourceReader)
}