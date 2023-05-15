package com.prayercompanion.prayercompanionandroid.data.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prayercompanion.prayercompanionandroid.domain.utils.PrayersAlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var prayersAlarmScheduler: PrayersAlarmScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED)
            return

        CoroutineScope(SupervisorJob()).launch {
            prayersAlarmScheduler.scheduleDailyService()
            prayersAlarmScheduler.scheduleTodayPrayersNotifications()
        }
    }
}