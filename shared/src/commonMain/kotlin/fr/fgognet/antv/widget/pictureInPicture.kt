package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.Navigator
import com.chrynan.navigation.SingleNavigationContext
import fr.fgognet.antv.view.main.RouteData

@OptIn(ExperimentalNavigationApi::class)
@Composable
expect fun HandlePictureInPicture(
    context: PlatformContext,
    navController: Navigator<RouteData, SingleNavigationContext<RouteData>>
)