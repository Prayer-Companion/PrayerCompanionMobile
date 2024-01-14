package com.prayercompanion.shared.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun AppBackground(modifier: Modifier = Modifier) {
    PrayerCompanionAndroidTheme {
        Box(
            modifier = modifier
                .background(
                    color = MaterialTheme.colors.background
                ),
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.images.background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}