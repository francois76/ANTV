package fr.fgognet.antv.widget

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
actual fun OrientationWrapper(
    portrait: @Composable () -> Unit,
    landscape: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            landscape()
        }

        else -> {
            portrait()
        }
    }
}