package com.prayercompanion.prayercompanionandroid.presentation.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

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