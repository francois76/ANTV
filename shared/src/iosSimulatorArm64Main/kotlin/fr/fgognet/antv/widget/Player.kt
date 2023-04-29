package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import fr.fgognet.antv.view.isPlaying.IsPlayingViewModelCommon

@Composable
actual fun Player(shouldShowControls: Boolean, controller: MediaController): Boolean {
    return false
}


actual class MediaController
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