package fr.fgognet.antv.service.player

import fr.fgognet.antv.widget.MediaController
import platform.AVFoundation.AVPlayer
import platform.AVKit.AVPlayerViewController

actual class MediaSessionServiceImpl {


    actual companion object {
        private val localPlayer = AVPlayer()

        private val controllerFuture: AVPlayerViewController = AVPlayerViewController()
        private val iosController: AVPlayerViewController?
            get() = if (controllerFuture.player != null) controllerFuture else null

        fun loadController() {
            controllerFuture.player = localPlayer
        }

        actual val isCasting: Boolean
            get() = false
        actual val controller: MediaController?
            get() = MediaController(iosMediaController = iosController)


    }

}