package fr.fgognet.antv.view.player

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.cast.CastPlayer
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerControlView.DEFAULT_SHOW_TIMEOUT_MS
import coil.ImageLoader
import coil.request.ImageRequest
import dev.icerock.moko.mvvm.createViewModelFactory
import fr.fgognet.antv.R
import fr.fgognet.antv.databinding.FragmentPlayerBinding
import fr.fgognet.antv.service.player.MediaData
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.main.findActivity

private const val TAG = "ANTV/PlayerView"

@UnstableApi
@Composable
fun PlayerView(
    setFullScreen: (visible: Boolean) -> Unit
) {
    val context: Context = LocalContext.current.applicationContext
    val model: PlayerViewModel = viewModel(factory = createViewModelFactory {
        PlayerViewModel().start(context)
    }
    )
    val state by model.playerData.ld().observeAsState()
    PlayerViewState(
        description = state?.description,
        player = state?.player,
        setFullScreen = setFullScreen
    )
}

@UnstableApi
@Composable
fun PlayerView(
    title: String,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val context: Context = LocalContext.current.applicationContext
    val model: PlayerViewModel = viewModel(factory = createViewModelFactory {
        PlayerViewModel().start(context)
    }
    )
    model.retrievePlayerEntity(title)
    val state by model.playerData.ld().observeAsState()
    if (state?.url != null) {
        val request = ImageRequest.Builder(LocalContext.current)
            .data(state?.imageCode)
            .target(
                onSuccess = { result ->
                    PlayerService.updateCurrentMedia(
                        MediaData(
                            state!!.url, title, state?.description, result.toBitmap(200, 200)
                        )
                    )
                },
                onError = {
                    PlayerService.updateCurrentMedia(
                        MediaData(
                            state!!.url, title, state?.description, null
                        )
                    )
                }
            )
            .build()
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .crossfade(true)
            .build()
        imageLoader.enqueue(request)

        PlayerViewState(
            description = state?.description,
            player = state?.player,
            setFullScreen = setFullScreen
        )
    }


}

@UnstableApi
@Composable
fun PlayerViewState(
    description: String?,
    player: Player?,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    context.findActivity()?.window?.decorView?.keepScreenOn = true
    AndroidViewBinding(factory = FragmentPlayerBinding::inflate) {
        // view.rootView.findViewById<MaterialToolbar>(R.id.topAppBar).title =       it.mediaMetadata.title
        videoView.findViewById<TextView>(R.id.video_description)?.text = description
        if (player is CastPlayer) {
            videoView.controllerHideOnTouch = false
            videoView.controllerShowTimeoutMs = 0
            videoView.showController()
            videoView.defaultArtwork = ResourcesCompat.getDrawable(
                context.resources!!,
                R.drawable.ic_baseline_cast_connected_400,
                context.theme
            )
        } else { // currentPlayer == localPlayer
            videoView.controllerHideOnTouch = false
            videoView.controllerShowTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS
            videoView.defaultArtwork = null
        }
        videoView.player = player
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                setFullScreen(true)
            }
            else -> {
                videoView.setControllerVisibilityListener(androidx.media3.ui.PlayerView.ControllerVisibilityListener { visibility: Int ->
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
}