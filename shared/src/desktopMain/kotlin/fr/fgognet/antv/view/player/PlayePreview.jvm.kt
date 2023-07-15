package fr.fgognet.antv.view.player

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.fgognet.antv.widget.*
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Preview
@Composable
fun PlayerViewPreview(
) {
    val state = PlayerData(
        title = "toto",
        description = "titi",
        isPlaying = true,
        duration = 42,
        currentPosition = 24,
        isCasting = false,
        bufferedPercentage = 2,
        playbackState = 1,
    )
    KeepScreenOn(getPlatformContext(), true)
    var shouldShowControls by remember { mutableStateOf(false) }
    Text(text = "coucou $shouldShowControls")
    if (state.isCasting) {
        shouldShowControls = true
    } else {
        if (shouldShowControls) {
            LaunchedEffect(Unit) {
                val duration = 5
                delay(duration.seconds)
                shouldShowControls = shouldShowControls.not()
            }
        }
        if (state.duration > 0) {
            player(
                modifier = Modifier
                    .background(color = Color.Red)
                    .clickable {
                        shouldShowControls = shouldShowControls.not()
                    }, context = getPlatformContext(), controller = MediaController()
            )
        }
    }
    PlayerControls(
        modifier = Modifier.fillMaxSize(),
        isVisible = { shouldShowControls },
        state = state,
        onReplayClick = { },
        onForwardClick = { },
        onPauseToggle = {

        },
        onSeekChanged = { _: Float ->

        },
    )
}

@Composable
@Preview
fun PlayerControl() {
    PlayerControls(
        isVisible = { true },
        state = PlayerData(
            title = "lorem ipsum",
            description = "dolor est",
            isPlaying = false,
            isCasting = false,
            playbackState = 0,
            bufferedPercentage = 100,
            currentPosition = 200000,
            duration = 2000000,
        ),
        onReplayClick = { },
        onForwardClick = { },
        onPauseToggle = { /*TODO*/ },
        onSeekChanged = {},
    )
}