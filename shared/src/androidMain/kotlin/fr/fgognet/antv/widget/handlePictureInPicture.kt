package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.chrynan.navigation.*
import fr.fgognet.antv.view.main.Route
import fr.fgognet.antv.view.main.RouteData
import io.github.aakira.napier.Napier

private const val TAG = "ANTV/HandlePictureInPictureWidget"

@OptIn(ExperimentalNavigationApi::class)
@Composable
actual fun HandlePictureInPicture(
    context: PlatformContext,
    navController: Navigator<RouteData, SingleNavigationContext<RouteData>>
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Napier.v(tag = TAG, message = "event received : $event")
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    if (context.findActivity()?.isInPictureInPictureMode == true) {
                        navController.push(
                            RouteData(
                                id = Route.PLAYER,
                                arguments = arrayListOf(),
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