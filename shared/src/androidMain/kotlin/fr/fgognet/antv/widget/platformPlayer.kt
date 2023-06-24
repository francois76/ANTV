package fr.fgognet.antv.widget

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView

@Composable
actual fun player(context: PlatformContext , controller: MediaController, onclick: ()->Unit) {
    AndroidView(
        modifier =
        Modifier
            .background(color = Color.Black)
            .clickable {
                onclick()
            },
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