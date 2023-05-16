package com.prayercompanion.prayercompanionandroid.presentation.features.qibla

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prayercompanion.prayercompanionandroid.R

@SuppressLint("MissingPermission")
@Composable
@Preview
fun QiblaScreen(
    viewModel: QiblaViewModel = hiltViewModel()
) {
    DisposableEffect(key1 = viewModel) {
        viewModel.onStart()
        onDispose {
            viewModel.onDispose()
        }
    }

    viewModel.qiblaDirection?.let { qiblaDirection ->
        val rotation: Float by animateFloatAsState(qiblaDirection.toFloat())

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier
                    .size(150.dp)
                    .rotate(rotation),
                painter = painterResource(R.drawable.ic_arrow),
                contentDescription = "",
            )

            Text(
                modifier = Modifier.padding(top = 100.dp),
                text = "Degree = ${rotation}",
            )
        }
    } ?: run {
        Text(text = "Retrieving location...")
    }
}