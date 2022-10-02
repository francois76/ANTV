package fr.fgognet.antv.service.player

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.util.TypedValue
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.session.MediaButtonReceiver
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.ListenableFuture
import fr.fgognet.antv.R
import fr.fgognet.antv.activity.main.MainActivity

private const val TAG = "ANTV/PlayerServiceListener"

@UnstableApi
class PlayerServiceListener(private val service: PlayerService) : Player.Listener,
    SessionAvailabilityListener,
    BroadcastReceiver(), MediaSession.Callback {


    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.v(TAG, "onIsPlayingChanged")
        if (isPlaying) {
            service.updatePlayingState(PlaybackStateCompat.STATE_PLAYING)
        } else {
            service.updatePlayingState(PlaybackStateCompat.STATE_PAUSED)
        }
        showMediaplayerNotification(
            isPlaying
        )
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        Log.v(TAG, "onAddMediaItems")
        return super.onAddMediaItems(mediaSession, controller, mediaItems)
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

    private fun showMediaplayerNotification(isPlaying: Boolean) {
        Log.v(TAG, "showNotification")
        val channelID = "media_playback_channel"
        val mNotificationManager = NotificationManagerCompat.from(service)
        mNotificationManager.createNotificationChannel(
            NotificationChannelCompat.Builder(
                channelID,
                NotificationManager.IMPORTANCE_LOW
            )
                .setDescription("Media playback controls")
                .setName("Media playback")
                .setShowBadge(false).build()
        )

        val background = TypedValue()
        service.theme?.resolveAttribute(
            android.R.attr.colorBackground,
            background,
            true
        )
        mNotificationManager.notify(
            0,
            NotificationCompat.Builder(service, channelID)
                .setContentTitle(
                    service.mediaSession.player.mediaMetadata.title
                )
                .setContentText(service.mediaSession.player.mediaMetadata.description)
                .setContentIntent(
                    PendingIntent.getActivity(
                        service, 0, Intent(
                            service,
                            MainActivity::class.java
                        ), PendingIntent.FLAG_IMMUTABLE
                    )
                )
                .setSmallIcon(R.mipmap.ic_launcher)
                // .setLargeIcon(PlayerService.currentMediaData?.bitmap)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(background.data)
                .setColorized(true)
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_baseline_skip_previous_24,
                        "restart",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            service,
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        )
                    )
                )
                .addAction(
                    if (isPlaying) NotificationCompat.Action(
                        R.drawable.ic_baseline_pause_24, "pause",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            service,
                            PlaybackStateCompat.ACTION_PAUSE
                        )
                    ) else NotificationCompat.Action(
                        R.drawable.ic_baseline_play_arrow_24, "play",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            service,
                            PlaybackStateCompat.ACTION_PLAY
                        )
                    )
                )
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_baseline_skip_next_24,
                        "next",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            service,
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        )
                    )
                ).build()
        )
    }

}