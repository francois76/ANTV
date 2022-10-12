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

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
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
        if (PlayerService.controller == null) {
            controllerFuture =
                MediaController.Builder(
                    this,
                    SessionToken(this, ComponentName(this, PlayerService::class.java))
                )
                    .buildAsync()
            controllerFuture?.addListener({
                PlayerService.controller = controller
                controller?.addListener(this)
            }, MoreExecutors.directExecutor())
        }
        setContent {
            ANTVApp()
        }
        CastContext.getSharedInstance(applicationContext)
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