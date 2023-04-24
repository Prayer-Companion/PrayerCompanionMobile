package com.prayercompanion.prayercompanionandroid.presentation.features.sign_in

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient


@Preview
@Composable
fun SignInScreen(
    googleSignInClient: GoogleSignInClient? = null,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val signInWithGoogleLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val result = it.resultCode == Activity.RESULT_OK
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            viewModel.onSignInWithGoogleResultReceived(result, task)
        }

    Button(onClick = {
        signInWithGoogleLauncher.launch(googleSignInClient?.signInIntent)
    }) {
        Text(text = "sign in")
    }

}