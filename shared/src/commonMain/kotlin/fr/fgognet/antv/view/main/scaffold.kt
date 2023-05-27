package fr.fgognet.antv.view.main

import CastButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import com.chrynan.navigation.Navigator
import com.chrynan.navigation.SingleNavigationContext
import com.chrynan.navigation.goTo
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.widget.Modal

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNavigationApi::class)
@Composable
fun scaffold(
    isOnPlayerScreen: Boolean,
    contextualRefreshFunction: () -> Unit,
    navController: Navigator<RouteData, SingleNavigationContext<RouteData>>,
    component: @Composable () -> Unit
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    val isFullScreen by remember { mutableStateOf(false) }
    if (openDialog) {
        Modal(title = stringResource(resource = MR.strings.info), content = stringResource(
            resource = MR.strings.credits,
        ), confirmButton = stringResource(resource = MR.strings.close), closeCallBack = {
            openDialog = false
        })
    }
    Scaffold(
        topBar = {
            if (!(isFullScreen && isOnPlayerScreen)) {
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
            if (!(isFullScreen && isOnPlayerScreen)) {
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
                                        imageResource = item?.iconName!!,
                                    ),
                                    contentDescription = stringResource(resource = item.nameID!!)
                                )
                            },
                            label = { Text(stringResource(resource = item?.nameID!!)) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                navController.goTo(
                                    destination = item!!,
                                )
                            }
                        )
                    }
                }
            }
        }
    ) {
        component()
    }
}