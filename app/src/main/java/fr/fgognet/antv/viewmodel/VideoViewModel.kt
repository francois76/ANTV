package fr.fgognet.antv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.util.MimeTypes


class VideoViewModel(application: Application) : AndroidViewModel(application) {

    var url: String? = null
    var isPlayerInit: Boolean = false
        private set
        get() {
            return url != null
        }

    fun initPlayer(url: String): ExoPlayer {
        this.url = url
        return buildPlayer()
    }

    fun buildPlayer(): ExoPlayer {
        val player =
            ExoPlayer.Builder(this.getApplication<Application>().applicationContext).build()
        val mediaItem: MediaItem =
            MediaItem.Builder()
                .setUri(url)
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        player.setMediaItem(mediaItem)
        player.prepare()
        return player
    }


}