package fr.fgognet.antv.service.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.session.MediaButtonReceiver

private const val TAG = "ANTV/PlayerServiceListener"

class PlayerServiceListener : PlayerListener, BroadcastReceiver() {

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.v(TAG, "onIsPlayingChanged")
        if (isPlaying) {
            PlayerService.updatePlayingState(PlaybackStateCompat.STATE_PLAYING)
        } else {
            PlayerService.updatePlayingState(PlaybackStateCompat.STATE_PAUSED)
        }
    }

    override fun onCastSessionAvailable() {
        Log.v(TAG, "onCastSessionAvailable")
        PlayerService.cast()
    }

    override fun onCastSessionUnavailable() {
        Log.v(TAG, "onCastSessionUnavailable")
        PlayerService.stopCast()
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.v(TAG, "onReceive")
        MediaButtonReceiver.handleIntent(PlayerService.mediaSession, intent)
    }
}