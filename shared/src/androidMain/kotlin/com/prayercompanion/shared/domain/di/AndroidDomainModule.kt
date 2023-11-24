package com.prayercompanion.shared.domain.di

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.prayercompanion.shared.R
import com.prayercompanion.shared.domain.usecases.GetAppLanguage
import com.prayercompanion.shared.domain.usecases.SetAppLanguage
import com.prayercompanion.shared.domain.utils.AppLocationManager
import com.prayercompanion.shared.domain.utils.AppLocationManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidDomainModule = module {
    single {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(androidContext().getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        GoogleSignIn.getClient(androidContext(), gso)
    }
    singleOf(::AppLocationManagerImpl) { bind<AppLocationManager>() }
    singleOf(::GetAppLanguage)
    singleOf(::SetAppLanguage)
}