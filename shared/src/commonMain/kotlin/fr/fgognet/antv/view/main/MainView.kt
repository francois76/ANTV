package fr.fgognet.antv.view.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.compose.NavContainer
import com.chrynan.navigation.compose.rememberNavigator
import com.chrynan.navigation.goTo
import fr.fgognet.antv.view.cardList.LiveCardListView
import fr.fgognet.antv.view.cardList.PlaylistCardListView
import fr.fgognet.antv.view.cardList.ReplayCardListView
import fr.fgognet.antv.view.player.PlayerView
import fr.fgognet.antv.view.replaySearch.ReplaySearchView
import fr.fgognet.antv.widget.buildColors
import fr.fgognet.antv.widget.getPlatformContext
import fr.fgognet.antv.widget.getSystemUIController
import fr.fgognet.antv.widget.handlePictureInPicture


@OptIn(ExperimentalNavigationApi::class)
@Composable
fun ANTVApp() {
    val appContext = getPlatformContext()
    val colorScheme = buildColors(context = appContext)
    val contextualRefreshFunction by remember {
        mutableStateOf({})
    }

    MaterialTheme(colorScheme = colorScheme) {
        val navController = rememberNavigator(allRoutes[Route.LIVE]!!)
        val systemUiController = getSystemUIController()
        handlePictureInPicture(getPlatformContext(), navController)
        NavContainer(navController) { _, destination ->
            when (destination.id) {
                Route.LIVE -> scaffold(
                    isOnPlayerScreen = false,
                    navController = navController,
                    contextualRefreshFunction = contextualRefreshFunction
                ) {
                    LiveCardListView(
                        goToVideo = { title ->
                            navController.goTo(
                                RouteData(
                                    id = Route.PLAYER,
                                    arguments = arrayListOf(title),
                                    iconName = null,
                                    nameID = null
                                )
                            )
                        },
                        updateContextualRefreshFunction = {
                            contextualRefreshFunction()
                        },
                        goToCurrentPlaying = {
                            navController.goTo(
                                RouteData(
                                    id = Route.PLAYER,
                                    arguments = null,
                                    iconName = null,
                                    nameID = null
                                )
                            )
                        },
                    )
                }

                Route.PLAYLIST -> scaffold(
                    isOnPlayerScreen = false,
                    navController = navController,
                    contextualRefreshFunction = contextualRefreshFunction
                ) {
                    PlaylistCardListView(goToVideos = {
                        navController.goTo(allRoutes[Route.REPLAY]!!)
                    },
                        goToCurrentPlaying = {
                            navController.goTo(
                                RouteData(
                                    id = Route.PLAYER,
                                    arguments = null,
                                    iconName = null,
                                    nameID = null
                                )
                            )
                        }
                    )
                }

                Route.REPLAY -> scaffold(
                    isOnPlayerScreen = false,
                    navController = navController,
                    contextualRefreshFunction = contextualRefreshFunction
                ) {
                    ReplayCardListView(
                        goToVideo = { title ->
                            navController.goTo(
                                RouteData(
                                    id = Route.PLAYER,
                                    arguments = arrayListOf(title),
                                    iconName = null,
                                    nameID = null
                                )
                            )
                        },
                        goToCurrentPlaying = {
                            navController.goTo(
                                RouteData(
                                    id = Route.PLAYER,
                                    arguments = null,
                                    iconName = null,
                                    nameID = null
                                )
                            )
                        },
                    )
                }

                Route.PLAYER -> scaffold(
                    isOnPlayerScreen = true,
                    navController = navController,
                    contextualRefreshFunction = contextualRefreshFunction
                ) {
                    PlayerView(
                        title = destination.arguments?.get(0) ?: null,
                        setFullScreen = {
                            systemUiController.setFullScreen(it)
                        }
                    )
                }

                Route.SEARCH -> scaffold(
                    isOnPlayerScreen = false,
                    navController = navController,
                    contextualRefreshFunction = contextualRefreshFunction
                ) {
                    ReplaySearchView(query = {
                        navController.goTo(allRoutes[Route.REPLAY]!!)
                    })
                }

                else -> Text("Unexpected Key: $destination")
            }
        }

        systemUiController.setPlatformConfiguration()
    }
}




