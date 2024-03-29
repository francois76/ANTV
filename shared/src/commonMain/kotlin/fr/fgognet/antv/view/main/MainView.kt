package fr.fgognet.antv.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import com.chrynan.navigation.*
import com.chrynan.navigation.compose.NavigationContainer
import com.chrynan.navigation.compose.rememberSavableNavigator
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.utils.HtmlText
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
fun ANTVApp(
    backHandler: (() -> Boolean) -> Unit,
    initialRoute: RouteData?,
    setFullScreen: (Boolean) -> Unit = {}
) {
    val navigator =
        rememberSavableNavigator(
            initialDestination = initialRoute ?: allRoutes[Route.LIVE]!!,
            duplicateDestinationStrategy = NavigationStrategy.DuplicateDestination.CLEAR_TO_ORIGINAL
        )
    backHandler {
        navigator.popDestination()
    }
    val context = getPlatformContext()
    val colorScheme = buildColors(context = context)
    val contextualRefreshFunction by remember {
        mutableStateOf({})
    }
    HandlePictureInPicture(context, navigator)
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var isFullScreen by remember { mutableStateOf(false) }


    MaterialTheme(colorScheme = colorScheme) {
        if (openDialog) {

            AlertDialog(
                onDismissRequest = {
                    openDialog = false
                },
                title = {
                    Text(stringResource(resource = MR.strings.info))
                },
                text = {
                    HtmlText(
                        htmlText = stringResource(
                            resource = MR.strings.credits,
                        ), colorScheme = colorScheme
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDialog = false
                        }
                    ) {
                        Text(stringResource(resource = MR.strings.close))
                    }
                }
            )
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
                                    contentDescription = "about",
                                    colorFilter = ColorFilter.tint(color = colorScheme.onBackground)
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
                                    contentDescription = "reload",
                                    colorFilter = ColorFilter.tint(color = colorScheme.onBackground)
                                )
                            }
                        }, title = {
                            if (context.getPlatform() == Platform.IOS && navigator.canPopDestination()) {
                                IconButton(onClick = {
                                    backHandler {
                                        navigator.popDestination()
                                    }
                                }) {
                                    Image(
                                        painter = painterResource(imageResource = MR.images.back_arrow),
                                        contentDescription = "back",
                                        colorFilter = ColorFilter.tint(color = colorScheme.onBackground)
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
                        NavigationBar() {
                            items.forEachIndexed { _, item ->
                                NavigationBarItem(
                                    icon = {
                                        Image(
                                            painterResource(
                                                imageResource = routeIcons[item?.id]!!,
                                            ),
                                            colorFilter = ColorFilter.tint(color = colorScheme.onBackground),
                                            contentDescription = stringResource(resource = routeNames[item?.id]!!)
                                        )
                                    },
                                    label = { Text(stringResource(resource = routeNames[item?.id]!!)) },
                                    selected = item?.id == destination.id,
                                    onClick = {
                                        navigator.push(item!!)
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
                                navigator.push(
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
                                navigator.push(
                                    RouteData(
                                        id = Route.PLAYER,
                                        arguments = arrayListOf(),
                                    )
                                )
                            },
                        )

                        Route.REPLAY -> ReplayCardListView(
                            goToVideo = { title ->
                                navigator.push(
                                    RouteData(
                                        id = Route.PLAYER,
                                        arguments = arrayListOf(title),
                                    )
                                )
                            },
                            goToCurrentPlaying = {
                                navigator.push(
                                    RouteData(
                                        id = Route.PLAYER,
                                        arguments = arrayListOf(),
                                    )
                                )
                            },
                        )

                        Route.PLAYLIST -> PlaylistCardListView(goToVideos = {
                            navigator.push(allRoutes[Route.REPLAY]!!)
                        },
                            goToCurrentPlaying = {
                                navigator.push(
                                    RouteData(
                                        id = Route.PLAYER,
                                        arguments = arrayListOf(),
                                    )
                                )
                            }
                        )

                        Route.SEARCH -> ReplaySearchView(query = {
                            navigator.push(allRoutes[Route.REPLAY]!!)
                        })

                        Route.PLAYER -> PlayerView(
                            title = if (destination.arguments.isEmpty()) null else destination.arguments[0],
                            setFullScreen = {
                                isFullScreen = it
                                setFullScreen(it)
                            }
                        )
                    }
                }
            }
        }
    }
}




