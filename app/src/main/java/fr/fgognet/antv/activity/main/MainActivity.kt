package fr.fgognet.antv.activity.main

import android.app.PictureInPictureParams
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.google.android.gms.cast.framework.CastContext
import com.google.common.util.concurrent.MoreExecutors
import fr.fgognet.antv.config.initCommonLogs
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import fr.fgognet.antv.view.main.ANTVApp

private const val TAG = "ANTV/MainActivity"

open class MainActivity : FragmentActivity(), Player.Listener {

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        initCommonLogs()
        MediaSessionServiceImpl.addListener(this)
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

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        MediaSessionServiceImpl.controller?.removeListener(this)
        super.onDestroy()
    }


}