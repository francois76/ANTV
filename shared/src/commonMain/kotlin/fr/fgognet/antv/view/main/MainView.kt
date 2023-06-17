package fr.fgognet.antv.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
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
import fr.fgognet.antv.widget.PlayerViewModel
import fr.fgognet.antv.widget.buildColors
import fr.fgognet.antv.widget.getPlatformContext
import fr.fgognet.antv.widget.getSystemUIController


@OptIn(ExperimentalNavigationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ANTVApp() {
    val appContext = getPlatformContext()
    val navigator = rememberNavigator(allRoutes[Route.LIVE]!!)
    val colorScheme = buildColors(context = appContext)
    val contextualRefreshFunction by remember {
        mutableStateOf({})
    }
    val systemUiController = getSystemUIController()
    HandlePictureInPicture(getPlatformContext(), navigator)
    var openDialog by rememberSaveable { mutableStateOf(false) }
    val isFullScreen by remember { mutableStateOf(false) }
    if (openDialog) {
        Modal(title = stringResource(resource = MR.strings.info), content = stringResource(
            resource = MR.strings.credits,
        ), confirmButton = stringResource(resource = MR.strings.close), closeCallBack = {
            openDialog = false
        })
    }
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    MaterialTheme(colorScheme = colorScheme) {
        NavigationContainer(navigator) { _, destination ->
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
                                                imageResource = item?.iconName!!,
                                            ),
                                            contentDescription = stringResource(resource = item.nameID!!)
                                        )
                                    },
                                    label = { Text(stringResource(resource = item?.nameID!!)) },
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
            ) {
                when (destination.id) {
                    Route.LIVE ->LiveCardListView(
                        goToVideo = { title ->
                            navigator.goTo(
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
                            navigator.goTo(
                                RouteData(
                                    id = Route.PLAYER,
                                    arguments = null,
                                    iconName = null,
                                    nameID = null
                                )
                            )
                        },
                    )
                    Route.REPLAY->ReplayCardListView(
                        goToVideo = { title ->
                            navigator.goTo(
                                RouteData(
                                    id = Route.PLAYER,
                                    arguments = arrayListOf(title),
                                    iconName = null,
                                    nameID = null
                                )
                            )
                        },
                        goToCurrentPlaying = {
                            navigator.goTo(
                                RouteData(
                                    id = Route.PLAYER,
                                    arguments = null,
                                    iconName = null,
                                    nameID = null
                                )
                            )
                        },
                    )
                    Route.PLAYLIST->PlaylistCardListView(goToVideos = {
                        navigator.goTo(allRoutes[Route.REPLAY]!!)
                    },
                        goToCurrentPlaying = {
                            navigator.goTo(
                                RouteData(
                                    id = Route.PLAYER,
                                    arguments = null,
                                    iconName = null,
                                    nameID = null
                                )
                            )
                        }
                    )
                    Route.SEARCH->ReplaySearchView(query = {
                        navigator.goTo(allRoutes[Route.REPLAY]!!)
                    })
                    Route.PLAYER->PlayerView(
                        title = destination.arguments?.get(0),
                        setFullScreen = {
                            systemUiController.setFullScreen(it)
                        }
                    )
                }
            }
            systemUiController.SetPlatformConfiguration()
        }
    }
}




