package fr.fgognet.antv.view.player

import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerControlView.DEFAULT_SHOW_TIMEOUT_MS
import dev.icerock.moko.mvvm.createViewModelFactory
import fr.fgognet.antv.R
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.main.findActivity

private const val TAG = "ANTV/PlayerView"

@UnstableApi
@Composable
fun PlayerView(
    setFullScreen: (visible: Boolean) -> Unit
) {
    val model: PlayerViewModel = viewModel(factory = createViewModelFactory {
        PlayerViewModel().start()
    }
    )
    val state by model.playerData.ld().observeAsState()
    PlayerViewState(
        state = state,
        setFullScreen = setFullScreen
    )
}

@UnstableApi
@Composable
fun PlayerView(
    title: String,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val model: PlayerViewModel = viewModel(factory = createViewModelFactory {
        PlayerViewModel().start()
    }
    )
    val state by model.playerData.ld().observeAsState()
    model.updateCurrentMedia(title)
    PlayerViewState(
        state = state,
        setFullScreen = setFullScreen
    )
}

@UnstableApi
@Composable
fun PlayerViewState(
    state: PlayerData?,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    context.findActivity()?.window?.decorView?.keepScreenOn = true
    AndroidView(
        factory = {
            androidx.media3.ui.PlayerView(context).apply {
                player = state?.player
                useController = false
                layoutParams =
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
            }
        })
    val shouldShowControls by remember { mutableStateOf(false) }
    val isPlaying by remember { mutableStateOf(state?.player?.isPlaying) }
    val totalDuration by remember { mutableStateOf(0L) }
    val currentTime by remember { mutableStateOf(0L) }
    val bufferedPercentage by remember { mutableStateOf(0) }
    val playbackState by remember { mutableStateOf(state?.player?.playbackState) }

    PlayerControls(
        modifier = Modifier.fillMaxSize(),
        isVisible = { shouldShowControls },
        isPlaying = { isPlaying == true },
        title = { state?.title ?: "" },
        playbackState = { playbackState ?: 0 },
        onReplayClick = { PlayerService.controller?.seekBack() },
        onForwardClick = { PlayerService.controller?.seekForward() },
        onPauseToggle = {
            PlayerService.controller?.pause()
        },
        totalDuration = { totalDuration },
        currentTime = { currentTime },
        bufferedPercentage = { bufferedPercentage },
        onSeekChanged = { timeMs: Float ->
            state?.player?.seekTo(timeMs.toLong())
        }
    )
}

@UnstableApi
@Composable
fun PlayerViewStateOld(
    state: PlayerData?,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    context.findActivity()?.window?.decorView?.keepScreenOn = true
    AndroidView(
        factory = {
            androidx.media3.ui.PlayerView(context).apply {
                if (state?.isCast == true) {
                    controllerHideOnTouch = false
                    controllerShowTimeoutMs = 0
                    showController()
                    defaultArtwork = ResourcesCompat.getDrawable(
                        context.resources!!,
                        R.drawable.ic_baseline_cast_connected_400,
                        context.theme
                    )
                } else { // currentPlayer == localPlayer
                    controllerHideOnTouch = false
                    controllerShowTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS
                    defaultArtwork = null
                }
                player = state?.player
                when (configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        setFullScreen(true)
                    }
                    else -> {
                        setControllerVisibilityListener(androidx.media3.ui.PlayerView.ControllerVisibilityListener { visibility: Int ->
                            Log.v(TAG, "Player controler visibility Changed: $visibility")
                            when (visibility) {
                                View.VISIBLE -> {
                                    setFullScreen(false)
                                }
                                View.GONE -> {
                                    setFullScreen(true)
                                }
                            }
                        })
                    }
                }
            }
        })
}


@Preview(widthDp = 941, heightDp = 423, device = Devices.AUTOMOTIVE_1024p)
@UnstableApi
@Composable
fun PlayerViewStatePreview() {
    PlayerViewState(
        state = PlayerData(
            player = null, url = "", imageCode = "",
            title = "lorem ipsum", description = "dolor est...", isCast = false
        ),
        setFullScreen = {
        }
    )
}