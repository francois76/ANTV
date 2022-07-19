package fr.fgognet.antv.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.MimeTypes

private val TAG = "VideoViewModel"

class VideoViewModel(application: Application) : AndroidViewModel(application),
    DefaultLifecycleObserver {

    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?> get() = _player
    private var url: String? = null

    init {
        // Alternatively expose this as a dependency
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }


    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.i(TAG, "onStop")
        releasePlayer()
    }

    fun updateUrl(url: String) {
        if (url != this.url) {
            this.url = url
            setUpPlayer()
        }

    }


    private fun setUpPlayer() {
        Log.i(TAG, "setUpPlayer")
        val player =
            ExoPlayer.Builder(this.getApplication<Application>().applicationContext).build()
        val mediaItem: MediaItem =
            MediaItem.Builder()
                .setUri(url)
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        this._player.value = player
    }

    private fun releasePlayer() {
        val player = _player.value ?: return
        this._player.value = null
        player.release()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared")
        releasePlayer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

}
