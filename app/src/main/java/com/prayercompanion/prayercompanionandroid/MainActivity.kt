package com.prayercompanion.prayercompanionandroid

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.prayercompanion.shared.domain.utils.Task
import com.prayercompanion.shared.presentation.App
import com.prayercompanion.shared.presentation.features.onboarding.sign_in.GoogleSignInSetup
import com.prayercompanion.shared.presentation.navigation.Route
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        GoogleSignIn.getClient(this, gso)
    }

    private val signInWithGoogleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val result = it.resultCode == Activity.RESULT_OK
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            .let { task ->
                Task<Pair<String?,String?>>(
                    isSuccessful = result,
                    result = task.result.idToken to null,
                    exception = task.exception
                )
            }
        GoogleSignInSetup.onResult(
            result,
            task
        )
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        GoogleSignInSetup.setup(::signInWithGoogle)

        setContent {
            App()
            return@setContent
            PrayerCompanionAndroidTheme {
                val navController = rememberNavController()
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    val route = Route.fromStringRoute(destination.route)
                    viewModel.onScreenChanged(route)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun signInWithGoogle() {
        signInWithGoogleLauncher.launch(
            googleSignInClient.signInIntent
        )
    }
}