package fr.fgognet.antv.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.util.MimeTypes


class VideoViewModel : ViewModel() {

    var player: ExoPlayer? = null
        private set

    fun initPlayer(url: String, player: ExoPlayer) {
        this.player = player
        val mediaItem: MediaItem =
            MediaItem.Builder()
                .setUri(url)
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        this.player!!.setMediaItem(mediaItem)
        this.player!!.prepare()
    }
}