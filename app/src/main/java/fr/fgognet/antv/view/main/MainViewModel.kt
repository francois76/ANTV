package fr.fgognet.antv.view.main

import android.app.PictureInPictureParams
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.service.player.PlayerService


data class MainData(
    val piPParams: PictureInPictureParams,
)

@UnstableApi
class MainViewModel : ViewModel(), Player.Listener {
    private val TAG = "ANTV/MainViewModel"

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null
    private val _mainData: MutableLiveData<MainData> =
        MutableLiveData(
            MainData(
                piPParams = PictureInPictureParams.Builder().build(),
            )
        )
    val mainData: LiveData<MainData> get() = _mainData


    fun start(context: Context) = apply { initialize(context) }

    private fun initialize(context: Context) {
        Log.v(TAG, "initialize")
        controllerFuture =
            MediaController.Builder(
                context,
                SessionToken(context, ComponentName(context, PlayerService::class.java))
            )
                .buildAsync()
        this.controller?.addListener(
            this
        )
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        Log.v(TAG, "onMediaItemTransition")
        super.onMediaItemTransition(mediaItem, reason)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.v(TAG, "onIsPlayingChanged")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val builder = PictureInPictureParams.Builder()
            builder.setAutoEnterEnabled(isPlaying)
            this._mainData.value = MainData(piPParams = builder.build())
        }
    }
}