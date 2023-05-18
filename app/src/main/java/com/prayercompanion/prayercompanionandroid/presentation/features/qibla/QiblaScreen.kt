package com.prayercompanion.prayercompanionandroid.presentation.features.qibla

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prayercompanion.prayercompanionandroid.R
import com.prayercompanion.prayercompanionandroid.domain.utils.SensorAccuracy

@SuppressLint("MissingPermission")
@Composable
fun QiblaScreen(
    viewModel: QiblaViewModel = hiltViewModel()
) {
    val openDialog = remember { mutableStateOf(false) }

    DisposableEffect(key1 = viewModel) {
        viewModel.onStart()
        onDispose {
            viewModel.onDispose()
        }
    }

    if (openDialog.value) {
        QiblaSensorAccuracyDialog {
            openDialog.value = false
        }
    }


    viewModel.qiblaDirection?.let { qiblaDirection ->
        val rotation: Float by animateFloatAsState(qiblaDirection.toFloat())

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.weight(0.5f))

            Image(
                modifier = Modifier
                    .weight(1f)
                    .rotate(rotation),
                painter = painterResource(R.drawable.qibla),
                contentDescription = "",
            )

            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                modifier = Modifier.weight(1f),
                text = "Degree = ${rotation}",
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                QiblaSensorAccuracyIndicator(color = viewModel.sensorAccuracy.color)

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = stringResource(id = viewModel.sensorAccuracy.nameId),
                    color = MaterialTheme.colors.secondary,
                )

//                if (viewModel.sensorAccuracy != SensorAccuracy.HIGH)
                if (true) {
                    IconButton(
                        onClick = {
                            openDialog.value = true
                        },
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            tint = MaterialTheme.colors.secondary,
                            contentDescription = "Info",
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.2f))
        }
    } ?: run {
        Text(text = "Retrieving location...")
    }
}