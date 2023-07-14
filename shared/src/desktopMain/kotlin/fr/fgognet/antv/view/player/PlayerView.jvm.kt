package fr.fgognet.antv.view.player

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.fgognet.antv.widget.KeepScreenOn
import fr.fgognet.antv.widget.MediaController
import fr.fgognet.antv.widget.getPlatformContext
import fr.fgognet.antv.widget.player
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
    KeepScreenOn(getPlatformContext())
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
            player(modifier = Modifier
                .background(color = Color.Red)
                .clickable {
                    shouldShowControls = shouldShowControls.not()
                },context = getPlatformContext(), controller = MediaController()
            )
        }
    }
    PlayerControls(
        modifier = Modifier.fillMaxSize(),
        isVisible = { shouldShowControls },
        state = state,
        onReplayClick = {  },
        onForwardClick = {  },
        onPauseToggle = {

        },
        onSeekChanged = { _: Float ->

        },
    )
}