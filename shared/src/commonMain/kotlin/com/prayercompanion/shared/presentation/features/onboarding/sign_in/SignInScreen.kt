package com.prayercompanion.shared.presentation.features.onboarding.sign_in

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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.presentation.theme.LocalSpacing
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.shared.presentation.utils.UiEvent
import com.prayercompanion.shared.presentation.utils.stringResource
import com.prayercompanion.shared.presentation.utils.toScreen
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SignInScreen : Screen, KoinComponent {

    private val viewModel: SignInViewModel by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        SignInScreen(
            navigate = { event ->
                navigator.replaceAll(event.route.toScreen())
            },
            launchGoogleSignInClient = {
                showGoogleSignIn { result, task ->
                    viewModel.onEvent(SignInEvents.OnSignInWithGoogleResultReceived(result, task))
                }
            },
            showToast = { message ->
//                showToast(message) todo check how to show a toast
            },
            uiEvents = viewModel.uiEventsChannel,
            onEvent = viewModel::onEvent,
            isLoadingState = viewModel.isLoading
        )
    }
}

@Composable
fun SignInScreen(
    navigate: (UiEvent.Navigate) -> Unit = {},
    launchGoogleSignInClient: () -> Unit = {},
    showToast: (String) -> Unit = {},
    uiEvents: Flow<UiEvent> = emptyFlow(),
    onEvent: (SignInEvents) -> Unit = {},
    isLoadingState: Boolean = false
) = PrayerCompanionAndroidTheme {
    val spacing = LocalSpacing.current

    LaunchedEffect(key1 = uiEvents) {
        uiEvents.collect {
            when (it) {
                is UiEvent.Navigate -> navigate(it)
                is UiEvent.ShowErrorSnackBarStr -> showToast(it.errorMessage)
                is UiEvent.LaunchSignInWithGoogle -> launchGoogleSignInClient()

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
                    painter = painterResource(Res.images.ic_app_logo),
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
                    onEvent(SignInEvents.OnSignInWithGoogleClicked)
                }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.images.ic_google),
                        contentDescription = stringResource(Res.strings.continue_with_google),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    Text(
                        text = stringResource(Res.strings.continue_with_google),
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