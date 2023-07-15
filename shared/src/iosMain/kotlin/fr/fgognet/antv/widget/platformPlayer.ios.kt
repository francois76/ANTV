package fr.fgognet.antv.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import io.kamel.core.utils.URL
import platform.AVFoundation.*
import platform.AVKit.AVPlayerViewController
import platform.Foundation.NSURL
import platform.UIKit.UIView


@Composable
actual fun Player(modifier: Modifier, context: PlatformContext, controller: MediaController) {
    UIKitView(
        modifier = modifier,
        factory = {
            val view = UIView()

            // A streamController object is defined to handle the AVPlayerViewController event.
            var streamController = AVPlayerViewController()

            // Stream Video URL value video related information - url info
            val streamVideoURL: NSURL =
                NSURL(string = "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_30MB.mp4")

            val player = AVPlayer(uRL = streamVideoURL)
            val playerLayer = AVPlayerLayer()
            playerLayer.player = player;
            streamController.player = player;

            view.layer.addSublayer(playerLayer)
            streamController.player?.play()
            view
        }
    )
}

