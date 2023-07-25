package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory

@Composable
actual fun CastButton() {
    AndroidView(factory = { context ->
        val mediaButton = MediaRouteButton(context)
        CastButtonFactory.setUpMediaRouteButton(context, mediaButton)
        mediaButton
    })
}