package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import platform.AVFoundation.*
import platform.AVKit.*


@Composable
actual fun player(modifier: Modifier, context:PlatformContext, controller: MediaController) {
    val player = AVPlayer()
    player.play()

    val playerController = AVPlayerViewController()
    playerController.player = player
}