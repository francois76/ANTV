package fr.fgognet.antv.service.androidAuto

import android.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession

open class AndroidAutoService : MediaLibraryService() {

    // TAG
    private val TAG = "ANTV/AndroidAutoService"

    // mediaSession
    private var mediaSession: MediaLibrarySession? = null

    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate")
        val callback = AndroidAutoServiceCallBack()
        mediaSession =
            MediaLibrarySession.Builder(
                this,
                ExoPlayer.Builder(this).setSeekBackIncrementMs(5000).setSeekForwardIncrementMs(5000)
                    .build(), callback
            )
                .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? =
        mediaSession

}