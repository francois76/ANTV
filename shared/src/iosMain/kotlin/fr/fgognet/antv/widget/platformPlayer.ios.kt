package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import io.github.aakira.napier.Napier
import platform.AVFoundation.AVPlayerLayer
import platform.UIKit.UIView

private const val TAG = "ANTV/WidgetPlayer"

@Composable
actual fun Player(modifier: Modifier, context: PlatformContext, controller: MediaController) {
    if (controller.iosMediaController?.player == null) {
        return
    }
    val playerLayer = remember {
        val p = AVPlayerLayer()
        p.player = controller.iosMediaController.player;
        p
    }
    ViewKit(modifier = modifier, playerLayer = playerLayer)
}

@Composable
fun ViewKit(modifier: Modifier, playerLayer: AVPlayerLayer) {
    Napier.v(tag = TAG, message = "ViewKit")
    UIKitView(
        modifier = modifier,
        factory = {
            Napier.v(tag = TAG, message = "building UIKIT Player")
            UIView().apply {
                layer.addSublayer(playerLayer)
            }
        }, onResize = { view, rect ->
            Napier.v(tag = TAG, message = "Resizing UIKIT Player")
            view.setFrame(rect)
            playerLayer.setFrame(rect)
        }
    )
}

