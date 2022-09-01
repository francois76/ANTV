package fr.fgognet.antv.jetpackView.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import fr.fgognet.antv.R
import fr.fgognet.antv.view.buildColors


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ANTVApp() {
    MaterialTheme(colorScheme = buildColors(context = LocalContext.current)) {
        val navController = rememberNavController()

        Scaffold(
            topBar = {
                MediumTopAppBar(title = {
                    Text(text = stringResource(id = R.string.app_name))
                }, actions = {
                    IconButton(onClick = { }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_info_24),
                            contentDescription = "about"
                        )
                    }
                    IconButton(onClick = { }) {
                    }
                    IconButton(onClick = { }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_replay_24),
                            contentDescription = "reload"
                        )
                    }
                })
            }, bottomBar = {
                var selectedItem by remember { mutableStateOf(0) }
                val items = listOf(LiveRoute, PlaylistRoute, SearchRoute)
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Image(
                                    painterResource(id = item.iconID),
                                    contentDescription = stringResource(id = item.nameID)
                                )
                            },
                            label = { Text(stringResource(id = item.nameID)) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                navController.navigateSingleTopTo(item.id)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            ANTVNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}