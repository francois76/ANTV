package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import fr.fgognet.antv.view.isPlaying.IsPlayingViewModelCommon
import fr.fgognet.antv.view.player.PlayerViewModelCommon

@Composable
actual fun Player(shouldShowControls: Boolean, controller: MediaController): Boolean {
    return false
}


actual class MediaController {
    actual fun seekBack() {
    }
}

actual class MediaSessionServiceImpl actual constructor() {
    actual companion object {
        actual val isCasting: Boolean
            get() = TODO("Not yet implemented")
        actual val controller: MediaController
            get() = TODO("Not yet implemented")
    }
}

actual class IsPlayingViewModel actual constructor() :
    IsPlayingViewModelCommon() {
    override fun initialize() {
        TODO("Not yet implemented")
    }

}

actual class PlayerViewModel actual constructor() :
    PlayerViewModelCommon() {
    override fun initialize(c: MediaController?) {
        TODO("Not yet implemented")
    }

}