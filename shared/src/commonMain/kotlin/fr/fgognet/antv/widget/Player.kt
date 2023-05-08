package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import fr.fgognet.antv.view.isPlaying.IsPlayingViewModelCommon
import fr.fgognet.antv.view.player.PlayerViewModelCommon


@Composable
expect fun player(shouldShowControls: Boolean, controller: MediaController): Boolean

@Composable
expect fun getStateEnded(): Int

expect class MediaController {
    fun seekBack()
    fun seekForward()
    fun pause()
    fun play()
    fun seekTo(toLong: Long)
}

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


