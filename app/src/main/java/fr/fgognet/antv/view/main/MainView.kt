package fr.fgognet.antv.view.main

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.mediarouter.app.MediaRouteButton
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.cast.framework.CastButtonFactory
import fr.fgognet.antv.R
import fr.fgognet.antv.view.buildColors
import fr.fgognet.antv.view.utils.HtmlText


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ANTVApp() {
    val appContext = LocalContext.current
    MaterialTheme(colorScheme = buildColors(context = appContext)) {
        val navController = rememberNavController()
        val systemUiController = rememberSystemUiController()
        systemUiController.isSystemBarsVisible = false
        systemUiController.setSystemBarsColor(Color.Transparent)
        systemUiController.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        val openDialog = remember { mutableStateOf(false) }

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(stringResource(id = R.string.info))
                },
                text = {
                    HtmlText(html = stringResource(id = R.string.credits))
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text("Close")
                    }
                }
            )
        }
        Scaffold(
            topBar = {
                MediumTopAppBar(title = {
                    Text(text = stringResource(id = R.string.app_name))
                }, actions = {
                    IconButton(onClick = {
                        openDialog.value = true
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_info_24),
                            contentDescription = "about"
                        )
                    }
                    AndroidView(factory = { context ->
                        val mediaButton = MediaRouteButton(context)
                        CastButtonFactory.setUpMediaRouteButton(context, mediaButton)
                        mediaButton
                    })
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
                                    painterResource(id = item.iconID!!),
                                    contentDescription = stringResource(id = item.nameID!!)
                                )
                            },
                            label = { Text(stringResource(id = item.nameID!!)) },
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                navController.navigateToTop(item.id)
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

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
