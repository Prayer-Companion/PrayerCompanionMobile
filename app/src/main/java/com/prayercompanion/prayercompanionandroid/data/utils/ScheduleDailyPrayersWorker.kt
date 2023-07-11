package com.prayercompanion.prayercompanionandroid.data.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ScheduleDailyPrayersWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val alarmScheduler: AndroidPrayersAlarmScheduler,
) : CoroutineWorker(context, workerParams) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override suspend fun doWork(): Result {
        requestAudioFocus()
        alarmScheduler.scheduleTodayPrayersNotifications()
        return Result.success()
    }

    private fun requestAudioFocus() {
        val focusRequest: AudioFocusRequest =
            AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build()
                )
                .build()

        audioManager.requestAudioFocus(focusRequest)
        audioManager.abandonAudioFocusRequest(focusRequest)
    }
}