package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import platform.AVFoundation.AVPlayerLayer
import platform.UIKit.UIView


@Composable
actual fun Player(modifier: Modifier, context: PlatformContext, controller: MediaController) {

    val playerLayer = AVPlayerLayer()
    playerLayer.player = controller.iosMediaController?.player;
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

