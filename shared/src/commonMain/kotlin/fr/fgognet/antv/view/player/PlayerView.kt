package fr.fgognet.antv.view.player

import android.content.res.Configuration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.session.MediaController
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import fr.fgognet.antv.view.main.findActivity
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

private const val TAG = "ANTV/PlayerView"


@Composable
fun PlayerView(
    title: String?,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val context = LocalContext.current
    val model: PlayerViewModel = getViewModel(factory = viewModelFactory {
        PlayerViewModel().start(MediaSessionServiceImpl.controller)
    }, key = "PlayerViewModel")
    val controller by model.controller.observeAsState()
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

    if (state != null) {
        if (state!!.isCasting) {
            shouldShowControls = true
        } else {
            if (shouldShowControls) {
                LaunchedEffect(Unit) {
                    val duration = 5
                    delay(duration.seconds)
                    shouldShowControls = shouldShowControls.not()
                }
            }
            if (state!!.duration > 0) {
                AndroidView(
                    modifier =
                    Modifier
                        .background(color = Color.Black)
                        .clickable {
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
            }
        }
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

