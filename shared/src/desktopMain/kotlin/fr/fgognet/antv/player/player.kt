package fr.fgognet.antv.player

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import fr.fgognet.antv.view.player.PlayerControls
import fr.fgognet.antv.view.player.PlayerData

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