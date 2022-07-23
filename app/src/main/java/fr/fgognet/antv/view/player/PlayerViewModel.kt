package fr.fgognet.antv.view.player

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.gms.cast.framework.CastContext


private val TAG = "ANTV/VideoViewModel"

class VideoViewModel(application: Application) : AndroidViewModel(application),
    DefaultLifecycleObserver, SessionAvailabilityListener, Player.Listener {

    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?> get() = _player

    private var localPlayer: Player? = null
    private var castPlayer: CastPlayer? = null
    private var url: String? = null

    init {
        // Alternatively expose this as a dependency
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    override fun onDestroy(owner: LifecycleOwner) {
        Log.v(TAG, "onDestroy")
        super.onDestroy(owner)
        releasePlayer()
    }

    override fun onCleared() {
        Log.v(TAG, "onCleared")
        super.onCleared()
        releasePlayer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }


    fun updateUrl(url: String, castContext: CastContext) {
        if (url != this.url) {
            this.url = url
            setUpPlayer(castContext)
        }

    }


    private fun setUpPlayer(castContext: CastContext) {
        Log.i(TAG, "setUpPlayer")
        castPlayer = CastPlayer(castContext)
        castPlayer?.addListener(this)
        castPlayer?.setSessionAvailabilityListener(this)

        val newPlayer: Player
        if (castPlayer?.isCastSessionAvailable == true) {
            newPlayer = castPlayer!!
        } else {
            newPlayer =
                ExoPlayer.Builder(this.getApplication<Application>().applicationContext).build()
        }


        val mediaItem: MediaItem =
            MediaItem.Builder()
                .setUri(url)
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        newPlayer.setMediaItem(mediaItem)
        newPlayer.prepare()
        newPlayer.play()
        this._player.value = newPlayer
    }

    private fun releasePlayer() {
        val player = _player.value ?: return
        this._player.value = null
        player.release()
    }

    override fun onCastSessionAvailable() {
        Log.v(TAG, "onCastSessionAvailable")
        setCurrentPlayer(castPlayer)
    }

    override fun onCastSessionUnavailable() {
        Log.v(TAG, "onCastSessionUnavailable")
        setCurrentPlayer(localPlayer)
    }

    private fun setCurrentPlayer(currentPlayer: Player?) {
        if (currentPlayer === this.player.value) {
            return
        }


        // Player state management.
        var playbackPositionMs = C.TIME_UNSET
        var playWhenReady = false
        val previousPlayer: Player? = this.player.value
        val previousMediaItem = previousPlayer?.currentMediaItem
        if (previousPlayer != null) {
            // Save state from the previous player.
            val playbackState = previousPlayer.playbackState
            if (playbackState != Player.STATE_ENDED) {
                playbackPositionMs = previousPlayer.currentPosition
                playWhenReady = previousPlayer.playWhenReady
            }
            previousPlayer.stop()
            previousPlayer.clearMediaItems()
        }
        currentPlayer?.setMediaItem(previousMediaItem!!, playbackPositionMs)
        currentPlayer?.playWhenReady = playWhenReady
        currentPlayer?.prepare()
        this._player.value = currentPlayer

    }

}
