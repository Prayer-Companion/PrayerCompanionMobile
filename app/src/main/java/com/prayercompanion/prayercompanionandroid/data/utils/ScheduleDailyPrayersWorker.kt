package com.prayercompanion.prayercompanionandroid.data.utils

import android.content.Context
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
        val focusRequest = AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS -> {} // Handle if necessary
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {} // Handle if necessary
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {} // Handle if necessary
                AudioManager.AUDIOFOCUS_GAIN -> {} // Handle if necessary
            }
        }

        audioManager.requestAudioFocus(
            focusRequest,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
        )
    }

    private fun abandonAudioFocus() {
        audioManager.abandonAudioFocus(null)
    }
}