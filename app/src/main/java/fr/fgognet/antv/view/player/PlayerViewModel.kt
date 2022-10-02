package fr.fgognet.antv.view.player

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
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


    fun start(context: Context) = apply { initialize(context) }

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


    private fun initialize(context: Context) {
        Log.v(TAG, "initialize")


    }


    override fun onCleared() {
        Log.v(TAG, "onCleared")
        super.onCleared()
    }


    fun updateCurrentMedia(title: String) {
        Log.v(TAG, "updateCurrentMedia")
        val entity = VideoDao.get(title)
        if (entity != null) {
            PlayerService.controller?.setMediaItem(
                MediaItem.Builder()
                    .setUri(entity.url)
                    .setMediaMetadata(
                        MediaMetadata.Builder().setTitle(entity.title)
                            .setDescription(entity.description)
                            .build()
                    )
                    .setMimeType(MimeTypes.APPLICATION_M3U8).build()
            )
            this._playerdata.value = PlayerData(
                url = entity.url,
                imageCode = entity.imageCode,
                title = entity.title,
                description = entity.description,
                player = PlayerService.controller
            )
        }
    }


}
