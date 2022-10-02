package fr.fgognet.antv.service.player

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.media.session.PlaybackState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.cast.CastPlayer
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionToken
import com.google.android.gms.cast.framework.CastContext
import fr.fgognet.antv.activity.main.MainActivity
import fr.fgognet.antv.activity.tv.TvActivity


class PlayerService : MediaSessionService() {
    // TAG
    private val TAG = "ANTV/PlayerService"

    // players
    private lateinit var localPlayer: Player
    private lateinit var castPlayer: CastPlayer
    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> get() = _player

    // mediaSession
    lateinit var mediaSession: MediaSession
        private set
    val mediaSessionToken: SessionToken
        get() = mediaSession.token
    private var currentMediaItem: MediaItem? = null
    private var mStateBuilder: PlaybackState.Builder =
        PlaybackState.Builder().setActions(
            PlaybackState.ACTION_PLAY or
                    PlaybackState.ACTION_PAUSE or
                    PlaybackState.ACTION_PLAY_PAUSE or
                    PlaybackState.ACTION_SKIP_TO_PREVIOUS
        )
    private var listeners: Map<Int, PlayerListener> =
        mapOf()

    fun updatePlayingState(state: Int) {
        Log.v(TAG, "updatePlayingState")
        mStateBuilder.setState(
            state,
            player.value?.currentPosition ?: 0, 1f
        )
        // mediaSession.setPlaybackState(mStateBuilder.build())
    }


    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "init")
        if (CastContext.getSharedInstance() != null) {
            castPlayer = CastPlayer(CastContext.getSharedInstance()!!)
        } else {
            Log.e(TAG, "no castcontext instance found")
        }
        localPlayer =
            ExoPlayer.Builder(application).build()
        val newPlayer: Player = if (castPlayer.isCastSessionAvailable) {
            castPlayer
        } else {
            localPlayer
        }

        mediaSession =
            MediaSession.Builder(this, localPlayer)
                .setSessionActivity(TaskStackBuilder.create(this).run {
                    addNextIntent(Intent(this@PlayerService, MainActivity::class.java))
                    addNextIntent(Intent(this@PlayerService, TvActivity::class.java))
                    getPendingIntent(
                        0,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                })
                .build()
        val servicePlayerListener = PlayerServiceListener(this)
        castPlayer.setSessionAvailabilityListener(servicePlayerListener)
        Log.d(TAG, "updating player")
        _player.value = newPlayer

    }


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    override fun onDestroy() {
        player.value?.release()
        mediaSession.release()
        super.onDestroy()
    }


    fun resyncOnLiveError() {
        // Re-initialize player at the current live window default position.
        if (player.value?.isCurrentMediaItemLive == true) {
            player.value?.seekToDefaultPosition()
            player.value?.prepare()
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
        if (player.value == null || currentPlayer === player) {
            return
        }
        val previousPlayer: Player = player.value!!
        // Player state management.
        var playbackPositionMs = C.TIME_UNSET
        var playWhenReady = false
        // Save state from the previous player.
        val playbackState = previousPlayer.playbackState
        if (playbackState != Player.STATE_ENDED) {
            playbackPositionMs = previousPlayer.currentPosition
            playWhenReady = previousPlayer.playWhenReady
        }
        previousPlayer.stop()
        previousPlayer.clearMediaItems()
        listeners.values.forEach {
            previousPlayer.removeListener(it)
            currentPlayer.addListener(it)
        }

        currentPlayer.setMediaItem(currentMediaItem!!, playbackPositionMs)
        currentPlayer.playWhenReady = playWhenReady
        currentPlayer.prepare()
        mediaSession.player = currentPlayer
        Log.d(TAG, "updating player")
        _player.value = currentPlayer
    }


}