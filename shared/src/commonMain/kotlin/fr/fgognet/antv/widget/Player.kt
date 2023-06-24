package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import fr.fgognet.antv.view.isPlaying.IsPlayingViewModelCommon
import fr.fgognet.antv.view.player.PlayerViewModelCommon


@Composable
expect fun getStateEnded(): Int

expect class MediaController {

    fun isInit(): Boolean
    fun seekBack()
    fun seekForward()
    fun pause()
    fun play()
    fun seekTo(toLong: Long)
}


expect class PlayerViewModel() : PlayerViewModelCommon {

}

expect class IsPlayingViewModel() : IsPlayingViewModelCommon {

}


