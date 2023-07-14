package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import fr.fgognet.antv.view.isPlaying.IsPlayingViewModelCommon
import fr.fgognet.antv.view.player.PlayerViewModelCommon


@Composable
expect fun getStateEnded(): Int




expect class PlayerViewModel() : PlayerViewModelCommon {

}

expect class IsPlayingViewModel() : IsPlayingViewModelCommon {

}


