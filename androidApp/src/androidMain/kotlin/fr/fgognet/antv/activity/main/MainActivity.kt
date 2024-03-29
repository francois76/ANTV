package fr.fgognet.antv.activity.main

import android.app.PictureInPictureParams
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.google.android.gms.cast.framework.CastContext
import com.google.common.util.concurrent.MoreExecutors
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import fr.fgognet.antv.util.initAndroidLog
import fr.fgognet.antv.utils.resetLogs
import fr.fgognet.antv.view.main.*

private const val TAG = "ANTV/MainActivity"

open class MainActivity : FragmentActivity(), Player.Listener {

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        initAndroidLog()
        MediaSessionServiceImpl.addListener(this)
        val activity = this
        var initialRoute: RouteData? = null
        if (intent.data != null && intent.data!!.host != null) {
            initialRoute =
                RouteData(
                    id = findBy(intent.data!!.host!!)!!,
                    arguments = arrayListOf(),
                )
        }

        setContent {
            Log.d(TAG, "recomposing")
            ANTVApp(backHandler = {
                onBackPressedDispatcher.addCallback {
                    if (!it()) {
                        activity.finish()
                    }
                }
            }, setFullScreen = {
                fullScreen(it)
            }, initialRoute = initialRoute)
        }
        fullScreen(false);

        enableEdgeToEdge()

        CastContext.getSharedInstance(applicationContext, MoreExecutors.directExecutor())
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.v(TAG, "onIsPlayingChanged")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val builder = PictureInPictureParams.Builder()
            builder.setAutoEnterEnabled(isPlaying && !MediaSessionServiceImpl.isCasting)
            if (this.isTaskRoot) {
                this.setPictureInPictureParams(builder.build())
            }
        }
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        Log.v(TAG, this.hashCode().toString())
        MediaSessionServiceImpl.controller?.removeListener(this)
        if (isFinishing) {
            Log.v(TAG, "finishing")
            MediaSessionServiceImpl.controllerFuture = null
        }
        resetLogs()
        super.onDestroy()
    }

    private fun fullScreen(isFullScreen: Boolean) {
        if (isFullScreen) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window?.insetsController?.hide(WindowInsets.Type.systemBars())
                window?.insetsController?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                actionBar?.hide()
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window?.insetsController?.show(WindowInsets.Type.systemBars())
                window?.insetsController?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                actionBar?.show()
            }
        }
    }


}
