package fr.fgognet.antv.service.player

import android.app.Application
import android.graphics.Bitmap
import android.media.session.PlaybackState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.cast.CastPlayer
import androidx.media3.common.*
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import com.google.android.gms.cast.framework.CastContext

data class MediaData(
    val url: String,
    val title: String?,
    val description: String?,
    val bitmap: Bitmap?
)

object PlayerService {
    // TAG
    private const val TAG = "ANTV/PlayerService"

    // players
    private lateinit var localPlayer: Player
    private lateinit var castPlayer: CastPlayer
    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> get() = _player

    // context
    private lateinit var application: Application

    // mediaSession
    lateinit var mediaSession: MediaSession
        private set
    val mediaSessionToken: SessionToken
        get() = mediaSession.token
    var currentMediaData: MediaData? = null
        private set
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

    fun updateCurrentMedia(mediaData: MediaData) {
        Log.v(TAG, "updateCurrentMedia")
        if (mediaData.url == currentMediaData?.url) {
            return
        }

        currentMediaData = mediaData
        currentMediaItem =
            MediaItem.Builder()
                .setUri(mediaData.url)
                .setMediaMetadata(
                    MediaMetadata.Builder().setTitle(mediaData.title)
                        .setDescription(mediaData.description)
                        .build()
                )
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        player.value?.setMediaItem(currentMediaItem!!)
        player.value?.prepare()
        player.value?.play()
    }

    @UnstableApi
    fun init(application: Application) {
        if (PlayerService::application.isInitialized) {
            return
        }
        Log.v(TAG, "init")
        this.application = application
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

        mediaSession = MediaSession.Builder(application.baseContext, newPlayer).build()
        // mediaSession.setPlaybackState(mStateBuilder.build())
        val servicePlayerListener = PlayerServiceListener()
        castPlayer.setSessionAvailabilityListener(servicePlayerListener)
        Log.d(TAG, "updating player")
        _player.value = newPlayer
        val listenerKey = registerListener(servicePlayerListener)
        Log.d(TAG, "registered $listenerKey")
    }

    fun release() {
        player.value?.release()
        mediaSession.release()
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

    fun registerListener(listener: PlayerListener): Int {
        Log.v(TAG, "registerListener ${listener.javaClass.canonicalName}")
        val key = listener.hashCode()
        listeners = listeners + Pair(key, listener)
        player.value?.addListener(listener)
        Log.d(TAG, "successfully registered with key $key")
        return key
    }

    fun unregisterListener(key: Int) {
        Log.v(TAG, "unregisterListener $key")
        val listener = listeners[key]
        listener?.let {
            player.value?.removeListener(it)
            Log.d(TAG, "successfully unregistered ${listener.javaClass.canonicalName}")
        }
        listeners = listeners - key
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