package fr.fgognet.antv.widget

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun Player(shouldShowControls: Boolean): Boolean {
    var shouldShowControls1 = shouldShowControls
    var context = getPlatformContext()
    AndroidView(
        modifier =
        Modifier
            .background(color = Color.Black)
            .clickable {
                shouldShowControls1 = shouldShowControls1.not()
            },
        factory = {
            androidx.media3.ui.PlayerView(context.androidContext).apply {
                player = controller
                useController = false
                layoutParams =
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
            }
        })
    return shouldShowControls1
}