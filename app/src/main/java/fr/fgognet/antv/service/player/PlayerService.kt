package fr.fgognet.antv.service.player

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.util.Log
import androidx.media3.cast.CastPlayer
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.android.gms.cast.framework.CastContext
import com.google.common.util.concurrent.MoreExecutors
import fr.fgognet.antv.activity.main.MainActivity
import fr.fgognet.antv.activity.tv.TvActivity


class PlayerService : MediaSessionService() {
    // TAG
    private val TAG = "ANTV/PlayerService"

    // players
    private lateinit var localPlayer: ExoPlayer
    private lateinit var castPlayer: CastPlayer

    // mediaSession
    private var mediaSession: MediaSession? = null


    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate")
        val castContext = CastContext.getSharedInstance(
            applicationContext,
            MoreExecutors.directExecutor()
        )
        castPlayer = CastPlayer(
            castContext.result
        )
        localPlayer =
            ExoPlayer.Builder(this).build()
        val newPlayer: Player = if (castPlayer.isCastSessionAvailable) {
            castPlayer
        } else {
            localPlayer
        }
        val servicePlayerListener = PlayerServiceListener(this)
        mediaSession =
            MediaSession.Builder(this, newPlayer)
                .setSessionActivity(TaskStackBuilder.create(this).run {
                    addNextIntent(Intent(this@PlayerService, MainActivity::class.java))
                    addNextIntent(Intent(this@PlayerService, TvActivity::class.java))
                    getPendingIntent(
                        0,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }).setCallback(servicePlayerListener)
                .build()
        castPlayer.setSessionAvailabilityListener(servicePlayerListener)
    }


    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession


    fun resyncOnLiveError() {
        Log.v(TAG, "resyncOnLiveError")
        if (mediaSession?.player?.isCurrentMediaItemLive == true) {
            mediaSession?.player?.seekToDefaultPosition()
            mediaSession?.player?.prepare()
        }
    }

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
        if (playbackPositionMs > 0) {
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
        var controller: MediaController? = null

        // waiting for next version of media3
        var currentMediaItem: MediaItem? = null
    }


}