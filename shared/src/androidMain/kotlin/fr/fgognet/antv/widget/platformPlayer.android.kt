package fr.fgognet.antv.widget

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView

@Composable
actual fun Player(modifier: Modifier, context: PlatformContext, controller: MediaController) {
    AndroidView(
        modifier =
        modifier.background(color = Color.Black),
        factory = {
            PlayerView(context.androidContext).apply {
                player = controller.androidController
                useController = false
                layoutParams =
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
            }
        })
}