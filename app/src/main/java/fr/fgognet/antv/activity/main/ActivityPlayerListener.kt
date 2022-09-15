package fr.fgognet.antv.activity.main

import android.app.PictureInPictureParams
import android.util.Log
import androidx.activity.ComponentActivity
import fr.fgognet.antv.service.player.PlayerListener

private const val TAG = "ANTV/MainActivityListener"

class ActivityPlayerListener(var activity: ComponentActivity) : PlayerListener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.v(TAG, "onIsPlayingChanged")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val builder = PictureInPictureParams.Builder()
            builder.setAutoEnterEnabled(isPlaying)
            activity.setPictureInPictureParams(builder.build())
        }
    }
}