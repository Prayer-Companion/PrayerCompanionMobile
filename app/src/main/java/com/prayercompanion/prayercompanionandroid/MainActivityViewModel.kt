package com.prayercompanion.prayercompanionandroid

import androidx.lifecycle.ViewModel
import com.prayercompanion.prayercompanionandroid.domain.models.AppLanguage
import com.prayercompanion.prayercompanionandroid.domain.usecases.GetAppLanguage
import com.prayercompanion.prayercompanionandroid.domain.usecases.SetAppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getAppLanguage: GetAppLanguage,
    private val setAppLanguage: SetAppLanguage
) : ViewModel() {
    fun onResume() {
        if (getAppLanguage.call() == null) {
            //setting the app language shouldn't be put before onResume as the activity is not
            // ready yet to allow language changes, calling it onCreate won't work
            setAppLanguage.call(AppLanguage.AR)
        }
    }
}