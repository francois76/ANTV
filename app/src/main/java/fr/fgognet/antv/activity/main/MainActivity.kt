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
import com.google.common.util.concurrent.MoreExecutors
import fr.fgognet.antv.config.initCommonLogs
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.view.main.ANTVApp

private const val TAG = "ANTV/MainActivity"

open class MainActivity : FragmentActivity(), Player.Listener {

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        initCommonLogs()
        if (PlayerService.controllerFuture == null) {
            PlayerService.controllerFuture =
                MediaController.Builder(
                    this,
                    SessionToken(this, ComponentName(this, PlayerService::class.java))
                )
                    .buildAsync()
            PlayerService.controllerFuture?.addListener({
                Log.d(TAG, "Media service built!")
                PlayerService.controller?.addListener(this)
            }, MoreExecutors.directExecutor())
        } else {
            PlayerService.controller?.addListener(this)
        }
        setContent {
            Log.d(TAG, "recomposing")
            ANTVApp()
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
            Log.w(TAG, "finishing")
        }
        super.onStop()
    }

}