package fr.fgognet.antv.activity.main

import android.app.PictureInPictureParams
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.media3.common.util.UnstableApi
import fr.fgognet.antv.service.player.PlayerListener

private const val TAG = "ANTV/MainActivityListener"

@UnstableApi
class ActivityPlayerListener(var activity: ComponentActivity) : PlayerListener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.v(TAG, "onIsPlayingChanged")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val builder = PictureInPictureParams.Builder()
            builder.setAutoEnterEnabled(isPlaying)
            activity.setPictureInPictureParams(builder.build())
        }
    }
}