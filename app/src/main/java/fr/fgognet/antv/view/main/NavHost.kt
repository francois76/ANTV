package fr.fgognet.antv.view.main


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.cardList.LiveCardListView
import fr.fgognet.antv.view.cardList.PlaylistCardListView
import fr.fgognet.antv.view.cardList.ReplayCardListView
import fr.fgognet.antv.view.player.PlayerView
import fr.fgognet.antv.view.replaySearch.ReplaySearchView

const val TAG = "ANTV/ANTVNavHost"


data class PlayingData(
    val hasPlayingData: Boolean,
    val imageCode: String,
    val title: String,
    val description: String,
)


class HasMediaPlaying : ViewModel(), Player.Listener {
    private val tag = "ANTV/HasMediaPlaying"

    fun start() = apply { initialize() }

    private val _playingData: MutableLiveData<PlayingData> =
        MutableLiveData(
            PlayingData(
                hasPlayingData = false,
                imageCode = "",
                title = "",
                description = ""
            )
        )
    val playingData: LiveData<PlayingData> get() = _playingData


    private fun initialize() {
        Log.v(tag, "initialize")
        PlayerService.controller?.addListener(this)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        Log.v(tag, "onMediaItemTransition")
        if (mediaItem != null) {
            this._playingData.value = PlayingData(
                hasPlayingData = true,
                imageCode = mediaItem.mediaMetadata.artworkUri.toString(),
                title = mediaItem.mediaMetadata.title.toString(),
                description = mediaItem.mediaMetadata.description.toString(),
            )
        } else {
            this._playingData.value = PlayingData(
                hasPlayingData = false,
                imageCode = "",
                title = "",
                description = ""
            )
        }
    }
}

@Composable
@UnstableApi
fun ANTVNavHost(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    navController: NavHostController,
    setFullScreenMode: (visible: Boolean) -> Unit
) {
    val context = LocalContext.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    if (context.findActivity()?.isInPictureInPictureMode == true) {
                        navController.navigateToChild(PlayerRoute.id)
                    }
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val model: HasMediaPlaying = viewModel(factory = createViewModelFactory {
        HasMediaPlaying().start()
    }
    )
    val playingData by model.playingData.ld().observeAsState()

    NavHost(
        navController = navController,
        startDestination = LiveRoute.id,
        modifier = modifier
    ) {

        composable(route = LiveRoute.id) {
            LiveCardListView(
                goToVideo = { title ->
                    navController.navigateToChild(
                        "${PlayerRoute.id}/$title"
                    )
                },
                goToCurrentPlaying = {
                    navController.navigateToChild(PlayerRoute.id)
                },
                playingData = playingData,
            )
        }
        composable(route = SearchRoute.id) {
            ReplaySearchView(query = {
                navController.navigateToChild(
                    ReplayRoute.id
                )
            })
        }
        composable(route = PlaylistRoute.id) {
            PlaylistCardListView(goToVideos = {
                navController.navigateToChild(
                    ReplayRoute.id
                )
            },
                playingData = playingData,
                goToCurrentPlaying = {
                    navController.navigateToChild(PlayerRoute.id)
                }
            )
        }
        composable(
            route = "${PlayerRoute.id}/{title}",
            arguments = PlayerRoute.arguments,
            deepLinks = PlayerRoute.deepLinks
        ) {
            val title = getEncodedArgument(it.arguments, "title")
            PlayerView(
                title = title,
                setFullScreen = setFullScreenMode
            )
        }
        composable(
            route = PlayerRoute.id,
        ) {
            PlayerView(
                setFullScreen = setFullScreenMode, title = null,
            )
        }
        composable(
            route = ReplayRoute.id,
        ) {
            ReplayCardListView(
                goToVideo = { title ->
                    navController.navigateToChild(
                        "${PlayerRoute.id}/$title"
                    )
                },
                playingData = playingData,
                goToCurrentPlaying = {
                    navController.navigateToChild(PlayerRoute.id)
                })
        }

    }
}

fun NavHostController.navigateToChild(route: String) =
    this.navigate(route) {
        Log.d(TAG, "navigate to $route")
        launchSingleTop = false
        restoreState = false
    }


fun NavHostController.navigateToTop(route: String) =
    this.navigate(route) {
        Log.d(TAG, "navigate to $route")
        popUpTo(
            this@navigateToTop.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

