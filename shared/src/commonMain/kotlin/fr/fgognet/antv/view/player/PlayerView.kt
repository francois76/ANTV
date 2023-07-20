package fr.fgognet.antv.view.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import fr.fgognet.antv.widget.*
import io.github.aakira.napier.Napier
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
    Napier.v(tag = TAG, message = "PlayerView")
    if (controller == null || !controller!!.isInit()) {
        model.loadPlayer(context = getPlatformContext())
    } else {
        model.loadMedia(title)
        val state by model.playerData.observeAsState()
        PlayerViewState(
            state = state,
            controller = controller!!,
            setFullScreen = setFullScreen
        )
    }

}

@Composable
fun PlayerViewState(
    state: PlayerData,
    controller: MediaController,
    setFullScreen: (visible: Boolean) -> Unit
) {
    Napier.v(tag = TAG, message = "PlayerViewState")
    Napier.v(tag = TAG, message = "state: $state")
    Box {
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
                Player(
                    modifier = Modifier
                        .clickable {
                            Napier.v(tag = TAG, message = "player click")
                            shouldShowControls = shouldShowControls.not()
                        }, context = getPlatformContext(), controller = controller
                )
            }
        }
        Napier.v(tag = TAG, message = "shouldShowControls : $shouldShowControls")
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
        OrientationWrapper(portrait = {
            setFullScreen(!shouldShowControls)
        }, landscape = {
            setFullScreen(true)
        })
    }
}


