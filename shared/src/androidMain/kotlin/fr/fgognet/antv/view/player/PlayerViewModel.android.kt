package fr.fgognet.antv.view.player

import android.content.ComponentName
import android.net.Uri
import android.util.Log
import androidx.media3.common.*
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.SessionToken
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastState
import com.google.common.util.concurrent.MoreExecutors
import fr.fgognet.antv.repository.VideoDao
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import fr.fgognet.antv.service.player.MediaSessionServiceListener
import fr.fgognet.antv.widget.MediaController
import fr.fgognet.antv.widget.PlatformContext
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val TAG = "ANTV/PlayerViewModel"

actual class PlayerViewModel : PlayerViewModelCommon(), Player.Listener {


    override fun initialize(c: MediaController?) {
        Napier.v(tag = TAG, message = "initialize")
        if (c != null) {
            this._controller.value = c
        }
        if (controller.value == null || controller.value!!.androidController == null) {
            return
        }
        MediaSessionServiceImpl.controller?.addListener(this)
        CastContext.getSharedInstance()?.addCastStateListener {
            this._playerdata.value = this.playerData.value.copy(
                isCasting = when (it) {
                    CastState.CONNECTING, CastState.CONNECTED -> true
                    CastState.NOT_CONNECTED, CastState.NO_DEVICES_AVAILABLE -> false
                    else -> false
                }
            )
            CastState.CONNECTED
        }
        val t = this
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                ticker().collect { isPlaying ->
                    if (isPlaying) {
                        t._playerdata.value = t.playerData.value.copy(
                            currentPosition = MediaSessionServiceImpl.controller?.currentPosition?.coerceAtLeast(
                                0L
                            )
                                ?: 0,
                            duration = MediaSessionServiceImpl.controller?.duration
                                ?: 1,
                            bufferedPercentage = MediaSessionServiceImpl.controller?.bufferedPercentage
                                ?: 0,
                            isEnded = STATE_ENDED == MediaSessionServiceImpl.controller?.playbackState,
                        )
                    }
                }
            }
        }

    }

    override fun loadPlayer(context: PlatformContext) {
        if (MediaSessionServiceImpl.controllerFuture == null) {
            MediaSessionServiceImpl.controllerFuture =
                androidx.media3.session.MediaController.Builder(
                    context.androidContext,
                    SessionToken(
                        context.androidContext,
                        ComponentName(context.androidContext, MediaSessionServiceImpl::class.java)
                    )
                )
                    .buildAsync()
            MediaSessionServiceImpl.controllerFuture?.addListener({
                MediaSessionServiceImpl.addFutureListener()
                initialize(
                    MediaSessionServiceImpl.controller
                )
            }, MoreExecutors.directExecutor())
        }
    }


    override fun onIsPlayingChanged(isPlaying: Boolean) {
        this._playerdata.value = this.playerData.value.copy(
            isPlaying = isPlaying,
        )
    }

    @UnstableApi
    override fun loadMedia(title: String?) {
        Napier.v(tag = TAG, message = "loadMedia")
        if (title == null) {
            loadCurrentMedia()
        } else {
            updateCurrentMedia(title)
        }
    }

    private fun loadCurrentMedia() {
        Napier.v(tag = TAG, message = "loadCurrentMedia")
        this._playerdata.value = this.playerData.value.copy(
            title = MediaSessionServiceImpl.controller?.mediaMetadata?.title.toString(),
            description = MediaSessionServiceImpl.controller?.mediaMetadata?.description?.toString()
                ?: "",
            duration = MediaSessionServiceImpl.controller?.duration?.coerceAtLeast(0) ?: 0,
            currentPosition = MediaSessionServiceImpl.controller?.currentPosition?.coerceAtLeast(
                0
            )
                ?: 0,
            isPlaying = MediaSessionServiceImpl.controller?.isPlaying == true,
            bufferedPercentage = MediaSessionServiceImpl.controller?.bufferedPercentage ?: 0,
            isEnded = STATE_ENDED == MediaSessionServiceImpl.controller?.playbackState,
        )
    }

    @UnstableApi
    private fun updateCurrentMedia(title: String) {
        Napier.v(tag = TAG, message = "updateCurrentMedia")
        if (title == playerData.value.title) {
            return
        }
        val entity = VideoDao.get(title)
        if (entity == null) {
            if (MediaSessionServiceImpl.controller != null && MediaSessionServiceImpl.currentMediaItem != null) {
                this._playerdata.value = this.playerData.value.copy(
                    title = MediaSessionServiceImpl.currentMediaItem?.mediaMetadata?.title.toString(),
                    description = MediaSessionServiceImpl.currentMediaItem?.mediaMetadata?.description.toString(),
                    duration = MediaSessionServiceImpl.controller?.duration?.coerceAtLeast(0) ?: 0,
                    currentPosition = MediaSessionServiceImpl.controller?.currentPosition?.coerceAtLeast(
                        0
                    )
                        ?: 0,
                    isPlaying = MediaSessionServiceImpl.controller?.isPlaying == true,
                    bufferedPercentage = MediaSessionServiceImpl.controller?.bufferedPercentage
                        ?: 0,
                    isEnded = STATE_ENDED == MediaSessionServiceImpl.controller?.playbackState,
                )
            }
        } else {
            //this is not the full mediaItem here
            val item = MediaItem.Builder()
                .setMediaId(entity.url)
                .setUri(entity.url)
                .setMediaMetadata(
                    MediaMetadata.Builder().setTitle(entity.title)
                        .setDescription(entity.description)
                        .setArtworkUri(Uri.parse(entity.imageCode))
                        .build()
                )
                .build()
            MediaSessionServiceListener.currentItems = mapOf(entity.url to item)
            MediaSessionServiceImpl.controller?.setMediaItem(item)
            MediaSessionServiceImpl.controller?.prepare()
            MediaSessionServiceImpl.controller?.play()

            this._playerdata.value = this.playerData.value.copy(
                title = entity.title,
                description = entity.description,
                duration = MediaSessionServiceImpl.controller?.duration?.coerceAtLeast(0) ?: 0,
                currentPosition = MediaSessionServiceImpl.controller?.currentPosition?.coerceAtLeast(
                    0
                )
                    ?: 0,
                isPlaying = MediaSessionServiceImpl.controller?.isPlaying == true,
                bufferedPercentage = MediaSessionServiceImpl.controller?.bufferedPercentage ?: 0,
                isEnded = STATE_ENDED == MediaSessionServiceImpl.controller?.playbackState,
            )
        }

    }

    override fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
        Log.v(TAG, "onPositionDiscontinuity")
        this._playerdata.value = this.playerData.value.copy(
            currentPosition = MediaSessionServiceImpl.controller?.currentPosition?.coerceAtLeast(
                0
            )
                ?: 0,
            duration = MediaSessionServiceImpl.controller?.duration?.coerceAtLeast(
                0
            )
                ?: 0,
        )
    }

    override fun onPlayerError(error: PlaybackException) {
        Napier.v(tag = TAG, message = "onPlayerError")
        try {
            when (error.errorCode) {
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT,
                PlaybackException.ERROR_CODE_IO_UNSPECIFIED,
                PlaybackException.ERROR_CODE_DECODER_INIT_FAILED,
                PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW -> {
                    Log.w(TAG, "error on playback: ${error.errorCode}")
                    if (MediaSessionServiceImpl.controller?.isCurrentMediaItemLive == true) {
                        MediaSessionServiceImpl.controller?.seekToDefaultPosition()
                        MediaSessionServiceImpl.controller?.prepare()
                    }
                }

                else -> Log.e(TAG, "error on playback: ${error.errorCode}")
            }
        } catch (_: Exception) {
        }
    }


    private fun ticker(): Flow<Boolean> = flow {
        while (true) {
            delay(1000)
            emit(MediaSessionServiceImpl.controller?.isPlaying == true)
        }
    }


}