package fr.fgognet.antv.service.player

import fr.fgognet.antv.widget.MediaController
import io.github.aakira.napier.Napier
import platform.AVFoundation.AVPlayer
import platform.AVKit.AVPlayerViewController

private const val TAG = "ANTV/MediaSessionServiceImpl"

actual class MediaSessionServiceImpl {


    actual companion object {
        private val localPlayer = AVPlayer()

        private val controllerFuture: AVPlayerViewController = AVPlayerViewController()
        private val iosController: AVPlayerViewController?
            get() = if (controllerFuture.player != null) controllerFuture else null

        fun loadController() {
            Napier.v(tag = TAG, message = "loadController")
            controllerFuture.player = localPlayer
        }

        actual val isCasting: Boolean
            get() = false
        actual val controller: MediaController?
            get() = MediaController(iosMediaController = iosController)


    }

}