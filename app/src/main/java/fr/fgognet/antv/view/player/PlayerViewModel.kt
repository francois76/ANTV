package fr.fgognet.antv.view.player

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
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

    private lateinit var _context: Context
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

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null


    private fun initialize(context: Context) {
        Log.v(TAG, "initialize")
        this._context = context

        controllerFuture =
            MediaController.Builder(
                this._context,
                SessionToken(
                    this._context,
                    ComponentName(this._context, PlayerService::class.java)
                )
            )
                .buildAsync()
        this._playerdata.value = PlayerData(
            url = "",
            imageCode = "",
            title = "",
            description = "",
            player = controller
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
            controller?.setMediaItem(
                MediaItem.Builder()
                    .setUri(entity.url)
                    .setMediaMetadata(
                        MediaMetadata.Builder().setTitle(entity.title)
                            .setDescription(entity.description)
                            .build()
                    )
                    .setMimeType(MimeTypes.APPLICATION_M3U8).build()
            )
            controller?.prepare()
            controller?.play()
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
