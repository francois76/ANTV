package fr.fgognet.antv.view.main


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.fgognet.antv.view.cardList.LiveCardListView
import fr.fgognet.antv.view.cardList.PlaylistCardListView
import fr.fgognet.antv.view.cardList.ReplayCardListView
import fr.fgognet.antv.view.player.PlayerView
import fr.fgognet.antv.view.replaySearch.ReplaySearchView

const val TAG = "ANTV/ANTVNavHost"


@Composable
@UnstableApi
fun ANTVNavHost(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    navController: NavHostController,
    setFullScreenMode: (visible: Boolean) -> Unit
) {
    val context = LocalContext.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    if (context.findActivity()?.isInPictureInPictureMode == true) {
                        navController.navigateToChild(Routes.PLAYER.value)
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


    NavHost(
        navController = navController,
        startDestination = Routes.LIVE.value,
        modifier = modifier
    ) {

        composable(route = Routes.LIVE.value) {
            LiveCardListView(
                goToVideo = { title ->
                    navController.navigateToChild(
                        "${Routes.PLAYER.value}/$title"
                    )
                },
                goToCurrentPlaying = {
                    navController.navigateToChild(Routes.PLAYER.value)
                },
            )
        }
        composable(route = Routes.SEARCH.value) {
            ReplaySearchView(query = {
                navController.navigateToChild(
                    Routes.REPLAY.value
                )
            })
        }
        composable(route = Routes.PLAYLIST.value) {
            PlaylistCardListView(goToVideos = {
                navController.navigateToChild(
                    Routes.REPLAY.value
                )
            },
                goToCurrentPlaying = {
                    navController.navigateToChild(Routes.PLAYER.value)
                }
            )
        }
        val playerRoute = getRoute(Routes.PLAYER)
        composable(
            route = "${Routes.PLAYER.value}/{title}",
            arguments = playerRoute?.arguments!!,
            deepLinks = playerRoute.deepLinks!!
        ) {
            val title = getEncodedArgument(it.arguments, "title")
            PlayerView(
                title = title,
                setFullScreen = setFullScreenMode
            )
        }
        composable(
            route = Routes.PLAYER.value,
        ) {
            PlayerView(
                setFullScreen = setFullScreenMode, title = null,
            )
        }
        composable(
            route = Routes.REPLAY.value,
        ) {
            ReplayCardListView(
                goToVideo = { title ->
                    navController.navigateToChild(
                        "${Routes.PLAYER.value}/$title"
                    )
                },
                goToCurrentPlaying = {
                    navController.navigateToChild(Routes.PLAYER.value)
                })
        }

    }
}

fun NavHostController.navigateToChild(route: String) =
    this.navigate(route) {
        Log.d(TAG, "navigate to $route")
        launchSingleTop = false
        restoreState = false
    }


fun NavHostController.navigateToTop(route: String) =
    this.navigate(route) {
        Log.d(TAG, "navigate to $route")
        popUpTo(
            this@navigateToTop.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

