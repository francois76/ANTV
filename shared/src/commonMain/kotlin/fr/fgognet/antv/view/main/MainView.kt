package fr.fgognet.antv.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.mediarouter.app.MediaRouteButton
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.cast.framework.CastButtonFactory
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.R
import fr.fgognet.antv.view.utils.HtmlText
import fr.fgognet.antv.view.utils.buildColors
import fr.fgognet.antv.widget.getPlatformContext
import fr.fgognet.antv.widget.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ANTVApp() {
    val appContext = getPlatformContext()
    val colorScheme = buildColors(context = appContext)
    MaterialTheme(colorScheme = colorScheme) {
        val navController = rememberNavController()
        val systemUiController = rememberSystemUiController()
        var isFullScreen by remember { mutableStateOf(false) }
        var openDialog by rememberSaveable { mutableStateOf(false) }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val isOnPlayerScreen = navBackStackEntry?.destination?.route?.contains(Routes.PLAYER.value)
        var contextualRefreshFunction by remember {
            mutableStateOf({})
        }
        systemUiController.setStatusBarColor(colorScheme.background)
        // TODO: find why the color is not the same as the navigationBar
        systemUiController.setNavigationBarColor(colorScheme.surface)
        systemUiController.systemBarsDarkContentEnabled = !isSystemInDarkTheme()
        systemUiController.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
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
                        html = stringResource(
                            resource = MR.strings.credits
                        )
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
        Scaffold(
            topBar = {
                if (!(isFullScreen && isOnPlayerScreen == true)) {
                    TopAppBar(actions = {
                        IconButton(onClick = {
                            openDialog = true
                        }) {
                            Image(
                                painter = painterResource(res = R.drawable.ic_baseline_info_24),
                                contentDescription = "about"
                            )
                        }
                        AndroidView(factory = { context ->
                            val mediaButton = MediaRouteButton(context)
                            CastButtonFactory.setUpMediaRouteButton(context, mediaButton)
                            mediaButton
                        })
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
                                painter = painterResource(id = R.drawable.ic_baseline_replay_24),
                                contentDescription = "reload"
                            )
                        }
                    }, title = {})
                }
            }, bottomBar = {
                if (!(isFullScreen && isOnPlayerScreen == true)) {
                    var selectedItem by rememberSaveable { mutableStateOf(0) }
                    val items = listOf(
                        getRoute(Routes.LIVE),
                        getRoute(Routes.PLAYLIST),
                        getRoute(Routes.SEARCH)
                    )
                    NavigationBar(modifier = Modifier.height(72.dp)) {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = {
                                    Image(
                                        painterResource(
                                            res = item?.iconName
                                        ),
                                        contentDescription = stringResource(resource = item?.nameID!!)
                                    )
                                },
                                label = { Text(stringResource(resource = item?.nameID!!)) },
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    navController.navigateToTop(item?.id!!)
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            ANTVNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                updateContextualRefreshFunction = {
                    contextualRefreshFunction = it
                },
                setFullScreenMode = {
                    systemUiController.isSystemBarsVisible = !it
                    isFullScreen = it
                }
            )
        }
    }
}




