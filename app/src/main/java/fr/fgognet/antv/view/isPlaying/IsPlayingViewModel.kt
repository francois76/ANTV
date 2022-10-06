package fr.fgognet.antv.view.isPlaying

import android.annotation.SuppressLint
import android.util.Log
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.service.player.PlayerService


private const val TAG = "ANTV/IsPlayingViewModel"

data class IsPlayingData(
    val imageCode: String,
    val title: String,
    val description: String,
)

@UnstableApi
@SuppressLint("StaticFieldLeak")
class IsPlayingViewModel : ViewModel(), Player.Listener {


    fun start() = apply { initialize() }

    private val _isPlayingData: MutableLiveData<IsPlayingData> =
        MutableLiveData(
            IsPlayingData(
                imageCode = "",
                title = "",
                description = "",
            )
        )
    val isPlayingdata: LiveData<IsPlayingData> get() = _isPlayingData


    private fun initialize() {
        Log.v(TAG, "initialize")
        PlayerService.controller?.addListener(this)
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        this._isPlayingData.value = IsPlayingData(
            imageCode = mediaMetadata.artworkUri.toString(),
            title = mediaMetadata.title.toString(),
            description = mediaMetadata.description.toString(),
        )
    }


    override fun onCleared() {
        Log.v(TAG, "onCleared")
        super.onCleared()
    }


}