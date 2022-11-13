package fr.fgognet.antv.service.player

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.media3.cast.CastPlayer
import androidx.media3.cast.DefaultMediaItemConverter
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import java.util.*


class MediaSessionServiceImpl : MediaLibraryService() {
    // TAG
    private val TAG = "ANTV/MediaSessionServiceImpl"

    // players
    private lateinit var localPlayer: ExoPlayer
    private lateinit var castPlayer: CastPlayer

    // mediaSession
    private var mediaSession: MediaLibrarySession? = null


    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate")
        localPlayer =
            ExoPlayer.Builder(this).setSeekBackIncrementMs(5000).setSeekForwardIncrementMs(5000)
                .build()
        val servicePlayerListener = MediaSessionServiceListener(this)
        mediaSession =
            MediaLibrarySession.Builder(this, localPlayer, MediaSessionServiceListener(this))
                .setId(UUID.randomUUID().toString())
                .setSessionActivity(TaskStackBuilder.create(this).run {
                    addNextIntentWithParentStack(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("antv://player/" + localPlayer.mediaMetadata.title)
                        )
                    )
                    getPendingIntent(
                        0,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                })
                .build()
        CastContext.getSharedInstance(
            applicationContext,
            MoreExecutors.directExecutor()
        ).addOnCompleteListener { result ->
            castPlayer = CastPlayer(
                result.result, DefaultMediaItemConverter(), 5000, 5000
            )
            result.result.addCastStateListener {
                isCasting = when (it) {
                    CastState.CONNECTING, CastState.CONNECTED -> true
                    CastState.NOT_CONNECTED, CastState.NO_DEVICES_AVAILABLE -> false
                    else -> false
                }
            }
            if (castPlayer.isCastSessionAvailable) {
                setCurrentPlayer(castPlayer)
            }
            castPlayer.setSessionAvailabilityListener(servicePlayerListener)
        }


    }


    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        if (controllerFuture != null) {
            MediaController.releaseFuture(controllerFuture!!)
            controllerFuture = null
            listenersFuture = arrayListOf()
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? =
        mediaSession


    fun cast() {
        Log.v(TAG, "cast")
        setCurrentPlayer(castPlayer)
    }

    fun stopCast() {
        Log.v(TAG, "stopCast")
        setCurrentPlayer(localPlayer)
    }


    private fun setCurrentPlayer(currentPlayer: Player) {
        Log.v(TAG, "setCurrentPlayer")
        if (currentPlayer === mediaSession?.player || mediaSession == null) {
            return
        }
        val previousPlayer: Player = mediaSession!!.player
        // Player state management.
        var playbackPositionMs = C.TIME_UNSET
        var playWhenReady = false
        // Save state from the previous player.
        val playbackState = previousPlayer.playbackState
        if (playbackState != Player.STATE_ENDED) {
            playbackPositionMs = previousPlayer.currentPosition
            playWhenReady = previousPlayer.playWhenReady
        }
        if (playbackPositionMs > 0 && currentMediaItem != null) {
            currentPlayer.setMediaItem(currentMediaItem!!, playbackPositionMs)
        }
        previousPlayer.stop()
        previousPlayer.clearMediaItems()
        currentPlayer.playWhenReady = playWhenReady
        currentPlayer.prepare()
        Log.d(TAG, "updating player")
        mediaSession?.player = currentPlayer
    }


    companion object {
        var controllerFuture: ListenableFuture<MediaController>? = null
        private var listenersFuture: ArrayList<Player.Listener> = arrayListOf()
        val controller: MediaController?
            get() = if (controllerFuture?.isDone == true) controllerFuture?.get() else null
        var isCasting: Boolean = false

        // waiting for next version of media3
        var currentMediaItem: MediaItem? = null

        fun addFutureListener() {
            listenersFuture.forEach {
                controller?.addListener(it)
            }
        }

        fun addListener(listener: Player.Listener) {
            if (controller == null) {
                listenersFuture.add(listener)
            } else {
                controller?.addListener(listener)
            }
        }
    }


}