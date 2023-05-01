package fr.fgognet.antv.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import castButton
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.widget.buildColors
import fr.fgognet.antv.widget.getPlatformContext
import fr.fgognet.antv.widget.getSystemUIController
import fr.fgognet.antv.widget.modal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ANTVApp() {
    val appContext = getPlatformContext()
    val colorScheme = buildColors(context = appContext)
    MaterialTheme(colorScheme = colorScheme) {
        val navController = rememberNavController()
        val systemUiController = getSystemUIController()
        var isFullScreen by remember { mutableStateOf(false) }
        var openDialog by rememberSaveable { mutableStateOf(false) }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val isOnPlayerScreen = navBackStackEntry?.destination?.route?.contains(Routes.PLAYER.value)
        var contextualRefreshFunction by remember {
            mutableStateOf({})
        }
        systemUiController.setPlatformConfiguration()
        if (openDialog) {
            modal(title = stringResource(resource = MR.strings.info), content = stringResource(
                resource = MR.strings.credits,
            ), confirmButton = stringResource(resource = MR.strings.close), closeCallBack = {
                openDialog = false
            })
        }
        Scaffold(
            topBar = {
                if (!(isFullScreen && isOnPlayerScreen == true)) {
                    TopAppBar(actions = {
                        IconButton(onClick = {
                            openDialog = true
                        }) {
                            Image(
                                painter = painterResource(imageResource = MR.images.ic_baseline_info_24),
                                contentDescription = "about"
                            )
                        }
                        castButton()
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
                if (!(isFullScreen && isOnPlayerScreen == true)) {
                    var selectedItem by rememberSaveable { mutableStateOf(0) }
                    val items = listOf(
                        allRoutes[Routes.LIVE],
                        allRoutes[Routes.PLAYLIST],
                        allRoutes[Routes.SEARCH]
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
                    systemUiController.setFullScreen(!it)
                    isFullScreen = it
                }
            )
        }
    }
}




