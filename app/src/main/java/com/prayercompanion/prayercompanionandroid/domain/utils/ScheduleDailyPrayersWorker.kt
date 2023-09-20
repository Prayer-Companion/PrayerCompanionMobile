package com.prayercompanion.prayercompanionandroid.domain.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.prayercompanion.prayercompanionandroid.data.utils.AndroidPrayersAlarmScheduler

class ScheduleDailyPrayersWorker constructor(
    private val alarmScheduler: AndroidPrayersAlarmScheduler,
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        alarmScheduler.scheduleTodayPrayersNotifications()
        return Result.success()
    }
}