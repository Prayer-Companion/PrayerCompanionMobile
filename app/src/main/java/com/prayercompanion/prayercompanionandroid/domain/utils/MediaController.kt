package com.prayercompanion.prayercompanionandroid.domain.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager

class MediaController constructor(
    context: Context
) {
    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val focusRequest: AudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
        .setAudioAttributes(
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build()
        )
        .build()

    fun pauseMedia() {
        audioManager.requestAudioFocus(focusRequest)
        audioManager.abandonAudioFocusRequest(focusRequest)
    }
}