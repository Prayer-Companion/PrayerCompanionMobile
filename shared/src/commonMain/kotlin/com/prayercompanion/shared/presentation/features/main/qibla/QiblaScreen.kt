package com.prayercompanion.shared.presentation.features.main.qibla

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.prayercompanion.prayercompanionandroid.moko_resources.Res
import com.prayercompanion.shared.BottomNavItem
import com.prayercompanion.shared.domain.models.SensorAccuracy
import com.prayercompanion.shared.presentation.features.main.qibla.components.QiblaSensorAccuracyDialog
import com.prayercompanion.shared.presentation.features.main.qibla.components.QiblaSensorAccuracyIndicator
import com.prayercompanion.shared.presentation.theme.LocalSpacing
import com.prayercompanion.shared.presentation.theme.PrayerCompanionAndroidTheme
import com.prayercompanion.shared.presentation.utils.createTabOptions
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent

object QiblaScreen : Tab, KoinComponent {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<QiblaViewModel>()

        QiblaScreen(
            onEvent = viewModel::onEvent,
            sensorAccuracy = viewModel.sensorAccuracy,
            qiblaDirection = viewModel.qiblaDirection
        )
    }

    override val options: TabOptions
        @Composable
        get() = createTabOptions(BottomNavItem.Qibla)
}

@Composable
fun QiblaScreen(
    onEvent: (QiblaUiEvent) -> Unit = { },
    sensorAccuracy: SensorAccuracy = SensorAccuracy.NO_CONTACT,
    qiblaDirection: Double? = 0.0
) = PrayerCompanionAndroidTheme {
    val spacing = LocalSpacing.current
    val isDialogOpen = remember { mutableStateOf(false) }

    DisposableEffect(key1 = true) {
        onEvent(QiblaUiEvent.OnStart)
        onDispose {
            onEvent(QiblaUiEvent.OnDispose)
        }
    }

    if (isDialogOpen.value) {
        QiblaSensorAccuracyDialog {
            isDialogOpen.value = false
        }
    }


    if (qiblaDirection == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Retrieving location..."
            )
        }
    } else {
        val rotation: Float by animateFloatAsState(qiblaDirection.toFloat())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.7f))
            Image(
                modifier = Modifier
                    .size(280.dp)
                    .rotate(rotation),
                painter = painterResource(Res.images.img_qibla_compass),
                contentDescription = "Qibla compass",
            )

            Spacer(modifier = Modifier.weight(0.4f))

            Text(
                modifier = Modifier.weight(1f),
                text = "${rotation.toInt()}°",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onPrimary,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = spacing.spaceLarge),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                QiblaSensorAccuracyIndicator(color = sensorAccuracy.color)

                Text(
                    modifier = Modifier.padding(start = spacing.spaceSmall),
                    text = stringResource(sensorAccuracy.nameId),
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.secondary
                )

                if (sensorAccuracy != SensorAccuracy.HIGH) {
                    IconButton(
                        onClick = {
                            isDialogOpen.value = true
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
            Spacer(modifier = Modifier.weight(0.35f))
        }
    }
}