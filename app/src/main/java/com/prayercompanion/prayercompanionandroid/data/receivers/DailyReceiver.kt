package com.prayercompanion.prayercompanionandroid.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prayercompanion.prayercompanionandroid.data.utils.AndroidPrayersAlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DailyReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AndroidPrayersAlarmScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(SupervisorJob()).launch {
            alarmScheduler.scheduleTodayPrayersNotifications()
        }
    }

}