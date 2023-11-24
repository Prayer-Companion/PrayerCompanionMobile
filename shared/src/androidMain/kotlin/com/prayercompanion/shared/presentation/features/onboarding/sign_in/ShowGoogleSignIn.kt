package com.prayercompanion.shared.presentation.features.onboarding.sign_in

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.prayercompanion.shared.R
import com.prayercompanion.shared.domain.utils.Task

@Composable
actual fun ShowGoogleSignIn(
    onSignInWithGoogleResultReceived: (Boolean, Task<String>) -> Unit
) {

    val context = LocalContext.current
    val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        GoogleSignIn.getClient(context, gso)
    }

    val signInWithGoogleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val result = it.resultCode == Activity.RESULT_OK
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            .let { task ->
                Task(
                    isSuccessful = result,
                    result = task.result.idToken,
                    exception = task.exception
                )
            }
        onSignInWithGoogleResultReceived(result, task)
    }

    LaunchedEffect(Unit) {
        signInWithGoogleLauncher.launch(
            googleSignInClient.signInIntent
        )
    }
}