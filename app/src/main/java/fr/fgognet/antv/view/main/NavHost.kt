package fr.fgognet.antv.view.main


import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.view.cardList.LiveCardListView
import fr.fgognet.antv.view.cardList.PlaylistCardListView
import fr.fgognet.antv.view.cardList.ReplayCardListView
import fr.fgognet.antv.view.player.PlayerView
import fr.fgognet.antv.view.replaySearch.ReplaySearchView

const val TAG = "ANTV/ANTVNavHost"

@Composable
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
                        navController.navigateToTop(PlayerRoute.id)
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
        startDestination = LiveRoute.id,
        modifier = modifier
    ) {

        composable(route = LiveRoute.id) {
            LiveCardListView(goToVideo = { url, imageCode, title, description ->
                navController.navigateToChild(
                    callRouteWithArguments(
                        PlayerRoute.id, hashMapOf(
                            "url" to url,
                            "image_code" to imageCode,
                            "title" to title,
                            "description" to description
                        )
                    )
                )
            },
                goToCurrentPlaying = {
                    navController.navigateToChild(PlayerRoute.id)
                }
            )
        }
        composable(route = SearchRoute.id) {
            ReplaySearchView(query = {
                navigateToReplayList(navController, it)
            })
        }
        composable(route = PlaylistRoute.id) {
            PlaylistCardListView(goToVideos = {
                navigateToReplayList(navController, it)
            },
                goToCurrentPlaying = {
                    navController.navigateToChild(PlayerRoute.id)
                })
        }
        composable(
            route = getRoute(PlayerRoute.id, PlayerRoute.argumentNames),
            arguments = PlayerRoute.arguments,
            deepLinks = PlayerRoute.deepLinks
        ) {
            val url = getEncodedArgument(it.arguments, "url")
            val imageCode = getEncodedArgument(it.arguments, "image_code")
            val title = getEncodedArgument(it.arguments, "title")
            val description = getEncodedArgument(it.arguments, "description")
            PlayerView(
                url = url,
                imageCode = imageCode,
                title = title,
                description = description,
                setFullScreen = setFullScreenMode
            )
        }
        composable(
            route = PlayerRoute.id,
        ) {
            PlayerView(
                setFullScreen = setFullScreenMode
            )
        }
        composable(
            route = getRoute(ReplayRoute.id, EventSearchQueryParams.allValues()),
            arguments = ReplayRoute.arguments,
            deepLinks = ReplayRoute.deepLinks
        ) {
            val navStackEntry = it
            ReplayCardListView(
                arguments = navStackEntry.arguments ?: Bundle(),
                goToVideo = { url, imageCode, title, description ->
                    navController.navigateToChild(
                        callRouteWithArguments(
                            PlayerRoute.id, hashMapOf(
                                "url" to url,
                                "image_code" to imageCode,
                                "title" to title,
                                "description" to description
                            )
                        )
                    )
                },
                goToCurrentPlaying = {
                    navController.navigateToChild(PlayerRoute.id)
                })
        }

    }
}

fun navigateToReplayList(
    navController: NavHostController,
    query: Map<EventSearchQueryParams, String>
) {
    navController.navigateToChild(
        callRouteWithArguments(
            ReplayRoute.id, EventSearchQueryParams.allValues().associate {
                if (query.containsKey(it)) {
                    Pair(it, query[it]!!)
                } else {
                    Pair(it, " ")
                }

            }.mapKeys { it.key.toString() }
        )
    )
}

fun NavHostController.navigateToChild(route: String) =
    this.navigate(route) {
        Log.d(TAG, "navigate to $route")
        popUpTo(
            this@navigateToChild.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
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

