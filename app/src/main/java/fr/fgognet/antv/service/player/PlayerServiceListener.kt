package fr.fgognet.antv.service.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.*
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

private const val TAG = "ANTV/PlayerServiceListener"

@UnstableApi
class PlayerServiceListener(private val service: PlayerService) : Player.Listener,
    SessionAvailabilityListener,
    BroadcastReceiver(), MediaSession.Callback {

    override fun onConnect(
        session: MediaSession,
        controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
        Log.v(TAG, "onConnect")
        return super.onConnect(session, controller)
    }


    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.v(TAG, "onIsPlayingChanged")
        service.showMediaplayerNotification(
            isPlaying
        )
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<List<MediaItem>> {
        Log.v(TAG, "onAddMediaItems")
        val updatedMediaItems: List<MediaItem> = mediaItems.map { mediaItem ->
            MediaItem.Builder()
                .setUri(mediaItem.mediaId)
                .setMediaId(mediaItem.mediaId)
                .setMediaMetadata(
                    MediaMetadata.Builder().setTitle(mediaItem.mediaMetadata.title)
                        .setDescription(mediaItem.mediaMetadata.description)
                        .build()
                )
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        }
        return Futures.immediateFuture(updatedMediaItems)
    }


    override fun onCastSessionAvailable() {
        Log.v(TAG, "onCastSessionAvailable")
        service.cast()
    }

    override fun onCastSessionUnavailable() {
        Log.v(TAG, "onCastSessionUnavailable")
        service.stopCast()
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.v(TAG, "onReceive")
    }

    override fun onPlayerError(error: PlaybackException) {
        Log.v(TAG, "onPlayerError")
        when (error.errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT,
            PlaybackException.ERROR_CODE_IO_UNSPECIFIED,
            PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW -> {
                Log.w(TAG, "error on playback: ${error.errorCode}")
                service.resyncOnLiveError()
            }

            else -> Log.e(TAG, "error on playback: ${error.errorCode}")
        }
    }


}