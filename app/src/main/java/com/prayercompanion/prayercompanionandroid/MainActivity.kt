package com.prayercompanion.prayercompanionandroid

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.prayercompanion.prayercompanionandroid.domain.models.quran.PrayerQuranReadingSections
import com.prayercompanion.prayercompanionandroid.domain.utils.tracking.Tracker
import com.prayercompanion.prayercompanionandroid.presentation.features.home_screen.HomeScreen
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.permissions.PermissionsRequestScreen
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.permissions.PermissionsRequestViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.sign_in.SignInScreen
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.sign_in.SignInViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.splash_screen.SplashScreen
import com.prayercompanion.prayercompanionandroid.presentation.features.onboarding.splash_screen.SplashScreenViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.qibla.QiblaScreen
import com.prayercompanion.prayercompanionandroid.presentation.features.qibla.QiblaViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.full_sections.FullPrayerQuranSections
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.quran.QuranScreen
import com.prayercompanion.prayercompanionandroid.presentation.features.quran.quran.QuranViewModel
import com.prayercompanion.prayercompanionandroid.presentation.features.settings.SettingsScreen
import com.prayercompanion.prayercompanionandroid.presentation.features.settings.SettingsScreenViewModel
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.prayercompanionandroid.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.prayercompanionandroid.presentation.utils.FeedbackUtils
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModel()
    private val googleSignInClient: GoogleSignInClient by inject()
    private val feedbackUtils: FeedbackUtils by inject()
    private val tracker: Tracker by inject()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        setContent {
            PrayerCompanionAndroidTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                var shouldShowBottomNavigationBar by rememberSaveable {
                    mutableStateOf(false)
                }
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    val route = Route.fromStringRoute(destination.route)
                    shouldShowBottomNavigationBar = route.bottomNavBar
                    tracker.trackScreenView(route, this::class.simpleName.toString())
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    snackbarHost = {
                        SnackbarHost(it) { data ->
                            Snackbar(
                                snackbarData = data,
                                backgroundColor = Color.LightGray,
                            )
                        }
                    },
                    bottomBar = {
                        if (shouldShowBottomNavigationBar) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { scaffold ->
                    NavHost(
                        modifier = Modifier.padding(scaffold),
                        navController = navController,
                        startDestination = Route.SplashScreen.routeName,
                    ) {
                        composable(Route.SplashScreen.routeName) {
                            val viewModel: SplashScreenViewModel = getViewModel()
                            SplashScreen(
                                uiEvents = viewModel.uiEvents,
                                navigate = navController::navigate
                            )
                        }
                        composable(Route.SignIn.routeName) {
                            val viewModel: SignInViewModel = getViewModel()
                            SignInScreen(
                                navigate = navController::navigate,
                                googleSignInClient = googleSignInClient,
                                uiEvents = viewModel.uiEventsChannel,
                                onEvent = viewModel::onEvent,
                                isLoadingState = viewModel.isLoading
                            )
                        }
                        composable(Route.PermissionsRequests.routeName) {
                            val viewModel: PermissionsRequestViewModel = getViewModel()
                            PermissionsRequestScreen(
                                navigate = navController::navigate,
                                uiState = viewModel.uiState,
                                uiEvents = viewModel.uiEvents,
                                onEvent = viewModel::onEvent
                            )
                        }
                        composable(Route.Home.routeName) {
                            HomeScreen(
                                viewModel = getViewModel(),
                                scaffoldState = scaffoldState,
                                activity = this@MainActivity
                            )
                        }
                        composable(Route.Qibla.routeName) {
                            val viewModel: QiblaViewModel = getViewModel()
                            QiblaScreen(
                                onEvent = viewModel::onEvent,
                                sensorAccuracy = viewModel.sensorAccuracy,
                                qiblaDirection = viewModel.qiblaDirection
                            )
                        }
                        composable(Route.Quran.routeName) {
                            val viewModel: QuranViewModel = getViewModel()
                            QuranScreen(
                                navigate = navController::navigate,
                                state = viewModel.state,
                                onEvent = viewModel::onEvent,
                                uiEventsChannel = viewModel.uiEventsChannel,
                                showSnackBar = {
                                    showSnackBar(scaffoldState, it)
                                }
                            )
                        }
                        composable(
                            route = Route.FullQuranSections.routeName,
                            arguments = listOf(
                                navArgument("sections") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val sectionsJson = it.arguments?.getString("sections") ?: "{}"
                            val sections = fromJson(sectionsJson)
                                ?: PrayerQuranReadingSections.EMPTY

                            FullPrayerQuranSections(sections) {
                                navController.popBackStack()
                            }
                        }
                        composable(Route.Settings.routeName) {
                            val viewModel: SettingsScreenViewModel = getViewModel()
                            SettingsScreen(
                                state = viewModel.state,
                                onEvent = viewModel::onEvent,
                                uiEvents = viewModel.uiEvents
                            ) {
                                feedbackUtils.showFeedbackDialog()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    @Composable
    private fun BottomNavigationBar(navController: NavController) {
        BottomNavigation(
            modifier = Modifier.height(62.dp),
            backgroundColor = MaterialTheme.colors.primary,

            ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            BottomNavItem.getOrdered().forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = screen.icon),
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(screen.nameId)) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(Route.Home.routeName) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    },
                    selectedContentColor = MaterialTheme.colors.onPrimary,
                    unselectedContentColor = MaterialTheme.colors.secondary
                )
            }
        }
    }

    private suspend fun showSnackBar(scaffoldState: ScaffoldState, message: String) {
        scaffoldState
            .snackbarHostState
            .showSnackbar(message)
    }
}