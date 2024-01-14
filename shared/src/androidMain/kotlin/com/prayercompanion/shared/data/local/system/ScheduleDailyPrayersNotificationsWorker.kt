package com.prayercompanion.shared.data.local.system

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.prayercompanion.shared.data.local.system.notification.AndroidPrayersAlarmScheduler

class ScheduleDailyPrayersNotificationsWorker constructor(
    private val alarmScheduler: AndroidPrayersAlarmScheduler,
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        alarmScheduler.scheduleTodayPrayersNotifications()
        return Result.success()
    }
}