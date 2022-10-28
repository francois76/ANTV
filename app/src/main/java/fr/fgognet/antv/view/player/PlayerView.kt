package fr.fgognet.antv.view.player

import android.content.res.Configuration
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
    val controller by model.controller.ld().observeAsState()
    if (controller == null) {
        model.loadPlayer(context = context)
    } else {
        model.loadMedia(title)
        PlayerViewState(
            model = model,
            controller = controller!!,
            setFullScreen = setFullScreen
        )
    }

}

@UnstableApi
@Composable
fun PlayerViewState(
    model: PlayerViewModel,
    controller: MediaController,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val state by model.playerData.ld().observeAsState()
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
                player = controller
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
            state = state!!,
            onReplayClick = { controller.seekBack() },
            onForwardClick = { controller.seekForward() },
            onPauseToggle = {
                if (state!!.isPlaying) {
                    controller.pause()
                } else {
                    controller.play()
                }
            },
            onSeekChanged = { timeMs: Float ->
                controller.seekTo(timeMs.toLong())
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

