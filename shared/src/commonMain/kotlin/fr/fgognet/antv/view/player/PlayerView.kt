package fr.fgognet.antv.view.player

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import fr.fgognet.antv.view.main.findActivity
import fr.fgognet.antv.widget.MediaController
import fr.fgognet.antv.widget.MediaSessionServiceImpl
import fr.fgognet.antv.widget.Player
import fr.fgognet.antv.widget.PlayerViewModelCommon
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
    model: PlayerViewModelCommon,
    controller: MediaController,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val state by model.playerData.observeAsState()
    val configuration = LocalConfiguration.current
    context.findActivity()?.window?.decorView?.keepScreenOn = true
    var shouldShowControls by remember { mutableStateOf(false) }

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
        if (state.duration > 0) {
            shouldShowControls = Player(shouldShowControls, controller)
        }
    }
    PlayerControls(
        modifier = Modifier.fillMaxSize(),
        isVisible = { shouldShowControls },
        state = state,
        onReplayClick = { controller.seekBack() },
        onForwardClick = { controller.seekForward() },
        onPauseToggle = {
            if (state.isPlaying) {
                controller.pause()
            } else {
                controller.play()
            }
        },
        onSeekChanged = { timeMs: Float ->
            controller.seekTo(timeMs.toLong())
        },
    )

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            setFullScreen(true)
        }

        else -> {
            setFullScreen(!shouldShowControls)
        }
    }
}


