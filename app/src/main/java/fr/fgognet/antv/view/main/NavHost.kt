package fr.fgognet.antv.view.main


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            route = getRoute(PlayerRoute.id, PlayerRoute.argumentNames),
            arguments = PlayerRoute.arguments,
            deepLinks = PlayerRoute.deepLinks
        ) {
            val url =
                it.arguments?.getString("url")
            val imageCode =
                it.arguments?.getString("image_code")
            val title =
                it.arguments?.getString("title")
            val description =
                it.arguments?.getString("description")
            PlayerView(
                url = url ?: "",
                imageCode = imageCode,
                title = title,
                description = description,
                setVisible = {})
        }
        composable(
            route = getRoute(ReplayRoute.id, EventSearchQueryParams.allValues()),
            arguments = ReplayRoute.arguments,
            deepLinks = ReplayRoute.deepLinks
        ) {
            val navStackEntry = it
            ReplayCardListView(goToVideo = { url, imageCode, title, description ->
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
            }, arguments = navStackEntry.arguments ?: Bundle())
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

private fun hideWindow(view: View) {
/*    val topBar = view.rootView.findViewById<AppBarLayout>(R.id.appBarLayout)
    val bottom = view.rootView.findViewById<NavigationBarView>(R.id.bottom_navigation)
    activity?.let {
        WindowCompat.setDecorFitsSystemWindows(it.window, false)
        it.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    topBar.visibility = View.GONE
    bottom.visibility = View.GONE*/

}

private fun showWindow(view: View) {
/*    val topBar = view.rootView.findViewById<AppBarLayout>(R.id.appBarLayout)
    val bottom = view.rootView.findViewById<NavigationBarView>(R.id.bottom_navigation)
    activity?.let {
        WindowCompat.setDecorFitsSystemWindows(it.window, true)
        it.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
    topBar?.visibility = View.VISIBLE
    bottom?.visibility = View.VISIBLE*/
}