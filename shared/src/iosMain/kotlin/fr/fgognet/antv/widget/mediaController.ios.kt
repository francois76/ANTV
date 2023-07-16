package fr.fgognet.antv.widget

import fr.fgognet.antv.repository.VideoEntity
import platform.AVFoundation.*
import platform.AVKit.AVPlayerViewController
import platform.Foundation.NSURL

// inspired by https://github.com/ayodelekehinde/Kicks/blob/master/shared/src/iosMain/kotlin/io/github/kicks/audioplayer/AudioPlayer.kt
actual class MediaController(val iosMediaController: AVPlayerViewController?) {


    actual fun isInit(): Boolean {
        return true
    }

    actual fun seekBack() {
    }

    actual fun seekForward() {
    }

    actual fun pause() {
        iosMediaController?.player?.pause()
    }

    actual fun play() {
        iosMediaController?.player?.play()
    }

    actual fun seekTo(toLong: Long) {
    }

    fun setMediaItem(entity: VideoEntity) {
        val streamVideoURL = NSURL(string = entity.url)
        iosMediaController?.player?.replaceCurrentItemWithPlayerItem(AVPlayerItem(uRL = streamVideoURL))
    }


}
