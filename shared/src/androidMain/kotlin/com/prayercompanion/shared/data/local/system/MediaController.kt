package com.prayercompanion.shared.data.local.system

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager

actual class MediaController constructor(
    context: Context
) {
    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val focusRequest: AudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
        .setAudioAttributes(
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build()
        )
        .build()

    actual fun pauseMedia() {
        audioManager.requestAudioFocus(focusRequest)
        audioManager.abandonAudioFocusRequest(focusRequest)
    }
}