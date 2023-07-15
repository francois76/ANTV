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

    // A streamController object is defined to handle the AVPlayerViewController event.
    var streamController = AVPlayerViewController()

    // Stream Video URL value video related information - url info
    val streamVideoURL: NSURL =
        NSURL(string = "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_30MB.mp4")

    val player = AVPlayer(uRL = streamVideoURL)
    val playerLayer = AVPlayerLayer()
    playerLayer.player = player;
    streamController.player = player;

    streamController.player?.play()
    UIKitView(
        modifier = modifier,
        factory = {
            UIView().apply {
                layer.addSublayer(playerLayer)
            }
        }, onResize = { view, rect ->
            view.setFrame(rect)
            playerLayer.setFrame(rect)
        }
    )
}

