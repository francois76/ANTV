package fr.fgognet.antv.view.player

import android.content.ComponentName
import android.content.res.Configuration
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import dev.icerock.moko.mvvm.createViewModelFactory
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import fr.fgognet.antv.view.main.findActivity

private const val TAG = "ANTV/PlayerView"


@UnstableApi
@Composable
fun PlayerView(
    title: String?,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val context = LocalContext.current
    val model: PlayerViewModel = viewModel(factory = createViewModelFactory {
        PlayerViewModel().start(MediaSessionServiceImpl.controller)
    }
    )
    if (MediaSessionServiceImpl.controllerFuture == null) {
        MediaSessionServiceImpl.controllerFuture =
            MediaController.Builder(
                context,
                SessionToken(
                    context,
                    ComponentName(context, MediaSessionServiceImpl::class.java)
                )
            )
                .buildAsync()
        MediaSessionServiceImpl.controllerFuture?.addListener({
            Log.d(TAG, "Media service built!")
            model.initialize(MediaSessionServiceImpl.controller)
        }, MoreExecutors.directExecutor())
    }
    if (title != null) {
        model.updateCurrentMedia(title)
    }
    val state by model.playerData.ld().observeAsState()
    if (state?.controller != null) {
        PlayerViewState(
            state = state,
            setFullScreen = setFullScreen
        )
    }

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
    var shouldShowControls by remember { mutableStateOf(false) }
    AndroidView(
        modifier =
        Modifier.clickable {
            shouldShowControls = shouldShowControls.not()
        },
        factory = {
            androidx.media3.ui.PlayerView(context).apply {
                player = state?.controller
                useController = false
                layoutParams =
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
            }
        })

    if (state != null) {
        PlayerControls(
            modifier = Modifier.fillMaxSize(),
            isVisible = { shouldShowControls },
            state = state,
            onReplayClick = { state.controller?.seekBack() },
            onForwardClick = { state.controller?.seekForward() },
            onPauseToggle = {
                if (state.isPlaying) {
                    state.controller?.pause()
                } else {
                    state.controller?.play()
                }
            },
            onSeekChanged = { timeMs: Float ->
                state.controller?.seekTo(timeMs.toLong())
            },
        )
    }

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            setFullScreen(true)
        }
        else -> {
            setFullScreen(!shouldShowControls)
        }
    }
}

