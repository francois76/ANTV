package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import com.chrynan.navigation.*
import fr.fgognet.antv.view.main.RouteData

@OptIn(ExperimentalNavigationApi::class)
@Composable
actual fun HandlePictureInPicture(
    context: PlatformContext,
    navController: Navigator<RouteData, SingleNavigationContext<RouteData>>
) {
}