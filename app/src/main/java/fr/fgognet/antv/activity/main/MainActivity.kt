package fr.fgognet.antv.activity.main

import android.app.PictureInPictureParams
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.android.gms.cast.framework.CastContext
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import fr.fgognet.antv.config.initCommonLogs
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.main.ANTVApp

private const val TAG = "ANTV/MainActivity"

open class MainActivity : FragmentActivity(), Player.Listener {

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private val controller: MediaController?
        get() = if (controllerFuture?.isDone == true) controllerFuture?.get() else null

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        initCommonLogs()
        if (controllerFuture == null) {
            if (PlayerService.controller == null) {
                controllerFuture =
                    MediaController.Builder(
                        this,
                        SessionToken(this, ComponentName(this, PlayerService::class.java))
                    )
                        .buildAsync()
                controllerFuture?.addListener({
                    PlayerService.controller = controller
                    PlayerService.controller?.addListener(this)
                    setContent {
                        Log.d(TAG, "recomposing + init")
                        ANTVApp()
                    }
                }, MoreExecutors.directExecutor())
            } else {
                PlayerService.controller?.addListener(this)
                setContent {
                    Log.d(TAG, "recomposing")
                    ANTVApp()

                }

            }
        }

        CastContext.getSharedInstance(applicationContext, MoreExecutors.directExecutor())
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.v(TAG, "onIsPlayingChanged")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val builder = PictureInPictureParams.Builder()
            builder.setAutoEnterEnabled(isPlaying)
            this.setPictureInPictureParams(builder.build())
        }
    }


    override fun onStop() {
        Log.v(TAG, "onStop")
        PlayerService.controller?.removeListener(this)
        if (isFinishing) {
            if (controllerFuture != null) {
                MediaController.releaseFuture(controllerFuture!!)
                PlayerService.controller = null
                this.controllerFuture = null
            }
            Log.w(TAG, "finishing")
        }
        super.onStop()
    }

}