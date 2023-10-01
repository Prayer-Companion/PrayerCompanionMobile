package com.prayercompanion.prayercompanionandroid.domain.di

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.prayercompanion.prayercompanionandroid.R
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

//todo figure out how to move this to the shared directory
val androidDomainModuleX = module {
    single {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(androidContext().getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        GoogleSignIn.getClient(androidContext(), gso)
    }
}