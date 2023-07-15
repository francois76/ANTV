package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import platform.AVFoundation.*

// inspired by https://github.com/ayodelekehinde/Kicks/blob/master/shared/src/iosMain/kotlin/io/github/kicks/audioplayer/AudioPlayer.kt
actual class MediaController(private val iosPlayer: AVPlayer) {
    actual fun seekBack() {

    }

    actual fun seekForward() {
    }

    actual fun pause() {
        this.iosPlayer.pause()
    }

    actual fun play() {
        this.iosPlayer.play()
    }

    actual fun seekTo(toLong: Long) {

    }

    actual fun isInit(): Boolean {
        return false
    }
}


@Composable
actual fun getStateEnded(): Int {
    return 0
}