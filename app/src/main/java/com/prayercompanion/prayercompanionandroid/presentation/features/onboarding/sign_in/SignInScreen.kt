package com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.sign_in

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptionsBuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.theme.LocalSpacing
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.utils.UiEvent
import com.prayercompanion.prayercompanionandroid.showToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Preview(locale = "ar")
@Composable
fun SignInScreen(
    navigate: (UiEvent.Navigate, NavOptionsBuilder.() -> Unit) -> Unit = { _, _ -> },
    googleSignInClient: GoogleSignInClient? = null,
    uiEvents: Flow<UiEvent> = emptyFlow(),
    onEvent: (SignInEvents) -> Unit = {},
    isLoadingState: Boolean = false
) = PrayerCompanionAndroidTheme {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val signInWithGoogleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val result = it.resultCode == Activity.RESULT_OK
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        onEvent(SignInEvents.OnSignInWithGoogleResultReceived(result, task))
    }

    LaunchedEffect(key1 = uiEvents) {
        uiEvents.collect {
            when (it) {
                is UiEvent.Navigate -> navigate(it) {
                    popUpTo(Route.SignIn.name) {
                        inclusive = true
                    }
                }

                is UiEvent.ShowErrorSnackBar -> context.showToast(it.errorMessage.asString(context))
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .aspectRatio(1f)
                        .align(Alignment.Center)
                        .background(MaterialTheme.colors.primary, RoundedCornerShape(100))
                        .padding(20.dp),
                    painter = painterResource(id = R.drawable.ic_app_logo),
                    contentDescription = "Location Icon"
                )
                if (isLoadingState) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        color = Color.White
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(topEnd = 50.dp, topStart = 50.dp)
                )
                .padding(
                    start = spacing.spaceLarge,
                    top = spacing.spaceLarge,
                    bottom = spacing.spaceExtraLarge,
                    end = spacing.spaceLarge
                )
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .defaultMinSize(minHeight = 45.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.onPrimary),
                shape = RoundedCornerShape(50.dp),
                onClick = {
                    signInWithGoogleLauncher.launch(googleSignInClient?.signInIntent)
                }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = stringResource(id = R.string.continue_with_google),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    Text(
                        text = stringResource(id = R.string.continue_with_google),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h3
                    )
                }
            }
//Commenting out anonymous sign in for now as it might add extra complexity and unnecessary edge cases
//            Spacer(modifier = Modifier.height(spacing.spaceMedium))
//            OrSeparator(
//                Modifier.fillMaxWidth()
//            )
//            Spacer(modifier = Modifier.height(spacing.spaceMedium))
//            Text(
//                modifier = Modifier.clickable {
//                    onEvent(SignInEvents.OnSignInAnonymously)
//                },
//                text = stringResource(id = R.string.continue_as_guest),
//            )
        }
    }

}