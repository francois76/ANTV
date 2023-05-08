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

    actual fun seekForward() {
    }

    actual fun pause() {
    }

    actual fun play() {
    }

    actual fun seekTo(toLong: Long) {
    }
}

actual class MediaSessionServiceImpl actual constructor() {
    actual companion object {
        actual val isCasting: Boolean
            get() = false
        actual val controller: MediaController
            get() = MediaController()
    }
}

actual class IsPlayingViewModel actual constructor() :
    IsPlayingViewModelCommon() {
    override fun initialize() {
    }

}

actual class PlayerViewModel actual constructor() :
    PlayerViewModelCommon() {
    override fun initialize(c: MediaController?) {
    }

    override fun loadMedia(title: String?) {
    }

    override fun loadPlayer(context: PlatformContext) {
    }

}

@Composable
actual fun getStateEnded(): Int {
    return 0
}