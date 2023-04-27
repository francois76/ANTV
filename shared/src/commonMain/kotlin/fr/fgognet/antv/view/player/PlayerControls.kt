package fr.fgognet.antv.view.player

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player.STATE_ENDED
import fr.fgognet.antv.R
import fr.fgognet.antv.widget.painterResource
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter

data class PlayerData(
    val title: String,
    val description: String,
    val isPlaying: Boolean,
    val duration: Long,
    val currentPosition: Long,
    val isCasting: Boolean,
    val bufferedPercentage: Int,
    val playbackState: Int,
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    state: PlayerData,
    onReplayClick: () -> Unit,
    onForwardClick: () -> Unit,
    onPauseToggle: () -> Unit,
    onSeekChanged: (timeMs: Float) -> Unit
) {

    val visible = remember(isVisible()) { isVisible() }

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.background(Color.Black.copy(alpha = 0.6f))) {
            TopControl(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(),
                state = state,
            )

            CenterControls(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.6f),
                state = state,
                onReplayClick = onReplayClick,
                onForwardClick = onForwardClick,
                onPauseToggle = onPauseToggle,
            )

            BottomControls(
                modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .animateEnterExit(
                        enter =
                        slideInVertically(
                            initialOffsetY = { fullHeight: Int ->
                                fullHeight
                            }
                        ),
                        exit =
                        slideOutVertically(
                            targetOffsetY = { fullHeight: Int ->
                                fullHeight
                            }
                        )
                    ),
                state = state,
                onSeekChanged = onSeekChanged
            )
        }
    }
}

@Composable
private fun TopControl(modifier: Modifier = Modifier, state: PlayerData) {

    Text(
        modifier = modifier.padding(16.dp),
        text = state.title,
        style = MaterialTheme.typography.titleMedium,
        color = Color.White
    )
}

@Composable
private fun CenterControls(
    modifier: Modifier = Modifier,
    state: PlayerData,
    onReplayClick: () -> Unit,
    onPauseToggle: () -> Unit,
    onForwardClick: () -> Unit
) {

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        IconButton(modifier = Modifier.size(80.dp), onClick = onReplayClick) {
            Image(
                modifier = Modifier.fillMaxSize(0.5f),
                contentScale = ContentScale.Crop,
                painter = painterResource(res = R.drawable.ic_baseline_replay_5_24),
                contentDescription = "Replay 5 seconds"
            )
        }

        IconButton(modifier = Modifier.size(80.dp), onClick = onPauseToggle) {
            Image(
                modifier = Modifier.fillMaxSize(0.5f),
                contentScale = ContentScale.Crop,
                painter =
                when {
                    state.isPlaying -> {
                        painterResource(res = R.drawable.ic_baseline_pause_24)
                    }

                    state.playbackState == STATE_ENDED -> {
                        painterResource(res = R.drawable.ic_baseline_replay_24)
                    }

                    else -> {
                        painterResource(res = R.drawable.ic_baseline_play_arrow_24)
                    }
                },
                contentDescription = "Play/Pause"
            )
        }

        IconButton(modifier = Modifier.size(80.dp), onClick = onForwardClick) {
            Image(
                modifier = Modifier.fillMaxSize(0.5f),
                contentScale = ContentScale.Crop,
                painter = painterResource(res = R.drawable.ic_baseline_forward_5_24),
                contentDescription = "Forward 5 seconds"
            )
        }
    }
}

@Composable
private fun BottomControls(
    modifier: Modifier = Modifier,
    state: PlayerData,
    onSeekChanged: (timeMs: Float) -> Unit
) {

    Column(modifier = modifier.padding(bottom = 32.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = state.bufferedPercentage.toFloat(),
                enabled = false,
                onValueChange = { /*do nothing*/ },
                valueRange = 0f..100f,
                colors =
                SliderDefaults.colors(
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = Color.Gray
                )
            )
            if (state.duration > 0) {
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.currentPosition.toFloat(),
                    onValueChange = onSeekChanged,
                    valueRange = 0f..state.duration.toFloat(),
                    colors =
                    SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTickColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "${state.currentPosition.toHour()}/${state.duration.toHour()}",
                color = Color.White,
                style = MaterialTheme.typography.titleSmall,
            )

        }
    }
}

fun Long.toHour(): String {
    return if (this <= 0L) {
        "..."
    } else {
        val current = LocalTime.fromMillisecondOfDay(this.toInt()).toJavaLocalTime()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        current.format(formatter)
    }
}

