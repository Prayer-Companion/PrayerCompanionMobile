package com.prayercompanion.prayercompanionandroid.presentation.features.sign_in

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.components.OrSeparator
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing


@Preview(locale = "ar")
@Composable
fun SignInScreen(
    googleSignInClient: GoogleSignInClient? = null,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current

    val signInWithGoogleLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val result = it.resultCode == Activity.RESULT_OK
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            viewModel.onSignInWithGoogleResultReceived(result, task)
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primaryVariant),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.onPrimary)
                .padding(spacing.spaceLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    signInWithGoogleLauncher.launch(googleSignInClient?.signInIntent)
                }) {
                Row(modifier = Modifier) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = stringResource(id = R.string.continue_with_google)
                    )
                    Text(text = stringResource(id = R.string.continue_with_google))
                }
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            OrSeparator(
                Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Text(
                modifier = Modifier.clickable {
                    viewModel.onSignInAnonymously()
                },
                text = stringResource(id = R.string.continue_as_guest),
            )
        }
    }

}