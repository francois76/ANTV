package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.chrynan.navigation.Navigator
import com.chrynan.navigation.SingleNavigationContext
import com.chrynan.navigation.goTo
import fr.fgognet.antv.view.main.Route
import fr.fgognet.antv.view.main.RouteData

@Composable
actual fun handlePictureInPicture(
    context: PlatformContext,
    navController: Navigator<RouteData, SingleNavigationContext<RouteData>>
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    if (context.findActivity()?.isInPictureInPictureMode == true) {
                        navController.goTo(
                            RouteData(
                                id = Route.PLAYER,
                                arguments = null,
                                iconName = null,
                                nameID = null
                            )
                        )
                    }
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}