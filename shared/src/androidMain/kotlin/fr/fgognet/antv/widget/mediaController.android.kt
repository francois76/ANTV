package fr.fgognet.antv.widget

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player

actual class MediaController(val androidController: androidx.media3.session.MediaController?) {
    val isCurrentMediaItemLive: Boolean?
        get() {
            return androidController?.isCurrentMediaItemLive
        }
    val isPlaying: Boolean?
        get() {
            return androidController?.isPlaying
        }

    val mediaMetadata: MediaMetadata?
        get() {
            return androidController?.mediaMetadata
        }
    val playbackState: Int?
        get() {
            return androidController?.playbackState
        }
    val bufferedPercentage: Int?
        get() {
            return androidController?.bufferedPercentage
        }
    val duration: Long?
        get() {
            return androidController?.duration
        }
    val currentPosition: Long?
        get() {
            return androidController?.currentPosition
        }

    actual fun seekBack() {
        androidController?.seekBack()
    }

    actual fun seekForward() {
        androidController?.seekForward()
    }

    actual fun pause() {
        androidController?.pause()
    }

    actual fun play() {
        androidController?.play()
    }

    actual fun seekTo(toLong: Long) {
        androidController?.seekTo(toLong)
    }

    fun addListener(it: Player.Listener) {
        androidController?.addListener(it)
    }

    fun setMediaItem(item: MediaItem) {
        androidController?.setMediaItem(item)
    }

    fun prepare() {
        androidController?.prepare()
    }

    fun seekToDefaultPosition() {
        androidController?.seekToDefaultPosition()
    }

    fun removeListener(listener: Player.Listener) {
        androidController?.removeListener(listener)
    }

    fun release() {
        androidController?.release()
    }

    actual fun isInit(): Boolean {
        return androidController != null
    }

}