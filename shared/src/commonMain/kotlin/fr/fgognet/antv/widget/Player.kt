package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import fr.fgognet.antv.view.isPlaying.IsPlayingViewModelCommon
import fr.fgognet.antv.view.player.PlayerViewModelCommon


@Composable
expect fun Player(shouldShowControls: Boolean, controller: MediaController): Boolean

expect class MediaController

expect class MediaSessionServiceImpl() {
    companion object {
        val isCasting: Boolean
        val controller: MediaController
    }
}

expect class PlayerViewModel() : PlayerViewModelCommon {

}

expect class IsPlayingViewModel() : IsPlayingViewModelCommon {

}


