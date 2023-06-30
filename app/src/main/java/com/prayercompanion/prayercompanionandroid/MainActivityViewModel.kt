package com.prayercompanion.prayercompanionandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayercompanion.prayercompanionandroid.data.preferences.DataStoresRepo
import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage
import com.prayercompanion.prayercompanionandroid.domain.usecases.SetAppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val setAppLanguage: SetAppLanguage,
    private val dataStoresRepo: DataStoresRepo
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
        }
    }
}