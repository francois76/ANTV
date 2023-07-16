package fr.fgognet.antv.widget

import fr.fgognet.antv.repository.VideoEntity
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import io.github.aakira.napier.Napier
import platform.AVFoundation.*
import platform.AVKit.AVPlayerViewController
import platform.Foundation.NSURL

private const val TAG = "ANTV/MediaController"

// inspired by https://github.com/ayodelekehinde/Kicks/blob/master/shared/src/iosMain/kotlin/io/github/kicks/audioplayer/AudioPlayer.kt
actual class MediaController(val iosMediaController: AVPlayerViewController?) {


    actual fun isInit(): Boolean {
        Napier.v(tag = TAG, message = "isInit")
        Napier.v(tag = TAG, message = "isInit: ${iosMediaController != null}")
        return iosMediaController != null
    }

    actual fun seekBack() {
    }

    actual fun seekForward() {
    }

    actual fun pause() {
        iosMediaController?.player?.pause()
        MediaSessionServiceImpl.invokeonIsPlayingChanged(false)
    }

    actual fun play() {
        iosMediaController?.player?.play()
        MediaSessionServiceImpl.invokeonIsPlayingChanged(true)
    }

    actual fun seekTo(toLong: Long) {
    }

    fun setMediaItem(entity: VideoEntity) {
        val streamVideoURL = NSURL(string = entity.url)
        iosMediaController?.player?.replaceCurrentItemWithPlayerItem(AVPlayerItem(uRL = streamVideoURL))
    }


}
