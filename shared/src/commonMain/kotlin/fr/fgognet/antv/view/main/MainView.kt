package fr.fgognet.antv.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrynan.navigation.*
import com.chrynan.navigation.compose.NavigationContainer
import com.chrynan.navigation.compose.rememberSavableNavigator
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.view.cardList.*
import fr.fgognet.antv.view.player.PlayerView
import fr.fgognet.antv.view.replaySearch.ReplaySearchView
import fr.fgognet.antv.widget.*
import kotlinx.serialization.ExperimentalSerializationApi


private const val TAG = "ANTV/MainView"

@OptIn(
    ExperimentalNavigationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalSerializationApi::class,
)
@Composable
fun ANTVApp(backHandler: (() -> Boolean) -> Unit, initialRoute: RouteData?) {
    val navigator =
        rememberSavableNavigator(
            initialDestination = initialRoute ?: allRoutes[Route.LIVE]!!,
            duplicateDestinationStrategy = NavigationStrategy.DuplicateDestination.CLEAR_TO_ORIGINAL
        )
    backHandler {
        navigator.goBack()
    }
    val context = getPlatformContext()
    val colorScheme = buildColors(context = context)
    val contextualRefreshFunction by remember {
        mutableStateOf({})
    }
    val systemUiController = getSystemUIController()
    HandlePictureInPicture(context, navigator)
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var isFullScreen by remember { mutableStateOf(false) }


    MaterialTheme(colorScheme = colorScheme) {
        if (openDialog) {
            Modal(title = stringResource(resource = MR.strings.info), content = stringResource(
                resource = MR.strings.credits,
            ), confirmButton = stringResource(resource = MR.strings.close), closeCallBack = {
                openDialog = false
            })
        }
        NavigationContainer(navigator) { (destination, _) ->
            KeepScreenOn(getPlatformContext(), destination.id == Route.PLAYER)
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
                        }, title = {
                            if (context.getPlatform() == Platform.IOS && navigator.canGoBack()) {
                                IconButton(onClick = {
                                    backHandler {
                                        navigator.goBack()
                                    }
                                }) {
                                    Image(
                                        painter = painterResource(imageResource = MR.images.back_arrow),
                                        contentDescription = "back"
                                    )
                                }
                            }
                        })
                    }
                }, bottomBar = {
                    if (!(isFullScreen && destination.id == Route.PLAYER)) {
                        val items = listOf(
                            allRoutes[Route.LIVE],
                            allRoutes[Route.PLAYLIST],
                            allRoutes[Route.SEARCH]
                        )
                        NavigationBar(modifier = Modifier.height(72.dp)) {
                            items.forEachIndexed { _, item ->
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
                                    selected = item?.id == destination.id,
                                    onClick = {
                                        navigator.goTo(item!!)
                                    }
                                )
                            }
                        }
                    }
                }
            ) { paddingValues: PaddingValues ->
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
                            title = if (destination.arguments.isEmpty()) null else destination.arguments[0],
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




