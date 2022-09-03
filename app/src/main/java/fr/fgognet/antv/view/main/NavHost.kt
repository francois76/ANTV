package fr.fgognet.antv.view.main


import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.fgognet.antv.view.PlayerView
import fr.fgognet.antv.view.cardList.LiveCardListView
import fr.fgognet.antv.view.cardList.PlaylistCardListView
import fr.fgognet.antv.view.cardList.ReplayCardListView
import fr.fgognet.antv.view.replaySearch.ReplaySearchView

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
            LiveCardListView(goToVideo = {
                val bundle = Bundle()
/*                bundle.putString(ARG_URL, cardData.url)
                bundle.putString(ARG_TITLE, cardData.title)
                bundle.putString(
                    ARG_DESCRIPTION,
                    cardData.description
                )
                bundle.putString(
                    ARG_IMAGE_CODE,
                    cardData.imageCode
                )
                Navigation.findNavController(it).navigate(R.id.playerFragment, bundle)*/
                navController.navigateSingleTopTo(PlayerRoute.id)
            })
        }
        composable(route = SearchRoute.id) {
            ReplaySearchView()
        }
        composable(route = PlaylistRoute.id) {
            PlaylistCardListView(goToVideos = {
/*                Navigation.findNavController(it)
                    .navigate(R.id.replayFragment, cardData.targetBundle as Bundle)*/
                navController.navigateSingleTopTo(ReplayRoute.id)
            })
        }
        composable(route = PlayerRoute.id) {
            PlayerView()
        }
        composable(route = ReplayRoute.id) {
            ReplayCardListView(goToVideo = {
/*                val bundle = Bundle()
                bundle.putString(ARG_URL, urlReplay)
                bundle.putString(ARG_TITLE, cardData.title)
                bundle.putString(
                    ARG_DESCRIPTION,
                    cardData.description
                )
                bundle.putString(
                    ARG_IMAGE_CODE,
                    cardData.imageCode
                )
                Navigation.findNavController(it)
                    .navigate(R.id.playerFragment, bundle)*/
                navController.navigateSingleTopTo(PlayerRoute.id)
            })
        }

    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
