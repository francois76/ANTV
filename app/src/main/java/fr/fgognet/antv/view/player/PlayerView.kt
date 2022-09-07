package fr.fgognet.antv.view.player

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import dev.icerock.moko.mvvm.createViewModelFactory
import fr.fgognet.antv.R
import fr.fgognet.antv.databinding.FragmentPlayerBinding
import fr.fgognet.antv.service.player.MediaData
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.main.findActivity

private const val TAG = "ANTV/PlayerView"


@Composable
fun PlayerView(
    setFullScreen: (visible: Boolean) -> Unit
) {
    PlayerView(
        description = PlayerService.currentMediaData?.description,
        setFullScreen = setFullScreen
    )
}

@Composable
fun PlayerView(
    url: String,
    title: String?,
    description: String?,
    imageCode: String?,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val request = ImageRequest.Builder(LocalContext.current)
        .data(imageCode)
        .target(
            onSuccess = { result ->
                PlayerService.updateCurrentMedia(
                    MediaData(
                        url, title, description, result.toBitmap(200, 200)
                    )
                )
            },
            onError = { error ->
                PlayerService.updateCurrentMedia(
                    MediaData(
                        url, title, description, null
                    )
                )
            }
        )
        .build()
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .crossfade(true)
        .build()
    imageLoader.enqueue(request)
    PlayerView(description = description, setFullScreen = setFullScreen)
}

@Composable
fun PlayerView(description: String?, setFullScreen: (visible: Boolean) -> Unit) {
    val context: Context = LocalContext.current.applicationContext
    val model: PlayerViewModel = viewModel(factory = createViewModelFactory {
        PlayerViewModel().start(context)
    }
    )
    val state by model.player.ld().observeAsState()
    PlayerViewState(description = description, player = state, setFullScreen = setFullScreen)

}

@Composable
fun PlayerViewState(
    description: String?,
    player: Player?,
    setFullScreen: (visible: Boolean) -> Unit
) {
    val context = LocalContext.current
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
            videoView.controllerShowTimeoutMs = StyledPlayerControlView.DEFAULT_SHOW_TIMEOUT_MS
            videoView.defaultArtwork = null
        }
        videoView.player = player
        videoView.setControllerVisibilityListener(StyledPlayerView.ControllerVisibilityListener { visibility: Int ->
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