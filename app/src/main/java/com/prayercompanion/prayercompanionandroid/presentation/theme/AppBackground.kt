package com.prayercompanion.prayercompanionandroid.presentation.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.prayercompanion.prayercompanionandroid.R

@Preview(showSystemUi = true)
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
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}