package fr.fgognet.antv.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.compose.NavigationContainer
import com.chrynan.navigation.compose.rememberNavigator
import com.chrynan.navigation.compose.rememberSavableNavigator
import com.chrynan.navigation.goBack
import com.chrynan.navigation.goTo
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.view.cardList.LiveCardListView
import fr.fgognet.antv.view.cardList.PlaylistCardListView
import fr.fgognet.antv.view.cardList.ReplayCardListView
import fr.fgognet.antv.view.player.PlayerView
import fr.fgognet.antv.view.replaySearch.ReplaySearchView
import fr.fgognet.antv.widget.CastButton
import fr.fgognet.antv.widget.HandlePictureInPicture
import fr.fgognet.antv.widget.Modal
import fr.fgognet.antv.widget.buildColors
import fr.fgognet.antv.widget.getPlatformContext
import fr.fgognet.antv.widget.getSystemUIController
import io.github.aakira.napier.Napier
import kotlinx.serialization.ExperimentalSerializationApi


private const val TAG = "ANTV/MainView"

@OptIn(ExperimentalNavigationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalSerializationApi::class,
)
@Composable
fun ANTVApp(backHandler:(()->Boolean)->Unit) {
    val navigator = rememberSavableNavigator(initialDestination = allRoutes[Route.LIVE]!!)
    backHandler{
        navigator.goBack()
    }
    val colorScheme = buildColors(context = getPlatformContext())
    val contextualRefreshFunction by remember {
        mutableStateOf({})
    }
    val systemUiController = getSystemUIController()
    HandlePictureInPicture(getPlatformContext(), navigator)
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var isFullScreen by remember { mutableStateOf(false) }
    if (openDialog) {
        Modal(title = stringResource(resource = MR.strings.info), content = stringResource(
            resource = MR.strings.credits,
        ), confirmButton = stringResource(resource = MR.strings.close), closeCallBack = {
            openDialog = false
        })
    }

    MaterialTheme(colorScheme = colorScheme) {
        NavigationContainer(navigator) { (destination, context) ->
            Napier.v(tag = TAG, message ="initialDestination : " + context.initialDestination.toString())
            Napier.v(tag = TAG, message = "destination : $destination")
            Napier.v(tag = TAG, message = "isFullScreen : $isFullScreen")
            Scaffold(
                topBar = {
                    if (!(isFullScreen && destination.id == Route.PLAYER)) {
                        TopAppBar(actions = {
                            IconButton(onClick = {
                                openDialog = true
                            }) {
                                Image(
                                    painter = painterResource(imageResource = MR.images.ic_baseline_info_24),
                                    contentDescription = "about"
                                )
                            }
                            CastButton()
                            var reloadEnabled by remember { mutableStateOf(true) }
                            IconButton(
                                enabled = reloadEnabled,
                                onClick = {
                                    if (reloadEnabled) {
                                        reloadEnabled = false
                                        contextualRefreshFunction()
                                        reloadEnabled = true
                                    }

                                }
                            ) {
                                Image(
                                    painter = painterResource(imageResource = MR.images.ic_baseline_replay_24),
                                    contentDescription = "reload"
                                )
                            }
                        }, title = {})
                    }
                }, bottomBar = {
                    if (!(isFullScreen && destination.id == Route.PLAYER)) {
                        var selectedItem by rememberSaveable { mutableStateOf(0) }
                        val items = listOf(
                            allRoutes[Route.LIVE],
                            allRoutes[Route.PLAYLIST],
                            allRoutes[Route.SEARCH]
                        )
                        NavigationBar(modifier = Modifier.height(72.dp)) {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        Image(
                                            painterResource(
                                                imageResource = routeIcons[item?.id]!!,
                                            ),
                                            contentDescription = stringResource(resource = routeNames[item?.id]!!)
                                        )
                                    },
                                    label = { Text(stringResource(resource = routeNames[item?.id]!!)) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        navigator.goTo(item!!)
                                    }
                                )
                            }
                        }
                    }
                }
            ) {paddingValues:PaddingValues->
                Column(modifier = Modifier.padding(paddingValues)) {
                    when (destination.id) {
                        Route.LIVE -> LiveCardListView(
                            goToVideo = { title ->
                                navigator.goTo(
                                    RouteData(
                                        id = Route.PLAYER,
                                        arguments = arrayListOf(title),
                                    )
                                )
                            },
                            updateContextualRefreshFunction = {
                                contextualRefreshFunction()
                            },
                            goToCurrentPlaying = {
                                navigator.goTo(
                                    RouteData(
                                        id = Route.PLAYER,
                                        arguments = arrayListOf(),
                                    )
                                )
                            },
                        )

                        Route.REPLAY -> ReplayCardListView(
                            goToVideo = { title ->
                                navigator.goTo(
                                    RouteData(
                                        id = Route.PLAYER,
                                        arguments = arrayListOf(title),
                                    )
                                )
                            },
                            goToCurrentPlaying = {
                                navigator.goTo(
                                    RouteData(
                                        id = Route.PLAYER,
                                        arguments = arrayListOf(),
                                    )
                                )
                            },
                        )

                        Route.PLAYLIST -> PlaylistCardListView(goToVideos = {
                            navigator.goTo(allRoutes[Route.REPLAY]!!)
                        },
                            goToCurrentPlaying = {
                                navigator.goTo(
                                    RouteData(
                                        id = Route.PLAYER,
                                        arguments = arrayListOf(),
                                    )
                                )
                            }
                        )

                        Route.SEARCH -> ReplaySearchView(query = {
                            navigator.goTo(allRoutes[Route.REPLAY]!!)
                        })

                        Route.PLAYER -> PlayerView(
                            title = if(destination.arguments.isEmpty())null else destination.arguments[0],
                            setFullScreen = {
                                isFullScreen = it
                                systemUiController.setFullScreen(it)
                            }
                        )
                    }
                }
            }
            systemUiController.SetPlatformConfiguration()
        }
    }
}




