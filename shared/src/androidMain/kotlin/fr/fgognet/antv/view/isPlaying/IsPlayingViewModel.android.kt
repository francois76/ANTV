package fr.fgognet.antv.view.isPlaying

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import io.github.aakira.napier.Napier

private const val TAG = "ANTV/IsPlayingViewModel"

actual class IsPlayingViewModel : IsPlayingViewModelCommon(), Player.Listener {


    override fun initialize() {
        Napier.v(tag = TAG, message = "initialize")
        MediaSessionServiceImpl.addListener(this)
        if (MediaSessionServiceImpl.controller != null && MediaSessionServiceImpl.currentMediaItem != null) {
            this._isPlayingData.value = IsPlayingData(
                hasPlayingData = true,
                imageCode = MediaSessionServiceImpl.currentMediaItem?.mediaMetadata?.artworkUri.toString(),
                title = MediaSessionServiceImpl.currentMediaItem?.mediaMetadata?.title.toString(),
                description = MediaSessionServiceImpl.currentMediaItem?.mediaMetadata?.description.toString(),
            )
        }

    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        Napier.v(tag = TAG, message = "onMediaItemTransition")
        if (mediaItem != null) {
            this._isPlayingData.value = IsPlayingData(
                hasPlayingData = true,
                imageCode = mediaItem.mediaMetadata.artworkUri.toString(),
                title = mediaItem.mediaMetadata.title.toString(),
                description = mediaItem.mediaMetadata.description.toString(),
            )
        } else {
            this._isPlayingData.value = IsPlayingData(
                hasPlayingData = false,
                imageCode = "",
                title = "",
                description = ""
            )
        }
    }


}