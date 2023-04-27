package com.prayercompanion.prayercompanionandroid

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PrayerCompanionApplication : Application() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    override fun onCreate() {
        super.onCreate()
        auth.currentUser?.getIdToken(false)
            ?.addOnSuccessListener {
                Consts.userToken = it.token
            }
    }
}