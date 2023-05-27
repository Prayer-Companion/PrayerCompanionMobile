package com.prayercompanion.prayercompanionandroid.domain.usecases

import com.google.firebase.auth.FirebaseAuth
import com.prayercompanion.prayercompanionandroid.data.utils.Consts
import com.prayercompanion.prayercompanionandroid.presentation.utils.AuthenticationHelper
import javax.inject.Inject

class UpdateAuthToken @Inject constructor(private val authenticationHelper: AuthenticationHelper) {

    private val auth = FirebaseAuth.getInstance()

    fun call(
        forceRefresh: Boolean,
        onSuccess: () -> Unit = {},
        onFailure: (exception: Exception) -> Unit = {},
        onUserNotSignedIn: () -> Unit = {}
    ) {
        val user = auth.currentUser ?: kotlin.run {
            onUserNotSignedIn()
            return
        }

        user.getIdToken(forceRefresh)
            .addOnSuccessListener {
                Consts.userToken = it.token
                onSuccess()
            }.addOnFailureListener { exception ->
                authenticationHelper.signOut()
                onFailure(exception)
            }
    }
}