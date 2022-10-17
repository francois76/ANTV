package fr.fgognet.antv.view.player

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastState
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
    val description: String,
    val isPlaying: Boolean,
    val totalDuration: Long,
    val currentTime: Long,
    val isCast: Boolean,
    val bufferedPercentage: Int,
    val playbackState: Int,
)


@UnstableApi
@SuppressLint("StaticFieldLeak")
class PlayerViewModel : ViewModel() {


    fun start() = apply { initialize() }

    private val _playerdata: MutableLiveData<PlayerData> =
        MutableLiveData(
            PlayerData(
                url = "",
                imageCode = "",
                title = "",
                description = "",
                player = null,
                isCast = false,
                isPlaying = false,
                totalDuration = 0,
                currentTime = 0,
                bufferedPercentage = 0,
                playbackState = 0
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
            player = PlayerService.controller,
            totalDuration = PlayerService.controller?.duration?.coerceAtLeast(0L) ?: 0,
            currentTime = PlayerService.controller?.currentPosition?.coerceAtLeast(0L) ?: 0,
            isPlaying = PlayerService.controller?.isPlaying == true,
            isCast = false,
            bufferedPercentage = PlayerService.controller?.bufferedPercentage ?: 0,
            playbackState = PlayerService.controller?.playbackState ?: 0
        )
        CastContext.getSharedInstance()?.addCastStateListener {
            this._playerdata.value = PlayerData(
                url = this.playerData.value.url,
                imageCode = this.playerData.value.imageCode,
                title = this.playerData.value.title,
                description = this.playerData.value.description,
                player = this.playerData.value.player,
                totalDuration = this.playerData.value.totalDuration,
                currentTime = this.playerData.value.currentTime,
                isPlaying = PlayerService.controller?.isPlaying == true,
                bufferedPercentage = this.playerData.value.bufferedPercentage,
                playbackState = this.playerData.value.playbackState,
                isCast = when (it) {
                    CastState.CONNECTING, CastState.CONNECTED -> true
                    CastState.NOT_CONNECTED, CastState.NO_DEVICES_AVAILABLE -> false
                    else -> false
                }
            )

            CastState.CONNECTED
        }
    }


    override fun onCleared() {
        Log.v(TAG, "onCleared")
        super.onCleared()
    }


    fun updateCurrentMedia(title: String) {
        if (PlayerService.controller?.currentMediaItem?.mediaMetadata?.title == title) {
            return
        }
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
                            .setArtworkUri(Uri.parse(entity.imageCode))
                            .build()
                    )
                    .build()
            )
            PlayerService.controller?.prepare()
            PlayerService.controller?.play()
            this._playerdata.value = PlayerData(
                url = entity.url,
                imageCode = entity.imageCode,
                title = entity.title,
                description = entity.description,
                player = this._playerdata.value.player,
                totalDuration = this.playerData.value.totalDuration,
                currentTime = this.playerData.value.currentTime,
                bufferedPercentage = this.playerData.value.bufferedPercentage,
                playbackState = this.playerData.value.playbackState,
                isPlaying = this._playerdata.value.player?.isPlaying == true,
                isCast = false
            )
        }
    }

    fun pause() {
        PlayerService.controller?.pause()
        this._playerdata.value = PlayerData(
            url = this.playerData.value.url,
            imageCode = this.playerData.value.imageCode,
            title = this.playerData.value.title,
            description = this.playerData.value.description,
            player = this.playerData.value.player,
            totalDuration = this.playerData.value.totalDuration,
            currentTime = this.playerData.value.currentTime,
            bufferedPercentage = this.playerData.value.bufferedPercentage,
            playbackState = this.playerData.value.playbackState,
            isPlaying = false,
            isCast = this.playerData.value.isCast
        )
    }

    fun play() {
        PlayerService.controller?.play()
        this._playerdata.value = PlayerData(
            url = this.playerData.value.url,
            imageCode = this.playerData.value.imageCode,
            title = this.playerData.value.title,
            description = this.playerData.value.description,
            player = this.playerData.value.player,
            totalDuration = this.playerData.value.totalDuration,
            currentTime = this.playerData.value.currentTime,
            bufferedPercentage = this.playerData.value.bufferedPercentage,
            playbackState = this.playerData.value.playbackState,
            isPlaying = true,
            isCast = this.playerData.value.isCast
        )
    }

    fun seekTo(timestamp: Float) {
        PlayerService.controller?.seekTo(timestamp.toLong())
        this._playerdata.value = PlayerData(
            url = this.playerData.value.url,
            imageCode = this.playerData.value.imageCode,
            title = this.playerData.value.title,
            description = this.playerData.value.description,
            player = this.playerData.value.player,
            totalDuration = this.playerData.value.totalDuration,
            currentTime = timestamp.toLong(),
            bufferedPercentage = this.playerData.value.bufferedPercentage,
            playbackState = this.playerData.value.playbackState,
            isPlaying = this.playerData.value.isPlaying,
            isCast = this.playerData.value.isCast
        )
    }

    fun seekBack() {
        PlayerService.controller?.seekBack()
        this._playerdata.value = PlayerData(
            url = this.playerData.value.url,
            imageCode = this.playerData.value.imageCode,
            title = this.playerData.value.title,
            description = this.playerData.value.description,
            player = this.playerData.value.player,
            totalDuration = this.playerData.value.totalDuration,
            currentTime = this.playerData.value.currentTime - PlayerService.controller?.seekBackIncrement!!,
            bufferedPercentage = this.playerData.value.bufferedPercentage,
            playbackState = this.playerData.value.playbackState,
            isPlaying = this.playerData.value.isPlaying,
            isCast = this.playerData.value.isCast
        )
    }

    fun seekForward() {
        PlayerService.controller?.seekForward()
        this._playerdata.value = PlayerData(
            url = this.playerData.value.url,
            imageCode = this.playerData.value.imageCode,
            title = this.playerData.value.title,
            description = this.playerData.value.description,
            player = this.playerData.value.player,
            totalDuration = this.playerData.value.totalDuration,
            currentTime = this.playerData.value.currentTime + PlayerService.controller?.seekForwardIncrement!!,
            bufferedPercentage = this.playerData.value.bufferedPercentage,
            playbackState = this.playerData.value.playbackState,
            isPlaying = this.playerData.value.isPlaying,
            isCast = this.playerData.value.isCast
        )
    }


}
