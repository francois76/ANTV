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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.util.UnstableApi
import fr.fgognet.antv.R
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    state: PlayerData,
    title: () -> String,
    onReplayClick: () -> Unit,
    onForwardClick: () -> Unit,
    onPauseToggle: () -> Unit,
    bufferedPercentage: () -> Int,
    playbackState: () -> Int,
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
                title = title
            )

            CenterControls(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                state = state,
                onReplayClick = onReplayClick,
                onForwardClick = onForwardClick,
                onPauseToggle = onPauseToggle,
                playbackState = playbackState
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
                bufferedPercentage = bufferedPercentage,
                onSeekChanged = onSeekChanged
            )
        }
    }
}

@Composable
private fun TopControl(modifier: Modifier = Modifier, title: () -> String) {
    val videoTitle = remember(title()) { title() }

    Text(
        modifier = modifier.padding(16.dp),
        text = videoTitle,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun CenterControls(
    modifier: Modifier = Modifier,
    state: PlayerData,
    playbackState: () -> Int,
    onReplayClick: () -> Unit,
    onPauseToggle: () -> Unit,
    onForwardClick: () -> Unit
) {

    val playerState = remember(playbackState()) { playbackState() }

    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        IconButton(modifier = Modifier.size(40.dp), onClick = onReplayClick) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_baseline_replay_5_24),
                contentDescription = "Replay 5 seconds"
            )
        }

        IconButton(modifier = Modifier.size(40.dp), onClick = onPauseToggle) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter =
                when {
                    state.isPlaying -> {
                        painterResource(id = R.drawable.ic_baseline_pause_24)
                    }
                    playerState == STATE_ENDED -> {
                        painterResource(id = R.drawable.ic_baseline_replay_24)
                    }
                    else -> {
                        painterResource(id = R.drawable.ic_baseline_play_arrow_24)
                    }
                },
                contentDescription = "Play/Pause"
            )
        }

        IconButton(modifier = Modifier.size(40.dp), onClick = onForwardClick) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.ic_baseline_forward_5_24),
                contentDescription = "Forward 5 seconds"
            )
        }
    }
}

@Composable
private fun BottomControls(
    modifier: Modifier = Modifier,
    state: PlayerData,
    bufferedPercentage: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit
) {


    val buffer = remember(bufferedPercentage()) { bufferedPercentage() }

    Column(modifier = modifier.padding(bottom = 32.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = buffer.toFloat(),
                enabled = false,
                onValueChange = { /*do nothing*/ },
                valueRange = 0f..100f,
                colors =
                SliderDefaults.colors(
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = Color.Gray
                )
            )

            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = state.currentTime.toFloat(),
                onValueChange = onSeekChanged,
                valueRange = 0f..state.totalDuration.toFloat(),
                colors =
                SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTickColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = state.totalDuration.formatMinSec(),
                color = MaterialTheme.colorScheme.primary
            )

            IconButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = {}
            ) {
                Image(
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = androidx.media3.ui.R.drawable.exo_ic_fullscreen_enter),
                    contentDescription = "Enter/Exit fullscreen"
                )
            }
        }
    }
}

fun Long.formatMinSec(): String {
    return if (this == 0L) {
        "..."
    } else {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(this)
                    )
        )
    }
}

private const val PLAYER_SEEK_BACK_INCREMENT = 5 * 1000L // 5 seconds
private const val PLAYER_SEEK_FORWARD_INCREMENT = 10 * 1000L // 10 seconds

@UnstableApi
@Composable
@Preview(widthDp = 941, heightDp = 423, device = Devices.AUTOMOTIVE_1024p)
fun PlayerControl() {
    PlayerControls(
        isVisible = { true },
        state = PlayerData(
            player = null,
            url = "",
            imageCode = "",
            title = "lorem ipsum",
            description = "dolor est",
            isPlaying = false,
            isCast = false,
            playbackState = 0,
            bufferedPercentage = 100,
            currentTime = 20,
            totalDuration = 200,
        ),
        title = { "lorem ipsum" },
        onReplayClick = { },
        onForwardClick = { },
        onPauseToggle = { /*TODO*/ },
        bufferedPercentage = { 0 },
        playbackState = { 0 },
        onSeekChanged = {},
    )
}