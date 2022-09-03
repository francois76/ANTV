package fr.fgognet.antv.view.main


import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.view.PlayerView
import fr.fgognet.antv.view.cardList.LiveCardListView
import fr.fgognet.antv.view.cardList.PlaylistCardListView
import fr.fgognet.antv.view.cardList.ReplayCardListView
import fr.fgognet.antv.view.replaySearch.ReplaySearchView
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

val TAG = "ANTV/ANTVNavHost"

@Composable
fun ANTVNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LiveRoute.id,
        modifier = modifier
    ) {
        composable(route = LiveRoute.id) {
            LiveCardListView(goToVideo = { url, imageCode ->
                navController.navigateToChild(
                    "${PlayerRoute.id}/${
                        URLEncoder.encode(
                            url,
                            StandardCharsets.UTF_8.toString()
                        )
                    }/${
                        URLEncoder.encode(
                            imageCode,
                            StandardCharsets.UTF_8.toString()
                        )
                    }"
                )
            })
        }
        composable(route = SearchRoute.id) {
            ReplaySearchView(query = {
                navigateToReplayList(navController, it)
            })
        }
        composable(route = PlaylistRoute.id) {
            PlaylistCardListView(goToVideos = {
                navigateToReplayList(navController, it)
            })
        }
        composable(
            route = "${PlayerRoute.id}/{${PlayerRoute.url}}/{${PlayerRoute.imageCode}}",
            arguments = PlayerRoute.arguments,
            deepLinks = PlayerRoute.deepLinks
        ) {
            val url =
                it.arguments?.getString(PlayerRoute.url)
            val imageCode =
                it.arguments?.getString(PlayerRoute.imageCode)
            PlayerView(url, imageCode)
        }
        composable(
            route = "${ReplayRoute.id}/${ReplayRoute.query}",
            arguments = ReplayRoute.arguments,
            deepLinks = ReplayRoute.deepLinks
        ) {
            val navStackEntry = it
            ReplayCardListView(goToVideo = { url, imageCode ->
                navController.navigateToChild(
                    "${PlayerRoute.id}/${
                        URLEncoder.encode(
                            url,
                            StandardCharsets.UTF_8.toString()
                        )
                    }/${
                        URLEncoder.encode(
                            imageCode,
                            StandardCharsets.UTF_8.toString()
                        )
                    }"
                )
            }, arguments = navStackEntry.arguments ?: Bundle())
        }

    }
}

fun navigateToReplayList(
    navController: NavHostController,
    query: Map<EventSearchQueryParams, String>
) {
    navController.navigateToChild(
        "${ReplayRoute.id}/${
            EventSearchQueryParams.allValues().joinToString("/") {
                if (query.containsKey(it)) {
                    "$it=${
                        URLEncoder.encode(
                            query.get(it),
                            StandardCharsets.UTF_8.toString()
                        )
                    }"
                } else {
                    "$it= "
                }
            }
        }"
    )
}

fun NavHostController.navigateToChild(route: String) =
    this.navigate(route) {
        Log.d(TAG, "navigate to $route")
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
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
