package com.prayercompanion.prayercompanionandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.presentation.navigation.Route
import com.prayercompanion.shared.data.preferences.DataStoresRepo
import com.prayercompanion.shared.domain.models.app.AppLanguage
import com.prayercompanion.shared.domain.usecases.GetAppLanguage
import com.prayercompanion.shared.domain.usecases.SetAppLanguage
import com.prayercompanion.shared.domain.utils.tracking.Tracker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivityViewModel constructor(
    private val setAppLanguage: SetAppLanguage,
    private val getAppLanguage: GetAppLanguage,
    private val dataStoresRepo: DataStoresRepo,
    private val tracker: Tracker
) : ViewModel() {
    fun onResume() {
        viewModelScope.launch {
            val hasSetArabicLanguageFirstTime = dataStoresRepo
                .appPreferencesDataStore.data.first()
                .hasSetArabicLanguageFirstTime

            if (hasSetArabicLanguageFirstTime.not()) {
                // setting the app language shouldn't be put before onResume as the activity is not
                // ready yet to allow language changes, calling it onCreate won't work
                setAppLanguage.call(AppLanguage.AR)
                dataStoresRepo.appPreferencesDataStore.updateData {
                    it.copy(
                        hasSetArabicLanguageFirstTime = true
                    )
                }
            }

            tracker.setAppLanguage(getAppLanguage.call().code)
        }
    }

    fun onScreenChanged(route: Route) {
        val screenName = route.routeName.substringBefore("/")
        tracker.trackScreenView(screenName, this::class.simpleName.toString())
    }
}