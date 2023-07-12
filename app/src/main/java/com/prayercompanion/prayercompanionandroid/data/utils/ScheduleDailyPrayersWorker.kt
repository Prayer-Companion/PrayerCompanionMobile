package com.prayercompanion.prayercompanionandroid.data.utils

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ScheduleDailyPrayersWorker @AssistedInject constructor(
    private val alarmScheduler: AndroidPrayersAlarmScheduler,
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        alarmScheduler.scheduleTodayPrayersNotifications()
        return Result.success()
    }
}