package fr.fgognet.antv.activity.main

import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
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

open class MainActivity : FragmentActivity() {

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null

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
            controllerFuture.addListener({
                PlayerService.controller = controller
            }, MoreExecutors.directExecutor())
        }

        setContent {
            ANTVApp()
        }
        CastContext.getSharedInstance(applicationContext)
    }


    override fun onStop() {
        super.onStop()
        if (isFinishing) {
            MediaController.releaseFuture(controllerFuture)
        }

    }

}