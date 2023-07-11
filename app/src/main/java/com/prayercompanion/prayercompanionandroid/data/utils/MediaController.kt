package com.prayercompanion.prayercompanionandroid.data.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MediaController @Inject constructor(
    @ApplicationContext
    private val context: Context
){
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun pauseMedia() {
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