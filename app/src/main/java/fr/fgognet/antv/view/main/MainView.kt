package fr.fgognet.antv.view.main

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.util.Linkify
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.mediarouter.app.MediaRouteButton
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.fgognet.antv.R
import fr.fgognet.antv.utils.linkifyHtml
import fr.fgognet.antv.view.buildColors


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ANTVApp() {
    val appContext = LocalContext.current
    MaterialTheme(colorScheme = buildColors(context = appContext)) {
        val navController = rememberNavController()
        val modalContent = linkifyHtml(stringResource(id = R.string.credits), Linkify.ALL)
        val modalTitle = stringResource(id = R.string.info)
        Scaffold(
            topBar = {
                MediumTopAppBar(title = {
                    Text(text = stringResource(id = R.string.app_name))
                }, actions = {
                    IconButton(onClick = {

                        val dialog = MaterialAlertDialogBuilder(
                            appContext,
                            com.google.android.material.R.style.MaterialAlertDialog_Material3
                        )
                            .setTitle(modalTitle)
                            .setMessage(
                                modalContent
                            )
                            .show()
/*                        (dialog.findViewById<TextView>(android.R.id.message))?.movementMethod =
                            LinkMovementMethod.getInstance()*/
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
