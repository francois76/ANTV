package fr.fgognet.antv.view.isPlaying

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import io.github.aakira.napier.Napier

private const val TAG = "ANTV/IsPlayingViewModel"

data class IsPlayingData(
    val hasPlayingData: Boolean,
    val imageCode: String,
    val title: String,
    val description: String,
)


class IsPlayingViewModel : ViewModel(), Player.Listener {

    fun start() = apply { initialize() }

    private val _isPlayingData: MutableLiveData<IsPlayingData> =
        MutableLiveData(
            IsPlayingData(
                hasPlayingData = false,
                imageCode = "",
                title = "",
                description = ""
            )
        )
    val isPlayingData: LiveData<IsPlayingData> get() = _isPlayingData


    private fun initialize() {
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