package fr.fgognet.antv.view.card

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
actual fun CompositeCardView(
    data: GenericCardData,
    buttonClicked: () -> Unit
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeCompositeCardView(data = data, buttonClicked = buttonClicked)
        }

        else -> {
            PortraitCompositeCardView(data = data, buttonClicked = buttonClicked)
        }
    }
}