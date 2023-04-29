package fr.fgognet.antv.view.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import fr.fgognet.antv.view.main.findActivity
import fr.fgognet.antv.widget.MediaController
import fr.fgognet.antv.widget.MediaSessionServiceImpl
import fr.fgognet.antv.widget.Player
import fr.fgognet.antv.widget.PlayerViewModel
import fr.fgognet.antv.widget.getPlatformContext
import fr.fgognet.antv.widget.orientationWrapper
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

private const val TAG = "ANTV/PlayerView"


@Composable
fun PlayerView(
    title: String?,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val model: PlayerViewModel = getViewModel(factory = viewModelFactory {
        PlayerViewModel().start(MediaSessionServiceImpl.controller)
    }, key = "PlayerViewModel") as PlayerViewModel
    val controller by model.controller.observeAsState()
    if (controller == null) {
        model.loadPlayer(context = getPlatformContext())
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
    val state by model.playerData.observeAsState()
    context.findActivity()?.window?.decorView?.keepScreenOn = true
    var shouldShowControls by remember { mutableStateOf(false) }

    if (state.isCasting) {
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
    orientationWrapper(portrait = {
        setFullScreen(!shouldShowControls)
    }, landscape = {
        setFullScreen(true)
    })

}


