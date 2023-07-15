package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.play
import platform.AVKit.AVPlayerViewController


@Composable
actual fun Player(modifier: Modifier, context: PlatformContext, controller: MediaController) {
    val player = AVPlayer()
    player.play()

    val playerController = AVPlayerViewController()
    playerController.player = player
}