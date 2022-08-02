package fr.fgognet.antv.service.player

import android.app.Application
import android.graphics.Bitmap
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.util.MimeTypes
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
    lateinit var mediaSession: MediaSessionCompat
        private set
    val mediaSessionToken: MediaSessionCompat.Token
        get() = mediaSession.sessionToken
    private lateinit var mediaSessionConnector: MediaSessionConnector
    var currentMediaData: MediaData? = null
        private set
    private var currentMediaItem: MediaItem? = null
    private var mStateBuilder: PlaybackStateCompat.Builder =
        PlaybackStateCompat.Builder().setActions(
            PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat.ACTION_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY_PAUSE or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        )
    private var listeners: Map<Int, PlayerListener> =
        mapOf()

    fun updatePlayingState(state: Int) {
        Log.v(TAG, "updatePlayingState")
        mStateBuilder.setState(
            state,
            player.value?.currentPosition ?: 0, 1f
        )
        mediaSession.setPlaybackState(mStateBuilder.build())
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
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        player.value?.setMediaItem(currentMediaItem!!)
        player.value?.prepare()
        player.value?.play()
    }


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
        mediaSession = initMediaSession()
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(newPlayer)
        val servicePlayerListener = PlayerServiceListener()
        castPlayer.setSessionAvailabilityListener(servicePlayerListener)
        Log.d(TAG, "updating player")
        _player.value = newPlayer
        val listenerKey = registerListener(servicePlayerListener)
        Log.d(TAG, "registered $listenerKey")
    }

    fun resyncOnLiveError() {
        // Re-initialize player at the current live window default position.
        player.value?.seekToDefaultPosition()
        player.value?.prepare()
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


    private fun initMediaSession(): MediaSessionCompat {
        val mediaSession = MediaSessionCompat(application, TAG)
        mediaSession.setMediaButtonReceiver(null)
        mediaSession.setPlaybackState(mStateBuilder.build())
        mediaSession.isActive = true
        return mediaSession
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
        mediaSessionConnector.setPlayer(currentPlayer)
        mediaSession.isActive = true
        Log.d(TAG, "updating player")
        _player.value = currentPlayer
    }


}