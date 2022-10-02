package fr.fgognet.antv.view.player

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.repository.VideoDao
import fr.fgognet.antv.service.player.PlayerService


private const val TAG = "ANTV/PlayerViewModel"

data class PlayerData(
    val player: Player?,
    val url: String,
    val imageCode: String,
    val title: String,
    val description: String
)

@UnstableApi
@SuppressLint("StaticFieldLeak")
class PlayerViewModel : ViewModel(),
    DefaultLifecycleObserver {


    fun start() = apply { initialize() }

    private val _playerdata: MutableLiveData<PlayerData> =
        MutableLiveData(
            PlayerData(
                url = "",
                imageCode = "",
                title = "",
                description = "",
                player = null
            )
        )
    val playerData: LiveData<PlayerData> get() = _playerdata


    private fun initialize() {
        Log.v(TAG, "initialize")
        this._playerdata.value = PlayerData(
            url = "",
            imageCode = "",
            title = "",
            description = "",
            player = PlayerService.controller
        )
    }


    override fun onCleared() {
        Log.v(TAG, "onCleared")
        super.onCleared()
    }


    fun updateCurrentMedia(title: String) {
        Log.v(TAG, "updateCurrentMedia")
        val entity = VideoDao.get(title)
        if (entity != null) {
            Log.d(TAG, "received entity:  $entity")
            //this is not the full mediaItem here
            PlayerService.controller?.setMediaItem(
                MediaItem.Builder()
                    .setMediaId(entity.url)
                    .setMediaMetadata(
                        MediaMetadata.Builder().setTitle(entity.title)
                            .setDescription(entity.description)
                            .build()
                    )
                    .build()
            )
            PlayerService.controller?.prepare()
            this._playerdata.value = PlayerData(
                url = entity.url,
                imageCode = entity.imageCode,
                title = entity.title,
                description = entity.description,
                player = this._playerdata.value.player
            )
        }
    }


}
